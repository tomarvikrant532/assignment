package com.netwoth.assignment.Assignment.service;

import com.convertapi.client.Config;
import com.convertapi.client.ConvertApi;
import com.convertapi.client.Param;
import com.netwoth.assignment.Assignment.model.TransactionRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PdfToExcelConverter {

    @Value("${secretKey}")
    String CONVERTAPI_SECRET;

    public List<TransactionRecord> convertPdfToExcelAndExtractData(MultipartFile pdfFile) throws Exception {
        Config.setDefaultSecret(CONVERTAPI_SECRET);
        log.info("Parsing started for PDF File");

        File tempPdfFile = File.createTempFile("uploaded", ".pdf");
        pdfFile.transferTo(tempPdfFile);

        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));

        ConvertApi.convert("pdf", "xlsx", new Param("File", tempPdfFile.toPath()))
                .get()
                .saveFilesSync(tempDir);

        File excelFile = tempDir.resolve(tempPdfFile.getName().replace(".pdf", ".xlsx")).toFile();

        log.info("Parsing Done");
        List<TransactionRecord> extractedData = extractDataFromExcel(excelFile);

        tempPdfFile.delete();

        return extractedData;
    }
    private List<TransactionRecord> extractDataFromExcel(File excelFile) throws IOException {
        List<TransactionRecord> extractedData = new ArrayList<>();
        log.info("Extracting Response");
        try (InputStream inputStream = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                Date date = row.getCell(0).getDateCellValue();
                String transactionDetails = row.getCell(1).getStringCellValue();
                String amount = row.getCell(2).getStringCellValue();

                TransactionRecord record = new TransactionRecord(date, transactionDetails, amount);
                extractedData.add(record);
            }
        }
        log.info("Response Extracted");

        return extractedData;
    }
}
