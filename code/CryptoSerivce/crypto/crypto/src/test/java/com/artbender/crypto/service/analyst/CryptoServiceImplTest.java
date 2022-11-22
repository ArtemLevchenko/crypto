package com.artbender.crypto.service.analyst;

import com.artbender.crypto.core.dto.CryptoAggregateResponse;
import com.artbender.crypto.core.dto.CryptoResponse;
import com.artbender.crypto.core.exception.CryptoDateFormatException;
import com.artbender.crypto.core.exception.SymbolNotFoundException;
import com.artbender.crypto.service.ServiceConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CryptoServiceImplTest {

    @Autowired
    private CryptoService cryptoService;

    @Test
    public void testFindMaxPriceCryptoBySymbol() throws Exception {
        CryptoResponse cryptoResponse = cryptoService.findMaxPriceCryptoBySymbol("BTC");
        Assert.assertNotEquals("Checked that our request has response for max price", 0, cryptoResponse.getCryptoResult().size());
    }

    @Test
    public void testFindMaxPriceCryptoBySymbolEmptySymbol() throws Exception {
        try {
            CryptoResponse cryptoResponse = cryptoService.findMaxPriceCryptoBySymbol("");
        } catch (SymbolNotFoundException symbolNotFoundException) {
            Assert.assertEquals("Checked that our input value is not valid", "Crypto symbol is NOT valid. Please check request params",
                    symbolNotFoundException.getMessage());
        }
    }

    @Test
    public void testFindMinPriceCryptoBySymbol() throws Exception {
        CryptoResponse cryptoResponse = cryptoService.findMinPriceCryptoBySymbol("DOGE");
        Assert.assertNotEquals("Checked that our request has response for min price", 0, cryptoResponse.getCryptoResult().size());
    }

    @Test
    public void testFindOldestPriceCryptoBySymbol() throws Exception {
        CryptoResponse cryptoResponse = cryptoService.findOldestPriceCryptoBySymbol("XRP");
        Assert.assertNotEquals("Checked that our request has response for oldest price", 0, cryptoResponse.getCryptoResult().size());
    }

    @Test
    public void testFindNewestPriceCryptoBySymbol() throws Exception {
        CryptoResponse cryptoResponse = cryptoService.findNewestPriceCryptoBySymbol("XRP");
        Assert.assertNotEquals("Checked that our request has response for newest price", 0, cryptoResponse.getCryptoResult().size());
    }

    @Test
    public void testFindAVGPriceForAllCryptos() throws Exception {
        CryptoAggregateResponse cryptoResponse = cryptoService.findAVGPriceForAllCryptos();
        Assert.assertEquals("Checked that our request has response for average prices", 5, cryptoResponse.getAggregateEntities().size());
    }

    @Test
    public void testFindNormalizedPriceForAllCryptos() throws Exception {
        CryptoAggregateResponse cryptoResponse = cryptoService.findNormalizedPriceForAllCryptos();
        Assert.assertEquals("Checked that our request has response for normalized prices", 5, cryptoResponse.getAggregateEntities().size());
    }

    @Test
    public void testFindNormalizedPriceOnSpecificDateForCrypto() throws Exception {
        CryptoAggregateResponse cryptoResponse = cryptoService.findNormalizedPriceOnSpecificDateForCrypto("2022-01-01", "2022-01-01");
        Assert.assertEquals("Checked that our request has response for normalized prices", "XRP", cryptoResponse.getAggregateEntities().get(0).getSymbol());
    }

    @Test
    public void testFindNormalizedPriceOnSpecificDateForCryptoInvalidDate() throws Exception {

        try {
            CryptoAggregateResponse cryptoResponse = cryptoService.findNormalizedPriceOnSpecificDateForCrypto("201-01", "2022-01-01");
        } catch (CryptoDateFormatException cryptoDateFormatException) {
            Assert.assertEquals("Checked that our input value is not valid", "Crypto start or end date is NOT valid. Date format should be: yyyy-MM-dd, 1990-03-23",
                    cryptoDateFormatException.getMessage());
        }
    }
}
