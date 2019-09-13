package com.deomap.flintro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.deomap.flintro.MainPart.MainScreenActivity;
import com.deomap.flintro.QuestionsActivity.QuestionsActivity;
import com.deomap.flintro.api.FirebaseUsers;
import com.deomap.flintro.login.SignInActivity;
import com.deomap.flintro.login.VerifyingSignInActivity;
import com.deomap.flintro.login.appStarted;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
TextView helloWorldD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUsers fbu = new FirebaseUsers();
        FirebaseUser user = fbu.curUser();
        fbu.userInstance().signOut();
        appStarted as = new appStarted();
        if(as.requestForUsing() == 0){
            startActivity(new Intent(this,SignInActivity.class));
        }
        else{
            if(user.isEmailVerified()){
                startActivity(new Intent(this, QuestionsActivity.class));
            }
            else{
                Intent intent = new Intent(this,VerifyingSignInActivity.class);
                intent.putExtra("mode",3);
                startActivity(intent);
            }
        }

    }
}
