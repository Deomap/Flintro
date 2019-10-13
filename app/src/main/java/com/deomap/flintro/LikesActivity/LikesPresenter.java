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

    //1 ilikes 2 melikes 3 reactions ----argmode
   //MAIN - velue, EXTRA - key
    /*
    mode
    1 st and nd
    2 st
    3 nd
    4 !st and !nd
    ------cbmode
     */

    private MainPartContract.iLikesActivity mView;
    private MainPartContract.iOpsModel mRepository;
    private ArrayList<String> universalList = new ArrayList<>();
    private ArrayList<String> extraInfoList = new ArrayList<>();
    private ArrayList<String> finalList = new ArrayList<>();
    private FirebaseUsers fbu = new FirebaseUsers();
    private FirebaseCloudstore fbcs = new FirebaseCloudstore();
    private FirestoreDataTranslator fdt = new FirestoreDataTranslator();
    private int arg_mode;
    private int cb_mode = 3;

    public LikesPresenter(MainPartContract.iLikesActivity view){
        this.mView = view;
        this.mRepository = new MainOpsModel();
    }

    @Override
    public void getList(int arg) {
        universalList.clear();
        extraInfoList.clear();
        finalList.clear();
        arg_mode = arg;
        cb_mode = 3;
        serverSide(arg);
    }

    @Override
    public void setCBMode(int mode) {
        cb_mode = mode;
    }

    @Override
    public void compileLists(int mode){
        finalList.clear();
        cb_mode = mode;
        switch (mode) {
            case 1:
                if(!universalList.isEmpty() && !extraInfoList.isEmpty()) {
                    for(String str : universalList) finalList.add(str);
                    for(String str : extraInfoList) finalList.add(str);
                }
                break;
            case 2:
                if(!universalList.isEmpty()) {
                    for (String str : universalList) finalList.add(str);
                }
                break;
            case 3:
                if(!extraInfoList.isEmpty()) {
                    for (String str : extraInfoList) finalList.add(str);
                }
                break;
            case 4:
                break;
        }
        mView.setList(finalList);
    }

    @Override
    public void likesListClicked(int pos) {
        if(arg_mode == 1){
            //get photo, name etc
        }
        if(arg_mode == 2){
            //
        }
        //pass
    }

    private void serverSide(int arg){
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
                            universalList = fdt.DS_LP_getList_string_to_array(document,"swipe/main/yes");
                            extraInfoList = fdt.DS_LP_getList_string_to_array(document, "swipe/extra");
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
            DocumentReference docRef = fbcs.DBInstance().collection("users").document(uID).collection("likes").document("meLikes");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("LA/getList()", "DocumentSnapshot (mode2) data: " + document.getData());
                            universalList = fdt.DS_LP_getList_string_to_array(document,"meLikes/nom");
                            extraInfoList = fdt.DS_LP_getList_string_to_array(document, "meLikes/yesm");

                            /*
                            for(String s : universalList){
                                Log.i("d",s);
                            }
                            */

                        } else {
                            Log.d("LA/getList()", "No such document");
                        }
                    } else {
                        Log.d("LA/getList()", "get failed with ", task.getException());
                    }

                    compileLists(cb_mode);
                }
            });
        }

        if(arg == 3) {
            DocumentReference docRef = fbcs.DBInstance().collection("users").document(uID).collection("likes").document("matches");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("LA/getList()", "DocumentSnapshot data: " + document.getData());
                            universalList = fdt.DS_LP_getList_string_to_array(document,"matches/main");
                            extraInfoList = fdt.DS_LP_getList_string_to_array(document, "matches/extra");

                        } else {
                            Log.d("LA/getList()", "No such document");
                        }
                    } else {
                        Log.d("LA/getList()", "get failed with ", task.getException());
                    }
                }
            });
        }

    }

}

