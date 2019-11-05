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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import com.deomap.flintro.api.FirebaseUsers;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class LikesActivity extends AppCompatActivity implements MainPartContract.iLikesActivity {

    private MainPartContract.iLikesPresenter mPresenter;
    ArrayList<UnitLAA> unitsLAA = new ArrayList<>();
    ArrayList<UnitLAM> unitsLAM = new ArrayList<>();
    ListView unitsList;
    Button iLikeBtn,meLikesBtn,reactionsBtn;
    TabLayout sliding_tabs;
    TextView sadText;
    ImageView sadImage;

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
                //case R.id.navigation_chat:
                    //startIntent("Chat");
                    //return true;
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
        unitsList = findViewById(R.id.likesList);
        TextView tv = findViewById(R.id.toolbarTV1);
        sliding_tabs = findViewById(R.id.sliding_tabs);
        sadText = findViewById(R.id.sadText);
        sadImage = findViewById(R.id.sadImage);

        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.getMenu().findItem(R.id.navigation_likes).setChecked(true);
        overridePendingTransition(0, 0);

        sadText.setVisibility(View.GONE);
        sadImage.setVisibility(View.GONE);

        sliding_tabs.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    meLikesClicked();
                }
                if(tab.getPosition()==1){
                    iLikeClicked();
                }
                if(tab.getPosition()==2){
                    reactionsClicked();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        meLikesClicked();

    }

    @Override
    public void toast(String msg, int time) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    //обработка нажатия кнопки (след. 3 метода)
    //1 - ответы
    public void iLikeClicked() {
        sadText.setVisibility(View.GONE);
        sadImage.setVisibility(View.GONE);
        mPresenter.getList(1);
    }

    //2 - взаимно
    public void meLikesClicked() {
        sadText.setVisibility(View.GONE);
        sadImage.setVisibility(View.GONE);
        mPresenter.getList(2);
    }

    //3 - реакции
    public void reactionsClicked() {
        sadText.setVisibility(View.GONE);
        sadImage.setVisibility(View.GONE);
        mPresenter.getList(3);
    }

    //здесь происходит отображение нужного списка пользователю
    @Override
    public void setList(ArrayList<String> universalList, ArrayList<String> extraInfoList, ArrayList<String> topicsList,ArrayList<String> qIDList,ArrayList<String> uIDList, String arg){
        unitsLAA.clear();
        unitsLAM.clear();
        unitsList.setVisibility(View.GONE);
        if(arg.equals("LAA")) {
            for (int i = 0; i < universalList.size(); i++) {
                unitsLAA.add(new UnitLAA(universalList.get(i), extraInfoList.get(i), topicsList.get(i), qIDList.get(i), uIDList.get(i)));
                Log.i("laa", "@");
            }
            if(universalList.size()!=0) {
                unitsList = (ListView) findViewById(R.id.likesList);
                AdapterLAA adapter = new AdapterLAA(this, R.layout.unit_laa, unitsLAA);
                unitsList.setAdapter(adapter);
                unitsList.setVisibility(View.VISIBLE);
            }
            else{
                sadText.setVisibility(View.VISIBLE);
                sadImage.setVisibility(View.VISIBLE);
            }
        }
        if(arg.equals("LAM")){
            for (int i = 0; i < universalList.size(); i++) {
                unitsLAM.add(new UnitLAM(universalList.get(i),extraInfoList.get(i)));

                Log.i("lam", universalList.get(i)+" , "+extraInfoList.get(i));
            }
            if(universalList.size()!=0) {
                unitsList = (ListView) findViewById(R.id.likesList);
                AdapterLAM adapter = new AdapterLAM(this, R.layout.unit_lam, unitsLAM);
                unitsList.setAdapter(adapter);
                unitsList.setVisibility(View.VISIBLE);
            }
            else{
                sadText.setVisibility(View.VISIBLE);
                sadImage.setVisibility(View.VISIBLE);
            }
        }
    }
}