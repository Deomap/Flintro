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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

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

    private String fuName,fuStatus, fuTxt1, fuTxt2, fuTxt3;

    FirestoreDataTranslator fdt = new FirestoreDataTranslator();


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
                        UIDQuery =  fdt.DS_SP_getList_string_to_array(document,"PSInfo/swipe");
                        for(String str : UIDQuery) Log.i("lld",str);
                        for(int i=0;i<UIDQuery.size();i++){
                            UIDQuery.set(i, UIDQuery.get(i).split(",")[1]);
                        }

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

    @Override
    public void showInLoop(){
        FirebaseUsers fu =  new FirebaseUsers();
        FirebaseCloudstore fcs = new FirebaseCloudstore();
        if(UIDQuery.size()-1 >= whereShowingNow) {
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

                            setInView();


                        } else {
                            Log.d("SP/sil()", "No such document");
                        }
                    } else {
                        Log.d("SP/sil()", "get failed with ", task.getException());
                    }
                }
            });
        }
        else{
            whereShowingNow = 0;
        }
    }


    private void setInView(){
        mView.setFoundUserInfo(fuName,fuStatus,fuTxt1,fuTxt2,fuTxt3);
    }
}
