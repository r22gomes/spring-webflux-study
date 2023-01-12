package academy.devdojo.springwebfluxessentials.error;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorDetails {

    private final String message;

}
