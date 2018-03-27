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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CheckoutPage extends AppCompatActivity {

    RecyclerView list;
    DatabaseReference databaseReference, database;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    SimpleDateFormat simpleDateFormat;
    Calendar calendar;
    String DATE, count, user_id;
    TextView countTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_page);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Checked Out Books");

        //        find and initialize firebase and recyclerview
        list = (RecyclerView) findViewById(R.id.orders_list);

        auth = FirebaseAuth.getInstance();
        final String user_id = auth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("User_Checkout").child(user_id);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.hasFixedSize();
//        show progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Checked Out Books");
        progressDialog.setCancelable(false);
        progressDialog.show();
//        create a date to look for today's date
        simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        calendar = Calendar.getInstance();
        DATE = simpleDateFormat.format(calendar.getTime());

//          add a value event listener
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    FirebaseRecyclerAdapter<BookOrder, BooksViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BookOrder, BooksViewHolder>
                            (BookOrder.class,R.layout.book_row,BooksViewHolder.class,databaseReference) {
                        @Override
                        protected void populateViewHolder(BooksViewHolder viewHolder, final BookOrder model, int position) {
//                            populate the view
                            viewHolder.title.setText(model.getTitle());
                            viewHolder.title.setTextColor(Color.parseColor("#000000"));
                            viewHolder.description.setText("Due Date: " + model.getDate());
                            viewHolder.description.setTextColor(Color.parseColor("#000000"));
                            Picasso.with(CheckoutPage.this).load(model.getImage()).into(viewHolder.image);
                            progressDialog.cancel();

                            final String book_id = getRef(position).getKey();

                            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(CheckoutPage.this, BookPage.class);
                                    intent.putExtra("book_id", book_id);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                                }
                            });

                        }
                    };
                    list.setAdapter(firebaseRecyclerAdapter);
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(CheckoutPage.this,"You Have No Checked Out Books",Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(CheckoutPage.this, CartPage.class));
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
            startActivity(new Intent(CheckoutPage.this, HelpHome.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.app_bar_cart){
            startActivity(new Intent(CheckoutPage.this, CartPage.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.nav_account){
            startActivity(new Intent(CheckoutPage.this, AccountPage.class));
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
                    startActivity(new Intent(CheckoutPage.this, Login.class));
                    finish();
                }
            });
            return true;
        }

        if (id == R.id.app_bar_search){
            startActivity(new Intent(CheckoutPage.this, SearchPage.class));
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
