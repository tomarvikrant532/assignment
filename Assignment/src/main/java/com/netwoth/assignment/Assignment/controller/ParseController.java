package com.netwoth.assignment.Assignment.controller;


import com.netwoth.assignment.Assignment.model.TransactionRecord;
import com.netwoth.assignment.Assignment.service.PdfToExcelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class ParseController {

    @Autowired
    PdfToExcelConverter pdfToExcelConverter;

    @PostMapping("/convert")
    public ResponseEntity<List<TransactionRecord>> convertPdfToExcel(@RequestParam("file") MultipartFile pdfFile) {
        try {
            List<TransactionRecord> extractedData = pdfToExcelConverter.convertPdfToExcelAndExtractData(pdfFile);
            return ResponseEntity.ok(extractedData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
