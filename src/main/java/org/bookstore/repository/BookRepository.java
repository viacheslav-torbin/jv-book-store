package org.bookstore.repository;

import java.util.List;
import org.bookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Book findById(Long id);
}
