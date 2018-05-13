package com.fbla.atlas.atlas.activities;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.pages.student.AboutPageFragment;
import com.fbla.atlas.atlas.pages.student.CartPage;
import com.fbla.atlas.atlas.pages.student.CheckoutPage;
import com.fbla.atlas.atlas.pages.student.Ebook_AudioBook_Page;
import com.fbla.atlas.atlas.pages.student.ErrorFoundFragment;
import com.fbla.atlas.atlas.pages.student.HistoryPage;
import com.fbla.atlas.atlas.pages.student.OverduePage;
import com.fbla.atlas.atlas.pages.student.AccountPage;
import com.fbla.atlas.atlas.pages.student.HomeFragment;
import com.fbla.atlas.atlas.pages.student.MapFragment;
import com.fbla.atlas.atlas.pages.student.ReservedPage;
import com.fbla.atlas.atlas.pages.student.SearchPage;
import com.fbla.atlas.atlas.pages.student.ShareFragment;
import com.fbla.atlas.atlas.help.HelpHome;
import com.fbla.atlas.atlas.services.BookNotificationServiceUser;
import com.fbla.atlas.atlas.services.OverdueBookService;
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

public class MainActivityNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference databaseReference;
    FirebaseAuth auth;
    ImageView imageView;
    TextView title,email;
    GoogleSignInClient mGoogleSignInClient;
    TextView countTV;
    String count;
    String user_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);


        startService(new Intent(MainActivityNavigation.this, BookNotificationServiceUser.class));
        startService(new Intent(MainActivityNavigation.this, OverdueBookService.class));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.navigation_home);

        View view = navigationView.getHeaderView(0);
        imageView = view.findViewById(R.id.nav_drawer_image);
        title = view.findViewById(R.id.nav_drawer_name);
        email = view.findViewById(R.id.nav_drawer_email);

        email.setText(auth.getCurrentUser().getEmail());

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").exists()){
                    String NAME = dataSnapshot.child("name").getValue().toString().trim();
                    title.setText(NAME);
                } else{
                    startActivity(new Intent(MainActivityNavigation.this, UserInformation.class));
                }

                if (dataSnapshot.child("pic").exists()){
                    String IMAGE = dataSnapshot.child("pic").getValue().toString().trim();
                    Picasso.with(MainActivityNavigation.this).load(IMAGE).into(imageView);
                }else{
                    Picasso.with(MainActivityNavigation.this).load(R.drawable.logoatlas).into(imageView);
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                startActivity(new Intent(MainActivityNavigation.this, CartPage.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                System.out.println("THis is what the textview says: " + countTV.getText().toString().trim());
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();
        Fragment fragment = null;

        //noinspection SimplifiableIfStatement

       if (id == R.id.help_button) {
           startActivity(new Intent(MainActivityNavigation.this, HelpHome.class));
           overridePendingTransition(R.anim.fadein, R.anim.fadeout);
       }

       if (id == R.id.app_bar_cart){
           startActivity(new Intent(MainActivityNavigation.this, CartPage.class));
           overridePendingTransition(R.anim.fadein, R.anim.fadeout);
       }

        if (id == R.id.nav_account){
            startActivity(new Intent(MainActivityNavigation.this, AccountPage.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.app_bar_sign_out) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(MainActivityNavigation.this, Login.class));
            finish();
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
                    startActivity(new Intent(MainActivityNavigation.this, Login.class));
                    finish();
                }
            });
            return true;
        }

        if (id == R.id.app_bar_search){
            startActivity(new Intent(MainActivityNavigation.this, SearchPage.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            return true;
        }

        if (fragment == fragment){
            // Do nothing
        }

        return super.onOptionsItemSelected(item);
    }

    public void displaySelectedScreen(int id){
        Fragment fragment = null;

        switch (id){
            case R.id.navigation_home:
                fragment = new HomeFragment();
                FragmentTransaction ft00 = getSupportFragmentManager().beginTransaction();
                ft00.replace(R.id.navigation_content, fragment);
                ft00.commit();
                break;
            case R.id.nav_checkedOut:
                startActivity(new Intent(MainActivityNavigation.this, CheckoutPage.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                break;
            case R.id.nav_reserved:
                startActivity(new Intent(MainActivityNavigation.this, ReservedPage.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                break;
            case R.id.nav_map:
                fragment = new MapFragment();
                FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                ft3.replace(R.id.navigation_content, fragment);
                ft3.addToBackStack(null);
                ft3.commit();
                break;

            case R.id.nav_overDue:
                startActivity(new Intent(MainActivityNavigation.this, OverduePage.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
            case R.id.nav_share:
                fragment = new ShareFragment();
                FragmentTransaction ft6 = getSupportFragmentManager().beginTransaction();
                ft6.replace(R.id.navigation_content, fragment);
                ft6.addToBackStack(null);
                ft6.commit();
                break;
            case R.id.nav_error:
                fragment = new ErrorFoundFragment();
                FragmentTransaction ft7 = getSupportFragmentManager().beginTransaction();
                ft7.replace(R.id.navigation_content, fragment);
                ft7.addToBackStack(null);
                ft7.commit();
                break;
            case R.id.nav_about:
                fragment = new AboutPageFragment();
                FragmentTransaction ft10 = getSupportFragmentManager().beginTransaction();
                ft10.replace(R.id.navigation_content, fragment);
                ft10.addToBackStack(null);
                ft10.commit();
                break;
            case R.id.nav_history:
                startActivity(new Intent(MainActivityNavigation.this, HistoryPage.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
        }

        if (fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.navigation_content, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);

        return true;


    }
}
