package TeApp.TeBackend.controller;

import TeApp.TeBackend.dto.evaluationDTO;
import TeApp.TeBackend.dto.recDTO;
import TeApp.TeBackend.entity.*;
import TeApp.TeBackend.repository.EvaluationRepo;
import TeApp.TeBackend.service.EvaluationService;
import TeApp.TeBackend.service.ObserverService;
import TeApp.TeBackend.service.InstructorService;
import TeApp.TeBackend.service.ClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationRepo evaluationRepository;

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private ObserverService observerService;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private ClassInfoService classInfoService;

    @GetMapping
    public List<Evaluation> getEvaluations() {
        return evaluationService.getAllEvaluations();
    }


    @PostMapping("/save")
    public ResponseEntity<Evaluation> createEvaluation(@RequestBody evaluationDTO evaluationdto) {
        // Retrieve associated entities from the database
        Observer observer = observerService.getObserverById(evaluationdto.getObserverId());
        Instructor instructor = instructorService.getInstructorById(evaluationdto.getInstructorId());
        ClassInfo classInfo = classInfoService.getClassById(evaluationdto.getClassId());

        // Check if any of the entities were not found
        if (observer == null || instructor == null || classInfo == null) {
            return ResponseEntity.badRequest().build();
        }

        // Create a new Evaluation entity
        Evaluation evaluation = new Evaluation();
        evaluation.setObserver(observer);
        evaluation.setInstructor(instructor);
        evaluation.setClassName(classInfo);
        evaluation.setDate(evaluationdto.getDate());

        // Set the report recommendations from the DTO
        List<ReportRecommendation> reportRecommendations = convertDTOtoReportRecommendations(evaluationdto.getRecommendations(), evaluation);
        evaluation.setReportRecommendations(reportRecommendations);

        // Save the evaluation to the database
        Evaluation savedEvaluation = evaluationRepository.save(evaluation);

        return ResponseEntity.ok(savedEvaluation);
    }

    private List<ReportRecommendation> convertDTOtoReportRecommendations(List<recDTO> recDTOs, Evaluation evaluation) {
        List<ReportRecommendation> reportRecommendations = new ArrayList<>();

        for (recDTO dto : recDTOs) {
            ReportRecommendation reportRecommendation = new ReportRecommendation();
            reportRecommendation.setDescription(dto.getDescription());
            reportRecommendation.setSelected(dto.isSelected());
            reportRecommendation.setFeedback(dto.getFeedback());
            reportRecommendation.setEvaluation(evaluation);
            reportRecommendations.add(reportRecommendation);
        }

        return reportRecommendations;
    }

    @PostMapping("/start")
    public ResponseEntity<Evaluation> startEvaluation(@RequestBody evaluationDTO dto) {
        Observer observer = observerService.getObserverById(dto.getObserverId());
        Instructor instructor = instructorService.getInstructorById(dto.getInstructorId());
        ClassInfo classInfo = classInfoService.getClassById(dto.getClassId());

        if (observer == null || instructor == null || classInfo == null) {
            return ResponseEntity.badRequest().build();
        }

        Evaluation evaluation = new Evaluation();
        evaluation.setObserver(observer);
        evaluation.setInstructor(instructor);
        evaluation.setClassName(classInfo);
        evaluation.setDate(dto.getDate());

        Evaluation saved = evaluationRepository.save(evaluation);
        Map<String, Object> response = new HashMap<>();
        response.put("evaluationId", saved.getEvaluation_id());

        return ResponseEntity.ok(saved);
    }
}
