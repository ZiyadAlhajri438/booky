//package com.example.assignment.mapper;
//
//import com.example.assignment.dto.Author;
//import com.example.assignment.dto.BookDto;
//import com.example.assignment.dto.OpenLibraryResponse;
//import com.example.assignment.entity.Book;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//@Mapper(componentModel = "spring")
//public interface BookyMapper {
//
//    @Mapping(target = "isbn", source = "isbn")
//    @Mapping(target = "title", source = "bookData.title")
//    @Mapping(target = "author", source = "authorName.authorName")
//    @Mapping(target = "pageCount", source = "bookData.numberOfPages")
//    @Mapping(target = "coverUrl", expression = "java(getCoverUrl(bookData))")
//    Book toEntity(OpenLibraryResponse bookData, String isbn, Author authorName);
//
//    BookDto toDto(Book book);
//
//    default String getCoverUrl(OpenLibraryResponse bookData) {
//        if (bookData.getCovers() != null && !bookData.getCovers().isEmpty()) {
//            return "https://covers.openlibrary.org/b/oclc/" + bookData.getCovers().get(0) + "-S.jpg";
//        }
//        return null;
//    }
//}
