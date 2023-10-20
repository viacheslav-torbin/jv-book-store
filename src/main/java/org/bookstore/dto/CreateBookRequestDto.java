package org.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    private String isbn;
    @PositiveOrZero
    private BigDecimal price;
    private String description;
    private String coverImage;
}
