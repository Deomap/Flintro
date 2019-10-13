package com.deomap.flintro.adapter;

public class TopicsPositionMatch {

    final String[] topicsRus = new String[]{"Избранное","Популярное","Матеша???","","",""};
    final String[] topicsEng = new String[]{"FavoriteQuestions","Popular", "Math","","",""};

    public TopicsPositionMatch(){
    }

    public String topicNameRus(int pos){
        return topicsRus[pos];
    }
    public String topicNameEng(int pos) {return topicsEng[pos];}
}
