package com.deomap.flintro.adapter;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class FirestoreDataTranslator {
    QueryDocumentSnapshot queryDocumentSnapshot;

    public FirestoreDataTranslator(){

    }

    public String[] QDS_string_to_array(QueryDocumentSnapshot queryDocumentSnapshot,String arg){
        int size = queryDocumentSnapshot.getData().size();
        Map<String,Object> map = queryDocumentSnapshot.getData();
        String[] array = new  String[size];
        for(int i =0; i<size;++i){
            array[0]=map.get(arg).toString();
        }
        return array;
    }
}
