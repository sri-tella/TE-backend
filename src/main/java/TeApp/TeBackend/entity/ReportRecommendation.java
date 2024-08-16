package TeApp.TeBackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "report_recommendations")
public class ReportRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportRecId;

    private String description;

    @Column(columnDefinition = "boolean default false")
    private boolean selected;

    private String feedback;

    @ManyToOne
    @JoinColumn(name = "evaluation_id")
    private Evaluation evaluation;
}
