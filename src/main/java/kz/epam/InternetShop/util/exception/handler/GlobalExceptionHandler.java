package kz.epam.InternetShop.util.exception.handler;

import kz.epam.InternetShop.util.exception.BadRequestException;
import kz.epam.InternetShop.util.exception.NotAvailableGoodsException;
import kz.epam.InternetShop.util.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NotFoundException.class, IllegalArgumentException.class})
    public ResponseEntity<?> notFoundException(NotFoundException ex, WebRequest request) {
        return responseEntity(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequestException(BadRequestException ex, WebRequest request) {
        return responseEntity(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAvailableGoodsException.class)
    public ResponseEntity<?> notAvailableGoodsException(NotAvailableGoodsException ex, WebRequest request) {
        return responseEntity(ex, request, HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<?> responseEntity(Exception ex, WebRequest request, HttpStatus status) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, status);
    }
}
