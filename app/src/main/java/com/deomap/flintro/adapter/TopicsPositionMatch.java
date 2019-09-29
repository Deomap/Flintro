package com.deomap.flintro.adapter;

public class TopicsPositionMatch {

    final String[] topicsRus = new String[]{"Тема","еще тема","тема тем","","",""};
    final String[] topicsEng = new String[]{"Math","another topic", "topic of topics","","",""};

    public TopicsPositionMatch(){
    }

    public String topicNameRus(int pos){
        return topicsRus[pos];
    }
    public String topicNameEng(int pos) {return topicsEng[pos];}
}
