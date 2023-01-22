package academy.devdojo.springwebfluxessentials.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@With
@Table("show")
public class Show {

    @Id
    private Integer id;

    @NotNull(message = "The name of the show must not be null")
    @NotEmpty(message = "The name of the show must not be empty")
    private String name;

}
