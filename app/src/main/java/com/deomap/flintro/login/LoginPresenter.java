package com.deomap.flintro.login;

import android.util.Log;

import androidx.annotation.NonNull;

import com.deomap.flintro.adapter.LoginContract;
import com.deomap.flintro.api.FirebaseUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.nio.file.FileVisitResult;

public class LoginPresenter implements LoginContract.LoginPresenter{
    private LoginContract.vSignIn mView;
    private LoginContract.Repository mRepository;
    FirebaseUsers fbu = new FirebaseUsers();

    @Override
    public void toast(String text){
        mView.showToast(text);
    }

    @Override
    public void tryStAuth(int mode){
        tryNdSignUp(mode);
    }



    public LoginPresenter(LoginContract.vSignIn view){
        this.mView = view;
        this.mRepository = new LoginModel();
    }

    private void loginApproved(){
        //GO TO MAIN APP SCREEN
    }

    private void tryNdSignUp(int mode){
        mView.goToVerifyingLoginActivity(mode);
    }




}
