package com.deomap.flintro.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseUsers{

    //FBU CRASHES APP
    public FirebaseUser curUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }
    public FirebaseAuth userInstance(){ return FirebaseAuth.getInstance(); }
    public String userName(){ return curUser().getDisplayName(); }
    public boolean isEmailVerified() { return curUser().isEmailVerified(); }

}

