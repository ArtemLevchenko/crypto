package com.artbender.crypto.web.controller;

import com.artbender.crypto.core.constant.ApiConstant;
import com.artbender.crypto.core.dto.CryptoAggregateResponse;
import com.artbender.crypto.core.dto.CryptoResponse;
import com.artbender.crypto.service.analyst.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Main Rest controller to handle crypto operation
 *
 * @author Artsiom Leuchanka
 */
@RestController
public class CryptoController {

    private final CryptoService cryptoService;

    @Autowired
    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @GetMapping(value = ApiConstant.MAX_PRICE_BY_SYMBOL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CryptoResponse> getMaxPriceCryptoBySymbol(@RequestParam String symbol) {
            return ResponseEntity.ok(cryptoService.findMaxPriceCryptoBySymbol(symbol));
    }

    @GetMapping(value = ApiConstant.MIN_PRICE_BY_SYMBOL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CryptoResponse> getMinPriceCryptoBySymbol(@RequestParam String symbol) {
        return ResponseEntity.ok(cryptoService.findMinPriceCryptoBySymbol(symbol));
    }

    @GetMapping(value = ApiConstant.OLDEST_PRICE_BY_SYMBOL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CryptoResponse> getOldestPriceCryptoBySymbol(@RequestParam String symbol) {
        return ResponseEntity.ok(cryptoService.findOldestPriceCryptoBySymbol(symbol));
    }

    @GetMapping(value = ApiConstant.NEWEST_PRICE_BY_SYMBOL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CryptoResponse> getNewestPriceCryptoBySymbol(@RequestParam String symbol) {
        return ResponseEntity.ok(cryptoService.findNewestPriceCryptoBySymbol(symbol));
    }

    @GetMapping(value = ApiConstant.AVG_PRICES_ALL_CRYPTOS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CryptoAggregateResponse> getAvgPriceAllCryptos() {
        return ResponseEntity.ok(cryptoService.findAVGPriceForAllCryptos());
    }

    @GetMapping(value = ApiConstant.NORMALIZED_PRICES_ALL_CRYPTOS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CryptoAggregateResponse> getNormalizedPriceAllCryptos() {
        return ResponseEntity.ok(cryptoService.findNormalizedPriceForAllCryptos());
    }


    @GetMapping(value = ApiConstant.NORMALIZED_PRICES_ON_SPECIFIC_DATE_BEST_CRYPTO, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CryptoAggregateResponse> getNormalizedPriceOnSpecificDateForCrypto(@RequestParam String startDate, @RequestParam String endDate) {
        return ResponseEntity.ok(cryptoService.findNormalizedPriceOnSpecificDateForCrypto(startDate, endDate));
    }


}
