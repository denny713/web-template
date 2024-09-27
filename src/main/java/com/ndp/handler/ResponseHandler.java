package com.ndp.handler;

import com.ndp.exception.BadRequestException;
import com.ndp.exception.ForbiddenException;
import com.ndp.exception.NotFoundException;
import com.ndp.exception.ServiceException;
import com.ndp.model.dto.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ResponseHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ResponseDto> handlingServiceExc(ServiceException exc, WebRequest req) {
        return new ResponseEntity<>(generateResponse(req, HttpStatus.INTERNAL_SERVER_ERROR,
                exc.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDto> handlingBadReqExc(BadRequestException exc, WebRequest req) {
        return new ResponseEntity<>(generateResponse(req, HttpStatus.BAD_REQUEST,
                exc.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto> handlingNotFoundExc(NotFoundException exc, WebRequest req) {
        return new ResponseEntity<>(generateResponse(req, HttpStatus.NOT_FOUND,
                exc.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ResponseDto> handlingForbiddenExc(ForbiddenException exc, WebRequest req) {
        return new ResponseEntity<>(generateResponse(req, HttpStatus.FORBIDDEN,
                exc.getMessage()), HttpStatus.FORBIDDEN);
    }

    private ResponseDto generateResponse(WebRequest req, HttpStatus status, String msg) {
        return new ResponseDto(status.value(), status.getReasonPhrase(), generateObject(req, status.value(), msg));
    }

    private Map<String, Object> generateObject(WebRequest req, int code, String msg) {
        Map<String, Object> result = new HashMap<>();
        result.put("timestamp", new Date());
        result.put("status", code);
        result.put("error", msg);
        result.put("path", req.getDescription(false));
        return result;
    }
}
