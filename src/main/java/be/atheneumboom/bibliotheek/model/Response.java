package be.atheneumboom.bibliotheek.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@SuperBuilder // builder pattern: zie controllers
@JsonInclude(JsonInclude.Include.NON_NULL) //only include values that are not null.
//Bijvoorbeeld bij succes zal message leeg zijn. De annotatie toont niets dat null is en maakt er geen probleem van
public class Response {
    protected LocalDateTime timeStamp;
    protected Integer statusCode;
    protected HttpStatus status;
    protected String reason; //reason for error
    protected String message; //succesMessage voor frontend
    protected String developerMessage;  //
    protected Map<?,?> data;

}
