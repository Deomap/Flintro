package com.deomap.flintro.SwipeActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.deomap.flintro.ChatActivity.ChatActivity;
import com.deomap.flintro.LikesActivity.LikesActivity;
import com.deomap.flintro.ProfileActivity.ProfileActivity;
import com.deomap.flintro.QuestionsActivity.QuestionsActivity;
import com.deomap.flintro.R;
import com.deomap.flintro.adapter.MainPartContract;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SwipeActivity extends AppCompatActivity implements MainPartContract.iSwipeActivity {

    private Button foundUserReportBtn;
    private Button foundUserLikeBtn;
    private Button foundUserDislikeBtn;
    private TextView userMatchedAnswers1;
    private TextView userMatchedAnswers2;
    private TextView userMatchedAnswers3;
    private TextView foundUserMainStatus;
    private TextView foundUserName;
    private ImageView foundUserPhoto;


    private MainPartContract.iSwipePresenter mPresenter;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_swipe:
                    startIntent("Swipe");
                    return true;
                case R.id.navigation_likes:
                    startIntent("Likes");
                    return true;
                case R.id.navigation_questions:
                    startIntent("Questions");
                    return true;
                case R.id.navigation_chat:
                    startIntent("Chat");
                    return true;
                case R.id.navigation_profile:
                    startIntent("Profile");
                    return true;
            }
            return false;
        }
    };

    @Override
    public void startIntent(String intentName) {
        switch (intentName) {
            case "Questions":
                Log.i("S", "Q");
                startActivity(new Intent(this, QuestionsActivity.class));
                break;
            case "Swipe":
                Log.i("S", "S");
                startActivity(new Intent(this, SwipeActivity.class));
                break;
            case "Profile":
                Log.i("S", "P");
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case "Likes":
                Log.i("S", "L");
                startActivity(new Intent(this, LikesActivity.class));
                break;
            case "Chat":
                Log.i("S", "C");
                startActivity(new Intent(this, ChatActivity.class));
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        mPresenter = new SwipePresenter(this);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.getMenu().findItem(R.id.navigation_swipe).setChecked(true);

        foundUserDislikeBtn = findViewById(R.id.foundUserDislikeBtn);
        foundUserLikeBtn = findViewById(R.id.foundUserLikeBtn);
        foundUserMainStatus = findViewById(R.id.foundUserMainStatus);
        foundUserName = findViewById(R.id.foundUserName);
        foundUserPhoto = findViewById(R.id.foundUserPhoto);
        foundUserReportBtn = findViewById(R.id.foundUserReportBtn);
        userMatchedAnswers1 = findViewById(R.id.userMatchedAnswers1);
        userMatchedAnswers2 = findViewById(R.id.userMatchedAnswers2);
        userMatchedAnswers3 = findViewById(R.id.userMatchedAnswers3);

        mPresenter.startShowing();

        overridePendingTransition(0, 0);
    }


    public void likeUserBtnClck(View view){
        foundUserName.setText("");
        foundUserMainStatus.setText("");
        userMatchedAnswers3.setText("");
        userMatchedAnswers2.setText("");
        userMatchedAnswers1.setText("");
        mPresenter.showInLoop();
    }

    public void dislikeUserBtnClck(View view){
        foundUserName.setText("");
        foundUserMainStatus.setText("");
        userMatchedAnswers3.setText("");
        userMatchedAnswers2.setText("");
        userMatchedAnswers1.setText("");
    }

    @Override
    public void toast(String msg, int time) {

    }

    @Override
    public void setFoundUserInfo(String fuName, String fuStatus, String fuTxt1, String fuTxt2, String fuTxt3) {
        foundUserName.setText(fuName);
        foundUserMainStatus.setText(fuStatus);
        userMatchedAnswers1.setText(fuTxt1);
        userMatchedAnswers2.setText(fuTxt2);
        userMatchedAnswers3.setText(fuTxt3);
    }
}