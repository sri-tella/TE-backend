package TeApp.TeBackend.service;

import TeApp.TeBackend.entity.PageContent;
import TeApp.TeBackend.repository.PageContentRepo;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PageContentService {

    private final PageContentRepo repo;

    public PageContentService(PageContentRepo repo) {
        this.repo = repo;
    }

    public Optional<PageContent> getByKey(String pageKey) {
        return repo.findByPageKey(pageKey);
    }

    public PageContent save(String pageKey, String htmlContent) {
        PageContent page = repo.findByPageKey(pageKey).orElse(new PageContent());
        page.setPageKey(pageKey);
        page.setHtmlContent(htmlContent);
        page.setUpdatedAt(LocalDateTime.now());
        return repo.save(page);
    }
}
