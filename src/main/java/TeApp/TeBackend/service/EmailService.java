package TeApp.TeBackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    @Value("${brevo.api.key:}")
    private String apiKey;

    @Value("${brevo.sender.email:}")
    private String senderEmail;

    @Value("${brevo.sender.name:Peer Lens}")
    private String senderName;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private void send(String toEmail, String subject, String textContent) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Cannot send email: BREVO_API_KEY is not set");
        }
        if (senderEmail == null || senderEmail.isBlank()) {
            throw new IllegalStateException("Cannot send email: BREVO_SENDER_EMAIL is not set (sender address is empty)");
        }
        if (toEmail == null || toEmail.isBlank()) {
            throw new IllegalStateException("Cannot send email: recipient has no email address on file");
        }

        Map<String, Object> payload = Map.of(
                "sender", Map.of("name", senderName, "email", senderEmail),
                "to", List.of(Map.of("email", toEmail)),
                "subject", subject,
                "textContent", textContent
        );

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BREVO_API_URL))
                    .timeout(Duration.ofSeconds(10))
                    .header("api-key", apiKey)
                    .header("Content-Type", "application/json")
                    .header("accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 300) {
                throw new RuntimeException("Brevo API error " + response.statusCode() + ": " + response.body());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to send email via Brevo: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Email send interrupted", e);
        }
    }

    // Walks to the innermost cause so send failures are visible in the API
    // response instead of only in server logs.
    public static String describeError(Throwable t) {
        Throwable cause = t;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        String msg = cause.getMessage();
        return msg != null ? msg : cause.toString();
    }

    public void sendAdminWelcomeEmail(String firstName, String lastName, String toEmail, String password) {
        send(toEmail, "Admin Account Created",
                "Hello " + firstName + " " + lastName + ",\n\n" +
                        "You have been granted ADMIN access in TeachApp.\n\n" +
                        "Your login credentials:\n" +
                        "Email: " + toEmail + "\n" +
                        "Password: " + password + "\n\n" +
                        "Please log in and change your password immediately.\n\n" +
                        "Thank you!"
        );
    }

    public void sendInstructorFormCompleteEmail(String observerEmail, String instructorName) {
        send(observerEmail, "Session Questionnaire Completed – Ready for Observation",
                "Hello,\n\n" +
                        "Instructor " + instructorName + " has completed their pre-observation questionnaire.\n\n" +
                        "Their session details are now available in the system. You can proceed with scheduling or conducting the observation.\n\n" +
                        "Log in to Peer Lens to review the session information.\n\n" +
                        "Thank you!"
        );
    }

    public void sendObservationCompleteEmail(String instructorEmail, String instructorName, String observerName) {
        send(instructorEmail, "Your Observation Has Been Completed",
                "Hello " + instructorName + ",\n\n" +
                        "Observer " + observerName + " has completed the observation of your session.\n\n" +
                        "Your evaluation report and recommendations will be available shortly in Peer Lens.\n\n" +
                        "Log in to your account to view the results.\n\n" +
                        "Thank you!"
        );
    }
}
