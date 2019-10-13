package com.deomap.flintro.adapter;


import android.provider.ContactsContract;

import com.deomap.flintro.MainPart.MainOpsModel;

import java.util.ArrayList;

public interface MainPartContract {
    interface View{
        void startIntent(String intentName);
        void toast(String msg, int time);
    }

    interface iChatActivity extends View{

    }

    interface iQuestionsActivity extends View{
        void initiateQuestionsList(ArrayList questionsList);
        void initiateAnswersList(ArrayList answersList);
        void setMainText(String text);
    }

    interface iLikesActivity extends View{
        void setList(ArrayList<String> finalList);
        void likesListClickedNext(int arg_mode);
        void checkBoxesStateChange(String mode);
        void nullFinalList();
    }

    interface iProfileActivity extends View{
        void fillProfile(String userName);
    }

    interface iSwipeActivity extends View{

    }

    interface iFLActivity extends View{
        void askName();
        void askInterests();
        void askPhoto();
        void accessSharedPreferences(String mode, String prefName, String type,String key, String value);
        void changeItemsAvailibility(String arg);
    }

    interface Presenter{

    }

    interface iChatPresenter extends Presenter{

    }

    interface iQuestionsPresenter extends Presenter {
        void getQuestions(int pos);
        void getAnswers(int pos);
        void sendUserAnswer(String answerText);
        void answerClicked(int pos);
    }

    interface iLikesPresenter extends Presenter{
        void getList(int arg);
        void setCBMode(int mode);
        void compileLists(int mode);
        void likesListClicked(int pos);
    }

    interface iProfilePresenter extends Presenter{
        void setupProfile();
    }

    interface iSwipePresenter extends Presenter{

    }

    interface iFLPresenter extends Presenter{
        void initiateNextStage(String name);
        void onPickedInterest(int position);
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
