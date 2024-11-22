package be.atheneumboom.bibliotheek.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Data
public class BibliotheekException extends RuntimeException {
    String message;
    public BibliotheekException(String s) {
        message=s;
    }
}
