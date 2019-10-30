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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.deomap.flintro.ChatActivity.ChatActivity;
import com.deomap.flintro.LikesActivity.LikesActivity;
import com.deomap.flintro.ProfileActivity.ProfileActivity;
import com.deomap.flintro.R;
import com.deomap.flintro.SwipeActivity.SwipeActivity;
import com.deomap.flintro.adapter.ImageTextAdapter;
import com.deomap.flintro.adapter.MainPartContract;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import io.opencensus.tags.Tag;

public class QuestionsActivity extends AppCompatActivity implements MainPartContract.iQuestionsActivity {

    private MainPartContract.iQuestionsPresenter mPresenter;
    private TextView mainText;
    private EditText userAnswer;
    private ImageView userAnswerButton;
    private ImageView backButton;
    private GridView topicsGrid;
    private ListView questionsList;
    private ListView answersList;

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
        mainText = findViewById(R.id.mainText);
        userAnswer = findViewById(R.id.userAnswer);
        userAnswerButton = findViewById(R.id.userAnswerButton);
        backButton = findViewById(R.id.backButton);
        //SHOULD BE IN FRAGMENTS:
        answersList = findViewById(R.id.answersList);
        topicsGrid = findViewById(R.id.interestsGrid);
        topicsGrid.setOnItemClickListener(gridviewOnItemClickListener);
        topicsGrid.setAdapter(new ImageTextAdapter(this));
        answersList.setOnItemClickListener(answersListviewOnItemClickListener);
        questionsList = findViewById(R.id.questionsList);
        questionsList.setOnItemClickListener(questionsListviewOnItemClickListener);


        Intent intent   = getIntent();
        if(intent.hasExtra("fromLAPos")){
            mPresenter.fromLikesActivity(intent.getIntExtra("fromLAPos",-1), intent.getStringExtra("fromLAQID"));
        }


        itemsAvailibilitySet(0);
        //
        overridePendingTransition(0, 0);
    }

    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            mPresenter.getQuestions(position,0);
            Log.i("okk","!!!!!!!!!!");

        }
    };

    ListView.OnItemClickListener questionsListviewOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i("QA/questionsList.OICL","clicked");
            mPresenter.getAnswers(position, "fromQA");
        }
    };

    ListView.OnItemClickListener answersListviewOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i("QA/answersList.OICL","clicked");
            mPresenter.answerClicked(position);
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
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void initiateQuestionsList(ArrayList questionsArray) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, questionsArray);
        questionsList.setAdapter(adapter);
    }

    @Override
    public void initiateAnswersList(ArrayList answersArray) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, answersArray);
        answersList.setAdapter(adapter);
    }

    @Override
    public void setMainText(String text){
        mainText.setText(text);
    }

    @Override
    public void itemsAvailibilitySet(int stage) {
        if(stage == 0){
            topicsGrid.setVisibility(View.VISIBLE);
            topicsGrid.setEnabled(true);
            questionsList.setVisibility(View.GONE);
            questionsList.setEnabled(false);
            answersList.setVisibility(View.GONE);
            answersList.setEnabled(false);
            userAnswer.setVisibility(View.GONE);
            userAnswer.setEnabled(false);
            userAnswerButton.setVisibility(View.GONE);
            userAnswerButton.setEnabled(false);
            backButton.setVisibility(View.GONE);
            backButton.setEnabled(false);
        }
        if(stage == 1){
            topicsGrid.setVisibility(View.GONE);
            topicsGrid.setEnabled(false);
            questionsList.setVisibility(View.VISIBLE);
            questionsList.setEnabled(true);
            answersList.setVisibility(View.GONE);
            answersList.setEnabled(false);
            userAnswer.setVisibility(View.GONE);
            userAnswer.setEnabled(false);
            userAnswerButton.setVisibility(View.GONE);
            userAnswerButton.setEnabled(false);
            backButton.setVisibility(View.VISIBLE);
            backButton.setEnabled(true);
        }
        if(stage == 2){
            topicsGrid.setVisibility(View.GONE);
            topicsGrid.setEnabled(false);
            questionsList.setVisibility(View.GONE);
            questionsList.setEnabled(false);
            answersList.setVisibility(View.VISIBLE);
            answersList.setEnabled(true);
            userAnswer.setVisibility(View.VISIBLE);
            userAnswer.setEnabled(true);
            userAnswerButton.setVisibility(View.VISIBLE);
            userAnswerButton.setEnabled(true);
            backButton.setVisibility(View.VISIBLE);
            backButton.setEnabled(true);
        }

    }

    public void backButtonClicked(View view){
        mPresenter.backStage();
    }

    public void userAnswerButtonClicked(View view){
        mPresenter.sendUserAnswer(userAnswer.getText().toString());
    }



}
