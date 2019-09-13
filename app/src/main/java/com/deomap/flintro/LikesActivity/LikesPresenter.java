package com.deomap.flintro.LikesActivity;

import com.deomap.flintro.MainPart.MainOpsModel;
import com.deomap.flintro.adapter.MainPartContract;

public class LikesPresenter implements MainPartContract.iLikesPresenter{
    private MainPartContract.iLikesActivity mView;
    private MainPartContract.iOpsModel mRepository;
    public LikesPresenter(MainPartContract.iLikesActivity view){
        this.mView = view;
        this.mRepository = new MainOpsModel();
    }
}
