package com.example.library.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
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
            throw new IllegalArgumentException("Kunde inte skapa användare - email kanske redan finns");
        }

    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    private void validateUser(User user) {
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Förnamn får inte vara tomt");
        }

        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Efternamn får inte vara tomt");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email får inte vara tom");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Lösenord får inte vara tomt");
        }

        // email-validering
        if (!user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Ogiltig email-format");
        }

        // Trimma whitespace
        user.setFirstName(user.getFirstName().trim());
        user.setLastName(user.getLastName().trim());
        user.setEmail(user.getEmail().trim().toLowerCase());
        user.setPassword(user.getPassword().trim());
    }
}
