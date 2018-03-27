package com.fbla.atlas.atlas.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fbla.atlas.atlas.R;

/**
 * Created by Hamza on 1/31/2018.
 */

public class BookOrderViewHolder extends RecyclerView.ViewHolder {

    public TextView title,date;
    public ImageView image;

    public BookOrderViewHolder(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.book_order_title);
        date = (TextView) itemView.findViewById(R.id.book_date);
        image = (ImageView) itemView.findViewById(R.id.book_order_image);


    }


}
