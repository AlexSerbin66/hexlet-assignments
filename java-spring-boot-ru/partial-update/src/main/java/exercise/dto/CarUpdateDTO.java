package exercise.dto;

import org.openapitools.jackson.nullable.JsonNullable;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

// BEGIN
@Getter
@Setter
public class CarUpdateDTO {

    @NotNull
    JsonNullable<String> model;

    @NotNull
    JsonNullable<String> manufacturer;

    @NotNull
    JsonNullable<Integer> enginePower;


}
// END
