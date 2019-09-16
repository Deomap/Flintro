package com.deomap.flintro.QuestionsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.deomap.flintro.FirstLaunchActivity.FLActivity;
import com.deomap.flintro.R;
import com.deomap.flintro.adapter.MainPartContract;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class QuestionsActivity extends AppCompatActivity implements MainPartContract.iQuestionsActivity {

    private MainPartContract.iQuestionsPresenter mPresenter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_swipe:
                    return true;
                case R.id.navigation_likes:
                    return true;
                case R.id.navigation_questions:
                    return true;
                case R.id.navigation_chat:
                    return true;
                case R.id.navigation_profile:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        mPresenter = new QuestionsPresenter(this);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        startActivity(new Intent(this, FLActivity.class));
    }

    @Override
    public void startIntent(String intentName) {

    }

    @Override
    public void toast(String msg, int time) {

    }
}
