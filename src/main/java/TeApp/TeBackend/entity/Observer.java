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
@Table(name = "observers")
public class Observer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long observer_id;


    private String firstname;
    private String lastname;
    private String email;
}
