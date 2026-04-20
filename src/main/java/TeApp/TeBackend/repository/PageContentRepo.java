package TeApp.TeBackend.repository;

import TeApp.TeBackend.entity.PageContent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PageContentRepo extends JpaRepository<PageContent, Long> {
    Optional<PageContent> findByPageKey(String pageKey);
}
