package com.deomap.flintro.adapter.Adapter_LA_Answers;

//класс (объект), который используется для удобного хранения информации для адаптера списка)
public class UnitLAA {
    private String question, answer,topic,qID,uID;
    private boolean liked;

    //конструктор
    public UnitLAA(String answer, String question, String topic, String qID, String uID){
        this.question = question;
        this.liked=true;
        this.answer = answer;
        this.topic = topic;
        this.qID = qID;
        this.uID = uID;
    }

    //геттеры, сеттеры:

    public String getQuestion() {
        return this.question;
    }
    public boolean getLiked() {
        return liked;
    }
    public String getAnswer(){
        return this.answer;
    }
    public String getTopic() {
        return this.topic;
    }
    public String getqID(){return this.qID;}
    public String getuID(){return this.uID;}

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
