package com.deomap.flintro.ProfileActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deomap.flintro.R;
import com.deomap.flintro.SwipeActivity.SwipeActivity;
import com.deomap.flintro.api.FirebaseCloudstore;
import com.deomap.flintro.api.FirebaseUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class OtherProfileActivity extends AppCompatActivity {

    ImageView profilePhoto;
    TextView userName,mainA,mainQ;
    androidx.appcompat.widget.Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        profilePhoto = findViewById(R.id.profilePhoto);
        userName = findViewById(R.id.userName);
        mainA = findViewById(R.id.mainA);
        mainQ = findViewById(R.id.mainQ);

        myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Профиль");
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        getName(intent.getStringExtra("uid"));
        setInfo(intent.getStringExtra("uid"));
    }

    private void setInfo(String uid){
        //фото
        FirebaseUsers f = new FirebaseUsers();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://flintro-706e1.appspot.com");

        // создаем ссылку на файл по адресу scoin.png
        // вызываем getDownloadUrl() и устанавливаем слушатель успеха,
        // который срабатывает в случае успеха процесса скачивания
        storageRef.child("userAvatars").child(f.uID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.with(OtherProfileActivity.this).load(uri)
                        .into(profilePhoto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Ошибка!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        //ответ
        FirebaseFirestore db = new FirebaseCloudstore().DBInstance();
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("OPA/getName()", "DocumentSnapshot data: " + document.getData());
                        try {
                            mainA.setText(document.get("mainStatus").toString());
                        }
                        catch (Exception e){

                        }
                        setSupportActionBar(myToolbar);
                    } else {
                        Log.d("OPA/getName()", "No such document");
                    }
                } else {
                    Log.d("OPA/getName()", "get failed with ", task.getException());
                    Toast.makeText(OtherProfileActivity.this,"Произошла ошибка",Toast.LENGTH_LONG).show();
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);

                }
            }
        });
    }

    private void getName(String uid){
        FirebaseFirestore db = new FirebaseCloudstore().DBInstance();
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("OPA/getName()", "DocumentSnapshot data: " + document.getData());
                        try {
                            userName.setText(document.get("name").toString());
                        }
                        catch (Exception e){

                        }
                        setSupportActionBar(myToolbar);
                    } else {
                        Log.d("OPA/getName()", "No such document");
                    }
                } else {
                    Log.d("OPA/getName()", "get failed with ", task.getException());
                    Toast.makeText(OtherProfileActivity.this,"Произошла ошибка",Toast.LENGTH_LONG).show();
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);

                }
            }
        });
    }

}
