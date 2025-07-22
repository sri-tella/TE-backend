package TeApp.TeBackend.controller;

import TeApp.TeBackend.dto.changePasswordDTO;
import TeApp.TeBackend.entity.Instructor;
import TeApp.TeBackend.entity.Observer;
import TeApp.TeBackend.entity.Roles;
import TeApp.TeBackend.entity.Users;
import TeApp.TeBackend.repository.InstructorRepo;
import TeApp.TeBackend.repository.ObserverRepo;
import TeApp.TeBackend.service.InstructorService;
import TeApp.TeBackend.service.ObserverService;
import TeApp.TeBackend.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private ObserverService observerService;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObserverRepo observerRepository;

    @Autowired
    private InstructorRepo instructorRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody Users user) {
        // Set first name and last name if provided
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First name must be provided");
        }

        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Last name must be provided");
        }

        // Check if the email already exists
        if (usersService.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        // Check if roles are provided in the request body
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            Set<Roles> roles = new HashSet<>(user.getRoles());
            user.setRoles(roles); // Set the roles for the user
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Roles must be provided");
        }

        Users newUser = usersService.registerUser(user);
        boolean isObserver = newUser.getRoles().stream()
                .anyMatch(role -> role.name().equalsIgnoreCase("OBSERVER"));

        if (isObserver) {
            // Save to observers table
            Observer observer = new Observer();
            observer.setFirstname(newUser.getFirstName());
            observer.setLastname(newUser.getLastName());
            observer.setEmail(newUser.getEmail());
            observerRepository.save(observer);
        }

        boolean isInstructor = newUser.getRoles().stream()
                .anyMatch(role -> role.name().equalsIgnoreCase("INSTRUCTOR"));

        if (isInstructor) {
            // Save to instructors table
            Instructor instructor = new Instructor();
            instructor.setFirstname(newUser.getFirstName());
            instructor.setLastname(newUser.getLastName());
            instructor.setEmail(newUser.getEmail());
            instructorRepository.save(instructor);
        }

        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Users user) {
        Users existingUser = usersService.findByEmail(user.getEmail());
        if (existingUser != null && passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            // Generate and return a login success response
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("id", existingUser.getId().toString());
            response.put("firstName", existingUser.getFirstName());
            response.put("lastName", existingUser.getLastName());
            response.put("email", existingUser.getEmail());
            response.put("roles", existingUser.getRoles().toString());

            // If role is OBSERVER
            if (existingUser.getRoles().stream().anyMatch(role -> role.name().equals("OBSERVER"))) {
                Observer observer = observerService.getObserverByEmail(existingUser.getEmail());
                if (observer != null) {
                    response.put("observerId", String.valueOf(observer.getObserver_id()));
                }
            }

            // If role is INSTRUCTOR
            if (existingUser.getRoles().stream().anyMatch(role -> role.name().equals("INSTRUCTOR"))) {
                Instructor instructor = instructorService.getInstructorByEmail(existingUser.getEmail());
                if (instructor != null) {
                    response.put("instructorId", String.valueOf(instructor.getInstructor_id()));
                }
            }
            return ResponseEntity.ok(response);
        }
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid credentials");
        return ResponseEntity.status(401).body(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody changePasswordDTO dto) {
        boolean result = usersService.changePassword(dto);
        if (result) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to change password");
        }
    }
}
