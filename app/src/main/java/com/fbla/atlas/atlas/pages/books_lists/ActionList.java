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
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ActionList extends AppCompatActivity {

    RecyclerView list;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Action Books");

        list = (RecyclerView) findViewById(R.id.action_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Books");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Action Books");
        progressDialog.show();
        progressDialog.setCancelable(false);

        final Query query = databaseReference.child("AllBooks").orderByChild("genre").equalTo("Action");

        final FirebaseRecyclerAdapter<Book, BooksViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Book, BooksViewHolder>(Book.class,
                R.layout.book_row, BooksViewHolder.class, query) {
            @Override
            protected void populateViewHolder(BooksViewHolder viewHolder, Book model, int position) {
                viewHolder.title.setText(model.getTitle());
                viewHolder.description.setText(model.getDescription());
                Picasso.with(ActionList.this).load(model.getImage()).into(viewHolder.image);
                progressDialog.cancel();


                final String book_id = getRef(position).getKey();
                viewHolder.ripple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ActionList.this, BookPage.class);
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
