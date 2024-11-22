package be.atheneumboom.bibliotheek.exceptions;

import be.atheneumboom.bibliotheek.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BibliotheekException.class)
    public ResponseEntity<Response> handleBibliotheekException(BibliotheekException bibliotheekException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Response.builder().
                        timeStamp(LocalDateTime.now())
                        //.developerMessage(bibliotheekException.getCause().toString())  //stacktrace
                        .message(bibliotheekException.getMessage())
                        .build());
    }
}
