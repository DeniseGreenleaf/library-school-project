//package com.example.library;
//
//import com.example.library.Books.Book;
//import com.example.library.Books.BookRepository;
//import com.example.library.Loans.Loan;
//import com.example.library.Loans.LoanRepository;
//import com.example.library.Loans.LoanService;
//import com.example.library.User.User;
//import com.example.library.User.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class LoanServiceTest {
//
//    @Mock
//    private LoanRepository loanRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private BookRepository bookRepository;
//
//    @InjectMocks
//    private LoanService loanService;
//
//    @Test
//    void createLoan_shouldCreateLoanWhenUserAndBookExistAndCopiesAvailable() {
//
//        //Arrange
//        User user = new User();
//        user.setUserId(1L);
//
//        Book book = new Book();
//        book.setBookId(10L);
//        book.setAvailableCopies(3);
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));
//        when(loanRepository.save(any(Loan.class))).thenAnswer(i -> i.getArgument(0));
//
//        //Act
//        Loan loan = loanService.loanBook(1L, 10L);
//
//        //Assert
//        assertNotNull(loan);
//        assertEquals(user, loan.getUser());
//        assertEquals(book, loan.getBook());
//        assertEquals(2, book.getAvailableCopies()); //should decrease by 1
//
//        verify(loanRepository).save(any(Loan.class));
//    }
//}
