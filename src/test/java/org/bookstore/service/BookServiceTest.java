package org.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Set;
import org.bookstore.dto.book.BookDto;
import org.bookstore.dto.book.CreateBookRequestDto;
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
    @DisplayName("Save new book")
    void saveBook_validBookDto_BookDto() {
        Category category = new Category();
        category.setId(1L);
        category.setName("name");
        category.setDescription("desc");

        Book book = new Book();
        book.setAuthor("author");
        book.setTitle("title");
        book.setCategories(Set.of(category));
        book.setIsbn("isbn-000001");
        book.setPrice(BigDecimal.TEN);
        book.setDescription("descr");
        book.setCoverImage("image");

        CreateBookRequestDto request = new CreateBookRequestDto(
                "title",
                "author",
                "isbn-000001",
                BigDecimal.TEN,
                "descr",
                "image",
                Set.of(1L)
        );

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
}
