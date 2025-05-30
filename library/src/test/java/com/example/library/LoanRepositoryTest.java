//package com.example.library;
//
//import com.example.library.Books.Book;
//import com.example.library.Books.BookRepository;
//import com.example.library.Loans.Loan;
//import com.example.library.Loans.LoanRepository;
//import com.example.library.User.User;
//import com.example.library.User.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//public class LoanRepositoryTest {
//
//    @Autowired
//    private LoanRepository loanRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private BookRepository bookRepository;
//
//    @Test
//    void shouldSaveAndRetrieveLoan() {
//        // Arrange
//        User user = new User(null, "Anna", "Andersson", "anna@test.com", "secret", LocalDate.now());
//        user = userRepository.save(user);
//
//        Book book = new Book("Testbok");
//        book.setAvailableCopies(3);
//        book = bookRepository.save(book);
//
//        Loan loan = new Loan();
//        loan.setUser(user);
//        loan.setBook(book);
//        loan.setLoanDate(LocalDate.now());
//        loan.setReturnDate(LocalDate.now().plusWeeks(2));
//        loan.setReturned(false);
//
//        Loan savedLoan = loanRepository.save(loan);
//
//        // Act
//        Optional<Loan> foundLoan = loanRepository.findById(savedLoan.getId());
//
//        // Assert
//        assertTrue(foundLoan.isPresent());
//        assertEquals(user.getUserId(), foundLoan.get().getUser().getUserId());
//        assertEquals(book.getBookId(), foundLoan.get().getBook().getBookId());
//    }
//}
