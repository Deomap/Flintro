package com.deomap.flintro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.deomap.flintro.FirstLaunchActivity.FLActivity;
import com.deomap.flintro.QuestionsActivity.QuestionsActivity;
import com.deomap.flintro.api.FirebaseCloudstore;
import com.deomap.flintro.api.FirebaseUsers;
import com.deomap.flintro.login.SignInActivity;
import com.deomap.flintro.login.VerifyingSignInActivity;
import com.deomap.flintro.login.appStarted;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/*
наверное это не нужно писать
но
в drawable лежат изображения, используемые в приложении
в layout лежат макеты используемые  для отображения активностей (есть еще например cellgrid - макет ячейки для сетки интересов)
в menu лежат меню (намример меню настроект в activity_profile.xml или основное меню приложения - нижняя полоса навигации)
gradle - штука для сборки проекта
AndroidManifest.xml - там прописано все что  должна знать ОС о моем приложении + необходимые разрешения и информация для Google play
 */



//MainActivity - основная точка входа в приложение, здесь проверяется нужно отправить пользователя в активность входа или в основную часть приложения
//и не только
public class MainActivity extends AppCompatActivity {

public static Context contextOfApplication;

    FirebaseCloudstore fbcs;
    FirebaseUsers fbu;
    FirebaseFirestore db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         fbu= new FirebaseUsers();
        FirebaseUser user = fbu.curUser();
        fbcs = new FirebaseCloudstore();
        db = fbcs.DBInstance();

        //fbu.userInstance().signOut();

        contextOfApplication = getApplicationContext();
        appStarted as = new appStarted();
        if(as.requestForUsing() == 0){
            startActivity(new Intent(this,SignInActivity.class));
        }
        else{
            user.reload();
            if(user.isEmailVerified()){
                ifFLActivityNeeded();
            }
            else{
                Intent intent = new Intent(this,VerifyingSignInActivity.class);
                intent.putExtra("mode",3);
                Log.i("MA", "mode 3 started");
                startActivity(intent);
            }
        }
    }

    //проверяется отметка в БД о том, входит пользователь в первый раз или нет
    private void ifFLActivityNeeded(){
        DocumentReference docRef = db.collection("users").document(fbu.curUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("MA", "DocumentSnapshot data: " );
                        String docData=document.getString("firstLaunch");

                        if(docData.equals("y")){
                            startActivity(new Intent(MainActivity.this, FLActivity.class));
                        }
                        else{
                            startActivity(new Intent(MainActivity.this, QuestionsActivity.class));
                        }
                    } else {
                        Log.d("MA", "No such document");
                        startActivity(new Intent(MainActivity.this, FLActivity.class));
                    }
                } else {
                    Log.d("MA", "get failed with ", task.getException());
                }
            }
        });
    }

    //через этот метод все остальные классы (не активности) приложения могут получать контекст, чтобы делать некоторые из задач, которые могут напрямую только активности
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }
}

































/*
--AAAAAA GOOGLE KEY (la four r)
-- put string into res
-- put serverside into models
-- clean code
--SOME SETTINGS IN DEVICE STORAGE
--SIGH UP! BUG (DOUBLE)
--liked questions??
--creating profile (null pointer fix)
--calculate matches etc
--firstlaunch "y"->"n"
--scenario: i canceled like, but user tried to press like in LA
--flactivity - enabled/disabled
--uploadImage model()
--FLA:back button
--addusertodatabase (lvp)
--serverside functions: errors
--QuestionsPresenter conttructor (delete)
--serverside in list adapter !!!! (докатились)
--
 */
