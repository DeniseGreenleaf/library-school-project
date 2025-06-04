package com.example.library.User;

import Exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        return userRepository.findByEmail(email.trim().toLowerCase());
    }

    public Optional<User> getUserById(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        return userRepository.findById(userId);
    }

    public User getUserByIdOrThrow(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public User createUser(User user) {
        validateUser(user);

        // Kontrollera om email redan finns
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("En användare med email " + user.getEmail() + " finns redan");
        }

        // Sätt registreringsdatum om det saknas
        if (user.getRegistrationDate() == null) {
            user.setRegistrationDate(LocalDate.now());
        }

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Kunde inte skapa användare, email måste vara unik");
        }

    }


    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Validera förnamn
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Förnamn får inte vara tomt");
        }

        if (user.getFirstName().trim().length() < 2) {
            throw new IllegalArgumentException("Förnamn måste vara minst 2 tecken");
        }

        // Validera efternamn
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Efternamn får inte vara tomt");
        }

        if (user.getLastName().trim().length() < 2) {
            throw new IllegalArgumentException("Efternamn måste vara minst 2 tecken");
        }

        // Validera email
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email får inte vara tom");
        }

        String email = user.getEmail().trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Ogiltig email-format");
        }

        if (email.length() > 255) {
            throw new IllegalArgumentException("Email får inte vara längre än 255 tecken");
        }

        // Validera lösenord
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Lösenord får inte vara tomt");
        }

        if (user.getPassword().trim().length() < 6) {
            throw new IllegalArgumentException("Lösenord måste vara minst 6 tecken");
        }

        if (user.getPassword().trim().length() > 100) {
            throw new IllegalArgumentException("Lösenord får inte vara längre än 100 tecken");
        }

        // Validera registreringsdatum om det finns
        if (user.getRegistrationDate() != null && user.getRegistrationDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Registreringsdatum kan inte vara i framtiden");
        }


        // Trimma whitespace
        user.setFirstName(user.getFirstName().trim());
        user.setLastName(user.getLastName().trim());
        user.setEmail(email);
        user.setPassword(user.getPassword().trim());
    }
}
