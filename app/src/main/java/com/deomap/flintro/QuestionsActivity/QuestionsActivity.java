package com.deomap.flintro.QuestionsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.deomap.flintro.ChatActivity.ChatActivity;
import com.deomap.flintro.LikesActivity.LikesActivity;
import com.deomap.flintro.ProfileActivity.ProfileActivity;
import com.deomap.flintro.R;
import com.deomap.flintro.SwipeActivity.SwipeActivity;
import com.deomap.flintro.adapter.ImageTextAdapter;
import com.deomap.flintro.adapter.MainPartContract;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class QuestionsActivity extends AppCompatActivity implements MainPartContract.iQuestionsActivity {

    private MainPartContract.iQuestionsPresenter mPresenter;
    private GridView topicsGrid;
    private ListView questionsList;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        mPresenter = new QuestionsPresenter(this);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.getMenu().findItem(R.id.navigation_questions).setChecked(true);
        //SHOULD BE IN FRAGMENTS:
        topicsGrid = findViewById(R.id.interestsGrid);
        topicsGrid.setOnItemClickListener(gridviewOnItemClickListener);
        topicsGrid.setAdapter(new ImageTextAdapter(this));
        questionsList = findViewById(R.id.questionsList);
        //
        overridePendingTransition(0, 0);
    }

    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            mPresenter.getQuestions(position);
            Log.i("okk","!!!!!!!!!!");

        }
    };

    @Override
    public void startIntent(String intentName) {
        switch (intentName) {
            case "Questions":
                Log.i("Q", "Q");
                startActivity(new Intent(this, QuestionsActivity.class));
                break;
            case "Swipe":
                Log.i("Q", "S");
                startActivity(new Intent(this, SwipeActivity.class));
                break;
            case "Profile":
                Log.i("Q", "P");
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case "Likes":
                Log.i("Q", "L");
                startActivity(new Intent(this, LikesActivity.class));
                break;
            case "Chat":
                Log.i("Q", "C");
                startActivity(new Intent(this, ChatActivity.class));
                break;
        }
    }

    @Override
    public void toast(String msg, int time) {

    }

    @Override
    public void initiateQuestionsList(String[] questionsArray) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, questionsArray);
        questionsList.setAdapter(adapter);
    }
}
