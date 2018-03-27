package com.fbla.atlas.atlas.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.fbla.atlas.atlas.R;

/**
 * Created by Hamza on 1/31/2018.
 */

public class GenreViewHolder extends RecyclerView.ViewHolder{

    public TextView title;
    public ImageView image;
    public RippleView ripple;


    public GenreViewHolder(View itemView) {
        super(itemView);

        ripple = itemView.findViewById(R.id.ripple_genre);
        title = itemView.findViewById(R.id.genre_title);
        image = itemView.findViewById(R.id.genre_image);

    }

}
