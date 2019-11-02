package com.deomap.flintro.ChatActivity;

import com.deomap.flintro.MainPart.MainOpsModel;
import com.deomap.flintro.adapter.LoginContract;
import com.deomap.flintro.adapter.MainPartContract;
import com.deomap.flintro.login.LoginModel;

//для чего нужен presenter понятно из MainPartContract, но здесь я решил что удобнее будет многие вещи реализовать непосредственно в активности
public class ChatPresenter implements MainPartContract.iChatPresenter {
    private MainPartContract.iChatActivity mView;
    private MainPartContract.iOpsModel mRepository;



    public ChatPresenter(MainPartContract.iChatActivity view){
        this.mView = view;
        this.mRepository = new MainOpsModel();
    }
}
