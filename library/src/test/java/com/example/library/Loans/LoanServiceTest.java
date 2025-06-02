package com.example.library.Loans;

import com.example.library.Books.Book;
import com.example.library.Books.BookRepository;
import com.example.library.User.User;
import com.example.library.User.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    private LoanService loanService;
    private LoanRepository loanRepository;
    private UserRepository userRepository;
    private BookRepository bookRepository;

    private User testUser;
    private Book testBook;

    @BeforeEach
    void setUp() {
        loanRepository = mock(LoanRepository.class);
        userRepository = mock(UserRepository.class);
        bookRepository = mock(BookRepository.class);

        loanService = new LoanService(loanRepository, userRepository, bookRepository);

        testUser = new User();
        testUser.setUserId(1L);
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("test@example.com");

        testBook = new Book();
        testBook.setBookId(1L);
        testBook.setTitle("Test Book");
        testBook.setAvailableCopies(2);
    }

    @Test
    void loanBook_shouldSucceed_whenUserAndBookExistAndBookAvailable() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(loanRepository.save(any(Loan.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Loan loan = loanService.loanBook(1L, 1L);

        // Assert
        assertNotNull(loan);
        assertEquals(testUser, loan.getUser());
        assertEquals(testBook, loan.getBook());
        assertFalse(loan.isReturned());
        assertEquals(LocalDate.now(), loan.getLoanDate());
        assertEquals(LocalDate.now().plusWeeks(2), loan.getReturnDate());
        assertEquals(1, testBook.getAvailableCopies()); // Blev 2 - 1 = 1

        // Verify
        verify(userRepository).findById(1L);
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(testBook);
        verify(loanRepository).save(any(Loan.class));
    }

    @Test
    void loanBook_shouldThrow_whenUserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> loanService.loanBook(1L, 1L));

        assertEquals("User not found", ex.getMessage());
        verify(userRepository).findById(1L);
        verify(bookRepository, never()).findById(any());
        verify(loanRepository, never()).save(any());
    }

    @Test
    void loanBook_shouldThrow_whenBookNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> loanService.loanBook(1L, 1L));

        assertEquals("Book not found", ex.getMessage());
        verify(bookRepository, never()).save(any());
        verify(loanRepository, never()).save(any());
    }

    @Test
    void loanBook_shouldThrow_whenNoAvailableCopies() {
        // Arrange
        testBook.setAvailableCopies(0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        // Act + Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> loanService.loanBook(1L, 1L));

        assertEquals("Book is not available for loan", ex.getMessage());
        verify(loanRepository, never()).save(any());
    }
}
