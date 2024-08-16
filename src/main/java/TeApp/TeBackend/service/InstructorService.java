package TeApp.TeBackend.service;

import TeApp.TeBackend.entity.Instructor;
import TeApp.TeBackend.repository.InstructorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InstructorService {

    @Autowired
    private InstructorRepo instructorRepository;

    public Instructor saveInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    public Instructor getInstructorById(Long id) {
        return instructorRepository.findById(id).orElse(null);
    }

    public void deleteInstructor(Long id) {
        instructorRepository.deleteById(id);
    }
}

