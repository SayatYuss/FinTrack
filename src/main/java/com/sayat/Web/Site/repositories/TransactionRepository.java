package com.sayat.Web.Site.repositories;

import com.sayat.Web.Site.models.Transaction;
import com.sayat.Web.Site.models.TransactionType;
import com.sayat.Web.Site.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByUserAndType(User user, TransactionType type);
}
