package exercise.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.validation.constraints.*;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;




@Entity
@Table(name = "guests")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Guest {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @NotBlank
    private String name;

    @Email
    private String email;

    @Size(min=11, max=13)
    @Pattern(regexp = "^\\+\\d*$", message = "Phone number should start with +")
    private String phoneNumber;

    @Pattern(regexp = "\\d{4}", message = "Club card number should contain 4 digits")
    private String clubCard;

    @Future
    private LocalDate cardValidUntil;

    @CreatedDate
    private LocalDate createdAt;
}
