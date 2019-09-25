package com.deomap.flintro.QuestionsActivity;

import android.util.Log;

import androidx.annotation.NonNull;

import com.deomap.flintro.MainActivity;
import com.deomap.flintro.MainPart.MainOpsModel;
import com.deomap.flintro.adapter.FirestoreDataTranslator;
import com.deomap.flintro.adapter.MainPartContract;
import com.deomap.flintro.api.FirebaseCloudstore;
import com.deomap.flintro.api.SharedPreferencesHub;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class QuestionsPresenter implements MainPartContract.iQuestionsPresenter {
    private MainPartContract.iQuestionsActivity mView;
    private MainPartContract.iOpsModel mRepository;
    FirebaseCloudstore fbcu = new FirebaseCloudstore();
    public QueryDocumentSnapshot queryDocumentSnapshot;
    FirestoreDataTranslator fdt = new FirestoreDataTranslator();

    SharedPreferencesHub sph = new SharedPreferencesHub(MainActivity.getContextOfApplication());
    public QuestionsPresenter(MainPartContract.iQuestionsActivity view){
        this.mView = view;
        this.mRepository = new MainOpsModel();
    }

    @Override
    public void getQuestions(int pos) {
        FirebaseFirestore db = fbcu.DBInstance();
        //
        db.collection("interests").document("Math").collection("questions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("dd", document.getId() + " => " + document.getData());
                                queryDocumentSnapshot = document;
                            }
                        } else {
                            Log.d("dd", "Error getting documents: ", task.getException());
                        }
                    }
                });
        //SHOULD BE IN MODEL!
        String[] questionsList = fdt.QDS_string_to_array(queryDocumentSnapshot,"text");
    }
}
