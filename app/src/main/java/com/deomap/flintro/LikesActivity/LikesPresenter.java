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

    //старые списки очищаются, если та же кнопка не нажата подряд, получаются новые
    @Override
    public void getList(int arg) {
        universalList.clear();
        extraInfoList.clear();
        topicList.clear();
        qIDList.clear();
        special.clear();
        uIDList.clear();

        if(arg!=arg_mode) {
            arg_mode = arg;
            serverSide(arg);
        }
    }

    //инициируется показ списка пользователю в активности
    private void setListInView(){
        Log.i("a5",universalList.size()+" "+extraInfoList.size());
        if(arg_mode==1) {
            mView.setList(universalList, extraInfoList, topicList,qIDList,uIDList, "LAA");
        }
        if(arg_mode == 2){
            mView.setList(universalList, extraInfoList, topicList,qIDList,uIDList, "LAM");
        }
    }

    //здесь списки понравившихся пользователей или ответов загружаются из БД по аргументу (описано в активности)
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
                            if(universalList.size()==0)setListInView();
                            special = fdt.DS_LP_getList_string_to_array(document,"iLike/answers/s");
                            Log.i("a1",universalList.size()+" "+extraInfoList.size());
                            if(special.size()==0){
                                setListInView();
                            }

                            //выделение из полученной записи БД ID пользователя, ID вопроса, темы
                            String topic,qID,uID;
                            for(String str : special){
                                topic = str.split(",")[0];
                                topicList.add(topic);
                                qID = str.split(",")[1];
                                qIDList.add(qID);
                                uID = str.split(",")[2];
                                uIDList.add(uID);
                                Log.i("topicQID",topic+" "+qID);
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
                                            mView.toast("Произошла ошибка при загрузке",1);
                                        }
                                    }
                                });
                            }
                        } else {
                            Log.d("LA/getList()", "No such document");
                            special.clear();
                            extraInfoList.clear();
                            setListInView();
                        }
                    } else {
                        Log.d("LA/getList()", "get failed with ", task.getException());
                        mView.toast("Произошла ошибка при загрузке",1);
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
                            if(extraInfoList.size()==0) setListInView();
                            for(String str : extraInfoList){
                                Log.i("str",str);
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
                                                    Log.i("sliv","j0");
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
                                            mView.toast("Произошла ошибка при загрузке",1);
                                            universalList.add("null");
                                        }
                                    }
                                });
                            }
                        } else {
                            Log.d("LA/getList()", "No such document");
                            setListInView();
                        }
                    } else {
                        Log.d("LA/getList()", "get failed with ", task.getException());
                        mView.toast("Произошла ошибка при загрузке",1);
                    }
                }
            });
        }
    }
}

