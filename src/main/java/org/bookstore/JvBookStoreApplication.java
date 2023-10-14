package org.bookstore;

import java.math.BigDecimal;
import org.bookstore.model.Book;
import org.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JvBookStoreApplication {

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(JvBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(BookService bookService) {
        return args -> {
            Book book = new Book();
            book.setAuthor("Author");
            book.setDescription("Description");
            book.setPrice(BigDecimal.ZERO);
            book.setTitle("Title");
            book.setIsbn("ISBN");
            bookService.save(book);
            bookService.findAll().forEach(System.out::println);
        };
    }
}
