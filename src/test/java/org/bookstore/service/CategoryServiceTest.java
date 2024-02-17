package org.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.bookstore.dto.category.CategoryDto;
import org.bookstore.dto.category.CreateCategoryRequestDto;
import org.bookstore.exceptions.EntityNotFoundException;
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
    @DisplayName("Save new category with valid request")
    void save_validRequestDto_CategoryDto() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "name",
                "some desc"
        );

        Category category = new Category();
        category.setName(requestDto.name());
        category.setDescription(requestDto.description());

        when(categoryRepository.save(category)).thenReturn(category);

        CategoryDto actual = categoryService.save(requestDto);
        assertThat(actual)
                .hasFieldOrPropertyWithValue("name", requestDto.name())
                .hasFieldOrPropertyWithValue("description", requestDto.description());
    }

    @Test
    @DisplayName("Update existing category with valid request")
    void update_validRequestDto_CategoryDto() {
        Category category = new Category();
        category.setName("name1");
        category.setDescription("desc1");
        category.setId(1L);

        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto("name2", "desc2");

        Category requestCategory = new Category();
        requestCategory.setId(1L);
        requestCategory.setName(requestDto.name());
        requestCategory.setDescription(requestDto.description());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(requestCategory);

        CategoryDto actual = categoryService.update(1L, requestDto);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("name", "name2")
                .hasFieldOrPropertyWithValue("description", "desc2");
    }

    @Test
    @DisplayName("Update not existing category")
    void update_notValidRequestDto_Exception() {
        Long id = 100L;
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "name",
                "desc");

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> categoryService.update(id, requestDto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Get category by valid id")
    void getById_validId_CategoryDto() {
        Category category = new Category();
        category.setName("name");
        category.setDescription("desc");
        category.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryDto actual = categoryService.getById(1L);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("name", "name")
                .hasFieldOrPropertyWithValue("description", "desc");
    }

    @Test
    @DisplayName("Get category by not existing id")
    void getById_notValidId_Exception() {
        Long id = 100L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getById(id))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
