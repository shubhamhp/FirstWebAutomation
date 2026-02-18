package com.bb.automation.runner;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class ExcelGenerator {

    public static void generateExcelReport(String xmlPath, String excelPath) {
        System.out.println("📊 Generating Excel Report from: " + xmlPath);

        try {
            // 1. Read XML File
            File xmlFile = new File(xmlPath);
            if (!xmlFile.exists()) {
                System.err.println("❌ Error: testng-results.xml not found at " + xmlPath);
                return;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            // 2. Create Excel Workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Test Results");

            // 3. Create Header Row
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Test Name", "Status", "Duration (ms)", "Exception/Message"};
            
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // 4. Parse XML and Fill Rows
            NodeList testMethods = doc.getElementsByTagName("test-method");
            int rowNum = 1;

            for (int i = 0; i < testMethods.getLength(); i++) {
                Node node = testMethods.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Skip configuration methods (BeforeTest, AfterSuite, etc.)
                    if ("true".equals(element.getAttribute("is-config"))) continue;

                    Row row = sheet.createRow(rowNum++);

                    // Column 1: Test Name
                    row.createCell(0).setCellValue(element.getAttribute("name"));

                    // Column 2: Status (PASS/FAIL/SKIP)
                    String status = element.getAttribute("status").toUpperCase();
                    Cell statusCell = row.createCell(1);
                    statusCell.setCellValue(status);
                    
                    // Simple Color Styling
                    CellStyle statusStyle = workbook.createCellStyle();
                    if (status.equals("FAIL")) {
                        Font redFont = workbook.createFont();
                        redFont.setColor(IndexedColors.RED.getIndex());
                        statusStyle.setFont(redFont);
                    } else if (status.equals("PASS")) {
                        Font greenFont = workbook.createFont();
                        greenFont.setColor(IndexedColors.GREEN.getIndex());
                        statusStyle.setFont(greenFont);
                    }
                    statusCell.setCellStyle(statusStyle);

                    // Column 3: Duration
                    row.createCell(2).setCellValue(element.getAttribute("duration-ms"));

                    // Column 4: Exception Message (if failed)
                    String exceptionMsg = "";
                    NodeList exceptions = element.getElementsByTagName("exception");
                    if (exceptions.getLength() > 0) {
                        Element exception = (Element) exceptions.item(0);
                        exceptionMsg = exception.getAttribute("class");
                        NodeList messages = exception.getElementsByTagName("message");
                        if (messages.getLength() > 0) {
                            exceptionMsg += ": " + messages.item(0).getTextContent();
                        }
                    }
                    row.createCell(3).setCellValue(exceptionMsg);
                }
            }

            // 5. Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 6. Write to File
            FileOutputStream fileOut = new FileOutputStream(excelPath);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            System.out.println("✅ Excel Report Created Successfully: " + excelPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}