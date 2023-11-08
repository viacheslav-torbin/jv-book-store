package org.bookstore.dto.book;

import java.math.BigDecimal;
import java.util.Set;
import org.bookstore.model.Category;

public record BookDto(Long id,
                      String title,
                      String author,
                      String isbn,
                      BigDecimal price,
                      String description,
                      String coverImage,
                      Set<Category> categories) {
    public void setCategories(Set<Category> set) {

    }
}
