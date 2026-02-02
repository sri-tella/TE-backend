package TeApp.TeBackend.repository;

import TeApp.TeBackend.entity.ReportRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRecommendationRepo extends JpaRepository<ReportRecommendation, Long> {
    // You can define custom query methods here if needed
}
