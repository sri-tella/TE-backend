package TeApp.TeBackend.controller;

import TeApp.TeBackend.dto.adminDTO;
import TeApp.TeBackend.entity.Roles;
import TeApp.TeBackend.entity.Users;
import TeApp.TeBackend.repository.UsersRepo;
import TeApp.TeBackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import TeApp.TeBackend.service.PasswordGenerator;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@CrossOrigin
public class AdminController {

    @Autowired
    private UsersRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    private EmailService emailService;

    /**
     * Create new admin
     */
    @PostMapping
    public ResponseEntity<?> createAdmin(@RequestBody adminDTO newAdminDTO) {
        if (userRepository.findAll().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(newAdminDTO.getEmail()))) {
            return ResponseEntity.badRequest().body("Error: Email already exists");
        }

        String tempPassword = passwordGenerator.generate();
        String encodedPassword = passwordEncoder.encode(tempPassword);
        System.out.println(tempPassword);

        Users user = new Users();
        user.setFirstName(newAdminDTO.getFirstName());
        user.setLastName(newAdminDTO.getLastName());
        user.setEmail(newAdminDTO.getEmail());
        user.setPassword(encodedPassword);
        user.getRoles().add(Roles.ADMIN);

        userRepository.save(user);

        emailService.sendAdminWelcomeEmail(
                user.getFirstName(),
                user.getFirstName(),
                user.getEmail(),
                tempPassword
        );

        return ResponseEntity.ok(user);
    }

    /**
     * List all admins
     */
    @GetMapping
    public List<Users> getAllAdmins() {
        return userRepository.findAllAdmins();
    }

    /**
     * Delete an admin
     */
    @DeleteMapping("/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            if (user.getRoles().contains(Roles.ADMIN)) {
                userRepository.deleteById(id);
                return "Admin deleted successfully";
            } else {
                return "Error: User is not an admin";
            }
        }).orElse("Error: User not found");
    }

    @PostMapping("/test-email")
    public String testEmail(@RequestParam String to) {
        emailService.sendAdminWelcomeEmail(
                "sri",
                "Tella",
                to,
                "TempPass123!"
        );
        return "Test email sent to " + to;
    }
}
