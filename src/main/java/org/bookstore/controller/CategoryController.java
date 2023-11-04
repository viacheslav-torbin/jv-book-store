package org.bookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookstore.dto.category.CategoryDto;
import org.bookstore.dto.category.CreateCategoryRequestDto;
import org.bookstore.model.Book;
import org.bookstore.model.Category;
import org.bookstore.service.BookService;
import org.bookstore.service.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final BookService bookService;

    @PostMapping (value = "")
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @GetMapping(value = "")
    public List<Category> getAll(@PageableDefault(size = 5) Pageable pageable) {
        return categoryService.findAll(pageable);
    }
    @GetMapping(value = "/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PutMapping(value = "/{id}")
    public CategoryDto updateCategory(@PathVariable Long id, CreateCategoryRequestDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @GetMapping("/{id}/books")
    public List<Book> getBooksByCategoryId(@PathVariable Long id, @PageableDefault(size = 5) Pageable pageable) {
        return bookService.findAllByCategoryId(id);
    }
}
