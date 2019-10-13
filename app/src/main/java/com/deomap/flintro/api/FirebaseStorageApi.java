package com.deomap.flintro.api;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseStorageApi {
    com.google.firebase.storage.FirebaseStorage storage;

    public FirebaseStorageApi(){

    }

    public FirebaseStorage FSInstance(){
        return FirebaseStorage.getInstance();
    }

    public StorageReference FSReference(){
        return FSInstance().getReference();
    }


}
