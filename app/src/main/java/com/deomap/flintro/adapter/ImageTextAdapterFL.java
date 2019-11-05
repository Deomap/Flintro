package com.deomap.flintro.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deomap.flintro.MainActivity;
import com.deomap.flintro.R;

//эта страшная штука нужна для GridView, чтобы работало и отображало в ячейках сетки одновременно и текст и картинку (Конкретно этот адаптер нужен для GridView в FLActivity)
public class ImageTextAdapterFL extends BaseAdapter {
    private Context mContext;

    TopicsPositionMatch tpm = new TopicsPositionMatch();

    public ImageTextAdapterFL(Context c) {
        mContext = c;
    }
    public int getCount() {
        return mThumbIds.length;
    }
    public Object getItem(int position) {
        return mThumbIds[position];
    }
    public long getItemId(int position) {
        return position;
    }

    //аналогично адаптеру производятся манипуляции с каждой отдельной ячейкой
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;

        if (convertView == null) {
            grid = new View(mContext);
            //LayoutInflater inflater = getLayoutInflater();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            grid = inflater.inflate(R.layout.cellgrid, parent, false);
        } else {
            grid = (View) convertView;
        }

        final ImageView imageView = (ImageView) grid.findViewById(R.id.imagepart);
        TextView textView = (TextView) grid.findViewById(R.id.textpart);
        imageView.setImageResource(mThumbIds[position]);
        //здесь определяется текст, который доолжны стоять в каждой ячейки
        final Drawable background = imageView.getBackground();
        background.setTint(MainActivity.getContextOfApplication().getResources().getColor(Colors[position]));
        textView.setText(tpm.topicNameRus(position+2));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                background.setTint(MainActivity.getContextOfApplication().getResources().getColor(R.color.graygray));
            }
        });

        return grid;
    }

    //здесь определяются ссылки на картинки, который доолжны стоять в каждой ячейки
    public Integer[] mThumbIds = {
            R.drawable.round_emoji_people_white_48, R.drawable.round_school_white_48,R.drawable.round_language_white_48,
            R.drawable.baseline_movie_creation_white_48, R.drawable.baseline_color_lens_white_48, R.drawable.baseline_audiotrack_white_48,
            R.drawable.round_local_hospital_white_48, R.drawable.round_restaurant_white_48, R.drawable.round_memory_white_48,
            R.drawable.round_sports_basketball_white_48, R.drawable.baseline_people_white_48, R.drawable.baseline_emoji_objects_white_48,
            R.drawable.baseline_flight_white_48, R.drawable.round_emoji_transportation_white_48, R.drawable.baseline_local_offer_white_48,
            R.drawable.baseline_all_inclusive_white_48, R.drawable.baseline_account_balance_white_48, R.drawable.round_favorite_white_48,
            R.drawable.baseline_menu_book_white_48};
    public Integer[] Colors = {
            R.color.red_A400,R.color.purple_A100,R.color.blue_A200,
            R.color.green_A400,R.color.yellow_800,R.color.pink_A200,
            R.color.indigo_A200,R.color.cyan_A700,R.color.green_600,
            R.color.amber_800,R.color.purple_A200,R.color.teal_A700,
            R.color.lime_700,R.color.orange_A200,R.color.indigo_A100,
            R.color.cyan_A700,R.color.pink_A700,R.color.brown_300,
            R.color.purple_A100,R.color.blue_400,R.color.lime_600,
    };
}

