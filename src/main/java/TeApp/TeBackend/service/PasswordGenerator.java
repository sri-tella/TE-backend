package TeApp.TeBackend.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PasswordGenerator {

    private static final String CHARACTERS_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String CHARACTERS_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "1234567890";
    private static final String SYMBOLS = "!@#$%^&*()";
    private static final int defaultLength = 12;

    private final SecureRandom random = new SecureRandom();

    public String generate() {

        List<Character> passwordChars = new ArrayList<>();

        for(int i = 0; i < 8; i++) {
            if(random.nextBoolean()) {
                passwordChars.add(CHARACTERS_UPPERCASE.charAt(random.nextInt(CHARACTERS_UPPERCASE.length())));
            } else {
                passwordChars.add(CHARACTERS_LOWERCASE.charAt(random.nextInt(CHARACTERS_LOWERCASE.length())));
            }
        }

        for (int i=0; i < 2; i++) {
            passwordChars.add(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        }

        for (int i=0; i < 2; i++) {
            passwordChars.add(SYMBOLS.charAt(random.nextInt(SYMBOLS.length())));
        }

        Collections.shuffle(passwordChars, random);

        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        return password.toString();

    }
}
