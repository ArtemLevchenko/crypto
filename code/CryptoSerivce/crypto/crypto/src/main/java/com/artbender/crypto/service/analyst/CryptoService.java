package com.artbender.crypto.service.analyst;

import com.artbender.crypto.core.dto.CryptoAggregateResponse;
import com.artbender.crypto.core.dto.CryptoResponse;

/**
 * Service Layer for work with Crypto entity and repository
 *
 * @author Artsiom Leuchanka
 */
public interface CryptoService {

    /**
     * @param symbol - request parameter for filter max prices
     * @return - crypto response for symbol type
     */
    CryptoResponse findMaxPriceCryptoBySymbol(String symbol);
    /**
     * @param symbol - request parameter for filter min prices
     * @return - crypto response for symbol type
     */
    CryptoResponse findMinPriceCryptoBySymbol(String symbol);
    /**
     * @param symbol - request parameter for filter oldest prices
     * @return - crypto response for symbol type
     */
    CryptoResponse findOldestPriceCryptoBySymbol(String symbol);
    /**
     * @param symbol - request parameter for filter newest prices
     * @return - crypto response for symbol type
     */
    CryptoResponse findNewestPriceCryptoBySymbol(String symbol);
    /**
     * @return - crypto response average prices for all cryptos in all period
     */
    CryptoAggregateResponse findAVGPriceForAllCryptos();
    /**
     * @return - crypto response normalized prices for all cryptos in all period
     */
    CryptoAggregateResponse findNormalizedPriceForAllCryptos();
    /**
     *  Date format for requested params: yyy-MM-dd
     *
     * @param startDate - requested start date
     * @param endDate - requested end date
     * @return - crypto response normalized prices for highest crypto in period
     */
    CryptoAggregateResponse findNormalizedPriceOnSpecificDateForCrypto(String startDate, String endDate);
}
