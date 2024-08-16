package TeApp.TeBackend.service;

import TeApp.TeBackend.entity.ClassInfo;
import TeApp.TeBackend.repository.ClassInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClassInfoService {

    @Autowired
    private ClassInfoRepo classRepository;

    public ClassInfo saveClassInfo(ClassInfo classInfo) {
        return classRepository.save(classInfo);
    }

    public List<ClassInfo> getAllClasses() {
        return classRepository.findAll();
    }

    public ClassInfo getClassById(Long id) {
        return classRepository.findById(id).orElse(null);
    }

    public void deleteClass(Long id) {
        classRepository.deleteById(id);
    }
}

