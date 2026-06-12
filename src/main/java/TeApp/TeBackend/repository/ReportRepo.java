package TeApp.TeBackend.repository;

import TeApp.TeBackend.dto.reportSummaryDTO;
import TeApp.TeBackend.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepo extends JpaRepository<Report, Long> {

    @Query("SELECT new TeApp.TeBackend.dto.reportSummaryDTO(r.report_id, r.evaluation, r.reportContent, r.createdAt) FROM Report r")
    List<reportSummaryDTO> findAllSummaries();
}
