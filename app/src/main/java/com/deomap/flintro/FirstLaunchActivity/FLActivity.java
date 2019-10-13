package com.deomap.flintro.FirstLaunchActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.deomap.flintro.QuestionsActivity.QuestionsActivity;
import com.deomap.flintro.R;
import com.deomap.flintro.adapter.ImageTextAdapter;
import com.deomap.flintro.adapter.MainPartContract;
import com.deomap.flintro.api.SharedPreferencesHub;

public class FLActivity extends AppCompatActivity implements MainPartContract.iFLActivity{
    private MainPartContract.iFLPresenter mPresenter;
    private EditText editText1;
    private TextView textView1 ;
    private Button nextButton;
    private GridView interestsGrid;
    private SharedPreferencesHub sph ;
    private Button downloadPhotoButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fl);
        mPresenter = new FLPresenter(this);
        editText1 = findViewById(R.id.editText1);
        textView1 = findViewById(R.id.textView1);
        nextButton = findViewById(R.id.nextButton);
        interestsGrid = findViewById(R.id.interestsGrid);
        downloadPhotoButton = findViewById(R.id.downloadPhotoButton);

        //mPresenter.initiateNextStage("");
        interestsGrid.setAdapter(new ImageTextAdapter(this));
        interestsGrid.setOnItemClickListener(gridviewOnItemClickListener);

        sph = new SharedPreferencesHub(this);
    }

    @Override
    public void changeItemsAvailibility(String arg){
        if(arg.equals("onCreate")){
            editText1.setVisibility(View.GONE);
            editText1.setEnabled(false);
            textView1.setVisibility(View.GONE);
            textView1.setEnabled(false);
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setEnabled(true);
            interestsGrid.setVisibility(View.GONE);
            interestsGrid.setEnabled(false);
            downloadPhotoButton.setVisibility(View.GONE);
            downloadPhotoButton.setEnabled(false);
        }
        if(arg.equals("name")){
            editText1.setVisibility(View.VISIBLE);
            editText1.setEnabled(true);
            textView1.setVisibility(View.VISIBLE);
            textView1.setEnabled(true);
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setEnabled(true);
            interestsGrid.setVisibility(View.GONE);
            interestsGrid.setEnabled(true);
            downloadPhotoButton.setVisibility(View.GONE);
            downloadPhotoButton.setEnabled(false);
        }
        if(arg.equals("photo")){
            editText1.setVisibility(View.GONE);
            editText1.setEnabled(false);
            textView1.setVisibility(View.VISIBLE);
            textView1.setEnabled(true);
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setEnabled(true);
            interestsGrid.setVisibility(View.GONE);
            interestsGrid.setEnabled(false);
            downloadPhotoButton.setVisibility(View.VISIBLE);
            downloadPhotoButton.setEnabled(true);
        }
        if(arg.equals("interests")){
            editText1.setVisibility(View.GONE);
            editText1.setEnabled(false);
            textView1.setVisibility(View.VISIBLE);
            textView1.setEnabled(true);
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setEnabled(true);
            interestsGrid.setVisibility(View.VISIBLE);
            interestsGrid.setEnabled(true);
            downloadPhotoButton.setVisibility(View.GONE);
            downloadPhotoButton.setEnabled(false);
        }
        if(arg.equals("finish")){
            editText1.setVisibility(View.GONE);
            editText1.setEnabled(false);
            textView1.setVisibility(View.GONE);
            textView1.setEnabled(false);
            nextButton.setVisibility(View.GONE);
            nextButton.setEnabled(false);
            interestsGrid.setVisibility(View.GONE);
            interestsGrid.setEnabled(false);
            downloadPhotoButton.setVisibility(View.GONE);
            downloadPhotoButton.setEnabled(false);
        }
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
