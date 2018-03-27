package com.fbla.atlas.atlas.help;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.activities.Login;
import com.fbla.atlas.atlas.pages.student.CartPage;
import com.fbla.atlas.atlas.pages.student.SearchPage;
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

public class HelpHome extends AppCompatActivity {

    Button checkout,signOut,map,search,sharing,account,checking, bug;
    String user_id, count;
    TextView countTV;
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_help_home);

        auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Help");

        checkout = (Button) findViewById(R.id.checkout_help);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpHome.this, CheckoutHelp.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

            }
        });
        checking = (Button) findViewById(R.id.view_help);
        checking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpHome.this, CheckingHelp.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

            }
        });
        signOut = (Button) findViewById(R.id.signout_help);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpHome.this, SignOutHelp.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

            }
        });
        map = (Button) findViewById(R.id.map_help);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpHome.this, MapHelp.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

            }
        });
        search = (Button) findViewById(R.id.search_help);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpHome.this, SearchHelp.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

            }
        });
        account = (Button) findViewById(R.id.account_help);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpHome.this, AccountHelp.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

            }
        });
        sharing = (Button) findViewById(R.id.share_help);
        sharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpHome.this, SharingHelp.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

            }
        });
        bug = (Button) findViewById(R.id.bug_help);
        bug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpHome.this, BugHelp.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

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
                startActivity(new Intent(HelpHome.this, CartPage.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                System.out.println("THis is what the textview says: " + countTV.getText().toString().trim());
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
            startActivity(new Intent(HelpHome.this, HelpHome.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.app_bar_cart){
            startActivity(new Intent(HelpHome.this, CartPage.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.nav_account){
            startActivity(new Intent(HelpHome.this, CartPage.class));
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
                    startActivity(new Intent(HelpHome.this, Login.class));
                    finish();
                }
            });
            return true;
        }

        if (id == R.id.app_bar_search){
            startActivity(new Intent(HelpHome.this, SearchPage.class));
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
