package com.fbla.atlas.atlas.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fbla.atlas.atlas.R;

public class ReviewViewHolder extends RecyclerView.ViewHolder {
    public TextView name, date, rating, comment;
    public ImageView image;


    public ReviewViewHolder(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.rating_name);
        date = (TextView) itemView.findViewById(R.id.rating_date);
        rating = (TextView) itemView.findViewById(R.id.rating_rating);
        image = (ImageView) itemView.findViewById(R.id.rating_image);
        comment = (TextView) itemView.findViewById(R.id.rating_comment);
    }
}
