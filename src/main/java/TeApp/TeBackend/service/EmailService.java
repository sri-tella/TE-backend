package TeApp.TeBackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${mail.sender.name:Peer Lens}")
    private String senderName;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void send(String toEmail, String subject, String textContent) {
        if (senderEmail == null || senderEmail.isBlank()) {
            throw new IllegalStateException("Cannot send email: GMAIL_USERNAME is not set (sender address is empty)");
        }
        if (toEmail == null || toEmail.isBlank()) {
            throw new IllegalStateException("Cannot send email: recipient has no email address on file");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderName + " <" + senderEmail + ">");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(textContent);
        mailSender.send(message);
    }

    // Walks to the innermost cause so SMTP errors (e.g. Gmail auth failures)
    // are visible in the API response instead of only in server logs.
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
