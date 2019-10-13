package com.deomap.flintro.QuestionsActivity;

import android.util.Log;

import androidx.annotation.NonNull;

import com.deomap.flintro.MainActivity;
import com.deomap.flintro.MainPart.MainOpsModel;
import com.deomap.flintro.adapter.FirestoreDataTranslator;
import com.deomap.flintro.adapter.MainPartContract;
import com.deomap.flintro.adapter.TopicsPositionMatch;
import com.deomap.flintro.api.FirebaseCloudstore;
import com.deomap.flintro.api.FirebaseUsers;
import com.deomap.flintro.api.SharedPreferencesHub;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class QuestionsPresenter implements MainPartContract.iQuestionsPresenter {
    private MainPartContract.iQuestionsActivity mView;
    private MainPartContract.iOpsModel mRepository;
    FirebaseCloudstore fbcs = new FirebaseCloudstore();
    TopicsPositionMatch tpm =  new TopicsPositionMatch();
    public QueryDocumentSnapshot queryDocumentSnapshot;
    FirestoreDataTranslator fdt = new FirestoreDataTranslator();
    private ArrayList<String>  questionsList = new ArrayList<>();
    private ArrayList<String>  questionsVotesList = new ArrayList<>();
    private ArrayList<String>  questionsIDList = new ArrayList<>();
    private ArrayList<String>  answersList  = new ArrayList<>();
    private ArrayList<String>  answersUserIDList =  new ArrayList<>();
    String selectedQuestion = "nullQuestion";
    public String selectedTopic = "nullTopic";
    private FirebaseUsers fbu = new FirebaseUsers();
    private int lastAnswerPosClicked;
    private int lastQuestionPosClicked;

    SharedPreferencesHub sph = new SharedPreferencesHub(MainActivity.getContextOfApplication());
    public QuestionsPresenter(MainPartContract.iQuestionsActivity view){
        this.mView = view;
        this.mRepository = new MainOpsModel();
    }

    @Override
    public void getQuestions(int pos) {
        FirebaseFirestore db = fbcs.DBInstance();

        questionsList.clear();
        questionsIDList.clear();
        questionsVotesList.clear();
        selectedTopic = tpm.topicNameEng(pos);
        //
        db.collection("interests").document(selectedTopic).collection("questions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("QP/getQuestions()", document.getId() + " => " + document.getData());
                                queryDocumentSnapshot = document;
                                questionsList.add(fdt.QDS_string_to_array(queryDocumentSnapshot,"text"));
                                questionsIDList.add(fdt.QDS_string_to_array(queryDocumentSnapshot,"id"));
                                questionsVotesList.add(fdt.QDS_string_to_array(queryDocumentSnapshot,"votes"));
                            }
                        } else {
                            Log.d("dd", "Error getting documents: ", task.getException());
                        }
                        mView.setMainText("QUESTIONS");
                        mView.initiateQuestionsList(questionsList);

                    }
                });
        //SHOULD BE IN MODEL!
    }

    @Override
    public void getAnswers(int pos) {
        FirebaseFirestore db = fbcs.DBInstance();
        //
        answersList.clear();
        answersUserIDList.clear();
        selectedQuestion = questionsIDList.get(pos);
        //Log.i("QP/sendUserAnswer()", selectedQuestion+"!!");
        //Log.d("QP/getAnswers()", "DocumentSnapshot data: " + selectedTopic+" "+selectedQuestion);
        DocumentReference docRef = db.collection("interests").document(selectedTopic).collection("answers").document(selectedQuestion);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("QP/getAnswers()", "DocumentSnapshot data: " + document.getData());
                        answersList = fdt.DS_QP_getAnswers_string_to_array(document,"AL");
                        answersUserIDList = fdt.DS_QP_getAnswers_string_to_array(document,"AIDL");
                    } else {
                        Log.d("QP/getAnswers()", "No such document");
                    }
                } else {
                    Log.d("QP/getAnswers()", "get failed with ", task.getException());
                }
                mView.setMainText("ANSWERS");
                mView.initiateAnswersList(answersList);
            }
        });
        //SHOULD BE IN MODEL!
    }

    @Override
    public void sendUserAnswer(String answerText) {
        if(!answerText.isEmpty()){
            String userID = fbu.curUser().getUid();

            Map<String, Object> answer = new HashMap<>();
            answer.put(userID, answerText);

            FirebaseFirestore db = fbcs.DBInstance();
            //Log.i("QP/sendUserAnswer()", selectedTopic+" "+selectedQuestion);
            Log.i("QP/sendUserAnswer()", selectedQuestion+"!");
            //
            db.collection("interests").document(selectedTopic).collection("answers").document(selectedQuestion)
                    .set(answer)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("QP/sendUserAnswer()", "DocumentSnapshot successfully written!");
                            addAnsweredQuestionToUser();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("QP/sendUserAnswer", "Error writing document", e);
                        }
                    });
            //
        }

        //MODELMODELMODEL!
    }

    @Override
    public void answerClicked(int pos) {
        Map<String, Object> like = new HashMap<>();
        //Log.i("QP/sendUserAnswer()", fbu.curUser().getUid()+" "+selectedQuestion);
        //Log.i("QP/sendUserAnswer()", selectedQuestion+"!!");
        String answerPath = selectedTopic+"/"+selectedQuestion+"/"+answersUserIDList.get(pos);
        like.put(answerPath,answersList.get(pos));
        //
        fbcs.DBInstance().collection("users").document(fbu.curUser().getUid()).collection("likes").document("answers")
                .set(like)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("QP/answerClicked()", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("QP/answerClicked()", "Error writing document", e);
                    }
                });

        mView.toast("liked",0);
    }

    private void addAnsweredQuestionToUser(){
        Map<String, Object> answer = new HashMap<>();
        //Log.i("QP/sendUserAnswer()", fbu.curUser().getUid()+" "+selectedQuestion);
        Log.i("QP/sendUserAnswer()", selectedQuestion+"!!");
        //
        fbcs.DBInstance().collection("users").document(fbu.curUser().getUid()).collection("answeredQuestions").document(selectedQuestion)
                .set(answer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("QP/sendUserAnswer()/ATU", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("QP/sendUserAnswer()/ATU", "Error writing document", e);
                    }
                });
    }
}




