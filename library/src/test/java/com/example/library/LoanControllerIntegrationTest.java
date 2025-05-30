//package com.example.library;
//
//import com.example.library.Books.Book;
//import com.example.library.Books.BookRepository;
//
//import com.example.library.Loans.LoanRepository;
//
//import com.example.library.User.User;
//import com.example.library.User.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//
//import org.springframework.boot.test.context.SpringBootTest;
//
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDate;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//
//public class LoanControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private BookRepository bookRepository;
//
//    @Autowired
//    private LoanRepository loanRepository;
//
//
//    @BeforeEach
//    void setup() {
//        loanRepository.deleteAll();
//        userRepository.deleteAll();
//        bookRepository.deleteAll();
//    }
//
//    @Test
//    void createLoan_ShouldReturnCreatedLoan() throws Exception {
//        // Arrange: spara testanvändare och bok
//        User user = new User(null, "Anna", "Test", "anna@test.com", "secret", LocalDate.now());
//        user = userRepository.save(user);
//
//        Book book = new Book("Test Book");
//        book.setAvailableCopies(5);
//        book = bookRepository.save(book);
//
//        // Act + Assert
//        mockMvc.perform(post("/loans")
//                        .param("userId", String.valueOf(user.getUserId()))
//                        .param("bookId", String.valueOf(book.getBookId())))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.user.userId").value(user.getUserId()))
//                .andExpect(jsonPath("$.book.bookId").value(book.getBookId()));
//    }
//}
