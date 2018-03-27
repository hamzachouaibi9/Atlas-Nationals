package com.fbla.atlas.atlas.pages.student;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.activities.Login;
import com.fbla.atlas.atlas.help.HelpHome;
import com.fbla.atlas.atlas.models.Book;
import com.fbla.atlas.atlas.view_holders.CartViewHolder;
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

public class HistoryPage extends AppCompatActivity {

    RecyclerView list;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    String user_id;
    ProgressDialog progressDialog;
    TextView countTV;
    String count;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);
        auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("History").child(user_id);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Book History");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("History");
        progressDialog.show();
        progressDialog.setCancelable(false);

        list = (RecyclerView) findViewById(R.id.history_list);
        list.hasFixedSize();
        list.setLayoutManager(new LinearLayoutManager(this));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    FirebaseRecyclerAdapter<Book, CartViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Book, CartViewHolder>
                            (Book.class, R.layout.book_layout_cart, CartViewHolder.class, databaseReference) {
                        @Override
                        protected void populateViewHolder(final CartViewHolder viewHolder, Book model, int position) {
                            viewHolder.title.setText(model.getTitle());
                            viewHolder.description.setText(model.getDescription());
                            Picasso.with(HistoryPage.this).load(model.getImage()).into(viewHolder.image);
                            progressDialog.dismiss();

                            final String book_id = getRef(position).getKey();

                            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ObjectAnimator animator = ObjectAnimator.ofFloat(viewHolder.delete,"rotation",360f);
                                    animator.setDuration(1000);
                                    AnimatorSet animatorSet = new AnimatorSet();
                                    animatorSet.playTogether(animator);
                                    animatorSet.start();
                                    animatorSet.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            databaseReference.child(book_id).removeValue();
                                        }
                                    });
                                }
                            });

                        }
                    };
                    list.setAdapter(firebaseRecyclerAdapter);
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(HistoryPage.this, "You don't have any book history", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(HistoryPage.this, CartPage.class));
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
            startActivity(new Intent(HistoryPage.this, HelpHome.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.app_bar_cart){
            startActivity(new Intent(HistoryPage.this, CartPage.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.nav_account){
            startActivity(new Intent(HistoryPage.this, AccountPage.class));
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
                    startActivity(new Intent(HistoryPage.this, Login.class));
                    finish();
                }
            });
            return true;
        }

        if (id == R.id.app_bar_search){
            startActivity(new Intent(HistoryPage.this, SearchPage.class));
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
