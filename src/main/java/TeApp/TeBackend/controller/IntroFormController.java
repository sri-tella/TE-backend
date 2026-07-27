package TeApp.TeBackend.controller;

import TeApp.TeBackend.dto.introFormDTO;
import TeApp.TeBackend.entity.ClassInfo;
import TeApp.TeBackend.entity.Instructor;
import TeApp.TeBackend.entity.Observer;
import TeApp.TeBackend.service.ClassInfoService;
import TeApp.TeBackend.service.EmailService;
import TeApp.TeBackend.service.InstructorService;
import TeApp.TeBackend.service.ObserverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/form")
public class IntroFormController {

    @Autowired
    private ObserverService observerService;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private ClassInfoService classInfoService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/instructor")
    public ResponseEntity<?> submitIntstructorForm(@RequestBody introFormDTO formDTO) {
        if (formDTO.getInstructorEmail() == null || formDTO.getInstructorEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Instructor email is required");
        }
        if (formDTO.getObserverId() == null) {
            return ResponseEntity.badRequest().body("Please select an observer");
        }

        Observer observer = observerService.getObserverById(formDTO.getObserverId());
        if (observer == null) {
            return ResponseEntity.badRequest().body("Selected observer was not found");
        }

        Instructor instructor = instructorService.getInstructorByEmail(formDTO.getInstructorEmail());

        if (instructor == null) {
            instructor = new Instructor();
            instructor.setFirstname(formDTO.getInstructorFirstName());
            instructor.setLastname(formDTO.getInstructorLastName());
            instructor.setEmail(formDTO.getInstructorEmail());
            instructor = instructorService.saveInstructor(instructor);
        }

        ClassInfo classInfo = new ClassInfo();
        classInfo.setTitle(formDTO.getCourseTitle());
        classInfo.setDescription(formDTO.getCourseDescription());
        classInfo.setTopic(formDTO.getTopic());
        classInfo.setDate(formDTO.getDate());
        classInfo.setTime(formDTO.getTime());
        classInfo.setGoal(formDTO.getGoal());
        classInfo.setOutline(formDTO.getOutline());
        classInfo.setHelp(formDTO.getHelp());
        classInfo.setInstructor(instructor);
        classInfo.setObserver(observer);
        classInfoService.saveClassInfo(classInfo);

        String instructorName = instructor.getFirstname() + " " + instructor.getLastname();
        try {
            emailService.sendInstructorFormCompleteEmail(observer.getEmail(), instructorName);
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                    "emailWarning", "Session saved, but could not notify the observer by email: "
                            + EmailService.describeError(e)
            ));
        }
        return ResponseEntity.ok(Map.of("message", "Session saved, observer notified"));
    }
}
