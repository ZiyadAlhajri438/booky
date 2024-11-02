package com.example.assignment.controller;


import com.example.assignment.dto.BookDto;
import com.example.assignment.dto.ReadingListDto;
import com.example.assignment.entity.Book;
import com.example.assignment.service.BookService;
import com.example.assignment.service.ReadingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booky")
public class BookyController {

    @Autowired
    private BookService bookService;
    @Autowired
    private ReadingListService readingListService;

    @PostMapping(value = "/addBook/{isbn}")
    public ResponseEntity<BookDto> addBook(@PathVariable String isbn) throws Exception {
        BookDto book = bookService.addBookByIsbn(isbn);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @GetMapping(value = "/getAllBook")
    public ResponseEntity<Page<Book>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(books);
    }

    @PostMapping(value = "/createReadingList/{name}")
    public ResponseEntity<ReadingListDto> createReadingList(@PathVariable String name) {
        ReadingListDto readingList = readingListService.createReadingList(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(readingList);
    }

    @PostMapping("/{readingListName}/books/{isbn}")
    public ResponseEntity<ReadingListDto> addBookToReadingList(
            @PathVariable String readingListName,
            @PathVariable String isbn) throws Exception {
        ReadingListDto updatedList = readingListService.addBookToReadingList(readingListName, isbn);
        return ResponseEntity.ok(updatedList);
    }

    @GetMapping("/{readingListName}/books")
    public ResponseEntity<Page<Book>> getBooksInReadingList(
            @PathVariable String readingListName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = readingListService.getBooksInReadingList(readingListName, pageable);
        return ResponseEntity.ok(books);
    }
}
