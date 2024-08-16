package TeApp.TeBackend.repository;

import TeApp.TeBackend.entity.ClassInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassInfoRepo extends JpaRepository<ClassInfo, Long> {
}
