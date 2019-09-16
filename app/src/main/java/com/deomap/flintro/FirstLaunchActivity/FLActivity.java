package com.deomap.flintro.FirstLaunchActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.deomap.flintro.R;
import com.deomap.flintro.adapter.MainPartContract;

public class FLActivity extends AppCompatActivity implements MainPartContract.iFLActivity{
    private MainPartContract.iFLPresenter mPresenter;
    EditText editText1;
    TextView textView1 ;
    Button nextButton;
    GridView interestsGrid;

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

    }

    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            textView1.setText(position+" "+id);

        }
    };

    @Override
    public void startIntent(String intentName) {

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

    public void goNextStageClicked(View view){
        mPresenter.initiateNextStage(editText1.getText().toString());
    }
}
