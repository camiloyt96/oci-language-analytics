package com.ociaiapp.backend.Controller;


import com.ociaiapp.backend.Service.OciLanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/language")
public class LanguageController {

    @Autowired
    private OciLanguageService languageService;

    @PostMapping("/analyze")
    public com.ociaiapp.backend.Model.LanguageAnalysisResult analyzeText(@RequestBody AnalyzeRequest request) {
        return languageService.analyzeText(request.getText());
    }

    @GetMapping("/test")
    public String test() {
        return "Language Service endpoint is working! 🤖";
    }

    // DTO para el request
    public static class AnalyzeRequest {
        private String text;
        
        public AnalyzeRequest() {}
        
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
}