package com.deomap.flintro.adapter;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirestoreDataTranslator {
    //QueryDocumentSnapshot queryDocumentSnapshot;

    public FirestoreDataTranslator(){

    }

    public String QDS_string_to_array(QueryDocumentSnapshot queryDocumentSnapshot,String arg){
        int size = queryDocumentSnapshot.getData().size();
        Map<String,Object> map = queryDocumentSnapshot.getData();
        //Log.i("FDT/QDS_string_to_array", map.toString());
        String s="";
        if(arg.equals("id")) {
            s = queryDocumentSnapshot.getId();
        }
        else {
            s = map.get(arg).toString();
        }
        return s;
    }

    public ArrayList<String> DS_QP_getAnswers_string_to_array(DocumentSnapshot documentSnapshot, String arg){
        int size = documentSnapshot.getData().size();
        Map<String,Object> map = documentSnapshot.getData();
        //Log.i("FDT/QDS_string_to_array", map.toString());
        if(arg.equals("AL")){
            ArrayList<String> answersList = new ArrayList<>();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                answersList.add(value);
            }
            return answersList;
        }
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
        if(arg.equals("AVL")){
            ArrayList<String> answersIDList = new ArrayList<>();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                //String nk = key.split(",")[1];

            }
            return answersIDList;
        }
        return null;
    }

    public ArrayList<String> DS_SP_getList_string_to_array(DocumentSnapshot documentSnapshot, String arg){
        int size = documentSnapshot.getData().size();
        Map<String,Object> map = documentSnapshot.getData();
        //Log.i("FDT/QDS_string_to_array", map.toString());
        if(arg.equals("PSInfo/swipe")){
            ArrayList<String> list = new ArrayList<>();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                list.add(key);
            }
            return list;
        }
        return null;
    }

    public ArrayList<String> DS_LP_getList_string_to_array(DocumentSnapshot documentSnapshot, String arg){

        int size = documentSnapshot.getData().size();
        Map<String,Object> map = documentSnapshot.getData();
        //Log.i("FDT/QDS_string_to_array", map.toString());

        if(arg.equals("meLikes/yesm")){
            ArrayList<String> list = new ArrayList<>();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                if(value.equals("yesm")) {list.add(key);}
            }
            return list;
        }

        if(arg.equals("meLikes/nom")){
            ArrayList<String> list = new ArrayList<>();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                if(value.equals("nom")){list.add(key);}
            }
            return list;
        }

        if(arg.equals("meLikes/uid")){
            ArrayList<String> list = new ArrayList<>();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                list.add(key);
            }
            return list;
        }

        if(arg.equals("iLike/answers")){
            ArrayList<String> list = new ArrayList<>();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                list.add(value);
            }
            return list;
        }

        if(arg.equals("iLike/answers/s")){
            ArrayList<String> list = new ArrayList<>();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                list.add(key);
                //Log.i("value",key);
            }
            return list;
        }

        if(arg.equals("iLike/swipe")){
            ArrayList<String> list = new ArrayList<>();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                list.add(key);
            }
            return list;
        }

        return null;
    }
}
