package TeApp.TeBackend.repository;

import TeApp.TeBackend.entity.Notification;
import TeApp.TeBackend.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, Long> {
    List<Notification> findByTargetRoleAndReadStatusFalse(Roles role);
}
