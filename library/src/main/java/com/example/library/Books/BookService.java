package com.example.library.Books;

import com.example.library.Author.Author;
import com.example.library.Author.AuthorRepository;
import com.example.library.BookWithDetailsDTO;
import com.example.library.DTOMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    // För GET /books - Lista alla böcker utan paginering
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // För GET /books - Lista alla böcker med paginering
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }


    // För POST /books - Skapa ny bok
    public Book createBook(Book book) {
        validateBook(book);

        Long authorId = book.getAuthor().getAuthorId();

        Author existingAuthor = authorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author with ID " + authorId + " not found"));

        book.setAuthor(existingAuthor);

        return bookRepository.save(book);
    }

//    @PostConstruct
//    public void verifySchema() {
//        List<Book> books = bookRepository.findAll();
//        books.forEach(System.out::println);
//    }

    // För GET /books/search - Sök böcker på title eller author utan paginering
    public List<Book> searchBooksByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookRepository.findByTitleContainingIgnoreCase(title.trim());
    }

    public List<Book> searchBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookRepository.findByAuthorNameContainingIgnoreCase(author.trim());
    }

    public List<BookWithDetailsDTO> getAllBooksWithDetails() {
        List<Book> books = bookRepository.findAll();
//        return DTOMapper.toBookDTOList(books);
        return books.stream().map(DTOMapper::toBookDTO).toList();
    }


    // För GET /books/search - Sök böcker med paginering
    public Page<Book> searchBooksByTitle(String title, Pageable pageable) {
        if (title == null || title.trim().isEmpty()) {
            return getAllBooks(pageable);
        }
        return bookRepository.findByTitleContainingIgnoreCase(title.trim(), pageable);
    }

    public Page<Book> searchBooksByAuthor(String author, Pageable pageable) {
        if (author == null || author.trim().isEmpty()) {
            return getAllBooks(pageable);
        }
        return bookRepository.findByAuthorNameContainingIgnoreCase(author.trim(), pageable);
    }

    // Avancerad sökning med flera filter
    public Page<Book> searchBooks(String title, String author, Integer minAvailableCopies, Pageable pageable) {
        // Om vi bara filtrerar på availableCopies
        if ((title == null || title.trim().isEmpty()) &&
                (author == null || author.trim().isEmpty()) &&
                minAvailableCopies != null && minAvailableCopies > 0) {
            return bookRepository.findByAvailableCopiesGreaterThan(minAvailableCopies, pageable);
        }
        // Om vi har titel och/eller författare
        if (minAvailableCopies == null || minAvailableCopies <= 0) {
            return bookRepository.findByTitleAndAuthorName(
                    title != null && !title.trim().isEmpty() ? title.trim() : null,
                    author != null && !author.trim().isEmpty() ? author.trim() : null,
                    pageable
            );
        }

        // Fallback till alla böcker om inga filter
        return getAllBooks(pageable);
    }


        // === Helper Methods ===
    private void validateBook(Book book) {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Titel får inte vara tom");
        }

        // Trimma whitespace
        book.setTitle(book.getTitle().trim());

        // Sätt default för availableCopies om det inte satts
        if (book.getAvailableCopies() <= 0) {
            book.setAvailableCopies(1);  // eller annat defaultvärde
        }

        if (book.getAuthor() != null) {
            book.setAuthor(book.getAuthor());
        }
    }
}