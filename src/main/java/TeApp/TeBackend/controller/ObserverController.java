package TeApp.TeBackend.controller;

import TeApp.TeBackend.entity.Observer;
import TeApp.TeBackend.service.ObserverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/observers")
public class ObserverController {

    @Autowired
    private ObserverService observerService;

    @PostMapping
    public Observer createObserver(@RequestBody Observer observer) {
        return observerService.saveObserver(observer);
    }

    @GetMapping
    public List<Observer> getAllObservers() {
        return observerService.getAllObservers();
    }

    @GetMapping("/{id}")
    public Observer getObserverById(@PathVariable Long id) {
        return observerService.getObserverById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteObserver(@PathVariable Long id) {
        observerService.deleteObserver(id);
    }
}

