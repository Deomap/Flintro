package com.deomap.flintro.ProfileActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.deomap.flintro.ChatActivity.ChatActivity;
import com.deomap.flintro.LikesActivity.LikesActivity;
import com.deomap.flintro.QuestionsActivity.QuestionsActivity;
import com.deomap.flintro.R;
import com.deomap.flintro.SwipeActivity.SwipeActivity;
import com.deomap.flintro.adapter.MainPartContract;
import com.deomap.flintro.api.SharedPreferencesHub;
import com.google.android.material.bottomnavigation.BottomNavigationView;

//активность профиля пользователя
//здесь же можно перейти в настройки и тд
public class ProfileActivity extends AppCompatActivity implements MainPartContract.iProfileActivity {

    ImageButton settingsButton;
    ImageView profilePhoto;
    TextView userName;
    private MainPartContract.iProfilePresenter mPresenter;
    SharedPreferencesHub sph ;

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
        setContentView(R.layout.activity_profile);
        mPresenter = new ProfilePresenter((MainPartContract.iProfileActivity) this);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        settingsButton = findViewById(R.id.settingsButton);
        profilePhoto = findViewById(R.id.profilePhoto);
        userName = findViewById(R.id.userName);
        mPresenter.setupProfile();
        navView.getMenu().findItem(R.id.navigation_profile).setChecked(true);
        overridePendingTransition(0, 0);
    }

    @Override
    public void startIntent(String intentName) {
        switch (intentName) {
            case "Questions":
                Log.i("P", "Q");
                startActivity(new Intent(this, QuestionsActivity.class));
                break;
            case "Swipe":
                Log.i("P", "S");
                startActivity(new Intent(this, SwipeActivity.class));
                break;
            case "Profile":
                Log.i("P", "P");
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case "Likes":
                Log.i("P", "L");
                startActivity(new Intent(this, LikesActivity.class));
                break;
            case "Chat":
                Log.i("P", "C");
                startActivity(new Intent(this, ChatActivity.class));
                break;
        }
    }
    @Override
    public void toast(String msg, int time) {

    }


    @Override
    public void fillProfile(String name) {
        userName.setText(name);
    }
}