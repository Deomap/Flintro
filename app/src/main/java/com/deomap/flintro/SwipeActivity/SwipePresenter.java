package com.deomap.flintro.SwipeActivity;

import android.provider.Contacts;
import android.util.Log;

import androidx.annotation.NonNull;

import com.deomap.flintro.MainPart.MainOpsModel;
import com.deomap.flintro.adapter.FirestoreDataTranslator;
import com.deomap.flintro.adapter.MainPartContract;
import com.deomap.flintro.api.FirebaseCloudstore;
import com.deomap.flintro.api.FirebaseUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SwipePresenter implements MainPartContract.iSwipePresenter {
    private MainPartContract.iSwipeActivity mView;
    private MainPartContract.iOpsModel mRepository;
    public SwipePresenter(MainPartContract.iSwipeActivity view){
        this.mView = view;
        this.mRepository = new MainOpsModel();
    }

    DocumentReference docRefSIL;

    int whereShowingNow = 0;
    ArrayList<String> UIDQuery = new ArrayList<>();
    ArrayList<String> PriorityQuery = new ArrayList<>();

    private String curUserID="null", curUserPriority = "null";

    private String fuName,fuStatus, fuTxt1, fuTxt2, fuTxt3, city;

    FirestoreDataTranslator fdt = new FirestoreDataTranslator();

    //первоначальная загрузка списка пользователей, которые могут подойти текущему
    @Override
    public void startShowing() {
        FirebaseUsers fu =  new FirebaseUsers();
        final DocumentReference docRef = new FirebaseCloudstore().DBInstance().collection("users").document(fu.uID()).collection("PSInfo").document("forSwipe");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("SP/ss()", "DocumentSnapshot data: " + document.getData());
                        UIDQuery =  fdt.DS_SP_getList_string_to_array(document,"PSInfo/swipeID");
                        PriorityQuery = fdt.DS_SP_getList_string_to_array(document,"PSInfo/swipePriority");
                        showInLoop();
                    } else {
                        Log.d("SP/ss()", "No such document");
                    }
                } else {
                    Log.d("SP/ss()", "get failed with ", task.getException());
                }
            }
        });
    }

    //по очереди из списка берется ID пользователя и из БД загружается фото и другая информация
    @Override
    public void showInLoop(){
        FirebaseUsers fu =  new FirebaseUsers();
        FirebaseCloudstore fcs = new FirebaseCloudstore();
        if(UIDQuery.size()-1 >= whereShowingNow) {
            curUserID = UIDQuery.get(whereShowingNow);
            curUserPriority = PriorityQuery.get(whereShowingNow);
            docRefSIL = new FirebaseCloudstore().DBInstance().collection("users").document(UIDQuery.get(whereShowingNow));
            docRefSIL.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            Log.d("SP/sil()", "DocumentSnapshot data: " + document.getData());
                            fuName = document.get("name").toString();
                            fuStatus = document.get("mainStatus").toString();
                            fuTxt1 = document.get("txtSwipe1").toString();
                            fuTxt2 = document.get("txtSwipe2").toString();
                            fuTxt3 = document.get("txtSwipe3").toString();
                            city = document.get("city").toString();
                            whereShowingNow++;
                            setInView();

                        } else {
                            Log.d("SP/sil()", "No such document");
                            curUserID="null";
                            curUserPriority="null";
                            whereShowingNow++;
                            showInLoop();
                        }
                    } else {
                        Log.d("SP/sil()", "get failed with ", task.getException());
                        curUserID="null";
                        curUserPriority="null";
                    }
                }
            });
        }
        else{
            //load new
            curUserPriority="null";
            curUserID = "null";
            whereShowingNow = 0;
        }
    }

    //если текущий пользователь уже нравится тому, кого лайкнул, то они оба находятся у друг друга в списке взаимных в LikesActivity и могу перейти в чат
    //если нет, но пользомателю, которого текущий лайкнул, в список предлагаемых добавляется текущий
    @Override
    public void liked() {
        FirebaseFirestore db = new FirebaseCloudstore().DBInstance();
        FirebaseUsers fu = new FirebaseUsers();

        if(curUserPriority.equals("1")) {

            Map<String, Object> updates1 = new HashMap<>();
            updates1.put(curUserID,"");

            new FirebaseCloudstore().DBInstance().collection("users").document(fu.uID()).collection("likes").document("meLikes")
                    .update(updates1)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("SP/liked", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("SP/liked", "Error writing document", e);
                        }
                    });

            Map<String, Object> updates2 = new HashMap<>();
            updates2.put(fu.uID(),"");

            new FirebaseCloudstore().DBInstance().collection("users").document(curUserID).collection("likes").document("meLikes")
                    .update(updates2)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("SP/liked", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("SP/liked", "Error writing document", e);
                        }
                    });

            DocumentReference docRef = db.collection("users").document(fu.uID()).collection("PSInfo").document("forSwipe");
            Map<String, Object> del = new HashMap<>();
            del.put(curUserID, FieldValue.delete());
            docRef.update(del).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("SP/delLiked1", "deleted");
                }
            });

            docRef = db.collection("users").document(curUserID).collection("PSInfo").document("forSwipe");
            Map<String, Object> del2 = new HashMap<>();
            del2.put(fu.uID(), FieldValue.delete());
            docRef.update(del2).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("SP/delLiked2", "deleted");
                }
            });
        }
        else{
            Map<String, Object> updates1 = new HashMap<>();
            updates1.put(fu.uID(),"1");

            new FirebaseCloudstore().DBInstance().collection("users").document(curUserID).collection("PSInfo").document("forSwipe")
                    .update(updates1)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("SP/liked", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("SP/liked", "Error writing document", e);
                        }
                    });

            DocumentReference docRef = db.collection("users").document(fu.uID()).collection("PSInfo").document("forSwipe");
            Map<String, Object> updates = new HashMap<>();
            updates.put(curUserID, FieldValue.delete());
            docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("SP/dis2", "deleted");
                }
            });
        }
    }

    @Override
    public void disliked() {
        FirebaseFirestore db = new FirebaseCloudstore().DBInstance();
        FirebaseUsers fu = new FirebaseUsers();

        if(curUserPriority.equals("1")){
            DocumentReference docRef = db.collection("users").document(fu.uID()).collection("PSInfo").document("forSwipe");
                Map<String, Object> updates = new HashMap<>();
                updates.put(curUserID, FieldValue.delete());
                docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("SP/dis1", "deleted");
                    }
                });
        }
        if(curUserPriority.equals("2")){
            DocumentReference docRef = db.collection("users").document(fu.uID()).collection("PSInfo").document("forSwipe");
            Map<String, Object> updates = new HashMap<>();
            updates.put(curUserID, FieldValue.delete());
            docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("SP/dis2", "deleted");
                }
            });
        }
        if(curUserPriority.equals("3")){
            DocumentReference docRef = db.collection("users").document(fu.uID()).collection("PSInfo").document("forSwipe");
            Map<String, Object> updates = new HashMap<>();
            updates.put(curUserID, FieldValue.delete());
            docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("SP/dis3", "deleted");
                }
            });
        }
        if(curUserPriority.equals("4")){
            DocumentReference docRef = db.collection("users").document(fu.uID()).collection("PSInfo").document("forSwipe");
            Map<String, Object> updates = new HashMap<>();
            updates.put(curUserID, FieldValue.delete());
            docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("SP/dis4", "deleted");
                }
            });
        }
    }


    private void setInView(){
        mView.setFoundUserInfo(fuName,fuStatus,fuTxt1,fuTxt2,fuTxt3);
    }
}
