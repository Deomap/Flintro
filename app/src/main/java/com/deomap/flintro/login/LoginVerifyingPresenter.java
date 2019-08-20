package com.deomap.flintro.login;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.deomap.flintro.adapter.LoginContract;
import com.deomap.flintro.api.FirebaseUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//MODE 0 - LOGIN
//MODE 1 - REGISTER
//MODE 3 - SIGNED IN, EMAIL NOT VERIFIED

public class LoginVerifyingPresenter implements LoginContract.LoginVerifyingPresenter, LoginModel.MyCallback {
    private LoginContract.vSignInVerifying mView;
    private LoginContract.Repository mRepository;
    FirebaseUsers fbu = new FirebaseUsers();
    private boolean flag;
    private boolean emailSent= false;

    public LoginVerifyingPresenter(LoginContract.vSignInVerifying view){
        this.mView = view;
        this.mRepository = new LoginModel();
    }

    @Override
    public void checkPassword(String passwordNeeded, String passwordEntered, TextView v){
        if(passwordNeeded.equals(passwordEntered)){
            mView.changeTextViewColor(v, "colorPrimary");
        }
    }

    @Override
    public void tryTo(final String email, String password, String passwordRepeated, int mode) {
        if(mode == 0){
            mRepository.logIn(email,password);
            fbu.userInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if(flag) {
                        FirebaseUser user = fbu.curUser();
                        if(user != null){
                            if(user.isEmailVerified()){
                                mView.showToast("NP");
                                mView.goToMainScreen(0);
                            }
                            else{
                                mView.neededMode(3);
                                mView.showToast("No email verified");
                            }
                        }
                        else{
                            mView.showToast("login failure");
                        }
                        flag = false;
                    }
                    else{
                        flag = true;
                    }
                }
            });
        }
        if(mode == 1) {
            if (checkPassword(password, passwordRepeated) == 1) {
                FirebaseUser user = fbu.curUser();
                mRepository.registerCallback((LoginModel.MyCallback) this);
                mRepository.signUp(email,password);
            } else {
                mView.showToast("passwordsNotEqual");
            }
        }
    }

    @Override
    public void emailVerifiedClicked() {
            if(fbu.curUser().isEmailVerified()){
                mView.showToast("LETSGO");
                mView.goToMainScreen(0);
            }
            else{
                mView.showToast("email not verified");
            }

    }

    private int checkPassword(String password, String passwordRepeated){
        if (password.equals(passwordRepeated)){
            return 1;
        }
        else {
            return 0;
        }
    }

    //LOGGED IN = SIGNED UP :)

    @Override
    public void returnCallbackLoggedIn() {
        mView.neededMode(3);
        FirebaseUser user = fbu.curUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("EMAIL", "Email sent.");
                            mView.showToast("sent");
                            emailSent=true;
                        }
                    }
                });

    }
    @Override
    public void returnCallbackNotLoggedIn(){
        mView.showToast("sign up failure");
    }
}
