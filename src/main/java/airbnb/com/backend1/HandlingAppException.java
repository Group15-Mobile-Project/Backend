package airbnb.com.backend1;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import airbnb.com.backend1.Exception.BadResultException;
import airbnb.com.backend1.Exception.EntityExistingException;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Exception.ErrorResponse;

@ControllerAdvice
public class HandlingAppException   {
    @ExceptionHandler({EntityNotFoundException.class, BadResultException.class,  EntityExistingException.class})
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handlingEntityException(RuntimeException ex) {
        // ErrorResponse err = new ErrorResponse(ex.getMessage(), ex, LocalDateTime.now());
        ErrorResponse err = new ErrorResponse(Arrays.asList(ex.getMessage())); 
        System.out.println(err);
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handlingArgumentException(MethodArgumentNotValidException ex) {
        // ErrorResponse err = new ErrorResponse(ex.getMessage(), ex, LocalDateTime.now());
        ErrorResponse err = new ErrorResponse(Arrays.asList(ex.getMessage())); 
        System.out.println(err);
        return new ResponseEntity<Object>(err, HttpStatus.BAD_REQUEST);
        
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handlingDataIntegrityException(DataIntegrityViolationException ex) {
       // ErrorResponse err = new ErrorResponse(ex.getMessage(), ex, LocalDateTime.now());
       ErrorResponse err = new ErrorResponse(Arrays.asList(ex.getMessage())); 
       System.out.println(err);
        return new ResponseEntity<Object>(err, HttpStatus.BAD_REQUEST);
        
    }
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handlingEntityException(EmptyResultDataAccessException ex) {
        // ErrorResponse err = new ErrorResponse(ex.getMessage(), ex, LocalDateTime.now());
        ErrorResponse err = new ErrorResponse(Arrays.asList(ex.getMessage())); 
        System.out.println(err);
        return new ResponseEntity<Object>(err, HttpStatus.BAD_REQUEST);
        
    }


}
