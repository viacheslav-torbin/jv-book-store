package org.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookstore.dto.book.BookDto;
import org.bookstore.dto.book.CreateBookRequestDto;
import org.bookstore.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Books management",description = "Endpoints for book management")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Getting all books")
    @GetMapping
    List<BookDto> getAll(@PageableDefault Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Getting book by id")
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable @Positive Long id) {
        return bookService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creating book")
    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookRequestDto) {
        return bookService.save(bookRequestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Updating book by id")
    @PutMapping("/{id}")
    public void updateBookById(@PathVariable @Positive Long id,
                               @RequestBody @Valid CreateBookRequestDto bookRequestDto) {
        bookService.updateBookById(id, bookRequestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deleting book by id")
    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable @Positive Long id) {
        bookService.deleteById(id);
    }
}
