package com.deomap.flintro.api;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseCloudstore {

    FirebaseFirestore db;

    public FirebaseFirestore DBInstance(){ return db = FirebaseFirestore.getInstance(); }

}
