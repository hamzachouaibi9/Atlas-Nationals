package com.fbla.atlas.atlas.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fbla.atlas.atlas.R;

/**
 * Created by hamza on 2/12/18.
 */

public class OverdueViewHolder extends RecyclerView.ViewHolder{

    public TextView title,genre;
    public ImageView image;
    public Button returned;

    public OverdueViewHolder(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.book_title_overdue);
        image = (ImageView) itemView.findViewById(R.id.book_image_overdue);
        returned = (Button) itemView.findViewById(R.id.overdue_returned);

    }
}
