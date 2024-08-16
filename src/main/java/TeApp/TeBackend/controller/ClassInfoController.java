package TeApp.TeBackend.controller;

import TeApp.TeBackend.entity.ClassInfo;
import TeApp.TeBackend.service.ClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}

