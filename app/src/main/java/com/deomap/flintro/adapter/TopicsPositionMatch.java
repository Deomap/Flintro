package com.deomap.flintro.adapter;

//класс используется для перевода тем из сетки интересов (ее можно увидеть например в Вопросах или при заполнении профиля после регистрации)
//в нужный формат
public class TopicsPositionMatch {

    //здесь определяются темы на русском (этот список например используется в ImageTextAdapter при заполнении самой сетки (на русском))
    final String[] topicsRus = new String[]{"Избранное","Популярное","","ИВР","Философия","Мировоззрение","Тема 7","Тема 8","Тема 9","Тема 10", "Тема 11","Тема 12"};
    //здесь определяются темы на английском (так они определены в БД)
    final String[] topicsEng = new String[]{"Favorite","Popular", "Math","","",""};

    public TopicsPositionMatch(){
    }

    //по позиции вовращается тема
    public String topicNameRus(int pos){
        return topicsRus[pos];
    }
    public String topicNameEng(int pos) {return topicsEng[pos];}

    //по теме возвращается позиция
    public int topicPosEng(String topic){
        for(int i =0;i<topicsEng.length;i++){
            if(topicsEng[i].equals(topic)){
                return i;
            }
        }
        return 0;
    }
}
