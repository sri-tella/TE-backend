package TeApp.TeBackend.controller;

import TeApp.TeBackend.dto.adminDTO;
import TeApp.TeBackend.dto.roleRequestDTO;
import TeApp.TeBackend.dto.roleRequestViewDTO;
import TeApp.TeBackend.entity.*;
import TeApp.TeBackend.repository.NotificationRepo;
import TeApp.TeBackend.repository.RoleRequestRepo;
import TeApp.TeBackend.repository.UsersRepo;
import TeApp.TeBackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import TeApp.TeBackend.service.PasswordGenerator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Autowired
    private RoleRequestRepo roleRequestRepo;

    @Autowired
    private NotificationRepo notificationRepo;

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

        Users user = new Users();
        user.setFirstName(newAdminDTO.getFirstName());
        user.setLastName(newAdminDTO.getLastName());
        user.setEmail(newAdminDTO.getEmail());
        user.setPassword(encodedPassword);
        user.getRoles().add(Roles.ADMIN);

        userRepository.save(user);

        Notification notification = new Notification();
        notification.setMessage("New admin " + user.getFirstName() + " " + user.getLastName() + " has been added.");
        notification.setTargetRole(Roles.ADMIN);
        notificationRepo.save(notification);

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
        return roleRequestRepo.findByStatus(RequestStatus.PENDING)
            .stream()
            .map(request -> {
                roleRequestViewDTO dto = new roleRequestViewDTO();
                dto.setId(request.getId());
                dto.setRequestedRole(request.getRequestedRole());

                Users user = request.getUser();
                dto.setFirstName(user.getFirstName());
                dto.setLastName(user.getLastName());
                dto.setEmail(user.getEmail());

                return dto;
            })
            .toList();
    }

    @PostMapping("/roleRequests")
    public String requestDualRole(@RequestBody roleRequestDTO dto) {
        Optional<Users> userOptional = userRepository.findById(dto.getId());
        if(userOptional.isEmpty()) {
            return " User not found";
        }


        if (roleRequestRepo.existsByUserAndStatus(userOptional, RequestStatus.PENDING)) {
            return "You already have a pending request";
        }

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
