package org.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record CreateBookRequestDto(
        Long id,
        @NotBlank String title,
        @NotBlank
        String author,
        @NotBlank
        String isbn,
        @NotNull
        @PositiveOrZero
        BigDecimal price,
        String description,
        String coverImage
)
{}
