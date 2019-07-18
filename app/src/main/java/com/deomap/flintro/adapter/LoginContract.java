package com.deomap.flintro.adapter;

import android.widget.TextView;

import com.deomap.flintro.login.LoginModel;
import com.google.firebase.auth.FirebaseUser;

public interface LoginContract {
    interface View{
        void showToast(String text);
    }

    interface vSignIn extends View{
        void goToVerifyingLoginActivity(int mode);
    }

    interface vSignInVerifying extends View{
        void changeTextViewColor(TextView v, String text);
        void neededMode(int mode);
    }

    interface Presenter{
        void toast(String text);
    }

    interface LoginPresenter extends Presenter{
        void tryStAuth(int mode);
    }

    interface LoginVerifyingPresenter{
        void checkPassword(String passwordNeeded, String passwordEntered, TextView v);
        void tryTo(String email, String password, String passwordRepeated, int mode);
        void emailVerifiedClicked();
    }

    interface Repository{
        Exception logIn(String email, String password);
        int signUp(String email, String password);
        void registerCallback(LoginModel.MyCallback callback);
    }
}
