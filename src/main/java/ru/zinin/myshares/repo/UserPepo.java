package ru.zinin.myshares.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zinin.myshares.model.User;

public interface UserPepo extends JpaRepository<User, Long> {
    User getUserByEmail(String email);
}
