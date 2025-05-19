package com.project.service.report;

import com.project.payload.request.report.ReportRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    @Value("${report.root}")
    private String REPORT_ROOT;

    /**
     * 템플릿 컴파일
     * jrxml to jsper
     *
     * @throws JRException
     */
    public void compile() throws JRException {
        File[] files = getFiles(new File(REPORT_ROOT + "/reports/template"), "jrxml");
        if (files.length > 0) {
            File destFileParent = new File(REPORT_ROOT + "/reports/compiled");
            if (!destFileParent.exists()) {
                destFileParent.mkdirs();
            }

            log.info("Compiling " + files.length + " report design files."); // deliberately using log.info instead of logging in the sample apps

            for (int i = 0; i < files.length; i++) {
                File srcFile = files[i];
                String srcFileName = srcFile.getName();
                String destFileName = srcFileName.substring(0, srcFileName.lastIndexOf(".jrxml")) + ".jasper";

                System.out.print("Compiling: " + srcFileName + " ... ");

                JasperCompileManager.compileReportToFile(
                        srcFile.getAbsolutePath(),
                        new File(destFileParent, destFileName).getAbsolutePath()
                );

                log.info("OK.");
            }
        } else {
            log.info("No report design files found to compile.");
        }
    }

    /**
     * 특정 디렉토리에서 파일을 조회하는 UTIL 함수
     *
     * @param parentFile
     * @param extension
     * @return
     */
    protected File[] getFiles(File parentFile, String extension) {
        List<File> fileList = new ArrayList<>();
        String[] files = parentFile.list();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String reportFile = files[i];
                if (reportFile.endsWith("." + extension)) {
                    fileList.add(new File(parentFile, reportFile));
                }
            }
        }
        return fileList.toArray(new File[fileList.size()]);
    }

    public void make(ReportRequest reportRequest) throws JRException, IOException {
        Map<String, Object> params = new HashMap<>();

        // 데이터 조회
        List<Map<String, Object>> docOfApplies = new ArrayList<>();

        Path dirOfFilled = Files.createTempDirectory("filled");

        List<Path> reportOfFilled = new ArrayList<>();
        for (Map<String, Object> docOfApply : docOfApplies) {
            Path fill = this.fill(reportRequest, docOfApply, dirOfFilled);
            reportOfFilled.add(fill);
        }

        // 임시 디렉토리 생성
        // 결과물 생성
        // 압축파일 생성
        // 파일 반환 ?

        this.fill(reportRequest, params, dirOfFilled);
        if ("pdf".equals(reportRequest.getType())) {
            this.pdf(reportRequest);
        }

        // 이게 파일을 반환하는게 좋냐 패스를 반환하는게 좋냐?? 그걸 모르겠네?
    }

    private Path fill(ReportRequest reportRequest, Map<String, Object> parameters, Path dirOfFilled) throws JRException {
        long start = System.currentTimeMillis();
        JasperFillManager.fillReportToFile(this.getSourcePath(reportRequest, "compiled"), this.getSourcePath(reportRequest, "filled"), parameters);
        System.err.println("PDF fill time : " + (System.currentTimeMillis() - start));
        return Paths.get("");
    }

    private String getSourcePath(ReportRequest reportRequest, String type) {
        if ("compiled".equals(type)) {
            return REPORT_ROOT + "/reports/" + reportRequest.getSvcSeq() + "_" + reportRequest.getType() + ".jasper";
        } else if ("filled".equals(type)) {
            return REPORT_ROOT + "/reports/" + reportRequest.getSvcSeq() + "_" + reportRequest.getType() + ".jrprint";
        } else {
            return REPORT_ROOT + "/reports/" + reportRequest.getSvcSeq() + "_" + reportRequest.getType() + ".jasper";
        }
    }

    private void pdf(ReportRequest reportRequest) throws JRException {
        long start = System.currentTimeMillis();
        JasperExportManager.exportReportToPdfFile(this.getSourcePath(reportRequest, "filled"));
        System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
    }
}
