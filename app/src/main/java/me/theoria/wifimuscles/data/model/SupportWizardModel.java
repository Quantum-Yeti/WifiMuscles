package me.theoria.wifimuscles.data.model;

public class SupportWizardModel {

    private final String title;
    private final String question;
    private final String optionOne;
    private final String optionTwo;
    private final int nextStepIfOptionOne;
    private final int nextStepIfOptionTwo;
    private final int supportIcon;


    public SupportWizardModel(String title, String question, String optionOne, String optionTwo, int nextStepIfOptionOne, int nextStepIfOptionTwo, int supportIcon) {
        this.title = title;
        this.question = question;
        this.optionOne = optionOne;
        this.optionTwo = optionTwo;
        this.nextStepIfOptionOne = nextStepIfOptionOne;
        this.nextStepIfOptionTwo = nextStepIfOptionTwo;
        this.supportIcon = supportIcon;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionOne() {
        return optionOne;
    }

    public String getOptionTwo() {
        return optionTwo;
    }

    public int getNextStepIfOptionOne() {
        return nextStepIfOptionOne;
    }

    public int getNextStepIfOptionTwo() {
        return nextStepIfOptionTwo;
    }

    public int getSupportIcon() {
        return supportIcon;
    }

}
