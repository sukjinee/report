package com.project.controller.report;

import com.project.payload.request.report.ReportRequest;
import com.project.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;

    @Value("${report.root}")
    private String REPORT_ROOT;

    @GetMapping("/compile") // http://localhost:8080/report/compile
    public ResponseEntity<String> compile() throws JRException {
        reportService.compile();
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/make/{svcSeq}/{type}") // http://localhost:8080/report/make/999/pdf
    public void makeReportBySvcSeq(@RequestBody @Valid ReportRequest reportRequest,
                                   @PathVariable("svcSeq") String svcSeq,
                                   @PathVariable("type") String type) throws JRException {

        reportRequest.setSvcSeq(svcSeq);
        reportRequest.setType(type);
//        reportService.make(reportRequest);
//        reportService.download(reportRequest);
    }
}
























