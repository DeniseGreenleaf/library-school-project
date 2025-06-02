package com.example.library.Loans;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

        public LoanController(LoanService loanService) {
            this.loanService = loanService;
    }


    @PostMapping
    public ResponseEntity<Loan> loanBook(@RequestBody LoanRequestDTO request) {
        try {
            Loan loan = loanService.loanBook(request.getUserId(), request.getBookId());
            return ResponseEntity.status(201).body(loan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<Loan> returnBook(@PathVariable Long id) {
        try {
            Loan loan = loanService.returnBook(id);
            return ResponseEntity.ok(loan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/extend")
    public ResponseEntity<Loan> extendLoan(@PathVariable Long id) {
            try{
                Loan loan = loanService.extendLoan(id);
                return ResponseEntity.ok(loan);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.notFound().build();
            }
//            catch (IllegalArgumentException e) {
//                return ResponseEntity.badRequest().build();
//            }
    }
}
