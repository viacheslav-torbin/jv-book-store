package org.bookstore.service;

import java.util.List;
import org.bookstore.dto.book.BookDto;
import org.bookstore.dto.book.CreateBookRequestDto;
import org.bookstore.model.Book;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    List<Book> findAllByCategoryId(Long id, Pageable pageable);

    BookDto updateBookById(Long id, CreateBookRequestDto updatedBook);

    void deleteById(Long id);
}
