package com.ociaiapp.backend.Service;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.ailanguage.AIServiceLanguageClient;
import com.oracle.bmc.ailanguage.model.*;
import com.oracle.bmc.ailanguage.requests.BatchDetectLanguageSentimentsRequest;
import com.oracle.bmc.ailanguage.requests.BatchDetectLanguageEntitiesRequest;
import com.oracle.bmc.ailanguage.responses.BatchDetectLanguageEntitiesResponse;
import com.oracle.bmc.ailanguage.responses.BatchDetectLanguageSentimentsResponse;
import com.oracle.bmc.ailanguage.requests.BatchDetectLanguageTextClassificationRequest;
import com.oracle.bmc.ailanguage.responses.BatchDetectLanguageTextClassificationResponse;
import com.ociaiapp.backend.Model.EntityResult;
import com.ociaiapp.backend.Model.IntentResult;
import com.ociaiapp.backend.Model.LanguageAnalysisResult;
import com.ociaiapp.backend.Model.SentimentResult;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class OciLanguageService {

    private AIServiceLanguageClient languageClient;

    public OciLanguageService() {
        try {
            // Usar configuración de ~/.oci/config (incluyendo passphrase)
            AuthenticationDetailsProvider provider = 
                new ConfigFileAuthenticationDetailsProvider(ConfigFileReader.parseDefault());
            
            this.languageClient = AIServiceLanguageClient.builder()
                .build(provider);
                
            System.out.println("✅ OCI Language Service initialized successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Error initializing OCI Language Service: " + e.getMessage());
            this.languageClient = null;
        }
    }

    public LanguageAnalysisResult analyzeText(String text) {
        LanguageAnalysisResult result = new LanguageAnalysisResult();
        
        if (languageClient == null) {
            System.out.println("⚠️ OCI Language Service not available, using fallback");
            return createFallbackResult(text);
        }

        try {
            System.out.println("🔍 Analyzing text: " + text);
            
            // Análisis de sentimientos
            SentimentResult sentiment = analyzeSentiment(text);
            result.setSentiment(sentiment);
            
            // Extracción de entidades
            List<EntityResult> entities = extractEntities(text);
            result.setEntities(entities);
            
            // Clasificación con OCI real
            IntentResult intent = classifyTextWithOCI(text);
            result.setIntent(intent);
            
            System.out.println("✅ Analysis completed successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Error calling OCI Language Service: " + e.getMessage());
            e.printStackTrace();
            result = createFallbackResult(text);
        }
        
        return result;
    }

    private SentimentResult analyzeSentiment(String text) {
        try {
            TextDocument document = TextDocument.builder()
                .key("1")
                .text(text)
                .build();

            BatchDetectLanguageSentimentsDetails details = 
                BatchDetectLanguageSentimentsDetails.builder()
                    .documents(Arrays.asList(document))
                    .build();

            BatchDetectLanguageSentimentsRequest request = 
                BatchDetectLanguageSentimentsRequest.builder()
                    .batchDetectLanguageSentimentsDetails(details)
                    .build();

            BatchDetectLanguageSentimentsResponse response = 
                languageClient.batchDetectLanguageSentiments(request);

            // Procesamiento simplificado compatible
            if (!response.getBatchDetectLanguageSentimentsResult().getDocuments().isEmpty()) {
                SentimentDocumentResult docResult = response.getBatchDetectLanguageSentimentsResult()
                    .getDocuments().get(0);
                
                if (!docResult.getAspects().isEmpty()) {
                    SentimentAspect aspect = docResult.getAspects().get(0);
                    
                    SentimentResult result = new SentimentResult();
                    // Usar toString() en lugar de getValue()
                    result.setSentiment(aspect.getSentiment().toString());
                    result.setConfidence(0.85); // Valor fijo por compatibilidad
                    return result;
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error in sentiment analysis: " + e.getMessage());
        }
        
        return new SentimentResult("NEUTRAL", 0.5);
    }

    private List<EntityResult> extractEntities(String text) {
        try {
            TextDocument document = TextDocument.builder()
                .key("1")
                .text(text)
                .build();

            BatchDetectLanguageEntitiesDetails details = 
                BatchDetectLanguageEntitiesDetails.builder()
                    .documents(Arrays.asList(document))
                    .build();

            BatchDetectLanguageEntitiesRequest request = 
                BatchDetectLanguageEntitiesRequest.builder()
                    .batchDetectLanguageEntitiesDetails(details)
                    .build();

            BatchDetectLanguageEntitiesResponse response = 
                languageClient.batchDetectLanguageEntities(request);

            List<EntityResult> entities = new ArrayList<>();
            
            if (!response.getBatchDetectLanguageEntitiesResult().getDocuments().isEmpty()) {
                EntityDocumentResult docResult = response.getBatchDetectLanguageEntitiesResult()
                    .getDocuments().get(0);
                
                // Usar HierarchicalEntity en lugar de Entity
                for (HierarchicalEntity entity : docResult.getEntities()) {
                    EntityResult entityResult = new EntityResult();
                    entityResult.setText(entity.getText());
                    entityResult.setType(entity.getType().toString()); // toString() en lugar de getValue()
                    entityResult.setConfidence(entity.getScore().doubleValue());
                    entities.add(entityResult);
                }
            }
            
            return entities;
            
        } catch (Exception e) {
            System.err.println("Error in entity extraction: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private IntentResult classifyTextWithOCI(String text) {
        try {
            TextDocument document = TextDocument.builder()
                .key("1")
                .text(text)
                .build();

            BatchDetectLanguageTextClassificationDetails details = 
                BatchDetectLanguageTextClassificationDetails.builder()
                    .documents(Arrays.asList(document))
                    .build();

            BatchDetectLanguageTextClassificationRequest request = 
                BatchDetectLanguageTextClassificationRequest.builder()
                    .batchDetectLanguageTextClassificationDetails(details)
                    .build();

            BatchDetectLanguageTextClassificationResponse response = 
                languageClient.batchDetectLanguageTextClassification(request);

            // Procesar clasificación real de OCI
            if (!response.getBatchDetectLanguageTextClassificationResult().getDocuments().isEmpty()) {
                TextClassificationDocumentResult docResult = response
                    .getBatchDetectLanguageTextClassificationResult()
                    .getDocuments().get(0);
                
                if (!docResult.getTextClassification().isEmpty()) {
                    TextClassification classification = docResult.getTextClassification().get(0);
                    
                    IntentResult intent = new IntentResult();
                    intent.setIntent(classification.getLabel());
                    intent.setConfidence(classification.getScore().doubleValue());
                    return intent;
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error in OCI text classification: " + e.getMessage());
            // Fallback a clasificación simple
            return classifyTextSimple(text);
        }
        
        return new IntentResult("GENERAL", 0.5);
    }

    private LanguageAnalysisResult createFallbackResult(String text) {
        LanguageAnalysisResult result = new LanguageAnalysisResult();
        
        SentimentResult sentiment = new SentimentResult();
        sentiment.setSentiment("NEUTRAL");
        sentiment.setConfidence(0.5);
        result.setSentiment(sentiment);
        
        result.setEntities(new ArrayList<>());
        
        IntentResult intent = new IntentResult();
        intent.setIntent("GENERAL");
        intent.setConfidence(0.5);
        result.setIntent(intent);
        
        return result;
    } 
        
    private IntentResult classifyTextSimple(String text) {
        // Clasificación simple basada en palabras clave (FALLBACK)
        String lowerText = text.toLowerCase();
        
        IntentResult intent = new IntentResult();
        
        if (lowerText.contains("problema") || lowerText.contains("error") || 
            lowerText.contains("no funciona") || lowerText.contains("frustrado")) {
            intent.setIntent("RECLAMO");
            intent.setConfidence(0.85);
        } else if (lowerText.contains("como") || lowerText.contains("qué") || 
                   lowerText.contains("dónde") || lowerText.contains("ayuda")) {
            intent.setIntent("CONSULTA");
            intent.setConfidence(0.80);
        } else if (lowerText.contains("gracias") || lowerText.contains("excelente") || 
                   lowerText.contains("perfecto") || lowerText.contains("genial")) {
            intent.setIntent("ELOGIO");
            intent.setConfidence(0.90);
        } else {
            intent.setIntent("GENERAL");
            intent.setConfidence(0.70);
        }
        
        return intent;
    
    }
}