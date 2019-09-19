package com.deomap.flintro.FirstLaunchActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.deomap.flintro.QuestionsActivity.QuestionsActivity;
import com.deomap.flintro.R;
import com.deomap.flintro.adapter.MainPartContract;
import com.deomap.flintro.api.SharedPreferencesHub;

public class FLActivity extends AppCompatActivity implements MainPartContract.iFLActivity{
    private MainPartContract.iFLPresenter mPresenter;
    EditText editText1;
    TextView textView1 ;
    Button nextButton;
    GridView interestsGrid;
    SharedPreferencesHub sph ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fl);
        mPresenter = new FLPresenter(this);
        editText1 = findViewById(R.id.editText1);
        textView1 = findViewById(R.id.textView1);
        nextButton = findViewById(R.id.nextButton);
        interestsGrid = findViewById(R.id.interestsGrid);
        mPresenter.initiateNextStage("");
        interestsGrid.setAdapter(new ImageTextAdapter(this));
        interestsGrid.setOnItemClickListener(gridviewOnItemClickListener);
        editText1.setVisibility(View.VISIBLE);
        sph = new SharedPreferencesHub(this);
    }

    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            Log.i("kk","!!!!!!!!!!");
            textView1.setText(position+" "+id);
            mPresenter.onPickedInterest(position);

        }
    };

    @Override
    public void startIntent(String intentName) {
        if(intentName.equals("Questions")){
            startActivity(new Intent(this, QuestionsActivity.class));
        }
    }

    @Override
    public void toast(String msg, int time) {

    }

    @Override
    public void askName() {
        textView1.setText("Как вас зовут?");
    }

    @Override
    public void askInterests() {
        textView1.setText("Укажите ваши интересы:");
        editText1.setVisibility(View.INVISIBLE);
        editText1.setEnabled(false);
    }

    @Override
    public void askPhoto() {

    }

    public void goNextStageClicked(View view){
        mPresenter.initiateNextStage(editText1.getText().toString());
    }

    @Override
    public void accessSharedPreferences(String mode, String prefName, String type,String key, String value){
        if(mode.equals("put")){
            sph.setPrefs(prefName,type,key,value);
        }
        if(mode.equals("get")){
            switch (type){
                case "String":
                    sph.getStringSP(prefName,key);
            }
        }
    }
}
