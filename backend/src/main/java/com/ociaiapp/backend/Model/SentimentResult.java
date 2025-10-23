package com.ociaiapp.backend.Model;

public class SentimentResult {
    private String sentiment;
    private Double confidence;

    public SentimentResult(String sentiment, Double confidence) {
    this.sentiment = sentiment;
    this.confidence = confidence;
}
    public SentimentResult(){}

    public String getSentiment() { return sentiment; }

    public void setSentiment(String sentiment) { 
        this.sentiment = sentiment;
    }

    public Double getConfidence() { return confidence; }
    
    public void setConfidence(Double confidence) { 
        this.confidence = confidence; 
    }
}