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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deomap.flintro.MainActivity;
import com.deomap.flintro.QuestionsActivity.QuestionsActivity;
import com.deomap.flintro.R;
import com.deomap.flintro.adapter.ImageTextAdapter;
import com.deomap.flintro.adapter.MainPartContract;
import com.deomap.flintro.api.FirebaseStorageApi;
import com.deomap.flintro.api.FirebaseUsers;
import com.deomap.flintro.api.SharedPreferencesHub;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
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
    private ImageView downloadedPhoto;

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

        storage = fsapi.FSInstance();
        storageReference = fsapi.FSReference();

        changeItemsAvailibility("onCreate");

        interestsGrid.setAdapter(new ImageTextAdapter(this));
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

    }

    @Override
    public void askName() {
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

    //происходит вызов активности, где пользователь может выбрать свое фото
    //для этого нужны слудующие 2 метода
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_AVATAR && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            photoPath = data.getData();
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
}
