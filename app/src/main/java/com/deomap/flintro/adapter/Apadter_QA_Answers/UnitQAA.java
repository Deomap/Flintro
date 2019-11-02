package com.deomap.flintro.adapter.Apadter_QA_Answers;

//аналогично UnitLAA

public class UnitQAA {
    private String answerText;
    private boolean liked;
    private String path;
    private String votes;

    public UnitQAA(String answerText, String path, String votes){
        this.answerText = answerText;
        this.liked=false;
        this.path = path;
        this.votes = votes;
    }
    public String getPath(){
        return this.path;
    }
    public void setLiked(boolean liked) {
        this.liked = liked;
    }
    public String getAnswerText(){return this.answerText;}
    public String getVotes(){return  this.votes;}

    public boolean getLiked() {
        return liked;
    }
}
