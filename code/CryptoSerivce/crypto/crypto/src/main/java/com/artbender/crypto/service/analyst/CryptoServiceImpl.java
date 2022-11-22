package com.artbender.crypto.service.analyst;

import com.artbender.crypto.core.dto.CryptoAggregateDTO;
import com.artbender.crypto.core.dto.CryptoAggregateResponse;
import com.artbender.crypto.core.dto.CryptoResponse;
import com.artbender.crypto.core.exception.CryptoDateFormatException;
import com.artbender.crypto.core.exception.SymbolNotFoundException;
import com.artbender.crypto.service.repository.CryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Implementation of service Layer to work with Crypto entity and repository
 *
 * @author Artsiom Leuchanka
 */
@Service
public class CryptoServiceImpl implements CryptoService {

    private final static String DATE_FORMAT = "yyyy-MM-dd";

    private final CryptoRepository cryptoRepository;

    @Autowired
    public CryptoServiceImpl(CryptoRepository cryptoRepository) {
        this.cryptoRepository = cryptoRepository;
    }

    @Override
    public CryptoResponse findMaxPriceCryptoBySymbol(String symbol) {
        if (symbol == null || symbol.isEmpty()) {
            throw new SymbolNotFoundException();
        }
        return CryptoResponse.builder()
                .cryptoResult(cryptoRepository.findMaxPriceCryptoBySymbol(symbol))
                .build();
    }

    @Override
    public CryptoResponse findMinPriceCryptoBySymbol(String symbol) {
        if (symbol == null || symbol.isEmpty()) {
            throw new SymbolNotFoundException();
        }
        return CryptoResponse.builder()
                .cryptoResult(cryptoRepository.findMinPriceCryptoBySymbol(symbol))
                .build();
    }

    @Override
    public CryptoResponse findOldestPriceCryptoBySymbol(String symbol) {
        if (symbol == null || symbol.isEmpty()) {
            throw new SymbolNotFoundException();
        }
        return CryptoResponse.builder()
                .cryptoResult(cryptoRepository.findOldestPriceCryptoBySymbol(symbol))
                .build();
    }

    @Override
    public CryptoResponse findNewestPriceCryptoBySymbol(String symbol) {
        if (symbol == null || symbol.isEmpty()) {
            throw new SymbolNotFoundException();
        }
        return CryptoResponse.builder()
                .cryptoResult(cryptoRepository.findNewestPriceCryptoBySymbol(symbol))
                .build();
    }

    @Override
    public CryptoAggregateResponse findAVGPriceForAllCryptos() {
        List<CryptoAggregateDTO> cryptoResultList = new ArrayList<>();
        for (CryptoRepository.CryptoAVGDetailsResult result : cryptoRepository.findAVGPriceForAllCryptos()) {
            cryptoResultList.add(new CryptoAggregateDTO(result.getResultPrice(), result.getSymbol()));
        }
        return CryptoAggregateResponse.builder()
                .aggregateEntities(cryptoResultList)
                .build();
    }

    @Override
    public CryptoAggregateResponse findNormalizedPriceForAllCryptos() {
        List<CryptoAggregateDTO> cryptoResultList = new ArrayList<>();
        for (CryptoRepository.CryptoNormalizedRangeDetailsResult result : cryptoRepository.findNormalizedPriceForAllCryptos()) {
            cryptoResultList.add(new CryptoAggregateDTO(calculateNormalizedPrice(result), result.getSymbol()));
        }
        return CryptoAggregateResponse.builder()
                .aggregateEntities(cryptoResultList)
                .build();
    }

    @Override
    public CryptoAggregateResponse findNormalizedPriceOnSpecificDateForCrypto(String startDate, String endDate) {
        Calendar startDateCalendar = Calendar.getInstance();
        Calendar endDateCalendar = Calendar.getInstance();
        this.setUpDates(startDateCalendar, endDateCalendar, startDate, endDate);
        List<CryptoAggregateDTO> cryptoResultList = new ArrayList<>();
        for (CryptoRepository.CryptoNormalizedRangeDetailsResult result : cryptoRepository.findNormalizedPriceOnSpecificDateForCrypto(startDateCalendar.getTime(), endDateCalendar.getTime())) {
            cryptoResultList.add(new CryptoAggregateDTO(calculateNormalizedPrice(result), result.getSymbol()));
        }
        if (cryptoResultList.isEmpty()) {
            return CryptoAggregateResponse.builder()
                    .aggregateEntities(new ArrayList<>())
                    .build();
        }
        // sorting by price and get the highest price
        cryptoResultList.sort(Comparator.comparing(CryptoAggregateDTO::getPrice));
        return CryptoAggregateResponse.builder()
                .aggregateEntities(Collections.singletonList(cryptoResultList.get(cryptoResultList.size() - 1)))
                .build();
    }


    private void setUpDates(Calendar startDateCalendar, Calendar endDateCalendar, String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            startDateCalendar.setTime(sdf.parse(startDate));
            endDateCalendar.setTime(sdf.parse(endDate));

            startDateCalendar.set(Calendar.HOUR, 0);
            startDateCalendar.set(Calendar.MINUTE, 0);
            startDateCalendar.set(Calendar.SECOND, 0);

            endDateCalendar.set(Calendar.HOUR, 23);
            endDateCalendar.set(Calendar.MINUTE, 59);
            endDateCalendar.set(Calendar.SECOND, 59);
        } catch (ParseException e) {
            throw new CryptoDateFormatException();
        }
    }

    private Double calculateNormalizedPrice(CryptoRepository.CryptoNormalizedRangeDetailsResult result) {
        double resultPrice = 0;
        if (result.getResultMinPrice() != 0) {
            resultPrice = (result.getResultMaxPrice() - result.getResultMinPrice()) / result.getResultMinPrice();
        }
        return resultPrice;
    }
}
