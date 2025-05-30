package com.example.library.Books;

import com.example.library.BookWithDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // GET /books - Lista alla böcker med paginering och sorting
    @GetMapping
    public ResponseEntity<Page<Book>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Integer minAvailableCopies) {

        // Skapa Sort objekt
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        // Skapa Pageable objekt
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Book> books;

        // Om inga filter används, returnera alla böcker
        if ((title == null || title.trim().isEmpty()) &&
                (author == null || author.trim().isEmpty()) &&
                (minAvailableCopies == null || minAvailableCopies <= 0)) {
            books = bookService.getAllBooks(pageable);
        } else {
            // Använd avancerad sökning med filter
            books = bookService.searchBooks(title, author, minAvailableCopies, pageable);
        }

        return ResponseEntity.ok(books);
    }

    // GET /books/search - Sök böcker på title eller author (behålls för bakåtkompatibilitet)
    @GetMapping("/search")
    public ResponseEntity<Page<Book>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        // Skapa Sort objekt
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        // Skapa Pageable objekt
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Book> books;

        if (title != null && !title.trim().isEmpty()) {
            books = bookService.searchBooksByTitle(title, pageable);
        } else if (author != null && !author.trim().isEmpty()) {
            books = bookService.searchBooksByAuthor(author, pageable);
        } else {
            books = bookService.getAllBooks(pageable);
        }

        return ResponseEntity.ok(books);
    }

    // GET /books/simple - Enkel lista utan paginering (för bakåtkompatibilitet)
    @GetMapping("/simple")
    public ResponseEntity<List<Book>> getAllBooksSimple() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    // POST /books - Skapa ny bok
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        try {
            Book createdBook = bookService.createBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/details")
    public ResponseEntity<List<BookWithDetailsDTO>> getBooksWithDetails() {
        List<BookWithDetailsDTO> dtos = bookService.getAllBooksWithDetails();
        return ResponseEntity.ok(dtos);
    }

    // Exception handling
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}