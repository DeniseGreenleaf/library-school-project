package com.example.library.Author;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public List<Author> findAuthorsByLastName(String lastName) {
        return authorRepository.findByLastNameIgnoreCase(lastName);
    }

    public Author createAuthor(Author author) {
        validateAuthor(author);
        return authorRepository.save(author);
    }

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author with ID " + id + " not found"));
    }

    private void validateAuthor(Author author) {
        if (author.getFirstName() == null || author.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (author.getLastName() == null || author.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
    }
}
