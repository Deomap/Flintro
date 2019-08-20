package com.deomap.flintro.MainPart;

import com.deomap.flintro.adapter.MainPartContract;

public class MainOpsModel implements MainPartContract.iOpsModel {

    private MyCallback callback;

    @Override
    public void registerCallback(MyCallback callback) { this.callback = callback;
    }

    public interface MyCallback{

    }

}
