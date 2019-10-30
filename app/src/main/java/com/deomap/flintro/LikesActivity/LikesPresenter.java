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
import com.google.firebase.firestore.FirebaseFirestore;

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

    //to use CB's -> get rid of comments marked "CB"

    private MainPartContract.iLikesActivity mView;
    private MainPartContract.iOpsModel mRepository;
    private ArrayList<String> universalList = new ArrayList<>();
    private ArrayList<String> extraInfoList = new ArrayList<>();
    private ArrayList<String> special = new ArrayList<>();
    //final list NU
    private ArrayList<String> topicList = new ArrayList<>();
    private ArrayList<String> finalList = new ArrayList<>();
    private ArrayList<String> qIDList = new ArrayList<>();
    private ArrayList<String> uIDList = new ArrayList<>();
    private FirebaseUsers fbu = new FirebaseUsers();
    private FirebaseCloudstore fbcs = new FirebaseCloudstore();
    private FirestoreDataTranslator fdt = new FirestoreDataTranslator();
    private int arg_mode;
    final int cb_mode = 2;

    int j=  0;

    public LikesPresenter(MainPartContract.iLikesActivity view){
        this.mView = view;
        this.mRepository = new MainOpsModel();
    }

    @Override
    public void getList(int arg) {

    //    mView.setCB(arg); CB

        universalList.clear();
        extraInfoList.clear();
        topicList.clear();
        qIDList.clear();
        special.clear();
        uIDList.clear();
    //    cb_mode = 3; CB
    //    serverSideOLD(arg); OLD
        if(arg!=arg_mode) {
            arg_mode = arg;
            serverSide(arg);
        }
    }

    private void setListInView(){
        Log.i("a5",universalList.size()+" "+extraInfoList.size());
        if(arg_mode==1) {
            mView.setList(universalList, extraInfoList, topicList,qIDList,uIDList, "LAA");
        }
        if(arg_mode == 2){
            mView.setList(universalList, extraInfoList, topicList,qIDList,uIDList, "LAM");
        }
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


    //new

    private void serverSide(int arg){
        if(arg == 1){
            DocumentReference docRef = fbcs.DBInstance().collection("users").document(new FirebaseUsers().uID()).collection("likes").document("answers");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("LA/getList()answers", "DocumentSnapshot data: " + document.getData());
                            universalList = fdt.DS_LP_getList_string_to_array(document,"iLike/answers");
                            special = fdt.DS_LP_getList_string_to_array(document,"iLike/answers/s");
                            Log.i("a1",universalList.size()+" "+extraInfoList.size());
                            if(special.size()==0){
                                setListInView();
                            }

                            String topic,qID,uID;
                            for(String str : special){
                                topic = str.split(",")[0];
                                topicList.add(topic);
                                qID = str.split(",")[1];
                                qIDList.add(qID);
                                uID = str.split(",")[2];
                                uIDList.add(uID);
                                Log.i("wtf",topic+" "+qID);
                                FirebaseFirestore db = new FirebaseCloudstore().DBInstance();
                                DocumentReference docRef2 = db.collection("interests").document(topic).collection("questions").document(qID);
                                docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d("LP/SS", "DocumentSnapshot data: " + document.getData());
                                                extraInfoList.add(document.get("text").toString());
                                                j++;
                                                if(j==special.size()){
                                                    j=0;
                                                    setListInView();
                                                }
                                                Log.i("a2",universalList.size()+" "+extraInfoList.size());
                                            } else {
                                                Log.d("LP/SS()", "No such document");
                                                extraInfoList.add("null");
                                                j++;
                                                if(j==special.size()){
                                                    j=0;
                                                    setListInView();
                                                }
                                            }
                                        } else {
                                            Log.d("LP/SS", "get failed with ", task.getException());
                                            extraInfoList.add("null");
                                        }
                                    }
                                });
                            }
                        } else {
                            Log.d("LA/getList()", "No such document");
                        }
                    } else {
                        Log.d("LA/getList()", "get failed with ", task.getException());
                    }
                }
            });
            Log.i("a3",universalList.size()+" "+extraInfoList.size());

        }
        if(arg==2){
            DocumentReference docRef = fbcs.DBInstance().collection("users").document(new FirebaseUsers().uID()).collection("likes").document("meLikes");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("LP/getList()", "DocumentSnapshot (mode2) data: " + document.getData());
                            extraInfoList = fdt.DS_LP_getList_string_to_array(document, "meLikes/uid");


                            for(String str : extraInfoList){
                                Log.i("dd",str);
                                FirebaseFirestore db = new FirebaseCloudstore().DBInstance();
                                DocumentReference docRef2 = db.collection("users").document(str.replaceAll("\\s",""));
                                docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                universalList.add(document.get("name").toString());
                                                j++;
                                                if(j==extraInfoList.size()){
                                                    j=0;
                                                    setListInView();
                                                    Log.i("sliv","lol");
                                                }
                                            } else {
                                                Log.d("LP/SS()", "No such document");
                                                universalList.add("null");
                                                j++;
                                                if(j==extraInfoList.size()){
                                                    j=0;
                                                    setListInView();
                                                    Log.i("LP/2","else1");
                                                }
                                            }
                                        } else {
                                            Log.d("LP/SS", "get failed with ", task.getException());
                                            universalList.add("null");
                                        }
                                    }
                                });
                            }


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






















    //OLD ---->>


    private void serverSideOLD(int arg){
        final String uID = fbu.uID();

        if(arg == 1) {
            DocumentReference docRef = fbcs.DBInstance().collection("users").document(uID).collection("likes").document("answers");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("LA/getList()answers", "DocumentSnapshot data: " + document.getData());
                            universalList = fdt.DS_LP_getList_string_to_array(document,"iLike/answers");
                            special = fdt.DS_LP_getList_string_to_array(document,"iLike/answers/s");

                            Log.i("DDDkans",Integer.toString(universalList.size()));
                        } else {
                            Log.d("LA/getList()", "No such document");
                        }
                    } else {
                        Log.d("LA/getList()", "get failed with ", task.getException());
                    }

                    compileListsOLD(cb_mode);
                }
            });

            DocumentReference docRef2 = fbcs.DBInstance().collection("users").document(uID).collection("likes").document("swipe");
            docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("LA/getList()swipe", "DocumentSnapshot data: " + document.getData());

                            extraInfoList = fdt.DS_LP_getList_string_to_array(document,"iLike/swipe");

                            Log.i("DDDkswipe",Integer.toString(extraInfoList.size()));

                        } else {
                            Log.d("LA/getList()", "No such document");
                        }
                    } else {
                        Log.d("LA/getList()", "get failed with ", task.getException());
                    }

                    compileListsOLD(cb_mode);
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

                    compileListsOLD(cb_mode);
                }
            });
        }

        if(arg == 3) {
            //pass
        }

    }

    @Override
    public void compileListsOLD(int mode){
        finalList.clear();
        //    cb_mode = mode;
        switch (mode) {
            case 1:
                if(!universalList.isEmpty()) {
                    for (String str : universalList) finalList.add(str);
                }
                if(!extraInfoList.isEmpty()) {
                    for (String str : extraInfoList) finalList.add(str);
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
        //mView.setList(finalList);
    }


    @Override
    public void setCBMode(int mode) {
        //    cb_mode = mode; CB
    }

}

