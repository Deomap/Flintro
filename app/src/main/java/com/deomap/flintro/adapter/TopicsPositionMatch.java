package com.deomap.flintro.adapter;

public class TopicsPositionMatch {

    final String[] topicsRus = new String[]{"Избранное","Популярное","Матеша???","ИВР","Философия","Мировоззрение","Тема 7","Тема 8","Тема 9","Тема 10", "Тема 11","Тема 12"};
    final String[] topicsEng = new String[]{"FavoriteQuestions","Popular", "Math","","",""};

    public TopicsPositionMatch(){
    }

    public String topicNameRus(int pos){
        return topicsRus[pos];
    }
    public String topicNameEng(int pos) {return topicsEng[pos];}

    public int topicPosEng(String topic){
        for(int i =0;i<topicsEng.length;i++){
            if(topicsEng[i].equals(topic)){
                return i;
            }
        }
        return 0;
    }
}
