package TeApp.TeBackend.controller;

import TeApp.TeBackend.dto.evaluationDTO;
import TeApp.TeBackend.dto.recDTO;
import TeApp.TeBackend.entity.*;
import TeApp.TeBackend.repository.EvaluationRepo;
import TeApp.TeBackend.service.ClassInfoService;
import TeApp.TeBackend.service.EmailService;
import TeApp.TeBackend.service.EvaluationService;
import TeApp.TeBackend.service.InstructorService;
import TeApp.TeBackend.service.ObserverService;
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

    @Autowired
    private EmailService emailService;

    @GetMapping
    public List<Evaluation> getEvaluations() {
        return evaluationService.getAllEvaluations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evaluation> getEvaluationById(@PathVariable Long id) {
        Evaluation evaluation = evaluationService.getEvaluationById(id);
        if (evaluation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(evaluation);
    }


    @PostMapping("/save")
    public ResponseEntity<Evaluation> createEvaluation(@RequestBody evaluationDTO evaluationdto) {
        Observer observer = observerService.getObserverById(evaluationdto.getObserverId());
        Instructor instructor = instructorService.getInstructorById(evaluationdto.getInstructorId());
        ClassInfo classInfo = classInfoService.getClassById(evaluationdto.getClassId());

        if (observer == null || instructor == null || classInfo == null) {
            return ResponseEntity.badRequest().build();
        }

        Evaluation evaluation = new Evaluation();
        evaluation.setObserver(observer);
        evaluation.setInstructor(instructor);
        evaluation.setClassName(classInfo);
        evaluation.setDate(evaluationdto.getDate());

        List<ReportRecommendation> reportRecommendations = convertDTOtoReportRecommendations(evaluationdto.getRecommendations(), evaluation);
        evaluation.setReportRecommendations(reportRecommendations);

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
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}/activity-log")
    public ResponseEntity<Evaluation> updateActivityLog(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Evaluation evaluation = evaluationService.getEvaluationById(id);
        if (evaluation == null) {
            return ResponseEntity.notFound().build();
        }
        evaluation.setActivityLog(body.get("activityLog"));
        Evaluation saved = evaluationRepository.save(evaluation);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/notify-instructor")
    public ResponseEntity<?> notifyInstructor(@RequestBody Map<String, String> body) {
        String instructorIdStr = body.get("instructorId");
        String observerName = body.get("observerName");

        if (instructorIdStr == null) {
            return ResponseEntity.badRequest().body("instructorId is required");
        }

        Instructor instructor = instructorService.getInstructorById(Long.parseLong(instructorIdStr));
        if (instructor == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            emailService.sendObservationCompleteEmail(
                    instructor.getEmail(),
                    instructor.getFirstname() + " " + instructor.getLastname(),
                    observerName
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Could not notify the instructor by email",
                    "reason", e.getMessage() == null ? e.toString() : e.getMessage()
            ));
        }
        return ResponseEntity.ok(Map.of("message", "Notification sent to instructor"));
    }
}
