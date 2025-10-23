package com.ociaiapp.backend.Model;

import java.util.List;

public class LanguageAnalysisResult {
    private SentimentResult sentiment;
    private List<EntityResult> entities;
    private IntentResult intent;

    // Constructors
    public LanguageAnalysisResult() {}

    // Getters and Setters
    public SentimentResult getSentiment() { return sentiment; }
    public void setSentiment(SentimentResult sentiment) { this.sentiment = sentiment; }

    public List<EntityResult> getEntities() { return entities; }
    public void setEntities(List<EntityResult> entities) { this.entities = entities; }

    public IntentResult getIntent() { return intent; }
    public void setIntent(IntentResult intent) { this.intent = intent; }
}