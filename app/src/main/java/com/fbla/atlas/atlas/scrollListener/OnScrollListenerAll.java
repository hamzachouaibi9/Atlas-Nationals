package com.fbla.atlas.atlas.scrollListener;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.models.Book;

/**
 * Created by hamza-chouaibi9 on 3/23/18.
 */

public class OnScrollListenerAll extends RecyclerView.OnScrollListener {

    Button All;

    public OnScrollListenerAll(View view, Button button) {

        All = button;

    }


    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                All.setVisibility(View.VISIBLE);
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                All.setVisibility(View.GONE);
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                All.setVisibility(View.GONE);
                break;

        }

    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dx > 0) {
            System.out.println("Scrolled Right");
        } else if (dx < 0) {
            System.out.println("Scrolled Left");
        } else {
            System.out.println("No Horizontal Scrolled");
        }

        if (dy > 0) {
            System.out.println("Scrolled Downwards");
        } else if (dy < 0) {
            System.out.println("Scrolled Upwards");
        } else {
            System.out.println("No Vertical Scrolled");
        }
    }
}

