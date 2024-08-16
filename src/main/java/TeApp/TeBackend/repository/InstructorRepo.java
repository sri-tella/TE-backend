package TeApp.TeBackend.repository;

import TeApp.TeBackend.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepo extends JpaRepository<Instructor, Long> {
}
