package com.dimas.bbbank.service.exception;

import com.dimas.bbbank.exception.ApiAccessDeniedException;
import com.dimas.bbbank.exception.ApiBaseException;
import com.dimas.bbbank.exception.ElementAlreadyExistsException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.naming.NoPermissionException;
import javax.naming.OperationNotSupportedException;
import javax.validation.ConstraintViolationException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.TreeMap;

import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;

@Slf4j
@Service
public class ExceptionCodeMapper {

    @Getter
    private final Map<ErrorInfo, List<Class<? extends Exception>>> exceptionMap = new TreeMap<>();

    {
        exceptionMap.put(new ErrorInfo(1L, "COMMON_ERROR"), List.of(Exception.class));
        exceptionMap.put(new ErrorInfo(3L, "ILLEGAL_ARGUMENT"), List.of(IllegalArgumentException.class));
        exceptionMap.put(new ErrorInfo(4L, "NO_SUCH_ELEMENT"), List.of(NoSuchElementException.class));
        exceptionMap.put(new ErrorInfo(5L, "INVALID_FORMAT"), List.of(InvalidFormatException.class));
        exceptionMap.put(new ErrorInfo(6L, "BIND_EXCEPTION"), List.of(BindException.class));
        exceptionMap.put(new ErrorInfo(7L, "CONSTRAINT_VIOLATION"), List.of(ConstraintViolationException.class));
        exceptionMap.put(new ErrorInfo(8L, "METHOD_ARGUMENT_NOT_VALID"), List.of(MethodArgumentNotValidException.class));
        exceptionMap.put(new ErrorInfo(10L, "NO_PERMISSION"), List.of(NoPermissionException.class));
        exceptionMap.put(new ErrorInfo(11L, "OPERATION_NOT_SUPPORTED"), List.of(OperationNotSupportedException.class));
        exceptionMap.put(new ErrorInfo(12L, "DATABASE_ERROR"), List.of(DataIntegrityViolationException.class));
        exceptionMap.put(new ErrorInfo(101L, "API_EXCEPTION"), List.of(ApiBaseException.class));
        exceptionMap.put(new ErrorInfo(102L, "ELEMENT_ALREADY_EXISTS"), List.of(ElementAlreadyExistsException.class));
        exceptionMap.put(new ErrorInfo(104L, "ACCESS_DENIED"), List.of(ApiAccessDeniedException.class));
    }

    public ExceptionCodeMapper add(ErrorInfo errorInfo, List<Class<? extends Exception>> exceptions) {
        notNull(errorInfo, "errorInfo cannot be null");
        notEmpty(exceptions, "exceptions cannot empty");
        if (exceptionMap.containsKey(errorInfo)) throw new IllegalArgumentException("ExceptionCodeMapper already contains definition for errorInfo " + errorInfo);
        if (containsAnyException(exceptions)) log.warn("ExceptionCodeMapper already contains exception from the list: " + exceptions + " If you define it with higher priority it will override default mapping");
        exceptionMap.put(errorInfo, exceptions);
        return this;
    }

    public ErrorInfo mapException(Class<? extends Exception> exceptionCls) {
        return exceptionMap.entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(type -> type.isAssignableFrom(exceptionCls)))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(new ErrorInfo(1L, "COMMON_ERROR"));
    }

    private boolean containsAnyException(List<Class<? extends Exception>> exceptions) {
        return exceptionMap.values().stream()
                .flatMap(Collection::stream)
                .anyMatch(exceptions::contains);
    }

    @Data
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @AllArgsConstructor
    public static class ErrorInfo implements Comparable<ErrorInfo> {
        @EqualsAndHashCode.Include
        Long code;
        String name;


        @Override
        public int compareTo(ErrorInfo other) {
            if (Objects.equals(other.code, this.code)) {
                return 0;
            }
            return other.code > this.code ? 1 : -1;
        }

    }

}
