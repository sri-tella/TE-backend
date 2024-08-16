package TeApp.TeBackend.service;

import TeApp.TeBackend.entity.Observer;
import TeApp.TeBackend.repository.ObserverRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ObserverService {

    @Autowired
    private ObserverRepo observerRepository;

    public Observer saveObserver(Observer observer) {
        return observerRepository.save(observer);
    }

    public List<Observer> getAllObservers() {
        return observerRepository.findAll();
    }

    public Observer getObserverById(Long id) {
        return observerRepository.findById(id).orElse(null);
    }

    public void deleteObserver(Long id) {
        observerRepository.deleteById(id);
    }
}
