package com.fbla.atlas.atlas.view_holders;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fbla.atlas.atlas.R;

/**
 * Created by Hamza on 2/5/2018.
 */

public class UserViewHolder extends RecyclerView.ViewHolder{

    public TextView name,school;
    public ImageView image;
    public Button checked, reserved, overdue;

    public UserViewHolder(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.user_name);
        school = (TextView) itemView.findViewById(R.id.user_school);
        image = (ImageView) itemView.findViewById(R.id.user_image);
        reserved = (Button) itemView.findViewById(R.id.user_reserve);
        checked = (Button) itemView.findViewById(R.id.user_checked);
        overdue = (Button) itemView.findViewById(R.id.user_overdue);

    }
}
