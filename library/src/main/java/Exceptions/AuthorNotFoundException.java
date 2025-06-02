package Exceptions;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String message) {
        super(message);
    }

    public AuthorNotFoundException(long id) {
        super("Author with ID " + id + " not found");
    }
}
