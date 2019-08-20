package com.deomap.flintro.adapter;


import com.deomap.flintro.MainPart.MainOpsModel;

public interface MainPartContract {
    interface View{
    }

    interface ivMainScreen extends View{

    }

    interface Presenter{

    }

    interface  iMainScreenPresenter extends Presenter{
    }

    interface Repository{

    }

    interface iOpsModel extends Repository{
        void registerCallback(MainOpsModel.MyCallback callback);
    }
}
