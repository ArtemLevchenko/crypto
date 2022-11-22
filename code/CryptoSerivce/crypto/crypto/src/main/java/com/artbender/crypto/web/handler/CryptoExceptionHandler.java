package com.artbender.crypto.web.handler;

import com.artbender.crypto.core.exception.CryptoDateFormatException;
import com.artbender.crypto.core.exception.SymbolNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Error handler on web level. Controller-advice
 *
 * @author Artsiom Leuchanka
 */
@ControllerAdvice
public class CryptoExceptionHandler {

    @ExceptionHandler({SymbolNotFoundException.class})
    public ResponseEntity<Object> handleSymbolNotFoundException(SymbolNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CryptoDateFormatException.class})
    public ResponseEntity<Object> handleCryptoDateFormatException(CryptoDateFormatException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
