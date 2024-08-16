package TeApp.TeBackend.service;

import TeApp.TeBackend.entity.ReportRecommendation;
import TeApp.TeBackend.repository.ReportRecommendationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportRecommendationService {

    @Autowired
    private ReportRecommendationRepo reportRecommendationRepo;

    public ReportRecommendation saveReportRecommendation(ReportRecommendation reportRecommendation) {
        return reportRecommendationRepo.save(reportRecommendation);
    }

    public List<ReportRecommendation> getAllReportRecommendations() {
        return reportRecommendationRepo.findAll();
    }

    public ReportRecommendation getReportRecommendationById(Long id) {
        return reportRecommendationRepo.findById(id).orElse(null);
    }

    public void deleteReportRecommendation(Long id) {
        reportRecommendationRepo.deleteById(id);
    }
}
