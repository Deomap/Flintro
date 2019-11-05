package com.deomap.flintro.SwipeActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.deomap.flintro.ChatActivity.ChatActivity;
import com.deomap.flintro.LikesActivity.LikesActivity;
import com.deomap.flintro.ProfileActivity.ProfileActivity;
import com.deomap.flintro.QuestionsActivity.QuestionsActivity;
import com.deomap.flintro.R;
import com.deomap.flintro.adapter.MainPartContract;
import com.deomap.flintro.api.FirebaseUsers;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

//автивность "Свайп"
public class SwipeActivity extends AppCompatActivity implements MainPartContract.iSwipeActivity {

    private ImageButton foundUserReportBtn;
    private Button foundUserLikeBtn;
    private Button foundUserDislikeBtn;
    private TextView userMatchedAnswers1;
    private TextView userMatchedAnswers2;
    private TextView userMatchedAnswers3;
    private TextView foundUserMainStatus;
    private TextView foundUserName;
    private ImageView foundUserPhoto;

    private MainPartContract.iSwipePresenter mPresenter;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_swipe:
                    startIntent("Swipe");
                    return true;
                case R.id.navigation_likes:
                    startIntent("Likes");
                    return true;
                case R.id.navigation_questions:
                    startIntent("Questions");
                    return true;
                //case R.id.navigation_chat:
                    //startIntent("Chat");
                    //return true;
                case R.id.navigation_profile:
                    startIntent("Profile");
                    return true;
            }
            return false;
        }
    };

    @Override
    public void startIntent(String intentName) {
        switch (intentName) {
            case "Questions":
                Log.i("S", "Q");
                startActivity(new Intent(this, QuestionsActivity.class));
                break;
            case "Swipe":
                Log.i("S", "S");
                startActivity(new Intent(this, SwipeActivity.class));
                break;
            case "Profile":
                Log.i("S", "P");
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case "Likes":
                Log.i("S", "L");
                startActivity(new Intent(this, LikesActivity.class));
                break;
            case "Chat":
                Log.i("S", "C");
                startActivity(new Intent(this, ChatActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        mPresenter = new SwipePresenter(this);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.getMenu().findItem(R.id.navigation_swipe).setChecked(true);

        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Свайп");
        setSupportActionBar(myToolbar);

        foundUserDislikeBtn = findViewById(R.id.foundUserDislikeBtn);
        foundUserLikeBtn = findViewById(R.id.foundUserLikeBtn);
        foundUserName = findViewById(R.id.foundUserName);
        foundUserPhoto = findViewById(R.id.foundUserPhoto);
        foundUserReportBtn = findViewById(R.id.foundUserReportBtn);
        userMatchedAnswers1 = findViewById(R.id.userMatchedAnswers1);
        userMatchedAnswers2 = findViewById(R.id.userMatchedAnswers2);
        userMatchedAnswers3 = findViewById(R.id.userMatchedAnswers3);

        mPresenter.startShowing();
        overridePendingTransition(0, 0);
    }

    //пользователь нажимает "Нравится" или  "Не нравится", все поля обнуляются, у Presenterа запрашивается новый пользователь

    public void likeUserBtnClck(View view){
        foundUserName.setText("");
        mPresenter.liked();
        //foundUserMainStatus.setText("");
        userMatchedAnswers3.setText("");
        userMatchedAnswers2.setText("");
        userMatchedAnswers1.setText("");
        mPresenter.showInLoop();
    }

    public void dislikeUserBtnClck(View view){
        foundUserName.setText("");
        mPresenter.disliked();
        //foundUserMainStatus.setText("");
        userMatchedAnswers3.setText("");
        userMatchedAnswers2.setText("");
        userMatchedAnswers1.setText("");
        mPresenter.showInLoop();
    }


    @Override
    public void toast(String msg, int time) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    //из Presenter устанавливается информация о другом предложенном пользователе
    @Override
    public void setFoundUserInfo(String fuName, String fuStatus, String fuTxt1, String fuTxt2, String fuTxt3) {
        foundUserName.setText(fuName);
        //foundUserMainStatus.setText(fuStatus);
        userMatchedAnswers1.setText(fuStatus);
        userMatchedAnswers2.setText(fuTxt2);
        userMatchedAnswers3.setText(fuTxt3);
        loadPhoto();
    }

    private void loadPhoto(){
        FirebaseUsers f = new FirebaseUsers();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://flintro-706e1.appspot.com");

        // создаем ссылку на файл по адресу scoin.png
        // вызываем getDownloadUrl() и устанавливаем слушатель успеха,
        // который срабатывает в случае успеха процесса скачивания
        storageRef.child("userAvatars").child(f.uID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.with(SwipeActivity.this).load(uri)
                        .into(foundUserPhoto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Ошибка!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}