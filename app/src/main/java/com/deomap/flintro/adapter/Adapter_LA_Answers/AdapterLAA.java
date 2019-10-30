package com.deomap.flintro.adapter.Adapter_LA_Answers;

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

import com.deomap.flintro.QuestionsActivity.QuestionsActivity;
import com.deomap.flintro.R;
import com.deomap.flintro.adapter.TopicsPositionMatch;
import com.deomap.flintro.api.FirebaseCloudstore;
import com.deomap.flintro.api.FirebaseUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterLAA extends ArrayAdapter<UnitLAA> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<UnitLAA> unitList;

    public AdapterLAA(Context context, int resource, ArrayList<UnitLAA> units) {
        super(context, resource, units);
        this.unitList = units;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, final ViewGroup parent) {

        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final UnitLAA unit = unitList.get(position);

        viewHolder.answerField.setText(unit.getAnswer());
        viewHolder.questionField.setText(unit.getQuestion());

        viewHolder.likedIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.answerField.setEnabled(false);
                viewHolder.questionField.setEnabled(false);
                viewHolder.likedIBtn.setEnabled(false);
                FirebaseFirestore db = new FirebaseCloudstore().DBInstance();
                FirebaseUsers fu = new FirebaseUsers();
                DocumentReference docRef = db.collection("users").document(fu.uID()).collection("likes").document("answers");

                Map<String,Object> updates = new HashMap<>();
                updates.put(unit.getTopic()+","+unit.getqID()+","+unit.getuID(), FieldValue.delete());
                docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("ALAA","deleted");
                    }
                });
            }
        });

        viewHolder.answerField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        viewHolder.questionField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),QuestionsActivity.class);
                int pos = new TopicsPositionMatch().topicPosEng(unit.getTopic());
                intent.putExtra("fromLAPos",pos);
                intent.putExtra("fromLAQID", unit.getqID());
                Log.i("e",Integer.toString(pos));
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        final ImageButton likedIBtn;
        final TextView questionField, answerField;
        ViewHolder(View view){
            likedIBtn = (ImageButton) view.findViewById(R.id.likedIBtn);
            questionField = (TextView) view.findViewById(R.id.questionField);
            answerField = (TextView) view.findViewById(R.id.answerField);
        }
    }
}