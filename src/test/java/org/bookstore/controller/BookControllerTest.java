package org.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;
import org.bookstore.dto.book.BookDto;
import org.bookstore.dto.book.CreateBookRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {
        "classpath:/scripts/categories/create-categories.sql",
        "classpath:/scripts/books/create-books.sql"
})
@Sql(scripts = {
        "classpath:scripts/categories/delete-categories.sql",
        "classpath:scripts/books/delete-books.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    private static ObjectMapper objectMapper;

    @BeforeAll
    @SneakyThrows
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Create book with valid dto")
    @Sql(scripts = {
            "classpath:/scripts/books/delete-books.sql",
            "classpath:/scripts/categories/create-categories.sql"
    })
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void createBook_validRequestDto_returnsDto() throws Exception {
        CreateBookRequestDto request = new CreateBookRequestDto(
                "title",
                "author",
                "12345678900",
                BigDecimal.TEN,
                "descr",
                "image",
                Set.of(1L)
        );

        MvcResult result = mockMvc.perform(post("/books")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("title", request.title())
                .hasFieldOrPropertyWithValue("author", request.author())
                .hasFieldOrPropertyWithValue("price", request.price())
                .hasFieldOrPropertyWithValue("description", request.description())
                .hasFieldOrPropertyWithValue("coverImage", request.coverImage())
                .hasFieldOrPropertyWithValue("categoriesIds", request.categoryIds());
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    @DisplayName("Getting all books")
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    void getAll_existingBooks_ListBookDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> actual = Arrays.stream(objectMapper.readValue(
                        result.getResponse().getContentAsString(), BookDto[].class))
                .toList();
        assertThat(actual).hasSize(3);
        assertThat(actual)
                .element(0)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("title", "book-1")
                .hasFieldOrPropertyWithValue("author", "Alice")
                .hasFieldOrPropertyWithValue("isbn", "isbn-000001")
                .hasFieldOrPropertyWithValue("price", new BigDecimal("200.00"))
                .hasFieldOrPropertyWithValue("description", "descr_1")
                .hasFieldOrPropertyWithValue("coverImage", "image_1");
    }

    @Test
    @DisplayName("Getting book by id")
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    void getBookById_existingBook_BookDto() throws Exception {
        long id = 1;
        MvcResult result = mockMvc.perform(get("/books/" + id))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class
        );

        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("title", "book-1")
                .hasFieldOrPropertyWithValue("author", "Alice")
                .hasFieldOrPropertyWithValue("isbn", "isbn-000001")
                .hasFieldOrPropertyWithValue("price", new BigDecimal("200.00"))
                .hasFieldOrPropertyWithValue("description", "descr_1")
                .hasFieldOrPropertyWithValue("coverImage", "image_1");
    }

    @Test
    @DisplayName("Updating book by id")
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    void updateBookById_existingBook_BookDto() throws Exception {
        long id = 1;
        CreateBookRequestDto request = new CreateBookRequestDto(
                "title",
                "author_changed",
                "12345678900",
                BigDecimal.TEN,
                "descr",
                "image_changed",
                Set.of(1L)
        );

        MvcResult result = mockMvc.perform(put("/books/" + id)
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class
        );

        assertThat(actual)
                .hasFieldOrPropertyWithValue("author", "author_changed")
                .hasFieldOrPropertyWithValue("coverImage", "image_changed");
    }

    @Test
    @DisplayName("Update non-existing book")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    @Sql(scripts = "classpath:/scripts/books/delete-books.sql")
    void updateBookById_nonExistingBook_exception() throws Exception {
        long id = 1;
        CreateBookRequestDto request = new CreateBookRequestDto(
                "title",
                "author_changed",
                "12345678900",
                BigDecimal.TEN,
                "descr",
                "image_changed",
                Set.of(1L)
        );

        mockMvc.perform(put("/books/" + id)
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("Delete existing book")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void deleteById_existingBook_Ok() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @DisplayName("Find non existing book")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void findById_nonExistingBook_Exception() throws Exception {
        mockMvc.perform(get("/books/100"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("Update non existing book")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void updateById_nonExistingBook_Exception() throws Exception {
        long id = 100;
        CreateBookRequestDto request = new CreateBookRequestDto(
                "title",
                "author_changed",
                "12345678900",
                BigDecimal.TEN,
                "descr",
                "image_changed",
                Set.of(1L)
        );

        mockMvc.perform(put("/books/" + id)
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    @DisplayName("Delete non existing book")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void deleteById_nonExistingBook_Exception() throws Exception {
        mockMvc.perform(delete("/books/100"))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
