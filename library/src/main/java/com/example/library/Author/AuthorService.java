package com.example.library.Author;

import Exceptions.AuthorNotFoundException;
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
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        return authorRepository.findByLastNameIgnoreCase(lastName.trim());
    }

    public Author createAuthor(Author author) {
        validateAuthor(author);

        author.setFirstName(author.getFirstName().trim());
        author.setLastName(author.getLastName().trim());

        return authorRepository.save(author);
    }

    public Author getAuthorById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Author ID must be a positive number");
        }
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    private void validateAuthor(Author author) {
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        if (author.getFirstName() == null || author.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (author.getLastName() == null || author.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
    }
}
