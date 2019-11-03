package com.deomap.flintro.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.deomap.flintro.FirstLaunchActivity.FLActivity;
import com.deomap.flintro.QuestionsActivity.QuestionsActivity;
import com.deomap.flintro.R;
import com.deomap.flintro.adapter.LoginContract;

public class VerifyingSignInActivity extends AppCompatActivity implements LoginContract.vSignInVerifying {

    LoginContract.LoginVerifyingPresenter mPresenter;
    TextView mPasswordField;
    TextView mPasswordConfirmation;
    TextView mEmailField;
    Button mLoginButton;
    Button mGetEmailButton;
    Button mEmailVerified;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifying_sign_in);

        mPasswordField = findViewById(R.id.passwordField);
        mPasswordConfirmation = findViewById(R.id.passwordConfirmation);
        mEmailField = findViewById(R.id.emailField);
        mGetEmailButton = findViewById(R.id.getEmailButton);
        mLoginButton = findViewById(R.id.loginButton);
        mEmailVerified =findViewById(R.id.emailVerified);
        Intent intent = getIntent();
        //получаем информацию из SignInActivity, что хочет пользователь, зарегестрироваться или войти
        mode = intent.getIntExtra("mode", 1);
        neededMode(mode);

        mPresenter = new LoginVerifyingPresenter((LoginContract.vSignInVerifying) this);

    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    //UI
    @Override
    public void changeTextViewColor(TextView v, String color) {
        v.setEnabled(false);
    }

    //посколько все элементы для всех 3х режимов находятся на одном экране, для каждого режима устанавливается видимость отдельных элементов
    @Override
    public void neededMode(int mode) {
        this.mode = mode;
        if(mode == 0){
            //showToast("AAA");
            mPasswordConfirmation.setEnabled(false);
            mPasswordConfirmation.setVisibility(View.INVISIBLE);
            mGetEmailButton.setEnabled(false);
            mGetEmailButton.setVisibility(View.INVISIBLE);
            mEmailVerified.setEnabled(false);
            mEmailVerified.setVisibility(View.INVISIBLE);
            mLoginButton.setEnabled(true);
            mLoginButton.setVisibility(View.VISIBLE);

        }
        if(mode == 1){
            //showToast("BBB");
                mPasswordConfirmation.setEnabled(true);
                mPasswordConfirmation.setVisibility(View.VISIBLE);
                mGetEmailButton.setEnabled(true);
                mGetEmailButton.setVisibility(View.VISIBLE);
            mEmailVerified.setEnabled(false);
            mEmailVerified.setVisibility(View.GONE);
            mLoginButton.setEnabled(false);
            mLoginButton.setVisibility(View.INVISIBLE);
        }
        if(mode == 3){
            //showToast("CCC");
            mPasswordConfirmation.setEnabled(false);
            mPasswordConfirmation.setVisibility(View.INVISIBLE);
            mGetEmailButton.setEnabled(true);
            mGetEmailButton.setVisibility(View.VISIBLE);
            mEmailVerified.setEnabled(true);
            mEmailVerified.setVisibility(View.VISIBLE);
            mLoginButton.setEnabled(false);
            mLoginButton.setVisibility(View.INVISIBLE);
        }
    }

    //переход непосрдетственно в основную активность или в активность заполнения профиля (в зависимости от режима)
    @Override
    public void goToMainScreen(int param) {

        if(param!=0) {
            startActivity(new Intent(this, FLActivity.class));
            mPresenter.addUserToDatabase();
        }
        else{
            startActivity(new Intent(this, QuestionsActivity.class));
        }
    }

    //обработка нажатий пользователя:

    public void getEmail(View view){
        mPresenter.tryTo(mEmailField.getText().toString(), mPasswordField.getText().toString(),mPasswordConfirmation.getText().toString(),mode);
    }

    public void emailVerified(View view){
        mPresenter.emailVerifiedClicked();
    }

    public void loginClicked(View view){
        mPresenter.tryTo(mEmailField.getText().toString(), mPasswordField.getText().toString(),mPasswordConfirmation.getText().toString(),mode);
    }
}
