package com.example.library.Loans;


import com.example.library.Books.Book;
import com.example.library.Books.BookRepository;
import com.example.library.DTOMapper;
import com.example.library.User.User;
import com.example.library.User.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public List<Loan> getLoansByUserId(Long userId) {
        return loanRepository.findByUserUserId(userId);
    }

    @Transactional
    public Loan loanBook(Long userId, Long bookId) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("Book is not available for loan");
        }

        // Minska tillgängliga exemplar
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);


        Loan loan = new Loan();
            loan.setUser(user);
            loan.setBook(book);
            loan.setLoanDate(LocalDate.now());
            loan.setReturnDate(LocalDate.now().plusWeeks(2)); // Exempel: lån i 2 veckor
            loan.setReturned(false);

        return loanRepository.save(loan);


    }

    public Loan returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        if (loan.isReturned()) {
            throw new IllegalStateException("Book already returned");
        }

        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());

        //återställ exemplar
        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    public Loan extendLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        if (loan.isReturned()) {
            throw new IllegalStateException("Cannot extend a returned loan");
        }

        loan.setReturnDate(loan.getReturnDate().plusWeeks(1)); // Förläng med 1 vecka

        return loanRepository.save(loan);
    }

}
