package com.deomap.flintro.LikesActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.deomap.flintro.ChatActivity.ChatActivity;
import com.deomap.flintro.ProfileActivity.ProfileActivity;
import com.deomap.flintro.QuestionsActivity.QuestionsActivity;
import com.deomap.flintro.R;
import com.deomap.flintro.SwipeActivity.SwipeActivity;
import com.deomap.flintro.adapter.FirestoreDataTranslator;
import com.deomap.flintro.adapter.MainPartContract;
import com.deomap.flintro.api.FirebaseCloudstore;
import com.deomap.flintro.api.FirebaseUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LikesActivity extends AppCompatActivity implements MainPartContract.iLikesActivity{

    private MainPartContract.iLikesPresenter mPresenter;
    private Button answersLikesList;
    private Button swipeLikesList;
    private Button matchesLikesList;
    private ListView likesList;

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
                Log.i("LA", "Q");
                startActivity(new Intent(this, QuestionsActivity.class));
                break;
            case "Swipe":
                Log.i("L", "S");
                startActivity(new Intent(this, SwipeActivity.class));
                break;
            case "Profile":
                Log.i("L", "P");
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case "Likes":
                Log.i("L", "L");
                startActivity(new Intent(this, LikesActivity.class));
                break;
            case "Chat":
                Log.i("L", "C");
                startActivity(new Intent(this, ChatActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        mPresenter = new LikesPresenter(this);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        answersLikesList = findViewById(R.id.answersLikesButton);
        matchesLikesList = findViewById(R.id.matchesLikesButton);
        swipeLikesList = findViewById(R.id.swipeLikesButton);
        likesList = findViewById(R.id.likesList);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.getMenu().findItem(R.id.navigation_likes).setChecked(true);
        overridePendingTransition(0, 0);
    }

    @Override
    public void toast(String msg, int time) {

    }

    //1
    public void swipeLikesClicked(View view) {
        mPresenter.getList(1);

    }

    //2
    public void answersLikesClicked(View view) {
        mPresenter.getList(2);

    }

    //3
    public void matchesLikesClicked(View view) {
        mPresenter.getList(3);

    }
}

