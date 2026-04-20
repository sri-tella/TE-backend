package TeApp.TeBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class PageContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String pageKey;

    @Column(columnDefinition = "LONGTEXT")
    private String htmlContent;

    private LocalDateTime updatedAt = LocalDateTime.now();
}
