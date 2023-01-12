package academy.devdojo.springwebfluxessentials.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ErrorService {


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDetails> noSuchElement(NoSuchElementException exception) {
        return ResponseEntity.status(404).body(new ErrorDetails(exception.getLocalizedMessage()));
    }

}
