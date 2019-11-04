package com.deomap.flintro.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deomap.flintro.R;

//эта страшная штука нужна для GridView, чтобы работало и отображало в ячейках сетки одновременно и текст и картинку
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

        ImageView imageView = (ImageView) grid.findViewById(R.id.imagepart);
        TextView textView = (TextView) grid.findViewById(R.id.textpart);
        imageView.setImageResource(mThumbIds[position]);
        //здесь определяется текст, который доолжны стоять в каждой ячейки
        textView.setText(tpm.topicNameRus(position+2));

        return grid;
    }

    //здесь определяются ссылки на картинки, который доолжны стоять в каждой ячейки
    public Integer[] mThumbIds = {
            R.drawable.wowrcon,R.drawable.wowrcon, R.drawable.wowrcon,
            R.drawable.wowrcon, R.drawable.wowrcon, R.drawable.wowrcon,
            R.drawable.wowrcon, R.drawable.wowrcon, R.drawable.wowrcon,
            R.drawable.wowrcon, R.drawable.wowrcon, R.drawable.wowrcon,
            R.drawable.wowrcon, R.drawable.wowrcon, R.drawable.wowrcon,
            R.drawable.wowrcon, R.drawable.wowrcon, R.drawable.wowrcon,
            R.drawable.wowrcon};
}

