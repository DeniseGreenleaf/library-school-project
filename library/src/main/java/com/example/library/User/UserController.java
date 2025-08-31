package com.example.library.User;

import Exceptions.UserNotFoundException;
import com.example.library.DTOMapper;
import com.example.library.Loans.Loan;
import com.example.library.Loans.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final LoanService loanService;

    public UserController(UserService userService, LoanService loanService) {
        this.userService = userService;
        this.loanService = loanService;
    }

    //Get email med email.
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {

        try {
            return userService.getUserByEmail(email)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST skapa användare
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody User user) {
        try {
            User created = userService.createUser(user);
            UserDto dto = new UserDto(created);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET hämta användar lån med user id
    @GetMapping("/{userId}/loans")
    public ResponseEntity<List<Loan>> getUserLoans(@PathVariable Long userId) {

        try {
            if (userService.getUserById(userId).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            List<Loan> loans = loanService.getLoansByUserId(userId);
            return ResponseEntity.ok(loans);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //GET hämta alla användare
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {

        try {
            List<User> users = userService.getAllUsers();
            List<UserDto> userDtos = DTOMapper.toUserDTOList(users);
            return ResponseEntity.ok(userDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}