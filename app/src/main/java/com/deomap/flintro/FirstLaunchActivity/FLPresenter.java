package com.deomap.flintro.FirstLaunchActivity;

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
    private int stage = 0;
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
        ++stage;
        FirebaseFirestore db = fbcu.DBInstance();
        switch (stage){
            case 1:
                mView.askName();
                break;
            case 2:
                if(!arg.equals("")){
                    userName = arg;
                    mView.askInterests();
                }
                break;
            case 3:
                break;
            default:
                break;

        }
        buildProfile();
    }

    @Override
    public void onPickedInterest(int id) {
        userPickedInterests.put(interestRelation(id),id);
    }

    private String interestRelation(int id){
        switch (id){
            case  1:
                return "hockey";
            default:
                return "errrr";
        }
    }

    private void buildProfile(){
        CollectionReference users = fbcu.DBInstance().collection("users");
        users.document(fbu.uID()).set(userPickedInterests);
    }

}
