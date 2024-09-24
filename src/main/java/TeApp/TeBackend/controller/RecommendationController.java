package TeApp.TeBackend.controller;

import TeApp.TeBackend.dto.recommendationDTO;
import TeApp.TeBackend.dto.selectedOptionDTO;
import TeApp.TeBackend.entity.Recommendation;
import TeApp.TeBackend.repository.RecommendationRepo;
import TeApp.TeBackend.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/options")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

//    @PostMapping
//    public Recommendation createRecommendation(@RequestBody Recommendation recommendation) {
//        return recommendationService.saveRecommendation(recommendation);
//    }

    @PostMapping("/saveSelected")
    public ResponseEntity<?> saveSelectedOptions(@RequestBody List<Map<String, Object>> selectedOptions) {
        List<recommendationDTO> recommendations = recommendationService.getAllRecommendations();

        for (recommendationDTO recommendationdto : recommendations) {
            recommendationdto.setSelected(false);  // Mark everything as not selected
            Recommendation recommendationEntity = recommendationService.convertToEntity(recommendationdto);
            recommendationService.saveRecommendation(recommendationEntity);
        }

        for (Map<String, Object> option : selectedOptions) {
            Long sectionId = ((Integer) option.get("section_id")).longValue();

            for (recommendationDTO recommendationdto : recommendations) {

                if(sectionId.equals(recommendationdto.getSectionId())) {
//                    System.out.println("inside if");
                    recommendationdto.setSelected(true);
                    Recommendation recommendationEntity = recommendationService.convertToEntity(recommendationdto);
                    recommendationService.saveRecommendation(recommendationEntity);
//              System.out.println("SAVED");
                }
            }
        }
        return ResponseEntity.ok().body(Map.of("message", "Selected options saved successfully"));
    }

    @GetMapping("/getSelectedRecommendations")
    public ResponseEntity<List<Recommendation>> getSelectedRecommendations() {
        List<Recommendation> selectedRecommendations = recommendationService.findSelectedRecommendations();
        return ResponseEntity.ok(selectedRecommendations);
    }

    @GetMapping
    public List<recommendationDTO> getAllRecommendations() {
        return recommendationService.getAllRecommendations();
    }

    @GetMapping("/{id}")
    public Recommendation getRecommendationById(@PathVariable Long id) {
        return recommendationService.getRecommendationById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteRecommendation(@PathVariable Long id) {
        recommendationService.deleteRecommendation(id);
    }
}

