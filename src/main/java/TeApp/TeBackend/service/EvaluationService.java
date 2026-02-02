package TeApp.TeBackend.service;

import TeApp.TeBackend.entity.Evaluation;
import TeApp.TeBackend.repository.EvaluationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepo evaluationRepository;

    public Evaluation saveEvaluation(Evaluation evaluation) {
        return evaluationRepository.save(evaluation);
    }

    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    public Evaluation getEvaluationById(Long id) {
        return evaluationRepository.findById(id).orElse(null);
    }

    public void deleteEvaluation(Long id) {
        evaluationRepository.deleteById(id);
    }
}

