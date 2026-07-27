package TeApp.TeBackend.controller;

import TeApp.TeBackend.entity.ClassInfo;
import TeApp.TeBackend.entity.Instructor;
import TeApp.TeBackend.entity.Observer;
import TeApp.TeBackend.service.ClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classes")
public class ClassInfoController {

    @Autowired
    private ClassInfoService classInfoService;

    @PostMapping
    public ClassInfo createClass(@RequestBody ClassInfo classInfo) {
        return classInfoService.saveClassInfo(classInfo);
    }

    @GetMapping
    public List<ClassInfo> getAllClasses() {
        return classInfoService.getAllClasses();
    }

    @GetMapping("/{id}")
    public ClassInfo getClassById(@PathVariable Long id) {
        return classInfoService.getClassById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteClass(@PathVariable Long id) {
        classInfoService.deleteClass(id);
    }

    @GetMapping("/with-instructors")
    public List<Map<String, Object>> getAllClassInfoWithInstructors(
            @RequestParam(value = "observerId", required = false) Long observerId) {
        List<ClassInfo> classes = classInfoService.getAllClasses();
        List<Map<String, Object>> response = new ArrayList<>();

        for (ClassInfo classInfo : classes) {
            Observer observer = classInfo.getObserver();

            // An observer only sees sessions their instructor assigned to
            // them. Sessions with no observer assigned (legacy data) show
            // for nobody rather than everybody.
            if (observerId != null) {
                if (observer == null || observer.getObserver_id() != observerId) {
                    continue;
                }
            }

            Map<String, Object> classMap = new HashMap<>();
            classMap.put("classId", classInfo.getClass_id());
            classMap.put("title", classInfo.getTitle());
            classMap.put("description", classInfo.getDescription());
            classMap.put("isArchived", classInfo.isArchived());

            Instructor instructor = classInfo.getInstructor();
            if (instructor != null) {
                classMap.put("instructorId", instructor.getInstructor_id());
                classMap.put("instructorFirstName", instructor.getFirstname());
                classMap.put("instructorLastName", instructor.getLastname());
                classMap.put("instructorEmail", instructor.getEmail());
            }
            if (observer != null) {
                classMap.put("observerId", observer.getObserver_id());
                classMap.put("observerFirstName", observer.getFirstname());
                classMap.put("observerLastName", observer.getLastname());
            }

            response.add(classMap);
        }

        return response;
    }

    @PutMapping("/{id}/archive")
    public void setArchiveStatus(@PathVariable Long id, @RequestBody boolean isArchived) {
        ClassInfo classInfo = classInfoService.getClassById(id);
        if (classInfo != null) {
            classInfo.setArchived(isArchived); 
            classInfoService.saveClassInfo(classInfo);
        }
    }
}

