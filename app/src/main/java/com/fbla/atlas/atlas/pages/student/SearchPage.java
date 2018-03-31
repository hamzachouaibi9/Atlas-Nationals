package com.fbla.atlas.atlas.pages.student;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.activities.Login;
import com.fbla.atlas.atlas.help.HelpHome;
import com.fbla.atlas.atlas.view_holders.BooksViewHolder;
import com.fbla.atlas.atlas.models.Book;
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

import static android.content.ContentValues.TAG;

public class SearchPage extends AppCompatActivity {

    EditText search;
    RecyclerView list;
    DatabaseReference database;
    ProgressDialog progressDialog;
    String user_id, count;
    TextView countTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid();

//        find the database and recyclerview
        list = (RecyclerView) findViewById(R.id.search_list);
        search = (EditText) findViewById(R.id.search_text);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.hasFixedSize();

        database = FirebaseDatabase.getInstance().getReference().child("Books").child("AllBooks");

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                final FirebaseRecyclerAdapter<Book, BooksViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Book, BooksViewHolder>
                        (Book.class, R.layout.book_row, BooksViewHolder.class, database) {
                    @Override
                    protected void populateViewHolder(BooksViewHolder viewHolder, Book model, int position) {
                        viewHolder.title.setText(model.getTitle());
                        viewHolder.description.setText(model.getDescription());
                        Picasso.with(SearchPage.this).load(model.getImage()).into(viewHolder.image);

                        Log.d(TAG, "populateViewHolder: " + getRef(position));

                        final String book_id = getRef(position).getKey();
                        viewHolder.ripple.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SearchPage.this, BookPage.class);
                                intent.putExtra("book_id", book_id);
                                startActivity(intent);
                            }
                        });
                    }
                };
                list.setAdapter(firebaseRecyclerAdapter);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null){
                    final FirebaseRecyclerAdapter<Book, BooksViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Book, BooksViewHolder>
                            (Book.class, R.layout.book_row, BooksViewHolder.class, database) {
                        @Override
                        protected void populateViewHolder(BooksViewHolder viewHolder, Book model, int position) {
                            viewHolder.title.setText(model.getTitle());
                            viewHolder.description.setText(model.getDescription());
                            Picasso.with(SearchPage.this).load(model.getImage()).into(viewHolder.image);

                            Log.d(TAG, "populateViewHolder: " + getRef(position));

                            final String book_id = getRef(position).getKey();
                            viewHolder.ripple.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SearchPage.this, BookPage.class);
                                    intent.putExtra("book_id", book_id);
                                    startActivity(intent);
                                }
                            });
                        }
                    };
                    list.setAdapter(firebaseRecyclerAdapter);
                }else{
                    Query query = database.orderByChild("stitle").startAt(String.valueOf(s)).endAt(s + "\uf8ff");

                    final FirebaseRecyclerAdapter<Book, BooksViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Book, BooksViewHolder>
                            (Book.class, R.layout.book_row, BooksViewHolder.class, query) {
                        @Override
                        protected void populateViewHolder(BooksViewHolder viewHolder, Book model, int position) {
                            viewHolder.title.setText(model.getTitle());
                            viewHolder.description.setText(model.getDescription());
                            Picasso.with(SearchPage.this).load(model.getImage()).into(viewHolder.image);

                            Log.d(TAG, "populateViewHolder: " + getRef(position));

                            final String book_id = getRef(position).getKey();
                            viewHolder.ripple.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SearchPage.this, BookPage.class);
                                    intent.putExtra("book_id", book_id);
                                    startActivity(intent);
                                }
                            });
                        }
                    };
                    list.setAdapter(firebaseRecyclerAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Query query = database.orderByChild("stitle").startAt(String.valueOf(s)).endAt(s + "\uf8ff");

                final FirebaseRecyclerAdapter<Book, BooksViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Book, BooksViewHolder>
                        (Book.class, R.layout.book_row, BooksViewHolder.class, query) {
                    @Override
                    protected void populateViewHolder(BooksViewHolder viewHolder, Book model, int position) {
                        viewHolder.title.setText(model.getTitle());
                        viewHolder.description.setText(model.getDescription());
                        Picasso.with(SearchPage.this).load(model.getImage()).into(viewHolder.image);

                        Log.d(TAG, "populateViewHolder: " + getRef(position));

                        final String book_id = getRef(position).getKey();
                        viewHolder.ripple.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SearchPage.this, BookPage.class);
                                intent.putExtra("book_id", book_id);
                                startActivity(intent);
                            }
                        });
                    }
                };
                list.setAdapter(firebaseRecyclerAdapter);
            }

        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                search.clearFocus();
            }
        }, 300);


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
                startActivity(new Intent(SearchPage.this, CartPage.class));
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
            startActivity(new Intent(SearchPage.this, HelpHome.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.app_bar_cart){
            startActivity(new Intent(SearchPage.this, CartPage.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.nav_account){
            startActivity(new Intent(SearchPage.this, AccountPage.class));
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
                    startActivity(new Intent(SearchPage.this, Login.class));
                    finish();
                }
            });
            return true;
        }

        if (fragment == fragment){
            // Do nothing
        }

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        return super.onOptionsItemSelected(item);
    }

}