package com.sayat.Web.Site.controllers;

import com.sayat.Web.Site.models.Transaction;
import com.sayat.Web.Site.models.TransactionType;
import com.sayat.Web.Site.models.User;
import com.sayat.Web.Site.services.TransactionService;
import com.sayat.Web.Site.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    public TransactionController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    // Отображение списка транзакций пользователя
    @GetMapping
    public String listUserTransactions(@AuthenticationPrincipal UserDetails userDetails,
                                       @RequestParam(name = "type", required = false) String type,
                                       Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(userDetails.getUsername());

        List<Transaction> transactions;
        if ("income".equalsIgnoreCase(type)) {
            transactions = transactionService.getUserTransactionsByType(user, TransactionType.INCOME);
        } else if ("expense".equalsIgnoreCase(type)) {
            transactions = transactionService.getUserTransactionsByType(user, TransactionType.EXPENSE);
        } else {
            transactions = transactionService.getUserTransactions(user);
        }

        model.addAttribute("transactions", transactions);
        return "transactions";
    }

    // Отображение страницы добавления транзакции
    @GetMapping("/add")
    public String showAddTransactionForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "transactions/add";
    }

    // Обработка формы добавления транзакции
    @PostMapping("/add")
    public String addTransaction(@AuthenticationPrincipal UserDetails userDetails,
                                 @ModelAttribute Transaction transaction,
                                 @RequestParam("date") String date,
                                 @RequestParam("type") String type) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(userDetails.getUsername());

        transaction.setUser(user);
        transaction.setDate(LocalDate.parse(date)); // Конвертация строки в LocalDate
        transaction.setType(TransactionType.valueOf(type.toUpperCase()));

        transactionService.saveTransaction(transaction);
        return "redirect:/transactions";
    }

    // Удаление транзакции
    @GetMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return "redirect:/transactions";
    }
}
