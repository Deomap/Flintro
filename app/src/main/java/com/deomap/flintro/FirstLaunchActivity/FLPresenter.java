package com.deomap.flintro.FirstLaunchActivity;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.deomap.flintro.MainPart.MainOpsModel;
import com.deomap.flintro.adapter.MainPartContract;
import com.deomap.flintro.api.FirebaseCloudstore;
import com.deomap.flintro.api.FirebaseUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FLPresenter implements MainPartContract.iFLPresenter  {

    Map<String, Object> userPickedInterests = new HashMap<>();
    private int stage = -1;
    String userName = "";
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
        Log.i("stage before",Integer.toString(stage));
        stage++;
        Log.i("stage after",Integer.toString(stage));
        FirebaseFirestore db = fbcu.DBInstance();
        switch (stage){
            case 1:
                mView.askPhoto();
                break;
            case 2:
                mView.askName();
                Log.i("st1","!!");
                break;
            case 3:
                if(!arg.equals("")){
                    userName = arg;
                    mView.askInterests();
                    Log.i("st2","!!");
                }
                break;
            case 4:
                buildProfile();
                break;
            default:
                break;

        }

    }

    @Override
    public void onPickedInterest(int position) {
        userPickedInterests.put(interestRelation(position),position);
        Log.i("putted","cff");
    }

    private String interestRelation(int position){
        switch (position){
            case  1:
                Log.i("case 1","!!");
                return "hockey";
            case 0:
                Log.i("case 0","!!");
                return "fvb";
            default:
                return "errrr";
        }
    }

    private void buildProfile(){
        Log.i("buildProfile","!!");
        CollectionReference users = fbcu.DBInstance().collection("users");
        users.document(fbu.uID()).collection("interests").add(userPickedInterests);
        loadProfileData();
        mView.startIntent("Questions");
    }

    private void loadProfileData(){
        mView.accessSharedPreferences("put", "userProfile","String", "userName", userName);
    }


}
