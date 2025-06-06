package TeApp.TeBackend.repository;

import TeApp.TeBackend.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstructorRepo extends JpaRepository<Instructor, Long> {
    Optional<Instructor> findByEmail(String email);

}
