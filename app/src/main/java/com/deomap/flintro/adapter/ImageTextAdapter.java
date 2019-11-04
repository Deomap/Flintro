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

//эта страшная штука нужна для GridView, чтобы работало и отображало в ячейках сетки одновременно и текст и картинку
public class ImageTextAdapter extends BaseAdapter {
        private Context mContext;

        TopicsPositionMatch tpm = new TopicsPositionMatch();

        public ImageTextAdapter(Context c) {
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

            ImageView imageView = (ImageView) grid.findViewById(R.id.imagepart);
            TextView textView = (TextView) grid.findViewById(R.id.textpart);
            imageView.setImageResource(mThumbIds[position]);
            Drawable background = imageView.getBackground();
            background.setTint(MainActivity.getContextOfApplication().getResources().getColor(Colors[position]));
            //здесь определяется текст, который доолжны стоять в каждой ячейки
            textView.setText(tpm.topicNameRus(position));

            return grid;
        }

        //здесь определяются ссылки на картинки, который доолжны стоять в каждой ячейки
        public Integer[] mThumbIds = {
                R.drawable.round_autorenew_white_48,R.drawable.round_trending_up_white_48, R.drawable.round_emoji_people_white_48,
                R.drawable.round_school_white_48,R.drawable.round_language_white_48, R.drawable.baseline_camera_roll_white_48,
                R.drawable.baseline_color_lens_white_48, R.drawable.baseline_audiotrack_white_48, R.drawable.round_local_hospital_white_48,
                R.drawable.round_restaurant_white_48, R.drawable.round_memory_white_48, R.drawable.round_sports_basketball_white_48,
                R.drawable.baseline_people_white_48, R.drawable.baseline_emoji_objects_white_48, R.drawable.baseline_flight_white_48,
                R.drawable.round_emoji_transportation_white_48, R.drawable.wowrcon, R.drawable.baseline_all_inclusive_white_48,
                R.drawable.wowrcon, R.drawable.round_favorite_white_48, R.drawable.round_chrome_reader_mode_white_48};
        public Integer[] Colors = {
                R.color.colorPrimary,R.color.colorPrimary,R.color.colorPrimary,
                R.color.colorPrimary,R.color.colorPrimary,R.color.colorPrimary,
                R.color.colorPrimary,R.color.colorPrimary,R.color.colorPrimary,
                R.color.colorPrimary,R.color.colorPrimary,R.color.colorPrimary,
                R.color.colorPrimary,R.color.colorPrimary,R.color.colorPrimary,
                R.color.colorPrimary,R.color.colorPrimary,R.color.colorPrimary,
                R.color.colorPrimary,R.color.colorPrimary,R.color.colorPrimary,
        };
    }

