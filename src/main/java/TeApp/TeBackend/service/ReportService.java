package TeApp.TeBackend.service;

import TeApp.TeBackend.entity.Evaluation;
import TeApp.TeBackend.entity.Report;
import TeApp.TeBackend.repository.EvaluationRepo;
import TeApp.TeBackend.repository.ReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepo reportRepository;

    @Autowired
    private EvaluationRepo evaluationRepository;

    public void savePdfReport(MultipartFile file, Long evaluationId, String reportContent) throws IOException {
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Evaluation not found"));

        Report report = new Report();
        report.setEvaluation(evaluation);
        report.setPdfContent(file.getBytes());
        report.setReportContent(reportContent);
        report.setCreatedAt(LocalDateTime.now());
        System.out.println("Saving report: " + report);
        reportRepository.save(report);
        System.out.println("Report saved successfully.");
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReportById(Long id) {
        return reportRepository.findById(id).orElse(null);
    }

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}
