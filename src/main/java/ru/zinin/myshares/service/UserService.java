package ru.zinin.myshares.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.zinin.myshares.component.TokenFactory;
import ru.zinin.myshares.model.Alert;
import ru.zinin.myshares.model.User;
import ru.zinin.myshares.model.UserDto;
import ru.zinin.myshares.repo.UserPepo;

import java.util.List;

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

    public ResponseEntity<?> getProfile() {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            User userFromDb = userRepo.getUserById(tokenFactory.getUserId());
            UserDto userDto = new UserDto();
            userDto.setEmail(userFromDb.getEmail());
            userDto.setUsername(userFromDb.getUsername());
            return ResponseEntity.ok(userDto);
        } else {
            return new ResponseEntity<>(
                    "invalid token",
                    HttpStatus.FORBIDDEN
            );
        }
    }

    public ResponseEntity<?> changeUsername(String newUsername) {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            User userFromDb = userRepo.getUserById(tokenFactory.getUserId());
            userFromDb.setUsername(newUsername);
            User save = userRepo.save(userFromDb);
            UserDto userDto = tokenFactory.addToken(save);
            return ResponseEntity.ok(userDto);
        } else {
            return new ResponseEntity<>(
                    "invalid token",
                    HttpStatus.FORBIDDEN
            );
        }
    }

    public ResponseEntity<?> changeEmail(String newEmail) {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            User userFromDb = userRepo.getUserById(tokenFactory.getUserId());
            userFromDb.setEmail(newEmail);
            userRepo.save(userFromDb);
            return ResponseEntity.ok("EMAIL SAVED");
        } else {
            return new ResponseEntity<>(
                    "invalid token",
                    HttpStatus.FORBIDDEN
            );
        }
    }

    public ResponseEntity<?> checkOldPassword(String oldPassword) {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            User userFromDb = userRepo.getUserById(tokenFactory.getUserId());
            return ResponseEntity.ok(oldPassword.equals(userFromDb.getPassword()));
        } else {
            return new ResponseEntity<>(
                    "invalid token",
                    HttpStatus.FORBIDDEN
            );
        }
    }

    public ResponseEntity<?> changePassword(String newPassword) {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            User userFromDb = userRepo.getUserById(tokenFactory.getUserId());
            userFromDb.setPassword(newPassword);
            userRepo.save(userFromDb);
            return ResponseEntity.ok("OK");
        } else {
            return new ResponseEntity<>(
                    "invalid token",
                    HttpStatus.FORBIDDEN
            );
        }
    }
}
