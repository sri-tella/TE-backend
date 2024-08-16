package TeApp.TeBackend.controller;

import TeApp.TeBackend.dto.evaluationDTO;
import TeApp.TeBackend.dto.recDTO;
import TeApp.TeBackend.entity.*;
import TeApp.TeBackend.repository.EvaluationRepo;
import TeApp.TeBackend.service.ObserverService;
import TeApp.TeBackend.service.InstructorService;
import TeApp.TeBackend.service.ClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationRepo evaluationRepository;

    @Autowired
    private ObserverService observerService;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private ClassInfoService classInfoService;

    @PostMapping("/save")
    public ResponseEntity<Evaluation> createEvaluation(@RequestBody evaluationDTO evaluationdto) {
        // Retrieve associated entities from the database
//        Observer observer = observerService.getObserverById(evaluationdto.getObserverId());
//        Instructor instructor = instructorService.getInstructorById(evaluationdto.getInstructorId());
//        ClassInfo classInfo = classInfoService.getClassById(evaluationdto.getClassId());
//
//        // Check if any of the entities were not found
//        if (observer == null || instructor == null || classInfo == null) {
//            return ResponseEntity.badRequest().build();
//        }

        // Create a new Evaluation entity
        Evaluation evaluation = new Evaluation();
//        evaluation.setObserver(observer);
//        evaluation.setInstructor(instructor);
//        evaluation.setClassName(classInfo);
        evaluation.setDate(evaluationdto.getDate());

        // Set the report recommendations from the DTO
        List<ReportRecommendation> reportRecommendations = convertDTOtoReportRecommendations(evaluationdto.getRecommendations());
        evaluation.setReportRecommendations(reportRecommendations);

        // Save the evaluation to the database
        Evaluation savedEvaluation = evaluationRepository.save(evaluation);

        return ResponseEntity.ok(savedEvaluation);
    }

    private List<ReportRecommendation> convertDTOtoReportRecommendations(List<recDTO> recDTOs) {
        List<ReportRecommendation> reportRecommendations = new ArrayList<>();

        for (recDTO dto : recDTOs) {
            ReportRecommendation reportRecommendation = new ReportRecommendation();
            reportRecommendation.setDescription(dto.getDescription());
            reportRecommendation.setSelected(dto.isSelected());
            reportRecommendation.setFeedback(dto.getFeedback());
            reportRecommendations.add(reportRecommendation);
        }

        return reportRecommendations;
    }
}
