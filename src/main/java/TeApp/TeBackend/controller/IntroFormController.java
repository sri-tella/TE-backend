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

    @PostMapping
    public ResponseEntity<Void> submitCombinedForm(@RequestBody introFormDTO formDTO) {
        Observer observer = new Observer();
        observer.setFirstname(formDTO.getObserverFirstName());
        observer.setLastname(formDTO.getObserverLastName());
        observer.setEmail(formDTO.getObserverEmail());
        observer = observerService.saveObserver(observer);

        Instructor instructor = new Instructor();
        instructor.setFirstname(formDTO.getInstructorFirstName());
        instructor.setLastname(formDTO.getInstructorLastName());
        instructor = instructorService.saveInstructor(instructor);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setTitle(formDTO.getCourseTitle());
        classInfo.setDescription(formDTO.getCourseDescription());
        classInfo.setInstructor(instructor);
        classInfoService.saveClassInfo(classInfo);

        return ResponseEntity.ok().build();
    }
}

