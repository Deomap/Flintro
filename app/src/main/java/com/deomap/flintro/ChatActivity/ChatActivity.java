package com.deomap.flintro.ChatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import android.widget.ListView;
import android.widget.Toast;

import com.deomap.flintro.LikesActivity.LikesActivity;
import com.deomap.flintro.ProfileActivity.OtherProfileActivity;
import com.deomap.flintro.ProfileActivity.ProfileActivity;
import com.deomap.flintro.QuestionsActivity.QuestionsActivity;
import com.deomap.flintro.R;
import com.deomap.flintro.SwipeActivity.SwipeActivity;
import com.deomap.flintro.adapter.MainPartContract;
import com.deomap.flintro.adapter.TopicsPositionMatch;
import com.deomap.flintro.api.FirebaseCloudstore;
import com.deomap.flintro.api.FirebaseUsers;
import com.deomap.flintro.api.SharedPreferencesHub;
import com.deomap.flintro.api.chatKeyStored;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

//активность чата, здесь происходит общение с собеседником
public class ChatActivity extends AppCompatActivity implements MainPartContract.iChatActivity{
    private MainPartContract.iChatPresenter mPresenter;

    private FirebaseListAdapter<ChatMessage> adapter;
    private String uID_fromIntent="null";
    FirebaseUsers fu = new FirebaseUsers();
    private Button sendBtn;
    String chatKey = "null";
    chatKeyStored cks;
    ImageButton fab;
    SharedPreferencesHub sph = new SharedPreferencesHub(this);

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

        ImageButton fab = findViewById(R.id.fab);

        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Чат");
        setSupportActionBar(myToolbar);
        fab.setEnabled(false);

        //navView.getMenu().findItem(R.id.navigation_chat).setChecked(true);
        overridePendingTransition(0, 0);

        afterCreation();
    }

    //обработка входа пользователя через нажатие на полосе навигации
    @Override
    protected void onResume(){
        super.onResume();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.getMenu().findItem(R.id.navigation_likes).setChecked(true);
        fab = findViewById(R.id.fab);
        Intent intent = getIntent();

        //здесь определяется с каким собеседником нужно открыть диалог по полученному из LikesActivity User-ID
        if(intent.hasExtra("fromALAM")){
            uID_fromIntent = intent.getStringExtra("fromALAM");
            chatKey = Integer.toString(uID_fromIntent.hashCode()+fu.uID().hashCode());
            fab.setEnabled(true);
            displayChatMessages();
            toast(uID_fromIntent,1);
        }
        else{
                uID_fromIntent = "null";
                toast("Произошла ошибка при загрузке", 1);
                fab.setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //обработка нажатия на пункты меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            FirebaseFirestore db = new FirebaseCloudstore().DBInstance();
            FirebaseUsers fu = new FirebaseUsers();
            DocumentReference docRef = db.collection("users").document(fu.uID()).collection("likes").document("meLikes");
            //if(!uID_fromIntent.equals("null")) {
                Map<String, Object> updates = new HashMap<>();
                updates.put(uID_fromIntent, FieldValue.delete());
                docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("CAAD", "deleted");
                    }
                });
            //}
            return true;
        }
        if (id == R.id.action_report) {
            Map<String, Object> setup = new HashMap<>();
            FirebaseFirestore fcs_db =  new FirebaseCloudstore().DBInstance();
            setup.put(uID_fromIntent,"ndr");
            fcs_db.collection("reported").document("notDefinedReason").update(setup);

            FirebaseFirestore db = new FirebaseCloudstore().DBInstance();
            FirebaseUsers fu = new FirebaseUsers();
            DocumentReference docRef = db.collection("users").document(fu.uID()).collection("likes").document("meLikes");
            //if(!uID_fromIntent.equals("null")) {
                Map<String, Object> updates = new HashMap<>();
                updates.put(uID_fromIntent, FieldValue.delete());
                docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("CAAD", "deleted");
                    }
                });
            //}
            return true;
        }
        if (id == R.id.action_view_profile) {
            Intent intent = new Intent(this, OtherProfileActivity.class);
            intent.putExtra("uid",uID_fromIntent);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //метод есть по всех активностях но не во всех я его определяю, нужен для показа всплывающих серых сообщений пользователю
    @Override
    public void toast(String msg, int time) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    //здесь происходит отправка сообщения в базу данных реального времен с помощью FirebaseRealtimeDatabase
    private void afterCreation(){
        ImageButton fab =
                findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);
                if(input.getText().toString().equals("")){
                    toast("Введите сообщение",1);
                }
                else {
                    FirebaseDatabase.getInstance()
                            .getReference(chatKey)
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getDisplayName())
                            );
                    input.setText("");
                }
            }
        });
    }

    //здесь сообщения выводятся пользователю
    private void displayChatMessages(){
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference(chatKey)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser().split(",")[0]);
                Log.i("user",model.getMessageUser());
                if(model.getMessageUser().split(",")[1].equals(fu.uID())){
                    messageUser.setTextColor(getResources().getColor(R.color.red_200));
                }
                else{
                    messageUser.setTextColor(getResources().getColor(R.color.light_blue_200));
                }

                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }
}

