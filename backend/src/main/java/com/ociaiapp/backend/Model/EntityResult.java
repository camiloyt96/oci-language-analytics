package com.ociaiapp.backend.Model;

public class EntityResult {
    private String text;
    private String type;
    private Double confidence;

    public EntityResult() {}

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }
}