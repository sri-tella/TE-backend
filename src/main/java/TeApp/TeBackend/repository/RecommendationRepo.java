package TeApp.TeBackend.repository;

import TeApp.TeBackend.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendationRepo extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findBySelected(boolean selected);
}
