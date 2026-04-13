package TeApp.TeBackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long report_id;

    @ManyToOne
    @JoinColumn(name = "evaluation_id")
    private Evaluation evaluation;

    @Lob
    @Column(name = "pdf_content", columnDefinition = "LONGBLOB")
    private byte[] pdfContent;

    @Column(name = "report_content", columnDefinition = "LONGTEXT")
    private String reportContent;

    private LocalDateTime createdAt;

}
