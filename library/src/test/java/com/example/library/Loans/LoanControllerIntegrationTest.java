package com.example.library.Loans;

import com.example.library.Author.Author;
import com.example.library.Author.AuthorRepository;
import com.example.library.Books.Book;
import com.example.library.Books.BookRepository;
import com.example.library.User.User;
import com.example.library.User.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Använder application-test.properties
@Transactional
public class LoanControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private LoanRepository loanRepository;

    private User testUser;
    private Book testBook;
    private Author testAuthor;

    @BeforeEach
    void setUp() {
        //   loanRepository.deleteAll(); // för tydlighet, även om @Transactional rensar
        testAuthor = new Author();
        testAuthor.setFirstName("Test");
        testAuthor.setLastName("Author");
        testAuthor = authorRepository.save(testAuthor);

        testUser = new User();
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);

        testBook = new Book();
        testBook.setTitle("Test Book");
        testBook.setAuthor(testAuthor);
        testBook.setAvailableCopies(5);
        testBook = bookRepository.save(testBook);
    }

    @Test
    public void createLoan_shouldSucceed_whenValid() throws Exception {
        // Arrange
        LoanRequestDTO request = new LoanRequestDTO(testUser.getUserId(), testBook.getBookId());
        String json = objectMapper.writeValueAsString(request);

        // Act
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                // Assert
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.userId", is(testUser.getUserId().intValue())))
                .andExpect(jsonPath("$.book.bookId", is(testBook.getBookId().intValue())))
                .andExpect(jsonPath("$.returned", is(false)));
    }

    @Test
    public void createLoan_shouldFail_whenUserNotFound() throws Exception {
        // Arrange
        LoanRequestDTO request = new LoanRequestDTO(999L, testBook.getBookId());
        String json = objectMapper.writeValueAsString(request);

        // Act
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createLoan_shouldFail_whenBookNotFound() throws Exception {
        // Arrange
        LoanRequestDTO request = new LoanRequestDTO(testUser.getUserId(), 999L);
        String json = objectMapper.writeValueAsString(request);

        // Act
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                //Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createLoan_shouldFail_whenBookUnavailable() throws Exception {
        // Arrange
        testBook.setAvailableCopies(0);
        bookRepository.save(testBook);
        LoanRequestDTO request = new LoanRequestDTO(testUser.getUserId(), testBook.getBookId());
        String json = objectMapper.writeValueAsString(request);

        // Act
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                //Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createLoan_shouldFail_whenInvalidJson() throws Exception {
        // Arrange
        String invalidJson = "{ \"userId\": \"abc\", \"bookId\": null }";

        // Act
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))

                //Assert
                .andExpect(status().isBadRequest());
    }
}