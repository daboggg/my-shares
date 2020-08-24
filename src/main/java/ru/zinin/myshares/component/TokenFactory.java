package ru.zinin.myshares.component;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.zinin.myshares.model.User;
import ru.zinin.myshares.model.UserDto;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class TokenFactory {

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    private Map<String, Long> tokens = new HashMap<>();

    //генерирует токен
    public static String generateNewToken() {
        byte[] randomBytes = new byte[36];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    // возвращает UserDto и добавляет токен в tokens
    public UserDto addToken(User user) {
        String token = generateNewToken();
        tokens.put(token, System.currentTimeMillis());
        return new UserDto(user.getUsername(), token);
    }
}
