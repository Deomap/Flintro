package com.deomap.flintro.adapter;

//класс используется для перевода тем из сетки интересов (ее можно увидеть например в Вопросах или при заполнении профиля после регистрации)
//в нужный формат
public class TopicsPositionMatch {

    //здесь определяются темы на русском (этот список например используется в ImageTextAdapter при заполнении самой сетки (на русском))
    final String[] topicsRus = new String[]{
            "Новые","Популярное","Психология",
            "Образование","Интернет","Кино",
            "Искусство","Музыка","Здоровье",
            "Еда", "Технологии","Спорт",
            "Политика", "Наука", "Путешествия",
            "Транспорт", "Мода", "Философия",
            "Культура", "Отношения","Книги"};
    //здесь определяются темы на английском (так они определены в БД)
    final String[] topicsEng = new String[]{
            "New","Popular", "Psychology",
            "Education","Internet","Cinema",
            "Art","Music","Health",
            "Food","Technologies","Sport",
            "Politic","Science","Adventures",
            "Transport","Fashion","Philosophy",
            "Culture","Relations","Books"};

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
