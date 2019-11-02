package com.deomap.flintro.login;

import android.content.Intent;

import com.deomap.flintro.api.FirebaseUsers;
import com.google.firebase.auth.FirebaseAuth;

//используется только MainActivity 1 раз, можно наполнять
//здесь проверяется отмечено ли существование пользователя в Firebase
public class appStarted {

    FirebaseUsers fbu = new FirebaseUsers();

    public int requestForUsing(){
        if(fbu.curUser() != null){
            return 1;
        }
        return 0;
    }

}
