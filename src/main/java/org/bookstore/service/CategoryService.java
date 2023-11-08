package org.bookstore.service;

import java.util.List;
import org.bookstore.dto.category.CategoryDto;
import org.bookstore.dto.category.CreateCategoryRequestDto;
import org.bookstore.model.Category;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<Category> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryRequestDto categoryDto);

    CategoryDto update(Long id, CreateCategoryRequestDto categoryDto);

    void deleteById(Long id);
}
