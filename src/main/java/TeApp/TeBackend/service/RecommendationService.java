package TeApp.TeBackend.service;

import TeApp.TeBackend.dto.recommendationDTO;
import TeApp.TeBackend.entity.Recommendation;
import TeApp.TeBackend.entity.Section;
import TeApp.TeBackend.repository.RecommendationRepo;
import TeApp.TeBackend.repository.SectionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepo recommendationRepository;

    public Recommendation saveRecommendation(Recommendation recommendation) {
        return recommendationRepository.save(recommendation);
    }

//    public List<Recommendation> getAllRecommendations() {
//        return recommendationRepository.findAll();
//    }

    public List<recommendationDTO> getAllRecommendations() {
        return recommendationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private recommendationDTO convertToDTO(Recommendation recommendation) {
        return new recommendationDTO(
                recommendation.getRec_id(),
                recommendation.getSection().getSection_id(),
                recommendation.getDescription(),
                recommendation.isSelected()
        );
    }

    public Recommendation convertToEntity(recommendationDTO recommendationdto) {
        Recommendation recommendation = new Recommendation();
        recommendation.setRec_id(recommendationdto.getRecId());
        recommendation.setDescription(recommendationdto.getDescription());
        recommendation.setSelected(recommendationdto.isSelected());
        Section section = new Section();
        section.setSection_id(recommendationdto.getSectionId());
        recommendation.setSection(section);
        return recommendation;
    }

    public Recommendation getRecommendationById(Long id) {
        return recommendationRepository.findById(id).orElse(null);
    }

    public void deleteRecommendation(Long id) {
        recommendationRepository.deleteById(id);
    }

    public List<Recommendation> findSelectedRecommendations() {
        return recommendationRepository.findBySelected(true);
    }

}

