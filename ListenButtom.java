package com.example.english_4;

public class ListenButtom {

    private String name;
    private String col_word;
    private String progres;
    private Boolean vidor;

    public  ListenButtom(String name, String col_word, String progres, Boolean vidor){
        this.name = name;
        this.col_word = col_word;
        this.progres = progres;
        this.vidor = vidor;
    }

    public  String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public  String getCol_word(){
        return this.col_word;
    }

    public void setCol_word(String col_word){
        this.col_word = col_word;
    }

    public  String getProgres(){
        return this.progres;
    }

    public void setProgres(String progres){
        this.progres = progres;
    }

    public  Boolean getVidor(){
        return this.vidor;
    }

    public void setVidor(Boolean vidor){
        this.vidor = vidor;
    }

}
