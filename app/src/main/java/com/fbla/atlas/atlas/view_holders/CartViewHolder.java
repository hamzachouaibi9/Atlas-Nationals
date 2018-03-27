package com.fbla.atlas.atlas.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.fbla.atlas.atlas.R;

/**
 * Created by Hamza on 1/16/2018.
 */

public class CartViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView description;
    public ImageView image;
    public Button delete;
    public RippleView ripple, deleteR;

    public CartViewHolder(View itemView) {
        super(itemView);

        ripple = (RippleView) itemView.findViewById(R.id.ripple_cart);
        deleteR = (RippleView) itemView.findViewById(R.id.delete_ripple);
        title = (TextView) itemView.findViewById(R.id.book_cart_title);
        description = (TextView) itemView.findViewById(R.id.book_cart_desc);
        delete = (Button) itemView.findViewById(R.id.book_cart_remove);
        image = (ImageView) itemView.findViewById(R.id.book_cart_image);


    }



}
