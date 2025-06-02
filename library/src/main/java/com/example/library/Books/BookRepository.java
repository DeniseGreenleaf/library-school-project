package com.example.library.Books;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface BookRepository extends JpaRepository<Book, Long> {

        // JPA ger : findAll(), findById(), save(), deleteById(), existsById() findAll(Pageable pageable) för paginering

        // custom queries för specifika metoder

        // Spring Data JPA skapar automatiskt dessa metoder baserat på metodnamnet

     // Sök böcker efter titel (case-insensitive) - med paginering
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    List<Book> findByTitleContainingIgnoreCase(String title);


    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(CONCAT(b.author.firstName, ' ', b.author.lastName)) LIKE LOWER(CONCAT('%', :authorName, '%'))")
    List<Book> findByAuthorNameContainingIgnoreCase(@Param("authorName") String authorName);

    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(CONCAT(b.author.firstName, ' ', b.author.lastName)) LIKE LOWER(CONCAT('%', :authorName, '%'))")
    Page<Book> findByAuthorNameContainingIgnoreCase(@Param("authorName") String authorName, Pageable pageable);

    Page<Book> findByAvailableCopiesGreaterThan(Integer minCopies, Pageable pageable);

    // JPQL QUERIES

    @Query("SELECT b FROM Book b WHERE " +
            "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:authorName IS NULL OR LOWER(CONCAT(b.author.firstName, ' ', b.author.lastName)) LIKE LOWER(CONCAT('%', :authorName, '%')))")
    Page<Book> findByTitleAndAuthorName(@Param("title") String title,
                                        @Param("authorName") String authorName,
                                        Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.availableCopies > :minCopies")
    List<Book> findBooksWithMinimumCopies(@Param("minCopies") Integer minCopies);

    @Query("SELECT b FROM Book b WHERE b.author.authorId = :authorId")
    List<Book> findBooksByAuthorId(@Param("authorId") Long authorId);

    // Hämta böcker med låg lagerställning
    @Query("SELECT b FROM Book b WHERE b.availableCopies <= :threshold ORDER BY b.availableCopies ASC")
    List<Book> findLowStockBooks(@Param("threshold") Integer threshold);

    //SQL QUERIES

    @Query(value = "SELECT * FROM books b " +
            "JOIN authors a ON b.author_id = a.author_id " +
            "WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:authorName IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :authorName, '%'))) " +
            "AND (:minCopies IS NULL OR b.available_copies >= :minCopies)",
            nativeQuery = true)
    List<Book> searchBooksNative(@Param("title") String title,
                                 @Param("authorName") String authorName,
                                 @Param("minCopies") Integer minCopies);

    @Query(value = "SELECT COUNT(*) FROM books WHERE available_copies > 0", nativeQuery = true)
    Long countAvailableBooks();

    @Query(value = "SELECT b.*, a.name as author_name " +
            "FROM books b " +
            "JOIN authors a ON b.author_id = a.author_id " +
            "WHERE b.available_copies = (SELECT MAX(available_copies) FROM books)",
            nativeQuery = true)
    List<Book> findMostAvailableBooks();

    // Statistik query
    @Query(value = "SELECT " +
            "COUNT(*) as total_books, " +
            "SUM(available_copies) as total_copies, " +
            "AVG(available_copies) as avg_copies_per_book " +
            "FROM books",
            nativeQuery = true)
    Object[] getBookStatistics();

    // OPTIONAL RETURN TYPES

    Optional<Book> findByTitle(String title);

    @Query("SELECT b FROM Book b WHERE b.bookId = :id")
    Optional<Book> findBookById(@Param("id") Long id);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) = LOWER(:title)")
    Optional<Book> findByTitleIgnoreCase(@Param("title") String title);
}