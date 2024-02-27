package exercise.dto;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ProductUpdateDTO {
    private String title;
    private int price;
}