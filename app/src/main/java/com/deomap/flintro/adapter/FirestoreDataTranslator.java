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
        s=map.get(arg).toString();
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
                answersIDList.add(value);
            }
            return answersIDList;
        }
        return null;
    }
}
