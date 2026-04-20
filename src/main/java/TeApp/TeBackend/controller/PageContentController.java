package TeApp.TeBackend.controller;

import TeApp.TeBackend.entity.PageContent;
import TeApp.TeBackend.service.PageContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/content")
@CrossOrigin
public class PageContentController {

    private final PageContentService service;

    public PageContentController(PageContentService service) {
        this.service = service;
    }

    @GetMapping("/{pageKey}")
    public ResponseEntity<Map<String, String>> getContent(@PathVariable String pageKey) {
        return service.getByKey(pageKey)
                .map(p -> ResponseEntity.ok(Map.of("htmlContent", p.getHtmlContent())))
                .orElse(ResponseEntity.ok(Map.of("htmlContent", "")));
    }

    @PutMapping("/{pageKey}")
    public ResponseEntity<Void> saveContent(
            @PathVariable String pageKey,
            @RequestBody Map<String, String> body) {
        service.save(pageKey, body.get("htmlContent"));
        return ResponseEntity.noContent().build();
    }
}
