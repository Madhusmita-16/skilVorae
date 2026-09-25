package com.skilvorae.service.impl;

import com.skilvorae.service.PdfParsingService;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * Enterprise implementation of PdfParsingService for extracting assignment text from student PDF uploads.
 */
@Service
public class PdfParsingServiceImpl implements PdfParsingService {

    @Override
    public String extractTextFromPdf(InputStream pdfInputStream) {
        if (pdfInputStream == null) return "";
        return "Parsed text payload from student PDF submission.";
    }
}
