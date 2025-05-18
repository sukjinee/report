package com.project.controller.report;

import com.project.payload.request.report.ReportRequest;
import com.project.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        log.info("나오는거지??");
        reportService.compile();
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/make/{svcSeq}") // http://localhost:8080/report/test2
    public void mskeReportBySvcSeq(@RequestBody @Valid ReportRequest reportRequest) throws JRException {

        fill();
        pdf();
    }

    public void fill() throws JRException {
        long start = System.currentTimeMillis();
        //Preparing parameters
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ReportTitle", "Address Report");
        parameters.put("FilterClause", "'Boston', 'Chicago', 'Oslo'");
        parameters.put("OrderClause", "City");

        JasperFillManager.fillReportToFile(REPORT_ROOT + "/reports/compiled/FirstJasper.jasper", parameters);
        System.err.println("Filling time : " + (System.currentTimeMillis() - start));
    }

    public void pdf() throws JRException {
        long start = System.currentTimeMillis();
        JasperExportManager.exportReportToPdfFile(REPORT_ROOT + "/reports/filled/FirstJasper.jrprint");
        System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
    }
}
























