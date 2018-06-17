package com.n26.analyser.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RestController
public class TransactionStatisticsExceptionHandler {

	@ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
	@ResponseStatus(BAD_REQUEST)
    @ResponseBody
	  public final ResponseEntity<ErrorDetails> handleBadRequest(Exception ex, WebRequest request) {
	    ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
	        request.getDescription(false));
	    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	  }
	
}
