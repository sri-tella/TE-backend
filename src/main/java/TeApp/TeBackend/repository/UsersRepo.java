package TeApp.TeBackend.repository;

import TeApp.TeBackend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsersRepo extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    @Query("SELECT u FROM Users u JOIN u.roles r WHERE r = TeApp.TeBackend.entity.Roles.ADMIN")
    List<Users> findAllAdmins();
}
