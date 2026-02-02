package TeApp.TeBackend.repository;

import TeApp.TeBackend.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepo extends JpaRepository<Section, Long> {
}
