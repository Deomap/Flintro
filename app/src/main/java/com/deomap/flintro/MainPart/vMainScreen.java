package com.deomap.flintro.MainPart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.deomap.flintro.R;
import com.deomap.flintro.adapter.MainPartContract;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class vMainScreen extends AppCompatActivity implements MainPartContract.ivMainScreen {

    private MainPartContract.iMainScreenPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v_main_screen);

        mPresenter = new MainScreenPresenter(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cities = db.collection("questions");
        Log.i("SS","GO FUCK YOURSELF");
        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "San Francisco");
        data1.put("state", "CA");
        data1.put("country", "USA");
        data1.put("capital", false);
        data1.put("population", 860000);
        data1.put("regions", Arrays.asList("west_coast", "norcal"));
        cities.document("howOldAreYou").set(data1);
    }
}
