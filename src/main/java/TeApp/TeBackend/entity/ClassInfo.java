package TeApp.TeBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "classes")
public class ClassInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long class_id;


    private String title;
    private String description;

    private String topic;
    private String date;
    private String time;

    @Column(length = 1000)
    private String goal;

    @Column(length = 1000)
    private String outline;

    @Column(length = 1000)
    private String help;

    @ManyToOne
    private Instructor instructor;

    // The observer the instructor assigned during session setup. Many
    // ClassInfo rows (from different instructors, or the same one) can
    // point at the same Observer.
    @ManyToOne
    private Observer observer;

    @OneToMany(mappedBy = "className", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Evaluation> evaluations;

    @Column(columnDefinition = "boolean default false")
    private boolean isArchived;
}
