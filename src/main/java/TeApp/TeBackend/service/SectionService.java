package TeApp.TeBackend.service;

import TeApp.TeBackend.entity.Section;
import TeApp.TeBackend.repository.SectionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SectionService {

    @Autowired
    private SectionRepo sectionRepository;

    public Section saveSection(Section section) {
        return sectionRepository.save(section);
    }

    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    public Section getSectionById(Long id) {
        return sectionRepository.findById(id).orElse(null);
    }

    public void deleteSection(Long id) {
        sectionRepository.deleteById(id);
    }
}

