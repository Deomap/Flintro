package com.deomap.flintro.FirstLaunchActivity;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.deomap.flintro.MainPart.MainOpsModel;
import com.deomap.flintro.adapter.FirestoreDataTranslator;
import com.deomap.flintro.adapter.MainPartContract;
import com.deomap.flintro.adapter.TopicsPositionMatch;
import com.deomap.flintro.api.FirebaseCloudstore;
import com.deomap.flintro.api.FirebaseUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FLPresenter implements MainPartContract.iFLPresenter  {

    Map<String, Object> userPickedInterests = new HashMap<>();
    Map<String, Object> name = new HashMap<>();
    TopicsPositionMatch tpm = new TopicsPositionMatch();
    private int stage = 0;
    String userName = "";
    String ms  = "null";
    Boolean photoDownloaded = false;
    String city = "null";
    String q2 = "null";
    String q3 =  "null";
    String text="";
    String cityNameFLA="";
    String  topic;
    FirebaseCloudstore fbcu = new FirebaseCloudstore();
    FirebaseUsers fbu = new FirebaseUsers();

    private MainPartContract.iFLActivity mView;
    private MainPartContract.iOpsModel mRepository;

    public FLPresenter(MainPartContract.iFLActivity view){
        this.mView = view;
        this.mRepository = new MainOpsModel();
    }

    @Override
    //arg in stage 2 is name
    public void initiateNextStage(String arg) {
        //Log.i("stage before",Integer.toString(stage));
        if(stage == 1){
            if(!(text.length()<2 || text.length()>30)){
                userName=text;
                text="";
            }
            else{
                mView.toast("Введите имя длиной от 2 до 30 символов",1);
                stage--;
                text="";
            }
        }
        if(stage == 5 || stage == 6 || stage == 7){
            if(!(text.length()<30 || text.length()>170)){
                if(stage==5){
                    ms=text;
                }
                if(stage==6){
                    q2=text;
                }
                if(stage==7){
                    q3=text;
                }
                text="";
            }
            else{
                mView.toast("Ответ должен быть длиной от 30 до 170 символов",1);
                stage--;
                text="";
            }
        }
        if(stage==3){
            if(photoDownloaded){
                //pass
                text="";
            }
            else{
                mView.toast("Фото  не загружено :(",1);
                stage--;
                text="";
            }
        }
        if(stage==2){
            city = cityNameFLA;
        }
        text="";
        stage++;
        //Log.i("stage after",Integer.toString(stage));
        FirebaseFirestore db = fbcu.DBInstance();
        switch (stage){
            case 1:
                mView.askName();
                mView.changeItemsAvailibility("name");
                break;
            case 2:
                mView.askCity();
                mView.changeItemsAvailibility("city");
                break;
            case 3:
                mView.askPhoto();
                mView.changeItemsAvailibility("photo");
                Log.i("st1","!!");
                break;
            case 4:
                userName = arg;
                mView.askInterests();
                mView.changeItemsAvailibility("interests");
                Log.i("st2","!!");
                break;
            case 5:
                mView.askQ1m("Что бы о вас сказал ваш лучший друг?");
                mView.changeItemsAvailibility("q1m");
                break;
            case 6:
                mView.askQ2("Как вы относитесь к фильмам ужасов?");
                mView.changeItemsAvailibility("q2");
                break;
            case 7:
                mView.askQ3("Согласился бы ты быть единственным зрителем на концерте любимой группы?");
                mView.changeItemsAvailibility("q3");
                break;
            case 8:
                mView.changeItemsAvailibility("finish");
                buildProfile();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPickedInterest(int position) {
        userPickedInterests.put(tpm.topicNameEng(position+2),5);
        Log.i("put","cff");
    }

    @Override
    public void setTextFromET(String text) {
        this.text = text;
    }

    @Override
    public void setPhotoDownloadedTrue() {
        photoDownloaded=true;
    }

    @Override
    public void setNameFLA(String name) {
        cityNameFLA = name;
    }

    //создание основных папок и файлов пользователя, запись имени и выбранных интересов в базу данных
    private void buildProfile(){
        Log.i("buildProfile","!!");
        mView.uploadImage();
        name.put("name",userName);

        FirebaseFirestore db = new FirebaseCloudstore().DBInstance();


        Map<String, Object> setup = new HashMap<>();
        setup.put("null","null");
        new FirebaseCloudstore().DBInstance().collection("users").document(new FirebaseUsers().uID()).set(setup).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                //запись в БД о том, что пользователь прошел заполнение профиля и заходит не в первый раз
                Map<String, Object> FL = new HashMap<>();
                FL.put("firstLaunch","n");
                new FirebaseCloudstore().DBInstance().collection("users").document(new FirebaseUsers().uID())
                        .update(FL)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("FLP/buildProfile()", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("FLP/buildProfile()", "Error writing document", e);
                            }
                        });

            }
        });

        Map<String, Object> cityMap = new HashMap<>();
        cityMap.put("city",city);
        new FirebaseCloudstore().DBInstance().collection("users").document(new FirebaseUsers().uID())
                .update(cityMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FLP/buildProfile()", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FLP/buildProfile()", "Error writing document", e);
                    }
                });


        Map<String, Object> FL = new HashMap<>();
        FL.put("firstLaunch","n");
        new FirebaseCloudstore().DBInstance().collection("users").document(new FirebaseUsers().uID())
                .update(FL)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FLP/buildProfile()", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FLP/buildProfile()", "Error writing document", e);
                    }
                });

        db.collection("users").document(new FirebaseUsers().uID()).collection("interests").document("topics")
                .set(userPickedInterests)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FLP/buildProfile()", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FLP/buildProfile()", "Error writing document", e);
                    }
                });

        db.collection("users").document(new FirebaseUsers().uID())
                .update(name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FLP/buildProfile()", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FLP/buildProfile()", "Error writing document", e);
                    }
                });

        //updating displayName
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName+","+new FirebaseUsers().uID()).build();
        new FirebaseUsers().curUser().updateProfile(profileUpdates);


        loadProfileData();
        mView.startIntent("Questions");
    }

    //загрузка имени пользователя во внутренее хранилище
    private void loadProfileData(){
        mView.accessSharedPreferences("put", "userProfile","String", "userName", userName);
        setupDocuments();
    }

    //всё также, формирование необходимых файлов в БД
    private void setupDocuments(){
        FirebaseUsers fu = new FirebaseUsers();
         Map<String, Object> setup = new HashMap<>();

         FirebaseFirestore fcs_db =  new FirebaseCloudstore().DBInstance();
        setup.put("mainStatus",ms);
        fcs_db.collection("users").document(fu.uID()).update(setup);
        setup.clear();

        setup.put("txtSwipe1","null");
        fcs_db.collection("users").document(fu.uID()).update(setup);
        setup.clear();

        setup.put("txtSwipe2",q2);
        fcs_db.collection("users").document(fu.uID()).update(setup);
        setup.clear();

        setup.put("txtSwipe3",q3);
        fcs_db.collection("users").document(fu.uID()).update(setup);
        setup.clear();
        setup.put("null,null,null","null");
        fcs_db.collection("users").document(fu.uID()).collection("likes").document("answers").set(setup);
        setup.clear();
        setup.put("null","null");
        fcs_db.collection("users").document(fu.uID()).collection("PSInfo").document("forSwipe").set(setup);
        fcs_db.collection("users").document(fu.uID()).collection("answeredQuestions").document("null").set(setup);
        //fcs_db.collection("users").document(fu.uID()).collection("interests").document("topics").set(setup);
        fcs_db.collection("users").document(fu.uID()).collection("likes").document("MLNotMutual").set(setup);
        fcs_db.collection("users").document(fu.uID()).collection("likes").document("meLikes").set(setup);
        fcs_db.collection("users").document(fu.uID()).collection("likes").document("swipe").set(setup);
        //setup.clear();
    }

}
