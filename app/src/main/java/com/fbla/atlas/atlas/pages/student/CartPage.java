package com.fbla.atlas.atlas.pages.student;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
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

public class CartPage extends AppCompatActivity {

    RecyclerView list;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    TextView countTV;
    String count;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Carted Books");

        //        find the recyclerview and initialize it
        list = (RecyclerView) findViewById(R.id.cart_list);
//        initialize firebase auth
        auth = FirebaseAuth.getInstance();
        String user_id = auth.getCurrentUser().getUid();
//        initialize firebase database and set the layout manager for recyclerview
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User_Cart").child(user_id);
        list.setLayoutManager(new LinearLayoutManager(this));
//        show progress dialog to indicate that data is being retrieved
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading User's Cart");
        progressDialog.setCancelable(false);
        progressDialog.show();
//        database event listener
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//               check if the data exists
                if (dataSnapshot.exists()){
//                      if the data does then put it into a firebaserecycleradapter
                    FirebaseRecyclerAdapter<Book, CartViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Book, CartViewHolder>
                            (Book.class,R.layout.book_layout_cart, CartViewHolder.class, databaseReference) {
                        @Override
                        protected void populateViewHolder(final CartViewHolder viewHolder, Book model, final int position) {
//                            dismiss the progress dialog when the data has been retrieved
                            progressDialog.cancel();
//                            set the text with data to populate the views
                            viewHolder.title.setText(model.getTitle());
                            viewHolder.description.setText(model.getDescription());
                            viewHolder.title.setTextColor(Color.parseColor("#000000"));
                            viewHolder.description.setTextColor(Color.parseColor("#000000"));
                            Picasso.with(CartPage.this).load(model.getImage()).into(viewHolder.image);

//                            string the book id
                            final String book_id = getRef(position).getKey();

                            viewHolder.ripple.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(CartPage.this, BookPage.class);
                                            intent.putExtra("book_id", book_id);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                                        }
                                    }, 300);


                                }
                            });

//                            set the on click listener for the delete button
                            viewHolder.deleteR.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            ObjectAnimator animator = ObjectAnimator.ofFloat(viewHolder.delete,"rotation",-360f);
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

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                        }
                    };
                    firebaseRecyclerAdapter.notifyDataSetChanged();
//                        set the recyclerview adapter to the fireabase recycler adapter
                    list.setAdapter(firebaseRecyclerAdapter);


                }else{
//                    if the data doesn't exist dismiss the progress dialog and make a toast saying that it doesn't exist
                    progressDialog.dismiss();
                    Toast.makeText(CartPage.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
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

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();

        int id = item.getItemId();
        Fragment fragment = null;

        //noinspection SimplifiableIfStatement

        if (id == R.id.help_button) {
            startActivity(new Intent(CartPage.this, HelpHome.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.app_bar_cart){
            startActivity(new Intent(CartPage.this, CartPage.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.nav_account){
            startActivity(new Intent(CartPage.this, AccountPage.class));
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
                    startActivity(new Intent(CartPage.this, Login.class));
                    finish();
                }
            });
            return true;
        }

        if (id == R.id.app_bar_search){
            startActivity(new Intent(CartPage.this, SearchPage.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            return true;
        }

        if (fragment == fragment){
            // Do nothing
        }

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

}
