package com.fbla.atlas.atlas.pages.student;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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

import com.facebook.login.LoginManager;
import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.activities.Login;
import com.fbla.atlas.atlas.help.HelpHome;
import com.fbla.atlas.atlas.models.Review;
import com.fbla.atlas.atlas.view_holders.ReviewViewHolder;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MoreReviews extends AppCompatActivity {

    RecyclerView list;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    String user_id, book_id;
    ProgressDialog progressDialog;
    TextView countTV;
    String count;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_reviews);

        book_id = getIntent().getStringExtra("book_id");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading all reviews");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Review_Comments").child(book_id);

        list = (RecyclerView) findViewById(R.id.more_reviews_list);
        list.hasFixedSize();
        list.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerAdapter<Review, ReviewViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Review, ReviewViewHolder>
                (Review.class, R.layout.review_design, ReviewViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(ReviewViewHolder viewHolder, Review model, int position) {
                viewHolder.name.setText(model.getName());
                viewHolder.date.setText(model.getDate());
                viewHolder.rating.setText("Rating: " + model.getRating());
                viewHolder.comment.setText(model.getComment());
                if (model.getImage().equals("default")){
                    Picasso.with(MoreReviews.this).load(R.drawable.logoatlas).into(viewHolder.image);
                }else{
                    Picasso.with(MoreReviews.this).load(model.getImage()).into(viewHolder.image);
                }
                progressDialog.dismiss();
            }
        };
        list.setAdapter(firebaseRecyclerAdapter);
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
                startActivity(new Intent(MoreReviews.this, CartPage.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                System.out.println("THis is what the textview says: " + countTV.getText().toString().trim());
            }
        });

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();

        int id = item.getItemId();
        Fragment fragment = null;

        //noinspection SimplifiableIfStatement

        if (id == R.id.help_button) {
            startActivity(new Intent(MoreReviews.this, HelpHome.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.app_bar_cart){
            startActivity(new Intent(MoreReviews.this, CartPage.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.nav_account){
            startActivity(new Intent(MoreReviews.this, AccountPage.class));
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
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(new Intent(MoreReviews.this, Login.class));
                    finish();
                }
            });
            return true;
        }

        if (id == R.id.app_bar_search){
            startActivity(new Intent(MoreReviews.this, SearchPage.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            return true;
        }

        if (fragment == fragment){
            // Do nothing
        }

        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        return super.onOptionsItemSelected(item);
    }
}
