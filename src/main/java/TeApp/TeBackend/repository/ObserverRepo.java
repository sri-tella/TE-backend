package TeApp.TeBackend.repository;

import TeApp.TeBackend.entity.Observer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObserverRepo extends JpaRepository<Observer, Long> {
}
