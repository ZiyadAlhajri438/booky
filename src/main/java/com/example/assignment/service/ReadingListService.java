package com.example.assignment.service;

import com.example.assignment.dto.Author;
import com.example.assignment.dto.BookDto;
import com.example.assignment.dto.OpenLibraryResponse;
import com.example.assignment.dto.ReadingListDto;
import com.example.assignment.entity.Book;
import com.example.assignment.entity.ReadingList;
import com.example.assignment.repository.BookRepository;
import com.example.assignment.repository.ReadingListRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ReadingListService {
    private final ReadingListRepository readingListRepository;
    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;

    public ReadingListDto createReadingList(String name) {
        ReadingList readingList = new ReadingList();
        readingList.setName(name);
        return convertToDTO(readingListRepository.save(readingList));
    }

    public ReadingListDto addBookToReadingList(String readingListName, String isbn) {
        ReadingList readingList = readingListRepository.findById(readingListName)
                .orElseThrow(() -> new ResourceNotFoundException("Reading list not found"));

        if (readingList.getBooks().stream().anyMatch(b -> b.getIsbn().equals(isbn))) {
            throw new DuplicateResourceException("Book already in list");
        }

        Book book = bookRepository.findByIsbn(isbn).orElseGet(() -> fetchAndSaveBook(isbn));

        readingList.getBooks().add(book);
        return convertToDTO(readingListRepository.save(readingList));
    }

    private Book fetchAndSaveBook(String isbn) {
        OpenLibraryResponse bookData = restTemplate.getForObject("https://openlibrary.org/isbn/" + isbn + ".json", OpenLibraryResponse.class);

        if (bookData == null) {
            throw new ResourceNotFoundException("Book not found with ISBN: " + isbn);
        }

        if (bookRepository.existsByIsbn(isbn)) {
            throw new DuplicateResourceException("Book already exists");
        }

        Author authorData = bookData.getAuthors().stream().findFirst().map(author -> restTemplate.getForObject("https://openlibrary.org" + author.getKey() + ".json", Author.class)).orElse(null);

        return bookRepository.save(Book.builder().isbn(isbn).title(bookData.getTitle()).author(authorData != null ? authorData.getAuthorName() : null).pageCount(bookData.getNumberOfPages()).coverUrl(bookData.getCovers() != null && !bookData.getCovers().isEmpty() ? "https://covers.openlibrary.org/b/oclc/" + bookData.getCovers().get(0) + "-S.jpg" : null).build());
    }

    public Page<Book> getBooksInReadingList(String readingListName, Pageable pageable) {
        List<Book> books = readingListRepository.findById(readingListName).orElseThrow(() -> new ResourceNotFoundException("Reading list not found")).getBooks();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), books.size());

        return new PageImpl<>(books.isEmpty() ? Collections.emptyList() : books.subList(start, end), pageable, books.size());
    }

    private ReadingListDto convertToDTO(ReadingList readingList) {
        return ReadingListDto.builder()
                .name(readingList.getName())
                .books(readingList.getBooks() == null ? Collections.emptyList()
                        : readingList.getBooks().stream().map(book -> BookDto.builder()
                        .isbn(book.getIsbn())
                        .title(book.getTitle())
                        .author(book.getAuthor()).pageCount(book.getPageCount()).coverUrl(book.getCoverUrl()).build())
                        .collect(Collectors.toList())).build();
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException extends RuntimeException {
    ResourceNotFoundException(String message) {
        super(message);
    }
}

@ResponseStatus(HttpStatus.CONFLICT)
class DuplicateResourceException extends RuntimeException {
    DuplicateResourceException(String message) {
        super(message);
    }
}

