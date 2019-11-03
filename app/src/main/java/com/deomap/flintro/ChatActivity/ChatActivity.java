package com.deomap.flintro.ChatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import android.widget.ListView;

import com.deomap.flintro.LikesActivity.LikesActivity;
import com.deomap.flintro.ProfileActivity.ProfileActivity;
import com.deomap.flintro.QuestionsActivity.QuestionsActivity;
import com.deomap.flintro.R;
import com.deomap.flintro.SwipeActivity.SwipeActivity;
import com.deomap.flintro.adapter.MainPartContract;
import com.deomap.flintro.api.FirebaseUsers;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

//активность чата, здесь происходит общение с собеседником
public class ChatActivity extends AppCompatActivity implements MainPartContract.iChatActivity{
    private MainPartContract.iChatPresenter mPresenter;

    private FirebaseListAdapter<ChatMessage> adapter;
    private String uID_fromIntent="null";
    FirebaseUsers fu = new FirebaseUsers();
    private Button sendBtn;

    //есть во всех активностях кроме FirstLaunch и активностей входа и регистрации
    //нужно для перехода в другую активность по нажатию на кнопку на красивой навигационной полоске снизу
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
                case R.id.navigation_chat:
                    startIntent("Chat");
                    return true;
                case R.id.navigation_profile:
                    startIntent("Profile");
                    return true;
            }
            return false;
        }
    };


    //как оказалось, инициировать переход в другую активность можно и не из текущей активности, но метод все же остался и используется везде в коде для этого
    @Override
    public void startIntent(String intentName) {
        switch (intentName){
            case "Questions":
                Log.i("CA","Q");
                startActivity(new Intent(this, QuestionsActivity.class));
                break;
            case "Swipe":
                Log.i("CA","S");
                startActivity(new Intent(this, SwipeActivity.class));
                break;
            case "Profile":
                Log.i("CA","P");
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case "Likes":
                Log.i("CA","L");
                startActivity(new Intent(this, LikesActivity.class));
                break;
            case "Chat":
                Log.i("CA","C");
                startActivity(new Intent(this, ChatActivity.class));
                break;
        }
    }

    //в onCreate описано, что должно произойти сразу после создания активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //определяются связи с обектами из макета (activity_chat.xml)
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mPresenter = new ChatPresenter(this);

        //здесь определяется с каким собеседником нужно открыть диалог по полученному из LikesActivity User-ID
        Intent intent = getIntent();
        if(intent.hasExtra("fromALAM")){
            uID_fromIntent = intent.getStringExtra("fromALAM");
        }
        else{
            uID_fromIntent = "null";
        }
        navView.getMenu().findItem(R.id.navigation_chat).setChecked(true);
        overridePendingTransition(0, 0);

        afterCreation();
        displayChatMessages();
    }

    //метод есть по всех активностях но не во всех я его определяю, нужен для показа всплывающих серых сообщений пользователю
    @Override
    public void toast(String msg, int time) {

    }

    //здесь происходит отправка сообщения в базу данных реального времен с помощью FirebaseRealtimeDatabase
    private void afterCreation(){
        ImageButton fab =
                findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);
                FirebaseDatabase.getInstance()
                        .getReference(fu.uID()+","+uID_fromIntent)
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        );
                input.setText("");
            }
        });
    }

    //здесь сообщения выводятся пользователю
    private void displayChatMessages(){
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference(fu.uID()+","+uID_fromIntent)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }
}

