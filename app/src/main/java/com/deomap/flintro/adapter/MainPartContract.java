package com.deomap.flintro.adapter;


import android.provider.ContactsContract;

import com.deomap.flintro.MainPart.MainOpsModel;

import java.util.ArrayList;

//зачем это подробно написано в LoginContract
//единственная проблема - эту часть было писать значительно сложнее и в итоге я решил все общение с БД перевести в Presenter'ы  :(
public interface MainPartContract {
    interface View{
        void startIntent(String intentName);
        void toast(String msg, int time);
    }

    interface iChatActivity extends View{

    }

    interface iQuestionsActivity extends View{
        void initiateQuestionsList(ArrayList questionsList);
        void initiateAnswersList(ArrayList answersList, ArrayList ansFinalList);
        void setMainText(String text);
        void itemsAvailibilitySet(int stage);
    }

    interface iLikesActivity extends View{
        void setList(ArrayList<String> universalList, ArrayList<String> extraInfoList, ArrayList<String> topicsList, ArrayList<String> qIDList, ArrayList<String> uIDList, String arg);
    }

    interface iProfileActivity extends View{
        void fillProfile(String userName);
        void loadPhoto();
    }

    interface iSwipeActivity extends View{
        void setFoundUserInfo(String fuName, String fuStatus, String fuTxt1,String fuTxt2,String fuTxt3);
    }

    interface iFLActivity extends View{
        void askName();
        void askInterests();
        void askPhoto();
        void askSex();
        void askQ1m(String txt);
        void askQ2(String txt);
        void askQ3(String txt);
        void accessSharedPreferences(String mode, String prefName, String type,String key, String value);
        void changeItemsAvailibility(String arg);
        void uploadImage();
    }

    interface Presenter{

    }

    interface iChatPresenter extends Presenter{

    }

    interface iQuestionsPresenter extends Presenter {
        void getQuestions(int pos, int fromLA);
        void getAnswers(int pos, String fromWho);
        void sendUserAnswer(String answerText);
        void backStage();
        void fromLikesActivity(int pos, String qID);
    }

    interface iLikesPresenter extends Presenter{
        void getList(int arg);
    }

    interface iProfilePresenter extends Presenter{
        void setupProfile();
    }

    interface iSwipePresenter extends Presenter{
        void startShowing();
        void showInLoop();
        void liked();
        void disliked();
    }

    interface iFLPresenter extends Presenter{
        void initiateNextStage(String name);
        void onPickedInterest(int position);
        void setTextFromET(String text);
        void setPhotoDownloadedTrue();
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
