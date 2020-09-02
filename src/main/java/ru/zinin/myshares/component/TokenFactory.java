package ru.zinin.myshares.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.zinin.myshares.model.User;
import ru.zinin.myshares.model.UserDto;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class TokenFactory {

    @Value("${time.validity.token}")
    private long timeValidityToken;

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    private Map<String, TokenHolder> tokens = new HashMap<>();

    private final HttpServletRequest request;

    public TokenFactory(HttpServletRequest request) {
        this.request = request;
    }

    //генерирует токен
    public static String generateNewToken() {
        byte[] randomBytes = new byte[36];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    // возвращает UserDto и добавляет токен в tokens
    public UserDto addToken(User user) {
        String token = generateNewToken();
        TokenHolder tokenHolder = new TokenHolder(user.getId(), token, System.currentTimeMillis());
        tokens.put(token, tokenHolder);
        return new UserDto(user.getUsername(), token);
    }

    public boolean isValidToken() {
        String tokenFromHeader = request.getHeader("Token");
        if (tokenFromHeader == null) return false;

        TokenHolder tokenHolder = tokens.get(tokenFromHeader);
        if (tokenHolder == null) return false;

        // если время создания токена + время валидности токена меньше текущего времени...
        if (tokenHolder.creationTimeToken + timeValidityToken < System.currentTimeMillis()) {
            return false;
        } else {
            return true;
        }
    }

    // продляет время валидности токена
    public void updateTimeValidityToken() {
        String tokenFromHeader = request.getHeader("Token");
        TokenHolder tokenHolder = tokens.get(tokenFromHeader);
        if (tokenHolder != null) {
            tokenHolder.setCreationTimeToken(System.currentTimeMillis());
        }
    }

    @Data
    @AllArgsConstructor
    class TokenHolder {
        private Long userId;
        private String token;
        private long creationTimeToken;
    }
}
