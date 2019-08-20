package com.deomap.flintro.MainPart;

import com.deomap.flintro.adapter.MainPartContract;

public class MainScreenPresenter implements MainPartContract.iMainScreenPresenter, MainOpsModel.MyCallback {

    private MainPartContract.iOpsModel mRepository;
    private MainPartContract.ivMainScreen mView;


    public MainScreenPresenter(MainPartContract.ivMainScreen view){
        this.mView = view;
        this.mRepository = new MainOpsModel();
    }
}
