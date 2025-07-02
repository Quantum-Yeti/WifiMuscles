package me.theoria.wifimuscles.data.model;

public class SupportModel {
    private final String title;
    private final String description;
    private final String detailedDescription;

    public SupportModel(String title, String description, String detailedDescription) {
        this.title = title;
        this.description = description;
        this.detailedDescription = detailedDescription;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDetailedDescription() { return detailedDescription; }
}

