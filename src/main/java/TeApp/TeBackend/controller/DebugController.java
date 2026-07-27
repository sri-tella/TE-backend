package TeApp.TeBackend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// Temporary: confirms whether Railway env vars are actually reaching the
// container, without exposing their values. Remove once mail is working.
@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Value("${spring.mail.username:}")
    private String mailUsername;

    @Value("${spring.mail.password:}")
    private String mailPassword;

    @GetMapping("/mail-config")
    public Map<String, Object> mailConfig() {
        return Map.of(
                "gmailUsernameSet", !mailUsername.isBlank(),
                "gmailUsernameLength", mailUsername.length(),
                "gmailAppPasswordSet", !mailPassword.isBlank(),
                "gmailAppPasswordLength", mailPassword.length()
        );
    }
}
