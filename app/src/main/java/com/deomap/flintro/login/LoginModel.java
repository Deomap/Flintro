package com.deomap.flintro.login;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.deomap.flintro.adapter.LoginContract;
import com.deomap.flintro.api.FirebaseUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;

public class LoginModel implements LoginContract.Repository {

    FirebaseUsers fbu = new FirebaseUsers();
    private Exception ex;
    private  MyCallback callback;
    private String SIEmail;
    private String SIPassword;


    //Return result of SIGNING UP(!)
    public interface MyCallback{
        void returnCallbackLoggedIn();
        void returnCallbackNotLoggedIn();
    }


    @Override
    public Exception logIn(String email, String password){
        Log.i("LoginModel","logIn() started");
        fbu.userInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LoginModel", "signInWithEmail:success");
                            ex = null;

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LoginModel", "signInWithEmail:failure");
                            ex = task.getException();
                            Log.i("LoginMode", ex.toString());
                        }
                    }
                });
        return ex;
    }

    @Override
    public int signUp( String email,  String password){
        SIEmail = email;
        SIPassword=password;
        fbu.userInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LM/signUp", "createUserWithEmail:successs");
                            FirebaseUser user = fbu.curUser();
                            callback.returnCallbackLoggedIn();
                            logIn(SIEmail,SIPassword);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("LM/signUp", "createUserWithEmail:failure", task.getException());
                            callback.returnCallbackNotLoggedIn();
                        }

                        // ...
                    }
                });
        return 0;
    }

    @Override
    public void registerCallback(MyCallback callback){
        this.callback = callback;
    }

}
