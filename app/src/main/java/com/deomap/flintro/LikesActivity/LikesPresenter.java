package com.deomap.flintro.LikesActivity;

import android.util.Log;

import androidx.annotation.NonNull;

import com.deomap.flintro.MainPart.MainOpsModel;
import com.deomap.flintro.adapter.FirestoreDataTranslator;
import com.deomap.flintro.adapter.MainPartContract;
import com.deomap.flintro.api.FirebaseCloudstore;
import com.deomap.flintro.api.FirebaseUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class LikesPresenter implements MainPartContract.iLikesPresenter{

    private MainPartContract.iLikesActivity mView;
    private MainPartContract.iOpsModel mRepository;
    private ArrayList<String> universalList = new ArrayList<>();
    private ArrayList<String> extraInfoList = new ArrayList<>();
    private FirebaseUsers fbu = new FirebaseUsers();
    private FirebaseCloudstore fbcs = new FirebaseCloudstore();
    private FirestoreDataTranslator fdt = new FirestoreDataTranslator();

    public LikesPresenter(MainPartContract.iLikesActivity view){
        this.mView = view;
        this.mRepository = new MainOpsModel();
    }


    private void setList(int arg){

    }

    @Override
    public void getList(int arg) {
        String uID = fbu.uID();

        if(arg == 1) {
            DocumentReference docRef = fbcs.DBInstance().collection("users").document(uID).collection("likes").document("swipe");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("LA/getList()", "DocumentSnapshot data: " + document.getData());
                            universalList = fdt.DS_LP_getList_string_to_array(document,"swipe/main");

                        } else {
                            Log.d("LA/getList()", "No such document");
                        }
                    } else {
                        Log.d("LA/getList()", "get failed with ", task.getException());
                    }
                }
            });
        }

        if(arg == 2) {
            DocumentReference docRef = fbcs.DBInstance().collection("users").document(uID).collection("likes").document("swipe");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("LA/getList()", "DocumentSnapshot data: " + document.getData());
                            universalList = fdt.DS_LP_getList_string_to_array(document,"answers/main");

                        } else {
                            Log.d("LA/getList()", "No such document");
                        }
                    } else {
                        Log.d("LA/getList()", "get failed with ", task.getException());
                    }
                }
            });
        }

        if(arg == 3) {
            DocumentReference docRef = fbcs.DBInstance().collection("users").document(uID).collection("likes").document("swipe");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("LA/getList()", "DocumentSnapshot data: " + document.getData());
                            universalList = fdt.DS_LP_getList_string_to_array(document,"matches/main");

                        } else {
                            Log.d("LA/getList()", "No such document");
                        }
                    } else {
                        Log.d("LA/getList()", "get failed with ", task.getException());
                    }
                }
            });
        }


        setList(arg);
    }

}

