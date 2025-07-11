package me.theoria.wifimuscles.data.model;

public class SupportModel {
    private final String title;
    private final String description;
    private final String detailedDescription;
    private final int supportIcon;

    public SupportModel(String title, String description, String detailedDescription, int supportIcon) {
        this.title = title;
        this.description = description;
        this.detailedDescription = detailedDescription;
        this.supportIcon = supportIcon;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDetailedDescription() { return detailedDescription; }
    public int getSupportIcon() { return supportIcon; }
}

