package com.artbender.crypto.service.repository;

import com.artbender.crypto.core.model.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Crypto Repository and custom Crypto searching injections
 *
 * @author Artsiom Leuchanka
 */
@Repository
public interface CryptoRepository extends JpaRepository<Crypto, Long> {

    @Query(value = "SELECT * FROM crypto.cryptotable WHERE symbol = ?1 AND price=(SELECT MAX(maxTable.price) FROM crypto.cryptotable maxTable where symbol = ?1)", nativeQuery = true)
    List<Crypto> findMaxPriceCryptoBySymbol(String symbol);

    @Query(value = "SELECT * FROM crypto.cryptotable WHERE symbol = ?1 AND price=(SELECT MIN(minTable.price) FROM crypto.cryptotable minTable where symbol = ?1)", nativeQuery = true)
    List<Crypto> findMinPriceCryptoBySymbol(String symbol);

    @Query(value = "SELECT * FROM crypto.cryptotable WHERE symbol = ?1 AND timestamp=(SELECT MIN(oldestTable.timestamp) FROM crypto.cryptotable oldestTable where symbol = ?1)", nativeQuery = true)
    List<Crypto> findOldestPriceCryptoBySymbol(String symbol);

    @Query(value = "SELECT * FROM crypto.cryptotable WHERE symbol = ?1 AND timestamp=(SELECT MAX(newestTable.timestamp) FROM crypto.cryptotable newestTable where symbol = ?1)", nativeQuery = true)
    List<Crypto> findNewestPriceCryptoBySymbol(String symbol);


    @Query(value = "SELECT AVG(price) as resultPrice, symbol FROM crypto.cryptotable GROUP BY symbol ORDER by resultPrice DESC", nativeQuery = true)
    List<CryptoAVGDetailsResult> findAVGPriceForAllCryptos();

    interface CryptoAVGDetailsResult {
        Double getResultPrice();
        String getSymbol();
    }

    @Query(value = "SELECT MAX(price) as resultMaxPrice, MIN(price) as resultMinPrice, symbol FROM crypto.cryptotable GROUP BY symbol", nativeQuery = true)
    List<CryptoNormalizedRangeDetailsResult> findNormalizedPriceForAllCryptos();

    interface CryptoNormalizedRangeDetailsResult {
        Double getResultMaxPrice();
        Double getResultMinPrice();
        String getSymbol();
    }

    @Query(value = "SELECT MAX(price) as resultMaxPrice, MIN(price) as resultMinPrice, symbol FROM crypto.cryptotable WHERE timestamp BETWEEN ?1 AND ?2 GROUP BY symbol", nativeQuery = true)
    List<CryptoNormalizedRangeDetailsResult> findNormalizedPriceOnSpecificDateForCrypto(Date startDate, Date endDate);
}
