package org.bookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

public record CreateBookRequestDto(
        @NotBlank(message = "Title must not be null or empty")
        @Size(max = 255, message = "Maximum allowed size 255 characters")
        String title,
        @NotBlank(message = "Author must not be null or empty")
        @Size(max = 255, message = "Maximum allowed size 255 characters")
        String author,
        @NotBlank(message = "Isbn must not be null or empty")
        @Size(min = 10, max = 13, message = "Required length 10-13 digits")
        String isbn,
        @NotNull(message = "Price must not be null")
        @Positive(message = "Price must not be negative")
        BigDecimal price,
        @Size(max = 255, message = "Maximum allowed size 255 characters")
        String description,
        @Size(max = 255, message = "Maximum allowed size 255 characters")
        String coverImage,
        @NotNull
        Set<Long> categoryIds) {
}
