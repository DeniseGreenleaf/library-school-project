package com.example.library.Author;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // GET /authors - lista alla
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    // GET /authors/name/{lastName} - hämta via efternamn
    @GetMapping("/name/{lastName}")
    public ResponseEntity<List<Author>> getAuthorsByLastName(@PathVariable String lastName) {
        return ResponseEntity.ok(authorService.findAuthorsByLastName(lastName));
    }

    // POST /authors - skapa ny
    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        try {
            Author createdAuthor = authorService.createAuthor(author);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Exceptionhandler (valfritt)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
