package TeApp.TeBackend.repository;

import TeApp.TeBackend.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepo extends JpaRepository<Report, Long> {
}
