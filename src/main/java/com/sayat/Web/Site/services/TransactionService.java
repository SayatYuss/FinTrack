package com.sayat.Web.Site.services;

import com.sayat.Web.Site.models.Transaction;
import com.sayat.Web.Site.models.TransactionType;
import com.sayat.Web.Site.models.User;
import com.sayat.Web.Site.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getUserTransactions(User user) {
        return transactionRepository.findByUser(user);
    }

    public List<Transaction> getUserTransactionsByType(User user, TransactionType type) {
        return transactionRepository.findByUserAndType(user, type);
    }

    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
