package com.example.library.Books;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BookRepository extends JpaRepository<Book, Long> {

        // JPA ger : findAll(), findById(), save(), deleteById(), existsById() findAll(Pageable pageable) för paginering

        // Endast custom queries för specifika affärsbehov:

        // Spring Data JPA skapar automatiskt dessa metoder baserat på metodnamnet

     // Sök böcker efter titel (case-insensitive) - med paginering
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Sök böcker efter författare (om author är en String-field)
//    List<Book> findByAuthorContainingIgnoreCase(String author);


    // För GET /books/search - sök på författare (måste använda custom query eftersom author är ett objekt)
    @Query("SELECT b FROM Book b WHERE LOWER(CONCAT(b.author.firstName, ' ', b.author.lastName)) LIKE LOWER(CONCAT('%', :authorName, '%'))")
    Page<Book> findByAuthorNameContainingIgnoreCase(@Param("authorName") String authorName, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE LOWER(CONCAT(b.author.firstName, ' ', b.author.lastName)) LIKE LOWER(CONCAT('%', :authorName, '%'))")
    List<Book> findByAuthorNameContainingIgnoreCase(@Param("authorName") String authorName);

    // Kombinerad sökning på titel OCH författare
    @Query("SELECT b FROM Book b WHERE " +
            "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:authorName IS NULL OR LOWER(CONCAT(b.author.firstName, ' ', b.author.lastName)) LIKE LOWER(CONCAT('%', :authorName, '%')))")
    Page<Book> findByTitleAndAuthorName(@Param("title") String title,
                                        @Param("authorName") String authorName,
                                        Pageable pageable);

    // Filter på availableCopies
    Page<Book> findByAvailableCopiesGreaterThan(int copies, Pageable pageable);

    }
