package com.tetrapak.supplierportal.e2e.tests;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;


import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class BackendTest {

    private static final String targetDir = "build/reports/karate-reports";

    @Test
    void testParallel() {
        String path = "classpath:net/publicisgroupe";

        Results res = Runner.path(path)
                .outputCucumberJson(true)
                .tags("~@ignore")
                .reportDir(targetDir)
                .outputJunitXml(true)
                .parallel(2);
        System.out.println(res.getReportDir());
        generateReport(res.getReportDir());
    }

    public static void generateReport(String karateOutputPath) {
        Collection<File> jsonFiles = FileUtils.listFiles(
                new File(karateOutputPath),
                new String[]{"json"},
                true
        );
        List<String> jsonPaths = new ArrayList(jsonFiles.size());
        jsonFiles.forEach(file -> jsonPaths.add(file.getAbsolutePath()));
        Configuration config = new Configuration(
                new File(targetDir),
                "TetraPak Supplier Portal Tests - API");
        ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, config);
        reportBuilder.generateReports();
    }
}
