package com.deomap.flintro.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.deomap.flintro.R;
import com.deomap.flintro.adapter.LoginContract;

public class SignInActivity extends AppCompatActivity implements LoginContract.vSignIn {

    Intent vSignInVerifying;
    private Button mRegButton;
    private Button mSignInButton;
    private int mode = 0;

    private LoginContract.LoginPresenter mPresenter;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_in);

            vSignInVerifying = new Intent(this, VerifyingSignInActivity.class);

            mRegButton = findViewById(R.id.registerButton);
            mSignInButton = findViewById(R.id.loginButton);

            mPresenter = new LoginPresenter((LoginContract.vSignIn) this);
    }

    @Override
    public void showToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToVerifyingLoginActivity(int mode){
        vSignInVerifying.putExtra("mode", mode);
        startActivity(vSignInVerifying);
    }



    public void registerChoosen(View view){
        mode = 1;
        nextStAuth();
    }

    public void loginChoosen(View view){
        mode = 0;
        nextStAuth();
    }

    public void nextStAuth(){
        mPresenter.tryStAuth(mode);
    }


}
