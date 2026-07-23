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
    public ResponseEntity<Void> submitIntstructorForm(@RequestBody introFormDTO formDTO) {
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
        classInfoService.saveClassInfo(classInfo);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/instructor/notify-observer")
    public ResponseEntity<?> notifyObserver(@RequestBody Map<String, String> body) {
        String observerEmail = body.get("observerEmail");
        String instructorName = body.get("instructorName");

        if (observerEmail == null || observerEmail.isBlank()) {
            return ResponseEntity.badRequest().body("Observer email is required");
        }

        emailService.sendInstructorFormCompleteEmail(observerEmail, instructorName);
        return ResponseEntity.ok(Map.of("message", "Notification sent to observer"));
    }
}
