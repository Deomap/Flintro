package com.deomap.flintro.api;

import com.google.firebase.storage.FirebaseStorage;

public class FirebaseStorageApi {
    com.google.firebase.storage.FirebaseStorage storage;

    public FirebaseStorageApi(){

    }

    public FirebaseStorage FSInstance(){
        return FirebaseStorage.getInstance();
    }
}
