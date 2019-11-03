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
    //private ArrayList<String> votesList = new ArrayList<>();
    String selectedQuestion = "null";
    public String selectedTopic = "null";
    public  String selectedTopicRus ="null";
    int poss;
    private ArrayList<String>  answersFinalList = new ArrayList<>();

    //stage отвечает за режим, в котором сейчас все отображается в активности
    private int stage = 0;
    /*
    stage:
        0 interests
        1 questions
        2 answers
     */

    private FirebaseUsers fbu = new FirebaseUsers();
    private int lastAnswerPosClicked;
    private int lastQuestionPosClicked;

    SharedPreferencesHub sph = new SharedPreferencesHub(MainActivity.getContextOfApplication());
    public QuestionsPresenter(MainPartContract.iQuestionsActivity view){
        this.mView = view;
        this.mRepository = new MainOpsModel();
    }

    public QuestionsPresenter() {

    }

    //получение списка вопросов по выбранной теме
    @Override
    public void getQuestions(int pos, int fromLA) {
        FirebaseFirestore db = fbcs.DBInstance();

        questionsList.clear();
        questionsIDList.clear();
        questionsVotesList.clear();
        selectedTopic = tpm.topicNameEng(pos);
        selectedTopicRus=tpm.topicNameRus(pos);

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
                                //questionsVotesList.add(fdt.QDS_string_to_array(queryDocumentSnapshot,"votes"));

                                stage = 1;
                                mView.setMainText(selectedTopicRus);
                                mView.initiateQuestionsList(questionsList);
                                mView.itemsAvailibilitySet(stage);
                            }
                        } else {
                            Log.d("dd", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    //получение списка ответов по выбранному вопросу
    @Override
    public void getAnswers(int pos, String fromWho) {
        this.poss=pos;
        FirebaseFirestore db = fbcs.DBInstance();
        //
        answersList.clear();
        answersUserIDList.clear();
        answersFinalList.clear();
        if(!fromWho.equals("fromQA")){
            selectedTopic = tpm.topicNameEng(pos);
            selectedTopicRus=tpm.topicNameRus(pos);
            selectedQuestion = fromWho;
        }
        else{
            selectedQuestion = questionsIDList.get(pos);
        }

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
                        //votesList = fdt.DS_QP_getAnswers_string_to_array(document, "AVL");

                        mView.setMainText(selectedTopicRus+": "+questionsList.get(poss));
                        stage = 2;

                        for(int i = 0 ;i<answersList.size();i++){
                            answersFinalList.add(selectedTopic+","+selectedQuestion+","+answersUserIDList.get(i));
                        }
                        mView.initiateAnswersList(answersList, answersFinalList);
                        mView.itemsAvailibilitySet(stage);
                    } else {
                        Log.d("QP/getAnswers()", "No such document");
                    }
                } else {
                    Log.d("QP/getAnswers()", "get failed with ", task.getException());
                }
            }
        });
    }

    //получсение ответов по ID вопроса, на который кликнул пользователь в LikesActivity
    @Override
    public void fromLikesActivity(int pos,  String qID){
        getAnswers(pos,qID);
    }

    //отправка ответа пользователя в БД
    @Override
    public void sendUserAnswer(String answerText) {
        if(!(answerText.length()<30 || answerText.length()>170)){
            String userID = fbu.curUser().getUid();

            Map<String, Object> answer = new HashMap<>();
            answer.put(userID, answerText);

            FirebaseFirestore db = fbcs.DBInstance();
            Log.i("QP/sendUserAnswer()", selectedQuestion+"!");
            //
            db.collection("interests").document(selectedTopic).collection("answers").document(selectedQuestion)
                    .update(answer)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("QP/sendUserAnswer()", "DocumentSnapshot successfully written!");
                            mView.toast("Ответ отправлен :)",1);
                            addAnsweredQuestionToUser();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("QP/sendUserAnswer", "Error writing document", e);
                        }
                    });
        } else{
            if(answerText.length()<30) mView.toast("Вы написали слишком мало :(",1);
            if(answerText.length()>150) mView.toast("Вы написали слишком много :)",1);
        }
    }

    private void setTextWithStage(){
        switch (stage) {
            case 0:mView.setMainText("О чём вы сейчас думаете?"); break;
            case 1:mView.setMainText(selectedTopicRus); break;
            case 2:mView.setMainText(selectedTopicRus+": "+questionsList.get(poss)); break;
        }
    }

    //нажата кнопка "Назад"
    @Override
    public void backStage() {
        setTextWithStage();
        stage--;
        mView.itemsAvailibilitySet(stage);
    }

    //добавление  отвеченного вопроса в профиль пользователя
    private void addAnsweredQuestionToUser(){
        Map<String, Object> answer = new HashMap<>();
        //Log.i("QP/sendUserAnswer()", fbu.curUser().getUid()+" "+selectedQuestion);
        Log.i("QP/sendUserAnswer()", selectedQuestion+"!!");
        //
        fbcs.DBInstance().collection("users").document(fbu.curUser().getUid()).collection("answeredQuestions").document(selectedQuestion)
                .update(answer)
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


