//package com.example.library;
//
//
//import com.example.library.Books.Book;
//import com.example.library.Loans.Loan;
//import com.example.library.Loans.LoanController;
//import com.example.library.Loans.LoanService;
//import com.example.library.User.User;
//import org.junit.jupiter.api.Test;
//
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.time.LocalDate;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//
//@WebMvcTest(LoanController.class)
//public class LoanControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private LoanService loanService;
//
//    @Test
//    public void loanBook_shouldReturnCreatedLoan() throws Exception {
//        // Arrange
//        User user = new User();
//        user.setUserId(1L);
//
//        Book book = new Book();
//        book.setBookId(2L);
//        book.setAvailableCopies(1);
//
//        Loan loan = new Loan();
//        loan.setId(100L);
//        loan.setUser(user);
//        loan.setBook(book);
//        loan.setLoanDate(LocalDate.now());
//        loan.setReturnDate(LocalDate.now().plusWeeks(2));
//        loan.setReturned(false);
//
//        Mockito.when(loanService.loanBook(1L, 2L)).thenReturn(loan);
//
//        // Act + Assert
//        mockMvc.perform(MockMvcRequestBuilders.post("/loans")
//                        .param("userId", "1")
//                        .param("bookId", "2"))
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(100L))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.user.userId").value(1L))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.book.bookId").value(2L));
//    }
//}
