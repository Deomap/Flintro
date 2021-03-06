package com.deomap.flintro.login;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.deomap.flintro.FirstLaunchActivity.FLActivity;
import com.deomap.flintro.MainActivity;
import com.deomap.flintro.QuestionsActivity.QuestionsActivity;
import com.deomap.flintro.adapter.LoginContract;
import com.deomap.flintro.api.FirebaseCloudstore;
import com.deomap.flintro.api.FirebaseUsers;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

//MODE 0 - LOGIN
//MODE 1 - REGISTER
//MODE 3 - SIGNED IN, EMAIL NOT VERIFIED

//Presenter для VerifyingSignInActivity
//эти 2 класса отвечают за весь вход и регистрацию пользователя
public class LoginVerifyingPresenter implements LoginContract.LoginVerifyingPresenter, LoginModel.MyCallback {
    private LoginContract.vSignInVerifying mView;
    private LoginContract.Repository mRepository;
    FirebaseUsers fbu = new FirebaseUsers();
    FirebaseCloudstore fbcs = new FirebaseCloudstore();
    private boolean flag;
    private boolean emailSent= false;

    public LoginVerifyingPresenter(LoginContract.vSignInVerifying view){
        this.mView = view;
        this.mRepository = new LoginModel();
    }

    //проверка на совпадение пароля и его повтора
    @Override
    public void checkPassword(String passwordNeeded, String passwordEntered, TextView v){
        if(passwordNeeded.equals(passwordEntered)){
            mView.changeTextViewColor(v, "colorPrimary");
        }
    }

    //здесь в зависимости от режима (режим прописан сверху) происходит регистрация пользователя/запрос разрешения на вход
    @Override
    public void tryTo(final String email, String password, String passwordRepeated, int mode) {
        Log.i("LVP-modeswitch", String.valueOf(mode));

        //вход
        if(mode == 0){
            mRepository.logIn(email,password);
            fbu.userInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    //if(flag) {
                        Log.i("lvp","spec");
                        FirebaseUser user = fbu.curUser();
                        if(user != null){
                            if(user.isEmailVerified()){
                                mView.showToast("Добро пожаловать :)");

                                DocumentReference docRef = new FirebaseCloudstore().DBInstance().collection("users").document(fbu.curUser().getUid());
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d("LVP", "DocumentSnapshot data: " );
                                                String docData=document.getString("firstLaunch");
                                                if(docData.equals("y")){
                                                    mView.goToMainScreen(13);
                                                }
                                                else{
                                                    mView.goToMainScreen(0);
                                                }
                                            } else {
                                                Log.d("LVP", "No such document");
                                            }
                                        } else {
                                            Log.d("LVP", "get failed with ", task.getException());
                                        }
                                    }
                                });
                            }
                            else{
                                mView.neededMode(3);
                                mView.showToast("No email verified");
                            }
                        flag = false;
                    }
                    else{
                        flag = true;
                    }
                }
            });
        }

        //регистрация
        if(mode == 1) {
            if (checkPassword(password, passwordRepeated) == 1) {
                FirebaseUser user = fbu.curUser();
                mRepository.registerCallback((LoginModel.MyCallback) this);
                mRepository.signUp(email,password);

            } else {
                mView.showToast("Пароли не совпадают");
            }
        }

        //пользователь зарегестрирован, но не подтвердил почту
        if(mode == 3){
            Log.i("LVP-modeswitch", String.valueOf(mode));
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
    }

    @Override
    public void emailVerifiedClicked() {
            fbu.curUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(fbu.curUser().isEmailVerified()){
                        mView.showToast("LETSGO");

                        mView.goToMainScreen(13);

                    }
                    else{
                        mView.showToast("email not verified");
                        mView.showToast(fbu.curUser().getUid());
                    }
                }
            });
    }

    //добавление пользователя в БД с пометкой, что он входит в первый раз
    @Override
    public void addUserToDatabase() {
        FirebaseFirestore db = fbcs.DBInstance();
        String uid = fbu.curUser().getUid();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("firstLaunch", "y");
        //!!!!!!!!
        db.collection("users").document(uid)
                .set(userInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("LVP-AddingUser", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("LVP-AddingUser", "Error writing document", e);
                    }
                });
    }

    private int checkPassword(String password, String passwordRepeated){
        if (password.equals(passwordRepeated)){
            return 1;
        }
        else {
            return 0;
        }
    }

    //LOGGED IN = зарегестрирован :)

    //если регистрация прошла успешно, пользователю отсылается письмо
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
