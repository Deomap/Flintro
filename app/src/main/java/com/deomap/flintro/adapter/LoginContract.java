package com.deomap.flintro.adapter;

import android.widget.TextView;

import com.deomap.flintro.login.LoginModel;

//связующая для всех частей приложения, отвечающих за вход и регистрацию (по архитектуре MVP)
//суть этой архитектуры в том, что всякий View, часть с логикой приложения, часть, отвечающая за связь с внешней БД и памятью, а также сторонние библиотеки
//-вот все они не должны ничего не знать друг о друге и общаться только через интерфейсы, где определены методы
public interface
LoginContract {
    interface View{
        void showToast(String text);
    }

    interface vSignIn extends View{
        void goToVerifyingLoginActivity(int mode);
    }

    interface vSignInVerifying extends View{
        void changeTextViewColor(TextView v, String text);
        void neededMode(int mode);
        void goToMainScreen(int param);
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
        void addUserToDatabase();

    }

    interface Repository{
        Exception logIn(String email, String password);
        int signUp(String email, String password);
        void registerCallback(LoginModel.MyCallback callback);
    }


}
