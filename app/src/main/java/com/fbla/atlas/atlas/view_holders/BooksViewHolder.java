package com.fbla.atlas.atlas.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.fbla.atlas.atlas.R;

/**
 * Created by Hamza on 1/11/2018.
 */

public class BooksViewHolder extends RecyclerView.ViewHolder{

    public TextView title;
    public TextView description;
    public ImageView image;
    public RippleView ripple;

    public BooksViewHolder(View itemView) {
        super(itemView);

        ripple = (RippleView) itemView.findViewById(R.id.ripple);
        title = (TextView) itemView.findViewById(R.id.book_title);
        description= (TextView) itemView.findViewById(R.id.book_description);
        image = (ImageView) itemView.findViewById(R.id.book_image);



    }

}
