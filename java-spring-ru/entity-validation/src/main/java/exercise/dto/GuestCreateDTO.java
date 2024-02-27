package exercise.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GuestCreateDTO {

    @NotBlank
    private String name;

    @Email
    private String email;

    @Size(min=11, max=13)
    @Pattern(regexp = "^\\+\\d*$", message = "Phone number should start with +")
    private String phoneNumber;

    @Pattern(regexp = "\\d{4}", message = "Club card number should contain 4 digits")
    private String clubCard;

    @NotNull
    @FutureOrPresent
    private LocalDate cardValidUntil;
}
