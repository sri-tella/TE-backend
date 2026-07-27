package TeApp.TeBackend.controller;

import TeApp.TeBackend.dto.changePasswordDTO;
import TeApp.TeBackend.entity.Instructor;
import TeApp.TeBackend.entity.Observer;
import TeApp.TeBackend.entity.Roles;
import TeApp.TeBackend.entity.Users;
import TeApp.TeBackend.repository.InstructorRepo;
import TeApp.TeBackend.repository.ObserverRepo;
import TeApp.TeBackend.repository.UsersRepo;
import TeApp.TeBackend.service.InstructorService;
import TeApp.TeBackend.service.ObserverService;
import TeApp.TeBackend.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    private UsersRepo usersRepo;

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
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First name must be provided");
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Last name must be provided");
        }
        if (usersService.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        boolean isObserver = user.getRoles() != null && user.getRoles().contains(Roles.OBSERVER);
        boolean isInstructor = user.getRoles() != null && user.getRoles().contains(Roles.INSTRUCTOR);
        if (isObserver == isInstructor) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select a role: Observer or Instructor");
        }

        Set<Roles> roles = new HashSet<>();
        roles.add(isObserver ? Roles.OBSERVER : Roles.INSTRUCTOR);
        user.setRoles(roles);
        user.setActiveRole(isObserver ? "OBSERVER" : "INSTRUCTOR");

        Users newUser = usersService.registerUser(user);

        if (isObserver) {
            Observer observer = new Observer();
            observer.setFirstname(newUser.getFirstName());
            observer.setLastname(newUser.getLastName());
            observer.setEmail(newUser.getEmail());
            observerRepository.save(observer);
        } else {
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
            Map<String, String> response = buildUserResponse(existingUser);
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        }
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid credentials");
        return ResponseEntity.status(401).body(response);
    }

    // Lets an already-logged-in client refresh its own roles/activeRole without
    // requiring a logout+login - e.g. after an admin grants a new role.
    @GetMapping("/user/{id}")
    public ResponseEntity<Map<String, String>> getUser(@PathVariable Long id) {
        Users existingUser = usersRepo.findById(id).orElse(null);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(buildUserResponse(existingUser));
    }

    private Map<String, String> buildUserResponse(Users existingUser) {
        Map<String, String> response = new HashMap<>();
        response.put("userId", String.valueOf(existingUser.getId()));
        response.put("firstName", existingUser.getFirstName());
        response.put("lastName", existingUser.getLastName());
        response.put("email", existingUser.getEmail());
        response.put("roles", existingUser.getRoles().toString());
        response.put("canEditContent", String.valueOf(existingUser.isCanEditContent()));
        response.put("activeRole", existingUser.getActiveRole() != null ? existingUser.getActiveRole() : "");

        Observer observer = observerService.getObserverByEmail(existingUser.getEmail());
        if (observer == null && existingUser.getRoles().stream().anyMatch(r -> r.name().equals("ADMIN"))) {
            observer = new Observer();
            observer.setFirstname(existingUser.getFirstName());
            observer.setLastname(existingUser.getLastName());
            observer.setEmail(existingUser.getEmail());
            observerRepository.save(observer);
        }
        if (observer != null) {
            response.put("observerId", String.valueOf(observer.getObserver_id()));
        }

        Instructor instructor = instructorService.getInstructorByEmail(existingUser.getEmail());
        if (instructor != null) {
            response.put("instructorId", String.valueOf(instructor.getInstructor_id()));
        }
        return response;
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody changePasswordDTO dto) {
        Users user = usersService.findByEmail(dto.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password is incorrect");
        }
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New passwords do not match");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        usersRepo.save(user);
        return ResponseEntity.ok("Password updated successfully");
    }

    @PatchMapping("/users/{id}/active-role")
    public ResponseEntity<?> setActiveRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return usersRepo.findById(id).map(u -> {
            String requestedRole = body.get("activeRole");
            Roles role;
            try {
                role = Roles.valueOf(requestedRole);
            } catch (IllegalArgumentException | NullPointerException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role");
            }
            if (!u.getRoles().contains(role)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not have the " + requestedRole + " role");
            }

            u.setActiveRole(requestedRole);
            usersRepo.save(u);

            Map<String, String> response = new HashMap<>();
            response.put("activeRole", u.getActiveRole());
            Observer observer = observerService.getObserverByEmail(u.getEmail());
            if (observer != null) {
                response.put("observerId", String.valueOf(observer.getObserver_id()));
            }
            Instructor instructor = instructorService.getInstructorByEmail(u.getEmail());
            if (instructor != null) {
                response.put("instructorId", String.valueOf(instructor.getInstructor_id()));
            }
            return ResponseEntity.ok(response);
        }).orElse(ResponseEntity.notFound().build());
    }
}
