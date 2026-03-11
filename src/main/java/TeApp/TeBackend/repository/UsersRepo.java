package TeApp.TeBackend.repository;

import TeApp.TeBackend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface UsersRepo extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    @Query("SELECT u FROM Users u JOIN u.roles r WHERE r = 'ADMIN'")
    List<Users> findAllAdmins();
}
