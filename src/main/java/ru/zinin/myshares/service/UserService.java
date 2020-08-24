package ru.zinin.myshares.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.zinin.myshares.component.TokenFactory;
import ru.zinin.myshares.model.User;
import ru.zinin.myshares.model.UserDto;
import ru.zinin.myshares.repo.UserPepo;

@Service
public class UserService {

    private final UserPepo userRepo;
    private final TokenFactory tokenFactory;

    public UserService(UserPepo userRepo, TokenFactory tokenFactory) {
        this.userRepo = userRepo;
        this.tokenFactory = tokenFactory;
    }

    public ResponseEntity<?> register(User user) {
        // получаем пользователя по email
        User userFromDb = userRepo.getUserByEmail(user.getEmail());

        // если пользователь с таким email существует...
        if (userFromDb != null) {
            return new ResponseEntity<>(
                    "пользователь с email: " + user.getEmail() + " уже существует",
                    HttpStatus.CONFLICT);
        } else {
            // если  не существует, то...
            // добавляем время создания
            user.setCreationTime(System.currentTimeMillis());
            // сохраняем в базе данных
            User savedUser = userRepo.save(user);
            // получем UserDto с username и token
            UserDto userDto = tokenFactory.addToken(savedUser);
            return ResponseEntity.ok(userDto);
        }
    }

    public ResponseEntity<?> login(User user) {
        // получаем пользователя по email
        User userFromDb = userRepo.getUserByEmail(user.getEmail());

        // если пользователь с таким email не существует...
        if (userFromDb == null) {
            return new ResponseEntity<>(
                    "пользователь с email: " + user.getEmail() + " не существует",
                    HttpStatus.CONFLICT
            );
        } else {
            // если пришедший пароль и парль из БД не совпадают...
            if (!userFromDb.getPassword().equals(user.getPassword())) {
                return new ResponseEntity<>(
                        "вы ввели неверный пароль",
                        HttpStatus.CONFLICT
                );
            } else {
                // возвращаем UserDto с username и token
                return ResponseEntity.ok(tokenFactory.addToken(userFromDb));
            }
        }
    }
}
