package TeApp.TeBackend.repository;

import TeApp.TeBackend.entity.Observer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ObserverRepo extends JpaRepository<Observer, Long> {
    Optional<Observer> findByEmail(String email);

}
