package TeApp.TeBackend.controller;

import TeApp.TeBackend.entity.Evaluation;
import TeApp.TeBackend.entity.Report;
import TeApp.TeBackend.repository.EvaluationRepo;
import TeApp.TeBackend.repository.ReportRepo;
import TeApp.TeBackend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public List<Report> getAllReports() {
        return reportService.getAllReports();
    }

    @GetMapping("/{id}")
    public Report getReportById(@PathVariable Long id) {
        return reportService.getReportById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
    }

    @PostMapping("/save-pdf")
    public ResponseEntity<String> savePdfReport(
            @RequestParam("file") MultipartFile file,
            @RequestParam("evaluationId") Long evaluationId) {
        try {
            reportService.savePdfReport(file, evaluationId);
            return ResponseEntity.ok("PDF report saved successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving PDF report.");
        }
    }

    @GetMapping("/{reportId}/pdf")
    public ResponseEntity<byte[]> getReportPdf(@PathVariable Long reportId) {
        // Logic to fetch the PDF content from the database
        Report report = reportService.getReportById(reportId);
        byte[] pdfContent = report.getPdfContent();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "report.pdf");

        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }
}
