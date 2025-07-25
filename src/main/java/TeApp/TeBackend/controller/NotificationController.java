package TeApp.TeBackend.controller;

import TeApp.TeBackend.entity.Notification;
import TeApp.TeBackend.entity.Roles;
import TeApp.TeBackend.repository.NotificationRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin
public class NotificationController {

    private final NotificationRepo notificationRepo;

    public NotificationController(NotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    @GetMapping
    public List<Notification> getAllNotifications() {
        return notificationRepo.findAll();
    }

    @GetMapping("/role/{role}")
    public List<Notification> getUnreadNotificationsByRole(@PathVariable Roles role) {
        return notificationRepo.findByTargetRoleAndReadStatusFalse(role);
    }

    @PostMapping
    public Notification createNotification(@RequestBody Notification notification) {
        return notificationRepo.save(notification);
    }

    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable Long id) {
        notificationRepo.deleteById(id);
    }

    @PatchMapping("/{id}/read")
    public String markAsRead(@PathVariable Long id) {
        Notification notification = notificationRepo.findById(id).orElseThrow();
        notification.setReadStatus(true);
        notificationRepo.save(notification);
        return "Notification marked as read";
    }
}
