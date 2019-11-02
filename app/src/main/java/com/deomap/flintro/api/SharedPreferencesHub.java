package com.deomap.flintro.api;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

//нужно для удобной работы с внутренним хранилещем данных приложения
//этим можно оптимизировать приложение от лишних обращений в сеть, но этого я не пока не сделал
public class SharedPreferencesHub {
    Context context;

public SharedPreferencesHub(Context context){
    this.context = context;
}

SharedPreferences.Editor editor;
SharedPreferences prefs;

//чтение информации
    public String getStringSP(String prefName, String key){
        prefs = context.getSharedPreferences(prefName, MODE_PRIVATE);
        String needed =prefs.getString(key,"Nulled");
        return needed;
    }

    //запись информации
    public void setPrefs(String prefName,String type, String key, String value){
        editor = context.getSharedPreferences(prefName,MODE_PRIVATE).edit();
        switch (type){
            case "String":
                editor.putString(key,value);
        }
        editor.apply();
    }
}
