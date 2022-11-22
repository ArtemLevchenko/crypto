package com.artbender;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Lambda function
 * 1) Trigger by upload S3 event
 * 2) Transform and convert data from CSV to Pojo
 * 3) Insert all new result to RDS
 *
 * @author Artsiom Leuchanka
 */

public class CryptoLambda implements RequestHandler<S3Event, APIGatewayProxyResponseEvent> {

    private final AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
            .withRegion(Regions.US_EAST_1)
            .build();

    private LambdaLogger log;


    @Override
    public APIGatewayProxyResponseEvent handleRequest(S3Event input, Context context) {
        log = context.getLogger();

        input.getRecords()
                .stream()
                .map(this::transformFromCsv)
                .forEach(this::saveInDb);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody("Transformation completed")
                .withIsBase64Encoded(false);
    }

    private List<Crypto> transformFromCsv(S3EventNotification.S3EventNotificationRecord notificationRecord) {
        String key = notificationRecord.getS3().getObject().getKey();
        String bucketName = notificationRecord.getS3().getBucket().getName();
        S3Object s3Object = amazonS3.getObject(bucketName, key);

        CSVParser csvParser = new CSVParserBuilder().withSeparator(',').build();
        List<Crypto> parsedCryptoData = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(s3Object.getObjectContent(), StandardCharsets.UTF_8))
                .withCSVParser(csvParser)
                .withSkipLines(1)
                .build()) {
            List<String[]> r = reader.readAll();
            r.forEach(line -> {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis(Long.parseLong(line[0]));
                String symbol = line[1];
                Double price = Double.valueOf(line[2]);
                parsedCryptoData.add(new Crypto(symbol, price, calendar));
            });
        } catch (IOException | CsvException e) {
            log.log("Parsing exception " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        log.log("Data is ready for save: " + parsedCryptoData.size());
        return parsedCryptoData;
    }

    private void saveInDb(List<Crypto> cryptos) {
        String userName = System.getenv("UserName");
        String password = System.getenv("Password");
        String url = System.getenv("URL");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(url, userName, password);
            connection.setAutoCommit(true);
            String compiledQuery = "INSERT INTO crypto.cryptotable(SYMBOL, TIMESTAMP, PRICE)" +
                    " VALUES" + "(?, ?, ?)";
            preparedStatement = connection.prepareStatement(compiledQuery);

            for (Crypto crypto : cryptos) {
                preparedStatement.setString(1, crypto.getSymbol());
                java.sql.Timestamp timestamp = new java.sql.Timestamp(crypto.getTimestamp().getTimeInMillis());
                preparedStatement.setTimestamp(2, timestamp);
                preparedStatement.setDouble(3, crypto.getPrice());
                preparedStatement.addBatch();
            }

            int[] result = preparedStatement.executeBatch();
            preparedStatement.close();
            connection.close();

            log.log("Data inserted " + result.length);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    class Crypto {
        private final String symbol;
        private final Double price;
        private final Calendar timestamp;

        public Crypto(String symbol, Double price, Calendar timestamp) {
            this.symbol = symbol;
            this.price = price;
            this.timestamp = timestamp;
        }

        public String getSymbol() {
            return symbol;
        }

        public Double getPrice() {
            return price;
        }

        public Calendar getTimestamp() {
            return timestamp;
        }
    }
}
