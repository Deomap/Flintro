package com.deomap.flintro.adapter.Adapter_LA_Answers;

public class UnitLAA {
    private String question;
    private boolean liked;
    private String answer;
    private String topic;
    private String qID;
    private String uID;

    public UnitLAA(String answer, String question, String topic, String qID, String uID){
        this.question = question;
        this.liked=true;
        this.answer = answer;
        this.topic = topic;
        this.qID = qID;
        this.uID = uID;
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
    public String getuID(){return this.uID;}

    public boolean getLiked() {
        return liked;
    }
}
