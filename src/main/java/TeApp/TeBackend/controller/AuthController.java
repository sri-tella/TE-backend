package TeApp.TeBackend.controller;

import TeApp.TeBackend.entity.Roles;
import TeApp.TeBackend.entity.Users;
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
//@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Users user) {
        Users existingUser = usersService.findByEmail(user.getEmail());
        if (existingUser != null && passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            // Generate and return a token (or other login success response)
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        }
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid credentials");
        return ResponseEntity.status(401).body(response);
    }
}
