package com.example.library.Loans;

public class LoanRequestDTO {
    private Long userId;
    private Long bookId;

    public LoanRequestDTO() {
    }

    public LoanRequestDTO(Long userId, Long bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    // getters och setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}
