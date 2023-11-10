package org.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequestDto(
        @Size(max = 255, message = "Maximum allowed size 255 characters")
        @NotBlank
        String name,
        @Size(max = 255, message = "Maximum allowed size 255 characters")
        String description
){
}
