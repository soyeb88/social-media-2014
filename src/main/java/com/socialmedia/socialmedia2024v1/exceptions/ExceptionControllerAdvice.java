package com.socialmedia.socialmedia2024v1.exceptions;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.socialmedia.socialmedia2024v1.controller.SocialMedia2024V1Controller;

@RestControllerAdvice
public class ExceptionControllerAdvice {
	
	@Autowired
	private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
	
	@Autowired
	Environment environment;
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorInfo> exceptionHandler(Exception exception){
		ErrorInfo error = new ErrorInfo();
		String errorMsg = exception.getMessage();
		error.setErrorMessage(errorMsg);
		error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.setTimestampInfo(LocalDateTime.now());
		logger.debug(error.toString());
		return new ResponseEntity<ErrorInfo>(error, HttpStatus.INTERNAL_SERVER_ERROR);	
	}
	
	@ExceptionHandler(ResourcesExceptionHandler.class)
	public ResponseEntity<ErrorInfo> resourceNotFoundExceptionHandler(ResourcesExceptionHandler resourcesExceptionHandler){
		ErrorInfo error = new ErrorInfo();
		error.setErrorMessage(resourcesExceptionHandler.getMessage());
		error.setErrorCode(HttpStatus.NOT_FOUND.value());
		error.setTimestampInfo(LocalDateTime.now());
		logger.debug(error.toString());
		return new ResponseEntity<ErrorInfo>(error, HttpStatus.NOT_FOUND);	
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorInfo> exceptionHandler(MethodArgumentNotValidException exception){
		ErrorInfo error = new ErrorInfo();
		String errorMsg = exception.getBindingResult().getAllErrors().stream().map(x->x.getDefaultMessage())
				.collect(Collectors.joining(", "));
		error.setErrorMessage(errorMsg);
		error.setErrorCode(HttpStatus.BAD_REQUEST.value());
		error.setTimestampInfo(LocalDateTime.now());
		logger.debug(error.toString());
		return new ResponseEntity<ErrorInfo>(error, HttpStatus.BAD_REQUEST);	
	}
}
