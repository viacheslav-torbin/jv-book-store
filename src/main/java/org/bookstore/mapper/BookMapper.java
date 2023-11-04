package org.bookstore.mapper;

import org.bookstore.dto.book.BookDto;
import org.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.bookstore.dto.book.CreateBookRequestDto;
import org.bookstore.model.Book;
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
    void updateBook(CreateBookRequestDto book, @MappingTarget Book entity);
}
