package com.example.library;

public class BookWithDetailsDTO {

    private Long id;
    private String title;
    private String authorName;
    private int availableCopies;

    public BookWithDetailsDTO() {}

    public BookWithDetailsDTO(Long id, String title, String authorName, int availableCopies) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.availableCopies = availableCopies;
    }

    // Getters och Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies;
    }

}
