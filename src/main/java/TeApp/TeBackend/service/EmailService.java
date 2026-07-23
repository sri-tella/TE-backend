package TeApp.TeBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendAdminWelcomeEmail(String firstName, String lastName, String toEmail, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Admin Account Created");
        message.setText(
                "Hello " + firstName + " " + lastName + ",\n\n" +
                        "You have been granted ADMIN access in TeachApp.\n\n" +
                        "Your login credentials:\n" +
                        "Email: " + toEmail + "\n" +
                        "Password: " + password + "\n\n" +
                        "Please log in and change your password immediately.\n\n" +
                        "Thank you!"
        );
        mailSender.send(message);
    }

    public void sendInstructorFormCompleteEmail(String observerEmail, String instructorName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(observerEmail);
        message.setSubject("Session Questionnaire Completed – Ready for Observation");
        message.setText(
                "Hello,\n\n" +
                        "Instructor " + instructorName + " has completed their pre-observation questionnaire.\n\n" +
                        "Their session details are now available in the system. You can proceed with scheduling or conducting the observation.\n\n" +
                        "Log in to Peer Lens to review the session information.\n\n" +
                        "Thank you!"
        );
        mailSender.send(message);
    }

    public void sendObservationCompleteEmail(String instructorEmail, String instructorName, String observerName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(instructorEmail);
        message.setSubject("Your Observation Has Been Completed");
        message.setText(
                "Hello " + instructorName + ",\n\n" +
                        "Observer " + observerName + " has completed the observation of your session.\n\n" +
                        "Your evaluation report and recommendations will be available shortly in Peer Lens.\n\n" +
                        "Log in to your account to view the results.\n\n" +
                        "Thank you!"
        );
        mailSender.send(message);
    }
}
