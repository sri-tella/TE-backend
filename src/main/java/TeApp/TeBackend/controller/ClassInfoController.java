package TeApp.TeBackend.controller;

import TeApp.TeBackend.entity.ClassInfo;
import TeApp.TeBackend.entity.Instructor;
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
    public List<Map<String, Object>> getAllClassInfoWithInstructors() {
        List<ClassInfo> classes = classInfoService.getAllClasses();
        List<Map<String, Object>> response = new ArrayList<>();

        for (ClassInfo classInfo : classes) {
            Map<String, Object> classMap = new HashMap<>();
            classMap.put("classId", classInfo.getClass_id());
            classMap.put("title", classInfo.getTitle());
            classMap.put("description", classInfo.getDescription());

            Instructor instructor = classInfo.getInstructor();
            if (instructor != null) {
                classMap.put("instructorId", instructor.getInstructor_id());
                classMap.put("instructorFirstName", instructor.getFirstname());
                classMap.put("instructorLastName", instructor.getLastname());
                classMap.put("instructorEmail", instructor.getEmail());
            }

            response.add(classMap);
        }

        return response;
    }
}

