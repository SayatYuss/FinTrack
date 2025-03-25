package com.sayat.Web.Site.controllers;

import com.sayat.Web.Site.models.Transaction;
import com.sayat.Web.Site.models.TransactionType;
import com.sayat.Web.Site.models.User;
import com.sayat.Web.Site.services.TransactionService;
import com.sayat.Web.Site.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionRestController {

    private final TransactionService transactionService;
    private final UserService userService;

    public TransactionRestController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    // Получение всех транзакций пользователя
    @GetMapping
    public ResponseEntity<List<Transaction>> getUserTransactions(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }

        User user = userService.findByUsername(userDetails.getUsername());
        List<Transaction> transactions = transactionService.getUserTransactions(user);
        return ResponseEntity.ok(transactions);
    }

    // Получение транзакций по типу (доход/расход)
    @GetMapping("/{type}")
    public ResponseEntity<List<Transaction>> getUserTransactionsByType(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String type) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        User user = userService.findByUsername(userDetails.getUsername());

        List<Transaction> transactions;
        if ("income".equalsIgnoreCase(type)) {
            transactions = transactionService.getUserTransactionsByType(user, TransactionType.INCOME);
        } else if ("expense".equalsIgnoreCase(type)) {
            transactions = transactionService.getUserTransactionsByType(user, TransactionType.EXPENSE);
        } else {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        return ResponseEntity.ok(transactions);
    }

    // Добавление новой транзакции
    @PostMapping("/add")
    public ResponseEntity<Transaction> addTransaction(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Transaction transaction) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        User user = userService.findByUsername(userDetails.getUsername());
        transaction.setUser(user);
        transaction.setDate(LocalDate.now()); // Если дата не передается, ставим текущую

        transactionService.saveTransaction(transaction);
        return ResponseEntity.ok(transaction);
    }

    // Удаление транзакции
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
