package com.example.library.Books;

import Exceptions.AuthorNotFoundException;
import Exceptions.BookNotFoundException;
import Exceptions.InvalidBookDataException;
import com.example.library.BookWithDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // GET /books Lista alla böcker med paginering och sorting
    @GetMapping
    public ResponseEntity<Page<Book>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Integer minAvailableCopies) {

        try{
        // Skapa Sort objekt
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        // Skapa Pageable objekt
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Book> books;

        // Om inga filter används, return alla böcker
        if ((title == null || title.trim().isEmpty()) &&
                (author == null || author.trim().isEmpty()) &&
                (minAvailableCopies == null || minAvailableCopies <= 0)) {
            books = bookService.getAllBooks(pageable);
        } else {
            // sökning med filter
            books = bookService.searchBooks(title, author, minAvailableCopies, pageable);
        }

        return ResponseEntity.ok(books);
    }  catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /books/{id} Optional return type
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        try {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Alternative endpoint details
    @GetMapping("/{id}/details")
    public ResponseEntity<Book> getBookByIdWithException(@PathVariable Long id) {
        try {
        Book book = bookService.getBookByIdOrThrow(id);
        return ResponseEntity.ok(book);

    } catch (BookNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}


@PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        try {
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    } catch (InvalidBookDataException | AuthorNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
}

    // GET /books/search Sök böcker på title eller author
    @GetMapping("/search")
    public ResponseEntity<Page<Book>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        try{
        // Skapa Sort sortering objekt
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        // Skapa Pageable
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
    } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Search exakt title med Optional return
    @GetMapping("/search/title/{title}")
    public ResponseEntity<Book> findBookByTitle(@PathVariable String title) {
        try {
        Optional<Book> book = bookService.findBookByTitle(title);
        return book.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //   --- Endpoints med query ----


    // Books by author ID
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable Long authorId) {
        try {
        List<Book> books = bookService.getBooksByAuthorId(authorId);
        return ResponseEntity.ok(books);
    } catch (AuthorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Books with minimum copies
    @GetMapping("/stock")
    public ResponseEntity<List<Book>> getBooksWithMinimumCopies(
            @RequestParam(defaultValue = "1") Integer minCopies) {
        try{
        List<Book> books = bookService.getBooksWithMinimumCopies(minCopies);
        return ResponseEntity.ok(books);
    } catch (InvalidBookDataException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Low stock books
    @GetMapping("/low-stock")
    public ResponseEntity<List<Book>> getLowStockBooks(
            @RequestParam(defaultValue = "3") Integer threshold) {
        try {
        List<Book> books = bookService.getLowStockBooks(threshold);
        return ResponseEntity.ok(books);
    } catch (InvalidBookDataException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Advanced search using native SQL
    @GetMapping("/advanced-search")
    public ResponseEntity<List<Book>> advancedSearch(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Integer minCopies) {
        try {
        List<Book> books = bookService.searchBooksAdvanced(title, author, minCopies);
        return ResponseEntity.ok(books);
    } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Stats endpoint
    @GetMapping("/stats/available-count")
    public ResponseEntity<Long> getAvailableBooksCount() {
        try {
        Long count = bookService.getAvailableBooksCount();
        return ResponseEntity.ok(count);
    } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Most available books
    @GetMapping("/most-available")
    public ResponseEntity<List<Book>> getMostAvailableBooks() {
        try {
        List<Book> books = bookService.getMostAvailableBooks();
        return ResponseEntity.ok(books);
    }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /books/simple - Enkel lista utan paginering
    @GetMapping("/simple")
    public ResponseEntity<List<Book>> getAllBooksSimple() {
        try {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/details")
    public ResponseEntity<List<BookWithDetailsDTO>> getBooksWithDetails() {
        try {
        List<BookWithDetailsDTO> dtos = bookService.getAllBooksWithDetails();
        return ResponseEntity.ok(dtos);
    } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

// exempel i postman
//GET /books
//GET /books/1
//GET /books/author/1
//GET /books/stock?minCopies=5
//GET /books/low-stock?threshold=2
//GET /books/advanced-search
//GET /books/stats/available-count
//GET /books?title=hobbit
//GET /books?author=tolkien
//GET /books?minAvailableCopies=3
//GET /books?page=0&size=10&sortBy=title&sortDir=asc
//GET /books?sortBy=availableCopies&sortDir=desc