package com.fbla.atlas.atlas.pages.books_lists;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.models.Book;
import com.fbla.atlas.atlas.pages.student.BookPage;
import com.fbla.atlas.atlas.view_holders.BooksViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AllList extends AppCompatActivity {

    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("All Books");

        list = (RecyclerView) findViewById(R.id.all_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.hasFixedSize();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading All Books");
        progressDialog.show();
        progressDialog.setCancelable(false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Books").child("AllBooks");

    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Book, BooksViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Book, BooksViewHolder>(Book.class,
                R.layout.book_row, BooksViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(BooksViewHolder viewHolder, Book model, int position) {
                viewHolder.title.setText(model.getTitle());
                viewHolder.description.setText(model.getDescription());
                Picasso.with(AllList.this).load(model.getImage()).into(viewHolder.image);
                progressDialog.cancel();

                final String book_id = getRef(position).getKey();
                viewHolder.ripple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AllList.this, BookPage.class);
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
