package org.bookstore.service;

import java.util.List;
import org.bookstore.dto.BookDto;
import org.bookstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    BookDto updateBookById(Long id, CreateBookRequestDto updatedBook);

    void deleteById(Long id);
}
