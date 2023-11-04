package org.bookstore.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bookstore.dto.category.CategoryDto;
import org.bookstore.dto.category.CreateCategoryRequestDto;
import org.bookstore.mapper.CategoryMapper;
import org.bookstore.model.Category;
import org.bookstore.repository.CategoryRepository;
import org.bookstore.service.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    @Override
    public List<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("No category with id " + id));
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        Category savedCategory = categoryMapper.toEntity(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(savedCategory));
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto categoryDto) {
        Category categoryFromDb = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't update category with id " + id));
        categoryMapper.updateCategory(categoryDto, categoryFromDb);
        return categoryMapper.toDto(categoryRepository.save(categoryFromDb));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
