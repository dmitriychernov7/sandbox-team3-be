package com.exadel.discountwebapp.exception;

import com.exadel.discountwebapp.exception.exception.client.*;
import com.exadel.discountwebapp.exception.exception.fileupload.*;
import com.exadel.discountwebapp.exception.response.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class WebExceptionHandler {

    private static final String FAILED_FILE_HANDLING_CODE = "COULD_NOT_HANDLE_FILE";
    private static final String FORBIDDEN_CODE = "FORBIDDEN";
    private static final String CONFLICT_CODE = "CONFLICT";
    private static final String AUTHORIZED_EXCEPTION_CODE = "UNAUTHORIZED";
    private static final String FAILED_EMAIL_CODE = "FAILED_TO_SEND_EMAIL";
    private static final String GLOBAL_EXCEPTION_CODE = "INTERNAL_SERVER_ERROR";
    private static final String ERROR_RACE_CONDITION = "ERROR RACE CONDITION DURING CONCURRENCY UPDATE";

    private static final String GLOBAL_EXCEPTION_MESSAGE = "Something is wrong";

    private static final String RESPONSE_CODE_PATTERN = "%s.%s.%s";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = UNPROCESSABLE_ENTITY)
    public ExceptionResponse unprocessableEntityException(MethodArgumentNotValidException ex) {
        FieldError field = ex.getBindingResult().getFieldError();
        String code = String.format(RESPONSE_CODE_PATTERN, field.getObjectName(), field.getField(), UNPROCESSABLE_ENTITY.value());
        return new ExceptionResponse(code, field.getDefaultMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = NOT_FOUND)
    public ExceptionResponse notFoundException(EntityNotFoundException ex) {
        String code = String.format(RESPONSE_CODE_PATTERN, ex.getClazz().getSimpleName(), ex.getFieldName(), NOT_FOUND.value());
        return new ExceptionResponse(code, ex.getMessage());
    }

    @ExceptionHandler(value = IncorrectFilterInputException.class)
    @ResponseStatus(value = BAD_REQUEST)
    public ExceptionResponse badRequestException(IncorrectFilterInputException ex) {
        String code = String.format(RESPONSE_CODE_PATTERN, ex.getClazz().getSimpleName(), ex.getFieldName(), BAD_REQUEST.value());
        return new ExceptionResponse(code, ex.getMessage());
    }

    @ExceptionHandler(ParseException.class)
    @ResponseStatus(value = UNPROCESSABLE_ENTITY)
    public ExceptionResponse unprocessableEntityParseException(ParseException ex) {
        String code = String.format(RESPONSE_CODE_PATTERN, ex.getClazz().getSimpleName(), ex.getFieldName(), UNPROCESSABLE_ENTITY.value());
        return new ExceptionResponse(code, ex.getMessage());
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(value = CONFLICT)
    public ExceptionResponse entityAlreadyExistsException(EntityAlreadyExistsException ex) {
        String code = String.format(RESPONSE_CODE_PATTERN, ex.getClazz().getSimpleName(), ex.getFieldName(), CONFLICT.value());
        return new ExceptionResponse(code, ex.getMessage());
    }

    @ExceptionHandler(EntityAlreadyUsedException.class)
    @ResponseStatus(value = CONFLICT)
    public ExceptionResponse entityAlreadyUsedException(EntityAlreadyUsedException ex) {
        String code = String.format(RESPONSE_CODE_PATTERN, ex.getClazz().getSimpleName(), ex.getFieldName(), CONFLICT.value());
        return new ExceptionResponse(code, ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(value = UNAUTHORIZED)
    public ExceptionResponse badCredentialsException(BadCredentialsException ex) {
        return new ExceptionResponse(AUTHORIZED_EXCEPTION_CODE, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = FORBIDDEN)
    public ExceptionResponse forbiddenException(AccessDeniedException ex) {
        return new ExceptionResponse(FORBIDDEN_CODE, ex.getMessage());
    }

    @ExceptionHandler(FileEmptyException.class)
    @ResponseStatus(value = CONFLICT)
    public ExceptionResponse fileEmptyException(FileEmptyException ex) {
        return new ExceptionResponse(FAILED_FILE_HANDLING_CODE, ex.getMessage());
    }

    @ExceptionHandler(FileOverSizeException.class)
    @ResponseStatus(value = CONFLICT)
    public ExceptionResponse fileOverSizeException(FileOverSizeException ex) {
        return new ExceptionResponse(FAILED_FILE_HANDLING_CODE, ex.getMessage());
    }

    @ExceptionHandler(IncorrectFileUrlException.class)
    @ResponseStatus(value = CONFLICT)
    public ExceptionResponse incorrectFileUrlException(IncorrectFileUrlException ex) {
        return new ExceptionResponse(FAILED_FILE_HANDLING_CODE, ex.getMessage());
    }

    @ExceptionHandler(FileUploadException.class)
    @ResponseStatus(value = BAD_REQUEST)
    public ExceptionResponse fileUploadException(FileUploadException ex) {
        return new ExceptionResponse(FAILED_FILE_HANDLING_CODE, ex.getMessage());
    }

    @ExceptionHandler(FileDestroyException.class)
    @ResponseStatus(value = BAD_REQUEST)
    public ExceptionResponse fileDestroyException(FileDestroyException ex) {
        return new ExceptionResponse(FAILED_FILE_HANDLING_CODE, ex.getMessage());
    }

    @ExceptionHandler(MailException.class)
    @ResponseStatus(value = CONFLICT)
    public ExceptionResponse mailException(MailException ex) {
        return new ExceptionResponse(FAILED_EMAIL_CODE, ex.getMessage());
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseStatus(value = CONFLICT)
    public ExceptionResponse optimisticLockingException(ObjectOptimisticLockingFailureException ex) {
        log.error(ERROR_RACE_CONDITION, ex);
        return new ExceptionResponse(CONFLICT_CODE, ERROR_RACE_CONDITION);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = INTERNAL_SERVER_ERROR)
    public ExceptionResponse globalException(Exception ex) {
        log.error(GLOBAL_EXCEPTION_MESSAGE, ex);
        return new ExceptionResponse(GLOBAL_EXCEPTION_CODE, GLOBAL_EXCEPTION_MESSAGE);
    }
}
