package com.bb.automation.runner;

import org.testng.TestNG;
import com.bb.automation.testUtils.EmailUtils;
import java.io.File;
import java.util.Collections;

public class TestRunner {

    public static void main(String[] args) {
        System.out.println("🚀 JAR LAUNCHER: Starting...");

        // --- STEP 1: LOCATE XML ---
        // Since Maven copied it to 'target', it is sitting right next to the JAR now.
        String xmlFileName = "testng.xml"; 
        File xmlFile = new File(xmlFileName);
        
        if (!xmlFile.exists()) {
            System.err.println("❌ Error: testng.xml not found!");
            System.err.println("👉 Make sure the Maven Resources Plugin copied it to the target folder.");
            System.exit(1);
        }

        // --- STEP 2: RUN TESTNG ---
        TestNG testng = new TestNG();
        testng.setTestSuites(Collections.singletonList(xmlFileName));
        System.out.println("✅ Found testng.xml. Running tests...");
        testng.run(); 
        
        System.out.println("🏁 TestNG Execution Finished.");

        // --- STEP 3: GENERATE EXCEL ---
        String xmlPath = "test-output/testng-results.xml";
        String excelPath = "test-output/TestResults.xlsx";
        
        if (new File(xmlPath).exists()) {
            System.out.println("📊 Generating Excel Report...");
            ExcelGenerator.generateExcelReport(xmlPath, excelPath);
        } else {
            System.err.println("⚠️ Warning: testng-results.xml not found. Skipping Excel.");
        }

        // --- STEP 4: SEND EMAIL ---
        System.out.println("📨 Preparing Email...");
        String extentReport = "test-reports/ExtentReports.html"; 

        if (new File(excelPath).exists() || new File(extentReport).exists()) {
            EmailUtils.sendEmail(
                "sphpatil01@gmail.com", 
                "Automation Execution Report", 
                "Hello,\n\nPlease find the attached reports.",
                extentReport,  // Attach HTML
                excelPath      // Attach Excel
            );
        } else {
            System.err.println("❌ No reports found to send!");
        }
    }
}