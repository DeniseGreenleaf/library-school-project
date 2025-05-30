package com.example.library.Loans;

import com.example.library.Books.Book;
import com.example.library.User.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @Column(name = "loan_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"password", "registrationDate"}) // Ignorera känslig data från User
    private User user;


    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonIgnoreProperties({"loans"}) // Ignorera eventuella cirkulära referenser från Book
    private Book book;

    @Column(name = "borrowed_date")
    private LocalDate loanDate;

    @Column(name = "due_date")
    private LocalDate dueDate;



    @Column(name = "returned_date")
    private Boolean returned;

    public Loan() {
    }

    public Loan(Long id, Book book, User user, LocalDate loanDate, LocalDate dueDate, Boolean returned) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returned = returned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getReturnDate() {
        return dueDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.dueDate = returnDate;
    }

    public Boolean isReturned() {
        return returned;
    }

    public void setReturned(Boolean returned) {
        this.returned = returned;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getReturned() {
        return returned;
    }
}
