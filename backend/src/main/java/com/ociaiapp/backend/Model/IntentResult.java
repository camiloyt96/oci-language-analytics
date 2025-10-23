package com.ociaiapp.backend.Model;

public class IntentResult {
    private String intent;
    private Double confidence;

    public IntentResult(String intent, Double confidence) {
    this.intent = intent;
    this.confidence = confidence;
}
    public IntentResult(){}

    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }

    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }
}