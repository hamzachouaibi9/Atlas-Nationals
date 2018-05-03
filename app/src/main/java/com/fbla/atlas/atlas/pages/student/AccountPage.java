package com.fbla.atlas.atlas.pages.student;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.activities.Login;
import com.fbla.atlas.atlas.activities.UserInfoUpdate;
import com.fbla.atlas.atlas.help.HelpHome;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class AccountPage extends AppCompatActivity{


    TextView name, school, sex, phone, grade, age, email;

    DatabaseReference databaseReference;

    ImageView pic;

    StorageReference storageReference;

    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;

    GoogleSignInClient mGoogleSignInClient;

    String user_id, count;

    TextView countTV;

    Button updateBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        initialize all of the firebase i.e: database, storage, and auth
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

//        start a progress dialog to show that user data is being retrieved
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading User Information From Atlas Archives");
        progressDialog.show();

//        find all of the views for the text
        name = (TextView) findViewById(R.id.date);
        grade = (TextView) findViewById(R.id.grade);
        sex = (TextView) findViewById(R.id.sex);
        school = (TextView) findViewById(R.id.school);
        phone = (TextView) findViewById(R.id.phone);
        pic = (ImageView) findViewById(R.id.pic);
        age = (TextView) findViewById(R.id.Age);
        email = (TextView) findViewById(R.id.email);
        updateBtn = (Button) findViewById(R.id.updateBtn);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountPage.this, UserInfoUpdate.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

//            String the user id from firebase auth to get the user data from the firebase database
        user_id = firebaseAuth.getCurrentUser().getUid();

//                database reference for getting the user information
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //                String the data from firebase database
                String NAME = dataSnapshot.child("Users").child(user_id).child("name").getValue().toString().trim();
                String GRADE = dataSnapshot.child("Users").child(user_id).child("grade").getValue().toString().trim();
                String SEX = dataSnapshot.child("Users").child(user_id).child("sex").getValue().toString().trim();
                String SCHOOL = dataSnapshot.child("Users").child(user_id).child("school").getValue().toString().trim();
                String PHONE = dataSnapshot.child("Users").child(user_id).child("phone").getValue().toString().trim();
                String AGE = dataSnapshot.child("Users").child(user_id).child("age").getValue().toString().trim();
//                for the image check whether it exists or not then string the data and put it into imageview
                if (dataSnapshot.child("Users").child(user_id).child("pic").exists()) {
                    String IMAGE = dataSnapshot.child("Users").child(user_id).child("pic").getValue().toString().trim();
                    Picasso.with(AccountPage.this).load(IMAGE).into(pic);
                } else {
                    Picasso.with(AccountPage.this).load(R.drawable.logoatlas).into(pic);
                }
//                set the text with the strings that were called above
                name.setText(NAME);
                grade.setText(GRADE);
                sex.setText(SEX);
                school.setText(SCHOOL);
                phone.setText(PHONE);
                age.setText(AGE);
                email.setText(firebaseAuth.getCurrentUser().getEmail());
//                dismiss the progress dialog when all of the data has been retrieved
                progressDialog.dismiss();

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
                startActivity(new Intent(AccountPage.this, CartPage.class));
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
            startActivity(new Intent(AccountPage.this, HelpHome.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.app_bar_cart){
            startActivity(new Intent(AccountPage.this, CartPage.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.nav_account){
//            startActivity(new Intent(AccountPage.this, AccountPage.class));
//            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
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
                    startActivity(new Intent(AccountPage.this, Login.class));
                    finish();
                }
            });
            return true;
        }

        if (id == R.id.app_bar_search){
            startActivity(new Intent(AccountPage.this, SearchPage.class));
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