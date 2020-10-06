package ru.zinin.myshares.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zinin.myshares.model.Alert;

import java.util.List;

public interface AlertRepo extends JpaRepository<Alert, Long> {
    List<Alert> getAllByUserId(Long userId);

    Alert getById(Long userId);
}
