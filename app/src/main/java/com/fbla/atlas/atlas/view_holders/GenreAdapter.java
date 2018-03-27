package com.fbla.atlas.atlas.view_holders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.models.Genre;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hamza_chouaibi9 on 1/8/18.
 */

public class GenreAdapter extends ArrayAdapter<Genre> {

    public GenreAdapter(@NonNull Context context, List<Genre> genre) {
        super(context, R.layout.genre_design,genre);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.genre_design, parent, false);
        }

        Genre genre = getItem(position);

        TextView title;
        ImageView image;

        title = (TextView) view.findViewById(R.id.genre_title);
        image = (ImageView) view.findViewById(R.id.genre_image);

        title.setText(genre.getTitle());
        Picasso.with(getContext()).load(genre.getImage()).into(image);

        return view;


    }
}
