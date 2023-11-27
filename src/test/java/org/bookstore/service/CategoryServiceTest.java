package org.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.bookstore.dto.category.CategoryDto;
import org.bookstore.dto.category.CreateCategoryRequestDto;
import org.bookstore.mapper.CategoryMapper;
import org.bookstore.mapper.impl.CategoryMapperImpl;
import org.bookstore.model.Category;
import org.bookstore.repository.CategoryRepository;
import org.bookstore.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Spy
    private CategoryMapper categoryMapper = new CategoryMapperImpl();

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Save new category")
    void saveCategory_validCategoryDto_CategoryDto() {
        Category category = new Category();
        category.setDescription("descr");
        category.setName("name");

        CreateCategoryRequestDto request = new CreateCategoryRequestDto(
                "name",
                "descr"
        );

        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toEntity(request)).thenReturn(category);
        CategoryDto dto = categoryService.save(request);
        assertThat(dto)
                .hasFieldOrPropertyWithValue("name", request.name())
                .hasFieldOrPropertyWithValue("description", request.description());
    }
}
