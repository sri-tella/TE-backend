package TeApp.TeBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rec_id;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "section_id")
    private Section section;

    private String description;

    @Column(columnDefinition = "boolean default false")
    private boolean selected;

}
