package com.deomap.flintro.api;
//здесь хранится ключ для чата, чтобы при нажатии на ChatActivity пользователь смог перейти к диалогу с последним пользователем
public class chatKeyStored {

    public String key = "null";

    public void setKey(String key){
        this.key=key;
    }

    public String getKey(){
        return this.key;
    }

    public chatKeyStored(){

    }
}
