package com.deomap.flintro.adapter.Adapter_LA_Answers;

public class UnitLAA {
    private String question;
    private boolean liked;
    private String answer;
    private String topic;
    private String qID;

    public UnitLAA(String answer, String question, String topic, String qID){
        this.question = question;
        this.liked=true;
        this.answer = answer;
        this.topic = topic;
        this.qID = qID;
    }
    public String getQuestion() {
        return this.question;
    }
    public String getAnswer(){
        return this.answer;
    }
    public void setLiked(boolean liked) {
        this.liked = liked;
    }
    public String getTopic() {
        return this.topic;
    }
    public String getqID(){return this.qID;}

    public boolean getLiked() {
        return liked;
    }
}
