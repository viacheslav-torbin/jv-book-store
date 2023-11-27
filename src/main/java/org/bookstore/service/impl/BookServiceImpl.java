package org.bookstore.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bookstore.dto.book.BookDto;
import org.bookstore.dto.book.CreateBookRequestDto;
import org.bookstore.exceptions.EntityNotFoundException;
import org.bookstore.mapper.BookMapper;
import org.bookstore.model.Book;
import org.bookstore.model.Category;
import org.bookstore.repository.BookRepository;
import org.bookstore.repository.CategoryRepository;
import org.bookstore.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book savedBook = bookRepository
                .save(bookMapper.toEntity(bookRequestDto));
        setCategories(bookRequestDto, savedBook);
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
    public List<BookDto> findAllByCategoryId(Long id, Pageable pageable) {
        return bookRepository.findAllByCategoriesId(id, pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
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
        bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No book with id " + id));
        bookRepository.deleteById(id);
    }

    private void setCategories(CreateBookRequestDto requestDto, Book book) {
        Set<Category> categorySet = requestDto.categoryIds().stream()
                .map(categoryRepository::getReferenceById)
                .collect(Collectors.toSet());
        book.setCategories(categorySet);
    }
}
