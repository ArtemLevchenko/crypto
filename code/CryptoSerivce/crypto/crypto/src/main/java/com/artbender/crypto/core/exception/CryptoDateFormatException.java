package com.artbender.crypto.core.exception;

/**
 * Exception for handle format data in search requests
 *
 * @author Artsiom Leuchanka
 */
public class CryptoDateFormatException extends RuntimeException {
    private final static String ERROR_MESSAGE = "Crypto start or end date is NOT valid. Date format should be: yyyy-MM-dd, 1990-03-23";

    public CryptoDateFormatException() {
        super(ERROR_MESSAGE);
    }
}
