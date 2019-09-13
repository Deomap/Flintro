package com.deomap.flintro.SwipeActivity;

import com.deomap.flintro.MainPart.MainOpsModel;
import com.deomap.flintro.adapter.MainPartContract;

public class SwipePresenter implements MainPartContract.iSwipePresenter {
    private MainPartContract.iSwipeActivity mView;
    private MainPartContract.iOpsModel mRepository;
    public SwipePresenter(MainPartContract.iSwipeActivity view){
        this.mView = view;
        this.mRepository = new MainOpsModel();
    }
}
