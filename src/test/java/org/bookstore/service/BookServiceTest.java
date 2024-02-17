package org.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.bookstore.dto.book.BookDto;
import org.bookstore.dto.book.CreateBookRequestDto;
import org.bookstore.exceptions.EntityNotFoundException;
import org.bookstore.mapper.BookMapper;
import org.bookstore.mapper.impl.BookMapperImpl;
import org.bookstore.model.Book;
import org.bookstore.model.Category;
import org.bookstore.repository.BookRepository;
import org.bookstore.repository.CategoryRepository;
import org.bookstore.service.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Spy
    private BookMapper bookMapper = new BookMapperImpl();

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Save valid book")
    void save_validRequestDto_BookDto() {
        Category category = new Category();
        category.setId(1L);
        category.setName("name");
        category.setDescription("desc");

        CreateBookRequestDto request = new CreateBookRequestDto(
                "title",
                "author",
                "isbn-000001",
                BigDecimal.TEN,
                "descr",
                "image",
                Set.of(1L)
        );

        Book book = new Book();
        book.setAuthor(request.author());
        book.setTitle(request.title());
        book.setCategories(Set.of(category));
        book.setIsbn(request.isbn());
        book.setPrice(request.price());
        book.setDescription(request.description());
        book.setCoverImage(request.coverImage());

        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toEntity(request)).thenReturn(book);
        when(categoryRepository.getReferenceById(1L)).thenReturn(category);
        BookDto dto = bookService.save(request);
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", request.title())
                .hasFieldOrPropertyWithValue("author", request.author())
                .hasFieldOrPropertyWithValue("isbn", request.isbn())
                .hasFieldOrPropertyWithValue("price", request.price())
                .hasFieldOrPropertyWithValue("description", request.description())
                .hasFieldOrPropertyWithValue("coverImage", request.coverImage())
                .hasFieldOrPropertyWithValue("categoriesIds", request.categoryIds());
    }

    @Test
    @DisplayName("Get book with valid id")
    void getBookById_validId_BookDto() {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Author");
        book.setTitle("Title");
        book.setIsbn("553322");
        book.setPrice(BigDecimal.valueOf(125.55));
        book.setDescription("some desc");
        book.setCoverImage("some url");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDto actual = bookService.findById(1L);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", book.getId())
                .hasFieldOrPropertyWithValue("title", book.getTitle())
                .hasFieldOrPropertyWithValue("author", book.getAuthor())
                .hasFieldOrPropertyWithValue("price", book.getPrice())
                .hasFieldOrPropertyWithValue("description", book.getDescription())
                .hasFieldOrPropertyWithValue("coverImage", book.getCoverImage());
    }

    @Test
    @DisplayName("Get book with not existing id")
    void getBookById_notValidId_Exception() {
        Long id = 100L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findById(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Update existing book with valid request")
    void updateBook_validRequestDto_BookDto() {
        Long id = 1L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Title",
                "Author",
                "553322",
                BigDecimal.valueOf(125.55),
                "some desc",
                "some url",
                Set.of(id));

        Category category = new Category();
        category.setId(id);

        Book book = new Book();
        book.setId(id);
        book.setAuthor(requestDto.author());
        book.setTitle(requestDto.title());
        book.setIsbn(requestDto.isbn());
        book.setPrice(requestDto.price());
        book.setDescription(requestDto.description());
        book.setCoverImage(requestDto.coverImage());
        book.setCategories(Set.of(category));

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        BookDto actual = bookService.updateBookById(id, requestDto);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("title", requestDto.title())
                .hasFieldOrPropertyWithValue("author", requestDto.author())
                .hasFieldOrPropertyWithValue("price", requestDto.price())
                .hasFieldOrPropertyWithValue("description", requestDto.description())
                .hasFieldOrPropertyWithValue("coverImage", requestDto.coverImage())
                .hasFieldOrPropertyWithValue("categoriesIds", requestDto.categoryIds());
    }
}
