package com.deomap.flintro.api;

import com.google.firebase.firestore.FirebaseFirestore;

//как и всё в папке api, нужен для упрощения жизни (обращаюсь к красиво названному методы и получаю что надо из FirebaseFirestore)
public class FirebaseCloudstore {
    FirebaseFirestore db;

    public FirebaseFirestore DBInstance(){ return db = FirebaseFirestore.getInstance(); }

}
