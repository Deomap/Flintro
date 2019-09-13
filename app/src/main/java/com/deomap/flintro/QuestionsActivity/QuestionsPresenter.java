package com.deomap.flintro.QuestionsActivity;

import com.deomap.flintro.MainPart.MainOpsModel;
import com.deomap.flintro.adapter.MainPartContract;

public class QuestionsPresenter implements MainPartContract.iQuestionsPresenter {
    private MainPartContract.iQuestionsActivity mView;
    private MainPartContract.iOpsModel mRepository;
    public QuestionsPresenter(MainPartContract.iQuestionsActivity view){
        this.mView = view;
        this.mRepository = new MainOpsModel();
    }
}
