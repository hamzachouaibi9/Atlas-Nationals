package com.fbla.atlas.atlas.view_holders;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fbla.atlas.atlas.R;

/**
 * Created by Hamza on 2/5/2018.
 */

public class RequestViewHolder extends RecyclerView.ViewHolder{

    public TextView title,date;
    public Button accept, decline;
    public ImageView image;


    public RequestViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.book_request_title);
        date = (TextView) itemView.findViewById(R.id.book_request_date);
        image = (ImageView) itemView.findViewById(R.id.book_request_image);
        accept = (Button) itemView.findViewById(R.id.book_request_accept);
        decline = (Button) itemView.findViewById(R.id.book_request_decline);

    }
}
