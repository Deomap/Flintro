package com.deomap.flintro.ChatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.deomap.flintro.LikesActivity.LikesActivity;
import com.deomap.flintro.ProfileActivity.ProfileActivity;
import com.deomap.flintro.QuestionsActivity.QuestionsActivity;
import com.deomap.flintro.R;
import com.deomap.flintro.SwipeActivity.SwipeActivity;
import com.deomap.flintro.adapter.MainPartContract;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChatActivity extends AppCompatActivity implements MainPartContract.iChatActivity{

    private MainPartContract.iChatPresenter mPresenter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_swipe:
                    startActivity(new Intent(ChatActivity.this, SwipeActivity.class));
                    return true;
                case R.id.navigation_likes:
                    startActivity(new Intent(ChatActivity.this, LikesActivity.class));
                    return true;
                case R.id.navigation_questions:
                    startActivity(new Intent(ChatActivity.this, QuestionsActivity.class));
                    return true;
                case R.id.navigation_chat:
                    return true;
                case R.id.navigation_profile:
                    startActivity(new Intent(ChatActivity.this, ProfileActivity.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mPresenter = new ChatPresenter(this);
    }

    @Override
    public void startIntent(String intentName) {

    }

    @Override
    public void toast(String msg, int time) {

    }
}

