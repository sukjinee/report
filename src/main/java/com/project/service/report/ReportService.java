package com.project.service.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
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

    void fill(Map<String, Object> parameters) throws JRException {
        JasperFillManager.fillReportToFile(REPORT_ROOT + "/reports/compiled/FirstJasper.jasper", parameters);
    }
}
