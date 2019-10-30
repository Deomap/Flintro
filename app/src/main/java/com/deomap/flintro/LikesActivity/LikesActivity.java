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
import com.deomap.flintro.adapter.Adapter_LA_Answers.AdapterLAA;
import com.deomap.flintro.adapter.Adapter_LA_Answers.UnitLAA;
import com.deomap.flintro.adapter.Apadter_LA_Matches.AdapterLAM;
import com.deomap.flintro.adapter.Apadter_LA_Matches.UnitLAM;
import com.deomap.flintro.adapter.MainPartContract;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class LikesActivity extends AppCompatActivity implements MainPartContract.iLikesActivity {

    private MainPartContract.iLikesPresenter mPresenter;
    private Button meLikesButton;
    private Button iLikeButton;
    private Button reactionsButton;
    //private ListView likesList;
    private CheckBox stOptionCB;
    private CheckBox ndOptionCB;
    private int st_cb_state = 1;
    private int nd_cb_state = 1;
    private int toPresenterCBMode = 3;
    private int arg_mode;

    ArrayList<UnitLAA> unitsLAA = new ArrayList<>();
    ArrayList<UnitLAM> unitsLAM = new ArrayList<>();
    ListView unitsList;

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
        unitsList = findViewById(R.id.likesList);
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
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
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

    ListView.OnItemClickListener likesListOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i("LA/likesList.OICL", "clicked");
            //pass
        }
    };

    @Override
    public void setList(ArrayList<String> universalList, ArrayList<String> extraInfoList, ArrayList<String> topicsList,ArrayList<String> qIDList,ArrayList<String> uIDList, String arg){
        unitsLAA.clear();
        unitsLAM.clear();
        unitsList.setVisibility(View.GONE);
        Log.i("yaya","setlist");
        if(arg.equals("LAA")) {
            for (int i = 0; i < universalList.size(); i++) {
                unitsLAA.add(new UnitLAA(universalList.get(i), extraInfoList.get(i), topicsList.get(i), qIDList.get(i), uIDList.get(i)));
                Log.i("wtf2", "@");
            }
            if(universalList.size()!=0) {
                unitsList = (ListView) findViewById(R.id.likesList);
                AdapterLAA adapter = new AdapterLAA(this, R.layout.unit_laa, unitsLAA);
                unitsList.setAdapter(adapter);
                unitsList.setVisibility(View.VISIBLE);
            }
        }
        if(arg.equals("LAM")){
            for (int i = 0; i < universalList.size(); i++) {
                unitsLAM.add(new UnitLAM(universalList.get(i),extraInfoList.get(i)));
                Log.i("wtf3", "@");
            }
            if(universalList.size()!=0) {
                unitsList = (ListView) findViewById(R.id.likesList);
                AdapterLAM adapter = new AdapterLAM(this, R.layout.unit_lam, unitsLAM);
                unitsList.setAdapter(adapter);
                unitsList.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void likesListClickedNext(int arg_mode) {
    }

    @Override
    public void nullFinalList() {

    }





    //(not)USING CB's ::

    private void defineState() {
        if (stOptionCB.isChecked() && ndOptionCB.isChecked()) {
            toPresenterCBMode = 1;
        }
        if (stOptionCB.isChecked() && !ndOptionCB.isChecked()) {
            toPresenterCBMode = 2;
        }
        if (!stOptionCB.isChecked() && ndOptionCB.isChecked()) {
            toPresenterCBMode = 3;
        }
        if (!stOptionCB.isChecked() && !ndOptionCB.isChecked()) {
            toPresenterCBMode = 4;
        }
    }

    @Override
    public void setCB(int arg) {

        if (arg == 2) {
            stOptionCB.setText("Взаимно");
            ndOptionCB.setText("Не взаимно");
            stOptionCB.setEnabled(true);
            stOptionCB.setVisibility(View.VISIBLE);
            ndOptionCB.setEnabled(true);
            ndOptionCB.setVisibility(View.VISIBLE);
        }
        if (arg == 1) {
            stOptionCB.setText("Ответы");
            ndOptionCB.setText("Люди");
            stOptionCB.setEnabled(true);
            stOptionCB.setVisibility(View.VISIBLE);
            ndOptionCB.setEnabled(true);
            ndOptionCB.setVisibility(View.VISIBLE);
        }
        if (arg == 3) {
            stOptionCB.setEnabled(false);
            stOptionCB.setVisibility(View.GONE);
            ndOptionCB.setEnabled(false);
            ndOptionCB.setVisibility(View.GONE);
        }

    }


    @Override
    public void checkBoxesStateChange(String mode) {

    }

    public void cbStateChanged(View view) {
        defineState();
        mPresenter.compileListsOLD(toPresenterCBMode);

    }

    private void setDefaultCBState() {
        toPresenterCBMode = 3;
        stOptionCB.setChecked(false);
        ndOptionCB.setChecked(true);
    }

    private void ifCBEnableSet(boolean ifEnable) {

    }

}