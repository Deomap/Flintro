package com.deomap.flintro.LikesActivity;

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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.deomap.flintro.ChatActivity.ChatActivity;
import com.deomap.flintro.ProfileActivity.ProfileActivity;
import com.deomap.flintro.QuestionsActivity.QuestionsActivity;
import com.deomap.flintro.R;
import com.deomap.flintro.SwipeActivity.SwipeActivity;
import com.deomap.flintro.adapter.MainPartContract;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class LikesActivity extends AppCompatActivity implements MainPartContract.iLikesActivity{

    private MainPartContract.iLikesPresenter mPresenter;
    private Button meLikesButton;
    private Button iLikeButton;
    private Button reactionsButton;
    private ListView likesList;
    private CheckBox stOptionCB;
    private CheckBox ndOptionCB;
    private int st_cb_state = 1;
    private int nd_cb_state = 1;
    private int toPresenterCBMode = 3;
    private int arg_mode;

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
        meLikesButton = findViewById(R.id.meLikesButton);
        reactionsButton = findViewById(R.id.reactionsButton);
        iLikeButton = findViewById(R.id.iLikeButton);
        likesList = findViewById(R.id.likesList);
        stOptionCB = findViewById(R.id.stOptionCB);
        ndOptionCB = findViewById(R.id.ndOptionCB);

        stOptionCB.setEnabled(false);
        stOptionCB.setVisibility(View.GONE);
        ndOptionCB.setEnabled(false);
        ndOptionCB.setVisibility(View.GONE);

        stOptionCB.setChecked(false);
        ndOptionCB.setChecked(true);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.getMenu().findItem(R.id.navigation_likes).setChecked(true);
        overridePendingTransition(0, 0);
    }

    @Override
    public void toast(String msg, int time) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    private void defineState(){
        if(stOptionCB.isChecked() && ndOptionCB.isChecked()){
            toPresenterCBMode = 1;
        }
        if(stOptionCB.isChecked() && !ndOptionCB.isChecked()){
            toPresenterCBMode = 2;
        }
        if(!stOptionCB.isChecked() && ndOptionCB.isChecked()){
            toPresenterCBMode = 3;
        }
        if(!stOptionCB.isChecked() && !ndOptionCB.isChecked()){
            toPresenterCBMode = 4;
        }
    }

    //1
    public void iLikeClicked(View view) {
        setDefaultCBState();
        defineState();
        mPresenter.getList(1);
    }

    //2
    public void meLikesClicked(View view) {
        setDefaultCBState();
        defineState();
        mPresenter.getList(2);
    }

    //3
    public void reactionsClicked(View view) {
        setDefaultCBState();
        defineState();
        mPresenter.getList(3);
    }

    public void cbStateChanged(View view){
        defineState();
        mPresenter.compileLists(toPresenterCBMode);

    }

    private void setDefaultCBState(){
        toPresenterCBMode = 3;
        stOptionCB.setChecked(false);
        ndOptionCB.setChecked(true);
    }

    private void ifCBEnableSet(boolean ifEnable){

    }

    ListView.OnItemClickListener likesListOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i("LA/likesList.OICL","clicked");
            //pass
        }
    };

    @Override
    public void setList(ArrayList<String> finalList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, finalList);
        likesList.setAdapter(adapter);
        likesList.setOnItemClickListener(likesListOnItemClickListener);
    }

    @Override
    public void likesListClickedNext(int arg_mode) {
    }

    @Override
    public void checkBoxesStateChange(String mode) {

    }

    @Override
    public void nullFinalList() {

    }

    @Override
    public void setCB(int arg) {
        if(arg == 2){
            stOptionCB.setText("Взаимно");
            ndOptionCB.setText("Не взаимно");
            stOptionCB.setEnabled(true);
            stOptionCB.setVisibility(View.VISIBLE);
            ndOptionCB.setEnabled(true);
            ndOptionCB.setVisibility(View.VISIBLE);
        }
        if(arg == 1){
            stOptionCB.setText("Ответы");
            ndOptionCB.setText("Люди");
            stOptionCB.setEnabled(true);
            stOptionCB.setVisibility(View.VISIBLE);
            ndOptionCB.setEnabled(true);
            ndOptionCB.setVisibility(View.VISIBLE);
        }
        if(arg == 3){
            stOptionCB.setEnabled(false);
            stOptionCB.setVisibility(View.GONE);
            ndOptionCB.setEnabled(false);
            ndOptionCB.setVisibility(View.GONE);
        }

    }
}

