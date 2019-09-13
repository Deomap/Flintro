package com.deomap.flintro.FirstLaunchActivity;

import com.deomap.flintro.MainPart.MainOpsModel;
import com.deomap.flintro.adapter.MainPartContract;

public class FLPresenter implements MainPartContract.iFLPresenter{
    private MainPartContract.iFLActivity mView;
    private MainPartContract.iOpsModel mRepository;
    public FLPresenter(MainPartContract.iFLActivity view){
        this.mView = view;
        this.mRepository = new MainOpsModel();
    }
}
