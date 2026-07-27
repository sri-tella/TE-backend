package TeApp.TeBackend.controller;

import TeApp.TeBackend.entity.Instructor;
import TeApp.TeBackend.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @PostMapping
    public ResponseEntity<?> createInstructor(@RequestBody Instructor instructor) {
        if (instructor.getEmail() == null || instructor.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Instructor email is required");
        }
        return ResponseEntity.ok(instructorService.saveInstructor(instructor));
    }

    @GetMapping
    public List<Instructor> getAllInstructors() {
        return instructorService.getAllInstructors();
    }

    @GetMapping("/{id}")
    public Instructor getInstructorById(@PathVariable Long id) {
        return instructorService.getInstructorById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteInstructor(@PathVariable Long id) {
        instructorService.deleteInstructor(id);
    }
}

