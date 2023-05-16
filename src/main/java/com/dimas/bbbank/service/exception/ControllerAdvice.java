package com.dimas.bbbank.service.exception;

import com.dimas.bbbank.exception.ApiAccessDeniedException;
import com.dimas.bbbank.exception.ApiBaseException;
import com.dimas.bbbank.exception.ElementAlreadyExistsException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.codec.DecodingException;
import org.springframework.core.codec.EncodingException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.util.NestedServletException;

import javax.naming.NoPermissionException;
import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.net.ConnectException;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
public class ControllerAdvice {

    protected final IExceptionResponseHandler exceptionResponseHandler;

    @ExceptionHandler({
            Exception.class,
            InterruptedException.class,
            ExecutionException.class,
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> extServiceException(Exception ex, HttpServletRequest exchange) {
        return exceptionResponseHandler.getErrorResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            NoSuchElementException.class,
            InvalidFormatException.class,
            DateTimeParseException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            WebExchangeBindException.class,
            DecodingException.class,
            EncodingException.class,
            ConstraintViolationException.class,
            MethodArgumentTypeMismatchException.class,
            HttpRequestMethodNotSupportedException.class,
            ServletRequestBindingException.class,
            MissingServletRequestPartException.class,
            BindException.class,
            DataIntegrityViolationException.class,
            NestedServletException.class,
            ElementAlreadyExistsException.class,
            HttpMediaTypeNotSupportedException.class,
            ApiBaseException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(Exception ex, HttpServletRequest exchange) {
        return exceptionResponseHandler.getErrorResponse(exchange, HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler({
            ApiAccessDeniedException.class,
            OperationNotSupportedException.class,
            NoPermissionException.class,
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Map<String, String>> forbiddenException(Exception ex, HttpServletRequest exchange) {
        return exceptionResponseHandler.getErrorResponse(exchange, HttpStatus.FORBIDDEN, ex);
    }

    @ExceptionHandler({
            ConnectException.class,
            TimeoutException.class
    })
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    public ResponseEntity<Map<String, String>> gatewayTimeoutException(Exception ex, HttpServletRequest exchange) {
        return exceptionResponseHandler.getErrorResponse(exchange, HttpStatus.GATEWAY_TIMEOUT, ex);
    }

    @ExceptionHandler({
            UnsupportedOperationException.class,
    })
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public ResponseEntity<Map<String, String>> handleNotImplemented(Exception ex, HttpServletRequest exchange) {
        return exceptionResponseHandler.getErrorResponse(exchange, HttpStatus.NOT_IMPLEMENTED, ex);
    }

}