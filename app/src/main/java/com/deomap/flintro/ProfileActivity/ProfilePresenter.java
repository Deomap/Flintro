package com.deomap.flintro.ProfileActivity;

import com.deomap.flintro.MainPart.MainOpsModel;
import com.deomap.flintro.adapter.MainPartContract;

public class ProfilePresenter implements MainPartContract.iProfilePresenter{
    private MainPartContract.iProfileActivity mView;
    private MainPartContract.iOpsModel mRepository;
    public ProfilePresenter(MainPartContract.iProfileActivity view){
        this.mView = view;
        this.mRepository = new MainOpsModel();
    }
}
