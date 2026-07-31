package TeApp.TeBackend.repository;

import TeApp.TeBackend.entity.RequestStatus;
import TeApp.TeBackend.entity.RoleRequest;
import TeApp.TeBackend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRequestRepo extends JpaRepository<RoleRequest, Long>{

    List<RoleRequest> findByStatus(RequestStatus status);

    boolean existsByUserAndStatus(Users user, RequestStatus status);

}
