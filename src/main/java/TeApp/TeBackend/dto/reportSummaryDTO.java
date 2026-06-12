package TeApp.TeBackend.dto;

import TeApp.TeBackend.entity.Evaluation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class reportSummaryDTO {
    private Long report_id;
    private Evaluation evaluation;
    private String reportContent;
    private LocalDateTime createdAt;
}
