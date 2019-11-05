package com.deomap.flintro.FirstLaunchActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.deomap.flintro.MainActivity;
import com.deomap.flintro.QuestionsActivity.QuestionsActivity;
import com.deomap.flintro.R;
import com.deomap.flintro.adapter.ImageTextAdapter;
import com.deomap.flintro.adapter.ImageTextAdapterFL;
import com.deomap.flintro.adapter.MainPartContract;
import com.deomap.flintro.api.FirebaseCloudstore;
import com.deomap.flintro.api.FirebaseStorageApi;
import com.deomap.flintro.api.FirebaseUsers;
import com.deomap.flintro.api.SharedPreferencesHub;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

//активность заполнения профиля, открывается сразу после регистрации или первого входа
public class FLActivity extends AppCompatActivity implements MainPartContract.iFLActivity{
    private MainPartContract.iFLPresenter mPresenter;
    private FirebaseStorageApi fsapi;
    private EditText editText1;
    private TextView textView1 ;
    private Button nextButton;
    private GridView interestsGrid;
    private SharedPreferencesHub sph ;
    private Button downloadPhotoButton;
    private Uri photoPath;
    private ImageView smile;
    private TextView smileText;
    private ImageView downloadedPhoto;
    private Spinner cities;
    ArrayList<String> spinnerArray = new ArrayList<>();

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private int PICK_IMAGE_AVATAR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fl);

        //mPresenter - как раз тот самый presenter, в который должна обращаться активность для действий не связанных с "внешним видом"
        mPresenter = new FLPresenter(this);
        editText1 = findViewById(R.id.editText1);
        textView1 = findViewById(R.id.textView1);
        nextButton = findViewById(R.id.nextButton);
        interestsGrid = findViewById(R.id.interestsGrid);
        downloadPhotoButton = findViewById(R.id.downloadPhotoButton);
        downloadedPhoto = findViewById(R.id.downloadedPhoto);
        fsapi = new FirebaseStorageApi();
        cities=findViewById(R.id.cities);
        smile=findViewById(R.id.smile);
        smileText=findViewById(R.id.smileText);
        setCities();

        storage = fsapi.FSInstance();
        storageReference = fsapi.FSReference();

        changeItemsAvailibility("onCreate");

        interestsGrid.setAdapter(new ImageTextAdapterFL(this));
        interestsGrid.setOnItemClickListener(gridviewOnItemClickListener);

        sph = new SharedPreferencesHub(this);
    }

    //нужно для изменения видимости элементов в зависимости от стадии
    //(если сейчас спрашивается имя пользователя, то приветственное сообщение нужно скрыть)
    @Override
    public void changeItemsAvailibility(String arg){
        if(arg.equals("onCreate")){
            editText1.setVisibility(View.GONE);
            editText1.setEnabled(false);
            textView1.setVisibility(View.GONE);
            textView1.setEnabled(false);
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setEnabled(true);
            interestsGrid.setVisibility(View.GONE);
            interestsGrid.setEnabled(false);
            downloadPhotoButton.setVisibility(View.GONE);
            downloadPhotoButton.setEnabled(false);
            downloadedPhoto.setVisibility(View.GONE);
            downloadedPhoto.setEnabled(false);
            cities.setVisibility(View.GONE);
            cities.setEnabled(false);
            smileText.setVisibility(View.VISIBLE);
            smileText.setEnabled(true);
            smile.setVisibility(View.VISIBLE);
            smile.setEnabled(true);
        }
        if(arg.equals("name")){
            editText1.setVisibility(View.VISIBLE);
            editText1.setEnabled(true);
            textView1.setVisibility(View.VISIBLE);
            textView1.setEnabled(true);
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setEnabled(true);
            interestsGrid.setVisibility(View.GONE);
            interestsGrid.setEnabled(true);
            downloadPhotoButton.setVisibility(View.GONE);
            downloadPhotoButton.setEnabled(false);
            downloadedPhoto.setVisibility(View.GONE);
            downloadedPhoto.setEnabled(false);
            cities.setVisibility(View.GONE);
            cities.setEnabled(false);
            smileText.setVisibility(View.GONE);
            smileText.setEnabled(false);
            smile.setVisibility(View.GONE);
            smile.setEnabled(false);
        }
        if(arg.equals("photo")){
            editText1.setVisibility(View.GONE);
            editText1.setEnabled(false);
            textView1.setVisibility(View.VISIBLE);
            textView1.setEnabled(true);
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setEnabled(true);
            interestsGrid.setVisibility(View.GONE);
            interestsGrid.setEnabled(false);
            downloadPhotoButton.setVisibility(View.VISIBLE);
            downloadPhotoButton.setEnabled(true);
            downloadedPhoto.setVisibility(View.VISIBLE);
            downloadedPhoto.setEnabled(true);
            cities.setVisibility(View.GONE);
            cities.setEnabled(false);
        }
        if(arg.equals("interests")){
            editText1.setVisibility(View.GONE);
            editText1.setEnabled(false);
            textView1.setVisibility(View.VISIBLE);
            textView1.setEnabled(true);
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setEnabled(true);
            interestsGrid.setVisibility(View.VISIBLE);
            interestsGrid.setEnabled(true);
            downloadPhotoButton.setVisibility(View.GONE);
            downloadPhotoButton.setEnabled(false);
            downloadedPhoto.setVisibility(View.GONE);
            downloadedPhoto.setEnabled(false);
            cities.setVisibility(View.GONE);
            cities.setEnabled(false);
        }
        if(arg.equals("sex")){
            editText1.setVisibility(View.VISIBLE);
            editText1.setEnabled(true);
            textView1.setVisibility(View.VISIBLE);
            textView1.setEnabled(true);
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setEnabled(true);
            interestsGrid.setVisibility(View.GONE);
            interestsGrid.setEnabled(true);
            downloadPhotoButton.setVisibility(View.GONE);
            downloadPhotoButton.setEnabled(false);
            downloadedPhoto.setVisibility(View.GONE);
            downloadedPhoto.setEnabled(false);
            cities.setVisibility(View.GONE);
            cities.setEnabled(false);
        }
        if(arg.equals("q1m")){
            editText1.setVisibility(View.VISIBLE);
            editText1.setEnabled(true);
            textView1.setVisibility(View.VISIBLE);
            textView1.setEnabled(true);
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setEnabled(true);
            interestsGrid.setVisibility(View.GONE);
            interestsGrid.setEnabled(true);
            downloadPhotoButton.setVisibility(View.GONE);
            downloadPhotoButton.setEnabled(false);
            downloadedPhoto.setVisibility(View.GONE);
            downloadedPhoto.setEnabled(false);
            cities.setVisibility(View.GONE);
            cities.setEnabled(false);
        }
        if(arg.equals("q2")){
            editText1.setVisibility(View.VISIBLE);
            editText1.setEnabled(true);
            textView1.setVisibility(View.VISIBLE);
            textView1.setEnabled(true);
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setEnabled(true);
            interestsGrid.setVisibility(View.GONE);
            interestsGrid.setEnabled(true);
            downloadPhotoButton.setVisibility(View.GONE);
            downloadPhotoButton.setEnabled(false);
            downloadedPhoto.setVisibility(View.GONE);
            downloadedPhoto.setEnabled(false);
            cities.setVisibility(View.GONE);
            cities.setEnabled(false);
        }
        if(arg.equals("q3")){
            editText1.setVisibility(View.VISIBLE);
            editText1.setEnabled(true);
            textView1.setVisibility(View.VISIBLE);
            textView1.setEnabled(true);
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setEnabled(true);
            interestsGrid.setVisibility(View.GONE);
            interestsGrid.setEnabled(true);
            downloadPhotoButton.setVisibility(View.GONE);
            downloadPhotoButton.setEnabled(false);
            downloadedPhoto.setVisibility(View.GONE);
            downloadedPhoto.setEnabled(false);
            cities.setVisibility(View.GONE);
            cities.setEnabled(false);
        }
        if(arg.equals("finish")){
            editText1.setVisibility(View.GONE);
            editText1.setEnabled(false);
            textView1.setVisibility(View.GONE);
            textView1.setEnabled(false);
            nextButton.setVisibility(View.GONE);
            nextButton.setEnabled(false);
            interestsGrid.setVisibility(View.GONE);
            interestsGrid.setEnabled(false);
            downloadPhotoButton.setVisibility(View.GONE);
            downloadPhotoButton.setEnabled(false);
            downloadedPhoto.setVisibility(View.GONE);
            downloadedPhoto.setEnabled(false);
            cities.setVisibility(View.GONE);
            cities.setEnabled(false);
        }
        if(arg.equals("city")){
            editText1.setVisibility(View.GONE);
            editText1.setEnabled(false);
            textView1.setVisibility(View.VISIBLE);
            textView1.setEnabled(true);
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setEnabled(true);
            interestsGrid.setVisibility(View.GONE);
            interestsGrid.setEnabled(false);
            downloadPhotoButton.setVisibility(View.GONE);
            downloadPhotoButton.setEnabled(false);
            downloadedPhoto.setVisibility(View.GONE);
            downloadedPhoto.setEnabled(false);
            cities.setVisibility(View.VISIBLE);
            cities.setEnabled(true);
        }
    }

    //слушатель нажатей для сетки интересов
    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            Log.i("grid clicked","!");
            mPresenter.onPickedInterest(position);

        }
    };

    @Override
    public void startIntent(String intentName) {
        if(intentName.equals("Questions")){
            startActivity(new Intent(this, QuestionsActivity.class));
        }
    }

    @Override
    public void toast(String msg, int time) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void askName() {
        editText1.setHint("Имя");
        editText1.setText("");
        textView1.setText("Как вас зовут?");
    }

    @Override
    public void askInterests() {
        textView1.setText("Укажите ваши интересы:");
    }

    @Override
    public void askPhoto() {
        textView1.setText("Загрузите ваше фото: ");

    }

    @Override
    public void askSex() {
    }

    @Override
    public void askQ1m(String txt) {
        editText1.setText("");
        editText1.setHint("Ответ");
        textView1.setText("Что бы о вас сказал ваш лучший друг?");
    }

    @Override
    public void askQ2(String txt) {
        editText1.setText("");
        editText1.setHint("Ответ");
        textView1.setText("Как вы относитесь к фильмам ужасов?");
    }

    @Override
    public void askQ3(String txt) {
        editText1.setText("");
        editText1.setHint("Ответ");
        textView1.setText("Согласились ли вы быть единственным зрителем на концерте любимой группы?");
    }

    private void setCities(){

        FirebaseFirestore db = new FirebaseCloudstore().DBInstance();
        db.collection("cities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("FLA", document.getId() + " => " + document.getData());
                                spinnerArray.add(document.getId());
                            }
                        } else {
                            Log.d("FLA", "Error getting documents: ", task.getException());
                            spinnerArray.add("null");
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FLActivity.this, android.R.layout.simple_spinner_item, spinnerArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        cities.setAdapter(adapter);
                        mPresenter.setNameFLA("Волгоград");
                        cities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mPresenter.setNameFLA(spinnerArray.get(position));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });

        cities.setSelection(0);


    }

    //происходит вызов активности, где пользователь может выбрать свое фото
    //для этого нужны слудующие 2 метода
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_AVATAR && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            photoPath = data.getData();
            mPresenter.setPhotoDownloadedTrue();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoPath);
                downloadedPhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void downloadPhotoClicked(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_AVATAR);
    }

    //метод для обработки нажатия кнопки "Далее"
    public void goNextStageClicked(View view){
        mPresenter.setTextFromET(editText1.getText().toString());
        mPresenter.initiateNextStage(editText1.getText().toString());
    }

    //да, у FLActivity есть свой метод для работы с внутренним хранилищем ¯\_(ツ)_/¯
    @Override
    public void accessSharedPreferences(String mode, String prefName, String type,String key, String value){
        if(mode.equals("put")){
            sph.setPrefs(prefName,type,key,value);
        }
        if(mode.equals("get")){
            switch (type){
                case "String":
                    sph.getStringSP(prefName,key);
            }
        }
    }

    //загрузка выбранного пользователем фото в хранилище Firebase
    @Override
    public void uploadImage() {

        if(photoPath != null)
        {
            //final ProgressDialog progressDialog = new ProgressDialog(this);
            //progressDialog.setTitle("Uploading...");
            //progressDialog.show();

            StorageReference ref = storageReference.child("userAvatars/"+ new FirebaseUsers().uID().toString());
            ref.putFile(photoPath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //progressDialog.dismiss();
                            Toast.makeText(FLActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressDialog.dismiss();
                            Toast.makeText(FLActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            //progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    public void askCity() {
        textView1.setText("Выберите город");
    }
}
