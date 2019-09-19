package com.deomap.flintro.ProfileActivity;

import com.deomap.flintro.MainActivity;
import com.deomap.flintro.MainPart.MainOpsModel;
import com.deomap.flintro.adapter.MainPartContract;
import com.deomap.flintro.api.SharedPreferencesHub;

public class ProfilePresenter implements MainPartContract.iProfilePresenter{
    private MainPartContract.iProfileActivity mView;
    private MainPartContract.iOpsModel mRepository;

    SharedPreferencesHub sph = new SharedPreferencesHub(MainActivity.getContextOfApplication());

    public ProfilePresenter(MainPartContract.iProfileActivity view){
        this.mView = view;
        this.mRepository = new MainOpsModel();
        
    }


    @Override
    public void setupProfile() {
        String userName = sph.getStringSP("userProfile", "userName");
        mView.fillProfile(userName);
    }


}
