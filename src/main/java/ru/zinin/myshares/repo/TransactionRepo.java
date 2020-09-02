package ru.zinin.myshares.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zinin.myshares.model.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
}
