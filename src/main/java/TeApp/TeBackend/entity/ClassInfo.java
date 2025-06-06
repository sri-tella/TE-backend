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
}
