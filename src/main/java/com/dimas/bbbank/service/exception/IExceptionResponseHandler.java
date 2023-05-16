package com.dimas.bbbank.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public interface IExceptionResponseHandler {

    ResponseEntity<Map<String, String>> getErrorResponse(HttpServletRequest request, HttpStatus status, Exception ex);

    String exResponseToJson(HttpServletRequest request, HttpStatus status, Exception ex);

    HashMap<String, String> getExResponseMap(HttpServletRequest request, HttpStatus status, Exception ex);

    String extractResponseExceptionMessage(Exception e);
}
