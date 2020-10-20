package ru.zinin.myshares.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zinin.myshares.model.Transaction;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    public List<Transaction> getAllByUserId(long userId);

    public Transaction getById(long id);

    void deleteAllByUserId(long userId);
}
