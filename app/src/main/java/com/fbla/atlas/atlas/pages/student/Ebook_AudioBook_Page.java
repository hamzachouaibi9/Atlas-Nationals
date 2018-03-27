package com.fbla.atlas.atlas.pages.student;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.activities.Login;
import com.fbla.atlas.atlas.help.HelpHome;
import com.fbla.atlas.atlas.models.BookOrder;
import com.fbla.atlas.atlas.view_holders.BooksViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Ebook_AudioBook_Page extends AppCompatActivity {

    TextView ebook_title, audio_title, ebook_all, audio_all;
    RecyclerView ebook_list, audio_list;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    String user_id;
    Query ebookDatabase, audioBookDatabase;
    String book_id;
    String count;
    TextView countTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook__audio_book__page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ebook and Audio Books");

        auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ebooks_audioBooks").child(user_id);
        ebookDatabase = databaseReference.orderByChild("type").equalTo("eBook").limitToLast(5);
        audioBookDatabase = databaseReference.orderByChild("type").equalTo("audioBook").limitToLast(5);

        ebook_list = (RecyclerView) findViewById(R.id.ebook_list);
        ebook_list.hasFixedSize();
        ebook_list.setLayoutManager(new LinearLayoutManager(this));
        audio_list = (RecyclerView) findViewById(R.id.audio_list);
        audio_list.hasFixedSize();
        audio_list.setLayoutManager(new LinearLayoutManager(this));

        ebook_title = (TextView) findViewById(R.id.ebook_text);
        ebook_all = (TextView) findViewById(R.id.ebook_all);
        audio_title = (TextView) findViewById(R.id.audio_text);
        audio_all = (TextView) findViewById(R.id.audio_all);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("type").equals("eBook")) {
                        FirebaseRecyclerAdapter<BookOrder, BooksViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BookOrder, BooksViewHolder>
                                (BookOrder.class, R.layout.book_row, BooksViewHolder.class, ebookDatabase) {
                            @Override
                            protected void populateViewHolder(BooksViewHolder viewHolder, BookOrder model, int position) {
                                viewHolder.title.setText(model.getTitle());
                                viewHolder.description.setText(model.getDate());
                                Picasso.with(getApplicationContext()).load(model.getImage()).into(viewHolder.image);

                                book_id = getRef(position).getKey();

                                viewHolder.ripple.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(getApplicationContext(), BookPage.class);
                                        i.putExtra("book_id", book_id);
                                        startActivity(i);
                                    }
                                });

                            }
                        };
                        ebook_list.setAdapter(firebaseRecyclerAdapter);
                    } else if (dataSnapshot.child("type").equals("audioBook")) {
                        FirebaseRecyclerAdapter<BookOrder, BooksViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BookOrder, BooksViewHolder>
                                (BookOrder.class, R.layout.book_row, BooksViewHolder.class, audioBookDatabase) {
                            @Override
                            protected void populateViewHolder(BooksViewHolder viewHolder, BookOrder model, int position) {
                                viewHolder.title.setText(model.getTitle());
                                viewHolder.description.setText(model.getDate());
                                Picasso.with(getApplicationContext()).load(model.getImage()).into(viewHolder.image);

                                book_id = getRef(position).getKey();

                                viewHolder.ripple.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(getApplicationContext(), BookPage.class);
                                        i.putExtra("book_id", book_id);
                                        startActivity(i);
                                    }
                                });

                            }
                        };
                        audio_list.setAdapter(firebaseRecyclerAdapter);
                    }
                } else {
                    audio_list.setVisibility(View.GONE);
                    ebook_list.setVisibility(View.GONE);
                    ebook_all.setVisibility(View.GONE);
                    audio_all.setVisibility(View.GONE);
                    audio_title.setVisibility(View.GONE);
                    ebook_title.setVisibility(View.GONE);
                    Toast.makeText(Ebook_AudioBook_Page.this, "You have no Ebooks and Audio books checked out.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static String fmt(double d) {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_navigation, menu);
        final MenuItem cart = menu.findItem(R.id.app_bar_cart);
        View actionView = MenuItemCompat.getActionView(cart);
        countTV = (TextView) actionView.findViewById(R.id.count_text);
        countTV.setText(count);
        countTV.setTextColor(Color.WHITE);
        countTV.setVisibility(View.VISIBLE);
        final DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("User_Cart").child(user_id);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int count = (int) dataSnapshot.getChildrenCount();
                    String COUNT = fmt(count);
                    countTV.setText(COUNT);

                }else{
                    countTV.setText("0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Ebook_AudioBook_Page.this, CartPage.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                System.out.println("THis is what the textview says: " + countTV.getText().toString().trim());
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();

        int id = item.getItemId();
        Fragment fragment = null;

        //noinspection SimplifiableIfStatement

        if (id == R.id.help_button) {
            startActivity(new Intent(Ebook_AudioBook_Page.this, HelpHome.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.app_bar_cart){
            startActivity(new Intent(Ebook_AudioBook_Page.this, CartPage.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.nav_account){
            startActivity(new Intent(Ebook_AudioBook_Page.this, AccountPage.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.app_bar_sign_out) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();

            // [START config_signin]
            // Configure Google Sign In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            // [END config_signin]
            @SuppressLint("RestrictedApi") GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(new Intent(Ebook_AudioBook_Page.this, Login.class));
                    finish();
                }
            });
            return true;
        }

        if (id == R.id.app_bar_search){
            startActivity(new Intent(Ebook_AudioBook_Page.this, SearchPage.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            return true;
        }

        if (fragment == fragment){
            // Do nothing
        }

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        return super.onOptionsItemSelected(item);
    }

}
