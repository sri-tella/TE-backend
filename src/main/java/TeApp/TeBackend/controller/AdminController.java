package TeApp.TeBackend.controller;

import TeApp.TeBackend.dto.adminDTO;
import TeApp.TeBackend.dto.roleRequestDTO;
import TeApp.TeBackend.dto.roleRequestViewDTO;
import TeApp.TeBackend.entity.*;
import TeApp.TeBackend.repository.InstructorRepo;
import TeApp.TeBackend.repository.NotificationRepo;
import TeApp.TeBackend.repository.ObserverRepo;
import TeApp.TeBackend.repository.RoleRequestRepo;
import TeApp.TeBackend.repository.UsersRepo;
import TeApp.TeBackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import TeApp.TeBackend.service.PasswordGenerator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/admins")
@CrossOrigin
public class AdminController {

    @Autowired private UsersRepo userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private PasswordGenerator passwordGenerator;
    @Autowired private EmailService emailService;
    @Autowired private RoleRequestRepo roleRequestRepo;
    @Autowired private NotificationRepo notificationRepo;
    @Autowired private ObserverRepo observerRepo;
    @Autowired private InstructorRepo instructorRepo;

    // ── Create user (Observer or Instructor) ─────────────────────────────
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody adminDTO dto) {
        if (userRepository.findAll().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(dto.getEmail()))) {
            return ResponseEntity.badRequest().body("Error: Email already exists");
        }

        String role = dto.getRole() != null ? dto.getRole().toUpperCase() : "OBSERVER";
        if (!role.equals("OBSERVER") && !role.equals("INSTRUCTOR")) {
            return ResponseEntity.badRequest().body("Error: role must be OBSERVER or INSTRUCTOR");
        }

        String tempPassword = passwordGenerator.generate();

        Users user = new Users();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(tempPassword));

        Set<Roles> roles = new HashSet<>();
        roles.add(Roles.valueOf(role));
        user.setRoles(roles);
        userRepository.save(user);

        if (role.equals("OBSERVER")) {
            Observer obs = new Observer();
            obs.setFirstname(user.getFirstName());
            obs.setLastname(user.getLastName());
            obs.setEmail(user.getEmail());
            observerRepo.save(obs);
        } else {
            Instructor inst = new Instructor();
            inst.setFirstname(user.getFirstName());
            inst.setLastname(user.getLastName());
            inst.setEmail(user.getEmail());
            instructorRepo.save(inst);
        }

        Notification notification = new Notification();
        notification.setMessage("New " + role + " account created: " + user.getFirstName() + " " + user.getLastName());
        notification.setTargetRole(Roles.ADMIN);
        notificationRepo.save(notification);

        emailService.sendAdminWelcomeEmail(user.getFirstName(), user.getLastName(), user.getEmail(), tempPassword);

        return ResponseEntity.ok(user);
    }

    // ── List admins ───────────────────────────────────────────────────────
    @GetMapping
    public List<Users> getAllAdmins() {
        return userRepository.findAllAdmins();
    }

    // ── Delete admin ──────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            if (user.getRoles().contains(Roles.ADMIN)) {
                userRepository.deleteById(id);
                return "Admin deleted successfully";
            }
            return "Error: User is not an admin";
        }).orElse("Error: User not found");
    }

    // ── List all non-admin users ──────────────────────────────────────────
    @GetMapping("/all-users")
    public List<Map<String, Object>> getAllUsers() {
        return userRepository.findAllNonAdmins().stream().map(u -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", u.getId());
            map.put("firstName", u.getFirstName());
            map.put("lastName", u.getLastName());
            map.put("email", u.getEmail());
            map.put("roles", u.getRoles().stream().map(Enum::name).toList());
            map.put("canSwitchRoles", u.getRoles().contains(Roles.OBSERVER) && u.getRoles().contains(Roles.INSTRUCTOR));
            map.put("canEditContent", u.isCanEditContent());
            return map;
        }).toList();
    }

    // ── Toggle role switching (grant/revoke dual role) ────────────────────
    @PatchMapping("/users/{id}/toggle-role-switch")
    public ResponseEntity<?> toggleRoleSwitch(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            Set<Roles> roles = user.getRoles();
            boolean hasObserver   = roles.contains(Roles.OBSERVER);
            boolean hasInstructor = roles.contains(Roles.INSTRUCTOR);

            if (hasObserver && hasInstructor) {
                // Revoke: keep the active role, remove the other
                String active = user.getActiveRole();
                if ("INSTRUCTOR".equals(active)) {
                    roles.remove(Roles.OBSERVER);
                } else {
                    roles.remove(Roles.INSTRUCTOR);
                }
            } else {
                // Grant: add the missing role + create its profile record if needed
                if (!hasObserver) {
                    roles.add(Roles.OBSERVER);
                    if (observerRepo.findByEmail(user.getEmail()).isEmpty()) {
                        Observer obs = new Observer();
                        obs.setFirstname(user.getFirstName());
                        obs.setLastname(user.getLastName());
                        obs.setEmail(user.getEmail());
                        observerRepo.save(obs);
                    }
                }
                if (!hasInstructor) {
                    roles.add(Roles.INSTRUCTOR);
                    if (instructorRepo.findByEmail(user.getEmail()).isEmpty()) {
                        Instructor inst = new Instructor();
                        inst.setFirstname(user.getFirstName());
                        inst.setLastname(user.getLastName());
                        inst.setEmail(user.getEmail());
                        instructorRepo.save(inst);
                    }
                }
            }

            user.setRoles(roles);
            userRepository.save(user);
            boolean canSwitch = user.getRoles().contains(Roles.OBSERVER) && user.getRoles().contains(Roles.INSTRUCTOR);
            return ResponseEntity.ok(Map.of("canSwitchRoles", canSwitch));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── Content edit permission ───────────────────────────────────────────
    @GetMapping("/observers")
    public List<Users> getAllObservers() {
        return userRepository.findAllObservers();
    }

    @GetMapping("/users/{id}/content-permission")
    public ResponseEntity<?> getContentPermission(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(Map.of("canEditContent", user.isCanEditContent())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/users/{id}/content-permission")
    public ResponseEntity<?> setContentPermission(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        return userRepository.findById(id).map(user -> {
            user.setCanEditContent(body.get("canEditContent"));
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("canEditContent", user.isCanEditContent()));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── Role requests ─────────────────────────────────────────────────────
    @PostMapping("/roleRequests/{id}/approve")
    public String approveRequest(@PathVariable Long id) {
        RoleRequest request = roleRequestRepo.findById(id).orElseThrow();
        request.setStatus(RequestStatus.APPROVED);
        roleRequestRepo.save(request);

        Users user = request.getUser();
        user.getRoles().add(request.getRequestedRole());
        userRepository.save(user);

        Notification notification = new Notification();
        notification.setMessage("Your role request has been approved! You now have " + request.getRequestedRole() + " access.");
        notification.setTargetRole(Roles.INSTRUCTOR);
        notificationRepo.save(notification);

        return "Request approved";
    }

    @GetMapping("/roleRequests")
    public List<roleRequestViewDTO> getPendingRequests() {
        return roleRequestRepo.findByStatus(RequestStatus.PENDING).stream().map(request -> {
            roleRequestViewDTO dto = new roleRequestViewDTO();
            dto.setId(request.getId());
            dto.setRequestedRole(request.getRequestedRole());
            Users user = request.getUser();
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            return dto;
        }).toList();
    }

    @PostMapping("/roleRequests")
    public String requestDualRole(@RequestBody roleRequestDTO dto) {
        Optional<Users> userOptional = userRepository.findById(dto.getId());
        if (userOptional.isEmpty()) return "User not found";
        if (roleRequestRepo.existsByUserAndStatus(userOptional, RequestStatus.PENDING)) return "You already have a pending request";

        RoleRequest request = new RoleRequest();
        Users user = userOptional.get();
        request.setUser(user);
        request.setRequestedRole(dto.getRequestedRole());
        request.setStatus(RequestStatus.PENDING);
        roleRequestRepo.save(request);

        Notification notification = new Notification();
        notification.setMessage("Role request pending from " + user.getFirstName() + " " + user.getLastName());
        notification.setTargetRole(Roles.ADMIN);
        notificationRepo.save(notification);

        return "Request submitted";
    }
}
