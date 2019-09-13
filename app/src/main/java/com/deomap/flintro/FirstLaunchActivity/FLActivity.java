package com.deomap.flintro.FirstLaunchActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.deomap.flintro.R;
import com.deomap.flintro.adapter.MainPartContract;

public class FLActivity extends AppCompatActivity implements MainPartContract.iFLActivity {
    private MainPartContract.iFLPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fl);
        mPresenter = new FLPresenter(this);
    }

    @Override
    public void startIntent(String intentName) {

    }

    @Override
    public void toast(String msg, int time) {

    }
}
