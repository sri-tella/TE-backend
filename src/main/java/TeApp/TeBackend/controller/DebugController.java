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

    @Value("${brevo.api.key:}")
    private String brevoApiKey;

    @Value("${brevo.sender.email:}")
    private String brevoSenderEmail;

    @GetMapping("/mail-config")
    public Map<String, Object> mailConfig() {
        return Map.of(
                "brevoApiKeySet", !brevoApiKey.isBlank(),
                "brevoApiKeyLength", brevoApiKey.length(),
                "brevoSenderEmailSet", !brevoSenderEmail.isBlank(),
                "brevoSenderEmail", brevoSenderEmail
        );
    }
}
