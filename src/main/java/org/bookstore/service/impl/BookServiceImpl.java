package org.bookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookstore.dto.book.BookDto;
import org.bookstore.dto.book.CreateBookRequestDto;
import org.bookstore.exceptions.EntityNotFoundException;
import org.bookstore.mapper.BookMapper;
import org.bookstore.model.Book;
import org.bookstore.repository.BookRepository;
import org.bookstore.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book savedBook = bookRepository
                .save(bookMapper.toEntity(bookRequestDto));
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("No book with id " + id));
    }

    @Override
    public List<Book> findAllByCategoryId(Long id) {
        return bookRepository.findAllByCategories_Id(id);
    }

    @Override
    public BookDto updateBookById(Long id, CreateBookRequestDto book) {
        Book bookFromDb = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't update book with id " + id));
        bookMapper.updateBook(book, bookFromDb);
        return bookMapper.toDto(bookRepository.save(bookFromDb));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
