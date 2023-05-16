package com.dimas.bbbank.service.exception;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dimas.bbbank.util.Util.API_ERROR_LBL_ERRORS;
import static com.dimas.bbbank.util.Util.API_ERROR_LBL_MESSAGE;
import static com.dimas.bbbank.util.Util.API_ERROR_LBL_PATH;
import static com.dimas.bbbank.util.Util.API_ERROR_LBL_STATUS;
import static com.dimas.bbbank.util.Util.API_ERROR_LBL_TIMESTAMP;
import static com.dimas.bbbank.util.Util.TIMESTAMP_DATE_FORMAT;
import static com.dimas.bbbank.util.Util.mapToString;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExceptionResponseHandler implements IExceptionResponseHandler {

    private final ObjectMapper objectMapper;
    private final ExceptionCodeMapper codeMapper;

    private final boolean includeStackTrace = false;

    public ResponseEntity<Map<String, String>> getErrorResponse(HttpServletRequest request, HttpStatus status, Exception ex) {
        if (status.is4xxClientError()) {
            log.warn(ex.getMessage(), ex);
        } else if (status.is5xxServerError()) {
            log.error(ex.getMessage(), ex);
        }
        return ResponseEntity.status(status).body(getExResponseMap(request, status, ex));
    }

    @SneakyThrows
    public String exResponseToJson(HttpServletRequest request, HttpStatus status, Exception ex) {
        return objectMapper.writeValueAsString(getExResponseMap(request, status, ex));
    }

    public HashMap<String, String> getExResponseMap(HttpServletRequest request, HttpStatus status, Exception ex) {
        final var errorInfo = codeMapper.mapException(ex.getClass());
        final var map = new HashMap<>(Map.of(
                API_ERROR_LBL_TIMESTAMP, new SimpleDateFormat(TIMESTAMP_DATE_FORMAT).format(new Date()),
                API_ERROR_LBL_PATH, request.getRequestURI(),
                API_ERROR_LBL_STATUS, String.valueOf(status.value()),
                API_ERROR_LBL_ERRORS, String.format("%s[%d]", errorInfo.getName(), errorInfo.getCode()),
                API_ERROR_LBL_MESSAGE, getExceptionMessage(ex, status)
        ));

        if (includeStackTrace) {
            final var stringWriter = new StringWriter();
            final var printWriter = new PrintWriter(stringWriter);
            ex.printStackTrace(printWriter);
            printWriter.flush();
            map.put("trace", stringWriter.toString());
        }
        return map;
    }

    @SneakyThrows
    public String extractResponseExceptionMessage(Exception e) {
        TypeReference<Map<String, String>> typeRef = new TypeReference<>() {
        };
        var map = objectMapper.readValue(e.getMessage(), typeRef);
        return map.get("message");
    }

    private String getExceptionMessage(Exception ex, HttpStatus status) {
        if (ex instanceof BindException) {
            return extractBindingException(((BindException) ex).getBindingResult());
        }
        if (ex instanceof ConstraintViolationException) {
            return extractConstraintViolationException((ConstraintViolationException) ex);
        }
        if (ex instanceof DataIntegrityViolationException) {
            return ex.getCause().getCause().getMessage();
        }
        return ex.getMessage() != null && !ex.getMessage().isEmpty() ? ex.getMessage() : status.getReasonPhrase();
    }

    private String extractConstraintViolationException(ConstraintViolationException e) {
        return e.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(";"));
    }

    private String extractBindingException(BindingResult bindingResult) {
        var errors = new HashMap<String, String>();
        bindingResult.getAllErrors().forEach((error) -> {
            final var fieldName = getFieldName(error);
            final var errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return mapToString(errors);
    }

    private String getFieldName(ObjectError error) {
        if (error instanceof FieldError) {
            return ((FieldError) error).getField();
        } else {
            return "";
        }
    }

}
