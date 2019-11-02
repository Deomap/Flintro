package com.deomap.flintro.adapter;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//данный класс активно эксплуатируется другими, чтобы не захламлять и так большую гору кода в Presenter'ах
//Методы не универсальны, для каждого типа документа и для разных задач написан отдельный метод
//Названия методов не самые лучшие, зато говорящие, класс все равно больше похож на самописную библиотеку
public class FirestoreDataTranslator {

    //конструктор
    public FirestoreDataTranslator(){

    }

    public String QDS_string_to_array(QueryDocumentSnapshot queryDocumentSnapshot,String arg){
        Map<String,Object> map = queryDocumentSnapshot.getData();
        String s="";
        if(arg.equals("id")) {
            s = queryDocumentSnapshot.getId();
        }
        else {
            s = map.get(arg).toString();
        }
        return s;
    }

    //например здесь происходит перевод HashMap, полученного из базы данных Firebase в список, содержащий только value
    //весь метод по сути создан для QuestionsPresenter
    public ArrayList<String> DS_QP_getAnswers_string_to_array(DocumentSnapshot documentSnapshot, String arg){

        Map<String,Object> map = documentSnapshot.getData();
        if(arg.equals("AL")){
            ArrayList<String> answersList = new ArrayList<>();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                answersList.add(value);
            }
            return answersList;
        }
        //а здесь key
        if(arg.equals("AIDL")){
            ArrayList<String> answersIDList = new ArrayList<>();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                //String nk = key.split(",")[0];
                answersIDList.add(key);
            }
            return answersIDList;
        }

        return null;
    }

    public ArrayList<String> DS_SP_getList_string_to_array(DocumentSnapshot documentSnapshot, String arg){
        Map<String,Object> map = documentSnapshot.getData();
        if(arg.equals("PSInfo/swipeID")){
            ArrayList<String> list = new ArrayList<>();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                list.add(key);
            }
            return list;
        }
        if(arg.equals("PSInfo/swipePriority")){
            ArrayList<String> list = new ArrayList<>();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String value = entry.getValue().toString();
                list.add(value);
            }
            return list;
        }
        return null;
    }

    public ArrayList<String> DS_LP_getList_string_to_array(DocumentSnapshot documentSnapshot, String arg){

        int size = documentSnapshot.getData().size();
        Map<String,Object> map = documentSnapshot.getData();

        if(arg.equals("meLikes/uid")){
            ArrayList<String> list = new ArrayList<>();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                list.add(key);
            }
            return list;
        }

        if(arg.equals("iLike/answers")){
            ArrayList<String> list = new ArrayList<>();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String value = entry.getValue().toString();
                list.add(value);
            }
            return list;
        }

        if(arg.equals("iLike/answers/s")){
            ArrayList<String> list = new ArrayList<>();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                list.add(key);
            }
            return list;
        }

        return null;
    }
}
