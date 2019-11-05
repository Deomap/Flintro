package com.deomap.flintro.adapter.Apadter_QA_Answers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.deomap.flintro.ChatActivity.ChatActivity;
import com.deomap.flintro.QuestionsActivity.QuestionsActivity;
import com.deomap.flintro.R;
import com.deomap.flintro.adapter.TopicsPositionMatch;
import com.deomap.flintro.api.FirebaseCloudstore;
import com.deomap.flintro.api.FirebaseUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//подробно работа адаптера описана в AdapterLAA.java.
//Данный адаптер служит для QuestionsActivity, когда пользователю необходимо список ответов других пользователей с использованием кнопки "Нравится"

public class AdapterQAA extends ArrayAdapter<UnitQAA> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<UnitQAA> unitList;

    public AdapterQAA(Context context, int resource, ArrayList<UnitQAA> units) {
        super(context, resource, units);
        this.unitList = units;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final UnitQAA unit = unitList.get(position);

        viewHolder.answerTextField.setText(unit.getAnswerText());

        //здесь происходит добавление ответа в понравившиеся и установление связи между пользователями (который нажал "Нравится" и который написал ответ)
        //для дальнейшего подбора во вкладке Swipe
        viewHolder.likedIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> like = new HashMap<>();
                like.put(unit.getPath(),unit.getAnswerText());

                new  FirebaseCloudstore().DBInstance().collection("users").document(new FirebaseUsers().curUser().getUid()).collection("likes").document("answers")
                        .update(like)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("AQAA", "DocumentSnapshot successfully written!");

                                viewHolder.likedIBtn.setImageResource(R.drawable.round_favorite_black_18);
                                //viewHolder.likedIBtn.set
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("AQAA", "Error writing document", e);
                            }
                        });

                Map<String, Object> toLikedUser = new HashMap<>();
                toLikedUser.put(new FirebaseUsers().uID(),"3");

                new  FirebaseCloudstore().DBInstance().collection("users").document(unit.getPath().split(",")[2]).collection("PSInfo").document("forSwipe")
                        .update(toLikedUser)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("AQAA/toLikedUser", "DocumentSnapshot successfully written!");
                                //viewHolder.likedIBtn.set
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("AQAA/tolikeduser", "Error writing document", e);
                            }
                        });

                Map<String, Object> toMe = new HashMap<>();
                toMe.put(unit.getPath().split(",")[2],"2");

                new  FirebaseCloudstore().DBInstance().collection("users").document(new FirebaseUsers().curUser().getUid()).collection("PSInfo").document("forSwipe")
                        .update(toMe)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("AQAA/tome", "DocumentSnapshot successfully written!");
                                //viewHolder.likedIBtn.set
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("AQAA/tome", "Error writing document", e);
                            }
                        });
            }
        });

        return convertView;
    }

    private class ViewHolder {
        final ImageButton likedIBtn;
        final TextView answerTextField;

        ViewHolder(View view) {
            likedIBtn = (ImageButton) view.findViewById(R.id.likedIBtn);
            answerTextField = (TextView) view.findViewById(R.id.answerTextField);
        }
    }
}