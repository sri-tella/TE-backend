package TeApp.TeBackend.controller;

import TeApp.TeBackend.dto.introFormDTO;
import TeApp.TeBackend.entity.ClassInfo;
import TeApp.TeBackend.entity.Instructor;
import TeApp.TeBackend.entity.Observer;
import TeApp.TeBackend.service.ClassInfoService;
import TeApp.TeBackend.service.InstructorService;
import TeApp.TeBackend.service.ObserverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/form")
public class IntroFormController {
    @Autowired
    private ObserverService observerService;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private ClassInfoService classInfoService;

    @PostMapping("/instructor")
    public ResponseEntity<Void> submitIntstructorForm(@RequestBody introFormDTO formDTO) {
        // 1. Save instructor
        Instructor instructor = instructorService.getInstructorByEmail(formDTO.getInstructorEmail());

        if (instructor == null) {
            // Create new only if not found
            instructor = new Instructor();
            instructor.setFirstname(formDTO.getInstructorFirstName());
            instructor.setLastname(formDTO.getInstructorLastName());
            instructor.setEmail(formDTO.getInstructorEmail());
            instructor = instructorService.saveInstructor(instructor);
        }

        // 2. Save class info
        ClassInfo classInfo = new ClassInfo();
        classInfo.setTitle(formDTO.getCourseTitle());
        classInfo.setDescription(formDTO.getCourseDescription());
        classInfo.setTopic(formDTO.getTopic());
        classInfo.setDate(formDTO.getDate());
        classInfo.setTime(formDTO.getTime());
        classInfo.setGoal(formDTO.getGoal());
        classInfo.setOutline(formDTO.getOutline());
        classInfo.setHelp(formDTO.getHelp());
        classInfo.setInstructor(instructor);  // assuming @ManyToOne mapping
        classInfoService.saveClassInfo(classInfo);

        return ResponseEntity.ok().build();
    }
}

