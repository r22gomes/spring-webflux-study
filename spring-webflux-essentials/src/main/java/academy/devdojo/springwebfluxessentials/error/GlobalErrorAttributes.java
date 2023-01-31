package academy.devdojo.springwebfluxessentials.error;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributesMap = super.getErrorAttributes(request, options);
        Throwable throwable = getError(request);
        if (throwable instanceof ResponseStatusException) {
            ResponseStatusException ex = (ResponseStatusException) throwable;
            errorAttributesMap.put("message", ex.getMessage());
            errorAttributesMap.put("developerMessage", "A ResponseStatusException Happened");
            return errorAttributesMap;
        }
        if (throwable instanceof IllegalArgumentException) {
            IllegalArgumentException ex = (IllegalArgumentException) throwable;
            errorAttributesMap.put("message", ex.getMessage());
            errorAttributesMap.put("status", HttpStatus.BAD_REQUEST.value());
            errorAttributesMap.put("developerMessage", "A ResponseStatusException Happened");
            return errorAttributesMap;
        }

        return errorAttributesMap;
    }

}
