package com.deomap.flintro.adapter.Apadter_LA_Matches;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterLAM extends ArrayAdapter<UnitLAM> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<UnitLAM> unitList;

    public AdapterLAM(Context context, int resource, ArrayList<UnitLAM> units) {
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
        final UnitLAM unit = unitList.get(position);

        viewHolder.nameField.setText(unit.getUserName());

        viewHolder.likedIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.nameField.setEnabled(false);
                viewHolder.toChatBtn.setEnabled(false);
                viewHolder.likedIBtn.setEnabled(false);
                FirebaseFirestore db = new FirebaseCloudstore().DBInstance();
                FirebaseUsers fu = new FirebaseUsers();
                DocumentReference docRef = db.collection("users").document(fu.uID()).collection("likes").document("meLikes");
                if(unit.getuID()!=null) {
                    Map<String, Object> updates = new HashMap<>();
                    updates.put(unit.getuID(), FieldValue.delete());
                    docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("ALAM", "deleted");
                        }
                    });
                }
            }
        });

        viewHolder.toChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("fromALAM",unit.getuID());
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        final ImageButton likedIBtn, toChatBtn;
        final TextView nameField;

        ViewHolder(View view) {
            likedIBtn = (ImageButton) view.findViewById(R.id.likedIBtn);
            toChatBtn = (ImageButton) view.findViewById(R.id.toChatBtn);
            nameField = (TextView) view.findViewById(R.id.nameField);
        }
    }
}