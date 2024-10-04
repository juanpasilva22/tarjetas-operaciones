package com.operation.creditcards.app.advice;

import com.operation.creditcards.app.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException exception) {
		Map<String, String> map =  new HashMap<>();
		exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
			map.put(fieldError.getField(), fieldError.getDefaultMessage());
		});

		return map;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(CustomException.class)
	public Map<String, String> handleCustomExceptionHandler(CustomException exception) {
		Map<String, String> map =  new HashMap<>();
		map.put("error", exception.getMessage());
		return map;
	}
}