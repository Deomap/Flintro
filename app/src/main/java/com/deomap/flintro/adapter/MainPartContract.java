package com.deomap.flintro.adapter;


import android.provider.ContactsContract;

import com.deomap.flintro.MainPart.MainOpsModel;

public interface MainPartContract {
    interface View{
        void startIntent(String intentName);
        void toast(String msg, int time);
    }

    interface iChatActivity extends View{

    }

    interface iQuestionsActivity extends View{

    }

    interface iLikesActivity extends View{

    }

    interface iProfileActivity extends View{

    }

    interface iSwipeActivity extends View{

    }

    interface iFLActivity extends View{

    }

    interface Presenter{

    }

    interface iChatPresenter extends Presenter{

    }

    interface iQuestionsPresenter extends Presenter {

    }

    interface iLikesPresenter extends Presenter{

    }

    interface iProfilePresenter extends Presenter{

    }

    interface iSwipePresenter extends Presenter{

    }

    interface iFLPresenter extends Presenter{

    }

    interface Repository{

    }

    interface iOpsModel extends Repository{
        void registerCallback(MainOpsModel.MyCallback callback);
        void readQuestions(String collection, int extra);
        void downloadPhoto(String userID, int WHAT);
        void getUserInfo(String userID, String infoType);
    }

}
