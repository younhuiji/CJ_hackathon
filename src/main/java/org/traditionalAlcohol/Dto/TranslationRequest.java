package org.traditionalAlcohol.Dto;

public class TranslationRequest {
    private String text;
    private String targetLang;

    public TranslationRequest(String text, String targetLang) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
