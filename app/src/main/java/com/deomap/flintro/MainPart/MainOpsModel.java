package com.deomap.flintro.MainPart;

import android.util.Log;

import androidx.annotation.NonNull;

import com.deomap.flintro.adapter.MainPartContract;
import com.deomap.flintro.api.FirebaseCloudstore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.ContentValues.TAG;

public class MainOpsModel implements MainPartContract.iOpsModel {

    private FirebaseCloudstore fbcs = new FirebaseCloudstore();
    private MyCallback callback;

    @Override
    public void registerCallback(MyCallback callback) { this.callback = callback;
    }

    @Override
    public void readQuestions(String collection, int extra) {
        Log.i("FFFF", "ff");
        fbcs.DBInstance().collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("MODEL: readQuestions", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("MODEL: readQuestions", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    @Override
    public void downloadPhoto(String userID, int WHAT) {
        //привязка фото в storage по userID
    }

    @Override
    public void getUserInfo(String userID, String infoType) {

    }

    public interface MyCallback {

    }


}
