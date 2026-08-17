package com.skilvorae.service;

import com.skilvorae.entity.Question;
import com.skilvorae.entity.QuestionOption;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PdfParsingService {

    public List<Question> parsePdfQuestions(MultipartFile file) throws IOException {
        try (PDDocument document = org.apache.pdfbox.Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            return extractQuestionsFromText(text);
        }
    }

    private List<Question> extractQuestionsFromText(String text) {
        List<Question> questions = new ArrayList<>();
        
        // This is a basic parser that assumes questions start with a number followed by a dot, e.g. "1. What is..."
        // Options start with A), B), C), D) etc.
        // It's a simplistic heuristic and will likely need manual correction via UI
        
        String[] lines = text.split("\\r?\\n");
        Question currentQuestion = null;
        
        Pattern questionPattern = Pattern.compile("^\\d+\\.\\s+(.*)");
        Pattern optionPattern = Pattern.compile("^([A-Da-d])[\\)\\.]\\s+(.*)");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            Matcher qMatcher = questionPattern.matcher(line);
            if (qMatcher.find()) {
                currentQuestion = new Question();
                currentQuestion.setQuestionText(qMatcher.group(1));
                currentQuestion.setOptions(new ArrayList<>());
                questions.add(currentQuestion);
                continue;
            }

            Matcher oMatcher = optionPattern.matcher(line);
            if (oMatcher.find() && currentQuestion != null) {
                QuestionOption option = new QuestionOption();
                option.setOptionText(oMatcher.group(2));
                // Assuming A is correct for parsing, but this should be manually reviewed
                option.setIsCorrect(oMatcher.group(1).equalsIgnoreCase("A"));
                option.setQuestion(currentQuestion);
                currentQuestion.getOptions().add(option);
                continue;
            }
            
            // If it's a continuation of a question text
            if (currentQuestion != null && currentQuestion.getOptions().isEmpty()) {
                currentQuestion.setQuestionText(currentQuestion.getQuestionText() + " " + line);
            }
        }
        
        return questions;
    }
}
