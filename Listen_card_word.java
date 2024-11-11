package com.example.english_4;

public class Listen_card_word {

    private String name_en;
    private String name_rus;
    private String transcription;
    private String example;

    public  Listen_card_word(String name_en, String name_rus, String transcription, String example){
        this.name_en = name_en;
        this.name_rus = name_rus;
        this.transcription = transcription;
        this.example = example;
    }

    public  String getName_en(){
        return this.name_en;
    }

    public void setName_en(String name_en){
        this.name_en = name_en;
    }

    public  String getName_rus(){
        return this.name_rus;
    }

    public void setName_rus(String name_rus){
        this.name_rus = name_rus;
    }

    public  String getTranscription(){
        return this.transcription;
    }

    public void setTranscription(String transcription){
        this.transcription = transcription;
    }

    public  String getExample(){
        return this.example;
    }

    public void setExample(String example){
        this.example = example;
    }

}
