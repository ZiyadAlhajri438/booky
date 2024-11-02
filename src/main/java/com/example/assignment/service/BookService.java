package com.example.assignment.service;

import com.example.assignment.dto.Author;
import com.example.assignment.dto.BookDto;
import com.example.assignment.dto.OpenLibraryResponse;
import com.example.assignment.entity.Book;
import com.example.assignment.repository.BookRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@Service
@Slf4j
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;

    public BookDto addBookByIsbn(String isbn) throws Exception {
        try {
            OpenLibraryResponse response = Optional.ofNullable(
                            restTemplate.getForObject("https://openlibrary.org/isbn/" + isbn + ".json", OpenLibraryResponse.class))
                    .orElseThrow(() -> new Exception("Book not found with ISBN: " + isbn));

            Author authorName = response.getAuthors().stream()
                    .findFirst()
                    .map(author -> restTemplate.getForObject("https://openlibrary.org" + author.getKey() + ".json", Author.class))
                    .orElse(null);

            return saveBook(response, isbn, authorName);
        } catch (RestClientException e) {
            log.error("Error fetching book details: {}", e.getMessage());
            throw new Exception("Error fetching book with ISBN: " + isbn);
        }
    }

    public BookDto saveBook(OpenLibraryResponse bookData, String isbn, Author authorName) throws Exception {
        if (bookRepository.existsByIsbn(isbn)) {
            throw new Exception("This ISBN it was stored");
        }

        Book book = Book.builder()
                .isbn(isbn)
                .title(bookData.getTitle())
                .author(authorName != null ? authorName.getAuthorName() : null)
                .pageCount(bookData.getNumberOfPages())
                .coverUrl(Optional.ofNullable(bookData.getCovers())
                        .filter(covers -> !covers.isEmpty())
                        .map(covers -> "https://covers.openlibrary.org/b/oclc/" + covers.get(0) + "-S.jpg")
                        .orElse(null))
                .build();

        return convertToBookDto(bookRepository.save(book));
    }

    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    private BookDto convertToBookDto(Book book) {
        return BookDto.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .pageCount(book.getPageCount())
                .coverUrl(book.getCoverUrl())
                .build();
    }
}