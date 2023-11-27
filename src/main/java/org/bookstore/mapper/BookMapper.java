package org.bookstore.mapper;

import java.util.stream.Collectors;
import org.bookstore.dto.book.BookDto;
import org.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.bookstore.dto.book.CreateBookRequestDto;
import org.bookstore.model.Book;
import org.bookstore.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl"
)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toEntity(CreateBookRequestDto bookDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategories(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoriesIds(book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
    }
    void updateBook(CreateBookRequestDto requestDto, @MappingTarget Book book);
}
