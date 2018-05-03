package com.fbla.atlas.atlas.pages.books_lists;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.models.Book;
import com.fbla.atlas.atlas.pages.student.BookPage;
import com.fbla.atlas.atlas.view_holders.BooksViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import static android.content.ContentValues.TAG;

public class HistoryList extends AppCompatActivity {

    RecyclerView list;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("History Books");

        list = (RecyclerView) findViewById(R.id.history_list);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Books").child("AllBooks");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading History Books");
        progressDialog.show();
        progressDialog.setCancelable(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.hasFixedSize();

    }

    @Override
    public void onStart() {
        super.onStart();
        final Query query = databaseReference.orderByChild("genre").equalTo("History");

        final FirebaseRecyclerAdapter<Book, BooksViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Book, BooksViewHolder>(Book.class,
                R.layout.book_row, BooksViewHolder.class, query) {
            @Override
            protected void populateViewHolder(BooksViewHolder viewHolder, Book model, int position) {
                viewHolder.title.setText(model.getTitle());
                viewHolder.description.setText(model.getDescription());
                Picasso.with(HistoryList.this).load(model.getImage()).into(viewHolder.image);
                progressDialog.cancel();

                Log.d(TAG, "populateViewHolder: "+getRef(position));

                final String book_id = getRef(position).getKey();
                viewHolder.ripple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HistoryList.this, BookPage.class);
                        intent.putExtra("book_id", book_id);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                    }
                });
            }
        };

        list.setAdapter(firebaseRecyclerAdapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
