package com.fbla.atlas.atlas.activities;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fbla.atlas.atlas.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Login extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG = "Login";
    EditText editTextemail;
    EditText editTextpassword;
    Button login;
    Button register;

    SignInButton googleBtn;

    ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    DatabaseReference databaseReference;

    CallbackManager mCallbackManager;

    GoogleSignInClient mGoogleSignInClient;

    private static final int RC_SIGN_IN = 9001;

    android.support.design.widget.FloatingActionButton fab_login;
    ProgressBar progressBar;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(this);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        fab_login = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging In User");
        progressDialog.setCancelable(false);

        editTextemail = (EditText) findViewById(R.id.editTextemail);
        editTextpassword = (EditText) findViewById(R.id.admin_password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        googleBtn = (SignInButton) findViewById(R.id.googleSignin_Btn);


        login.setOnClickListener(this);
        register.setOnClickListener(this);
        googleBtn.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            final String uid = mAuth.getCurrentUser().getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("isAdmin").exists()) {
                        startActivity(new Intent(Login.this, AdminMainActivity.class));
                    } else {
                        Intent i=new Intent(Login.this, MainActivityNavigation.class);
                        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(Login.this, gso);


        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.facebookLogin_Btn);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
    }

    public void onClick(View v) {
        Log.d(TAG, "onClick: a button has been clicked");
        switch (v.getId()){
            case R.id.login:
                userLogin();
                Log.d(TAG, "onClick: LOGIN");
                printKeyHash(this);
                break;

            case R.id.googleSignin_Btn:
                signIn();
                break;

            case R.id.register:
                startActivity(new Intent(Login.this, SignUp.class));
                overridePendingTransition(R.anim.rotate_out,R.anim.rotate_in);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
        //Facebook Result
        mCallbackManager.onActivityResult(requestCode, resultCode, data);



    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        progressDialog.show();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            String uid = task.getResult().getUser().getUid();
                            DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            data.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        startActivity(new Intent(Login.this, MainActivityNavigation.class));
                                    }else{
                                        startActivity(new Intent(Login.this, UserInformation.class));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        progressDialog.show();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(Login.this, "FacebookLogin Successful", Toast.LENGTH_SHORT).show();
                            String uid = task.getResult().getUser().getUid();
                            DatabaseReference  data = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            data.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        startActivity(new Intent(Login.this, MainActivityNavigation.class));
                                    }else{
                                        startActivity(new Intent(Login.this, UserInformation.class));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        progressDialog.dismiss();
                        // [END_EXCLUDE]
                    }
                });
    }


    private void animateButtonWidth() {
        ValueAnimator anim = ValueAnimator.ofInt(login.getMeasuredWidth(), getFabWidth());

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = login.getLayoutParams();
                layoutParams.width = val;
                login.requestLayout();
            }
        });
        anim.setDuration(250);
        anim.start();
    }


    private void userLogin() {

        final String email=editTextemail.getText().toString().trim();
        final String password=editTextpassword.getText().toString().trim();

        boolean haserror=false;


        if (email.isEmpty()){
            editTextemail.setError("Email is required");
            editTextemail.requestFocus();
            haserror=true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextemail.setError("Please enter a valid email");
            haserror=true;
        }

        if (password.isEmpty()){
            editTextpassword.setError("Password is required");
            editTextpassword.requestFocus();
            haserror=true;
        }


        if (haserror){
            return;
        }

        final String EMAIL = editTextemail.getText().toString().trim();
        final String Password = editTextpassword.getText().toString().trim();

        progressDialog.setMessage("Logging User To Atlas");
        progressDialog.show();

        animateButtonWidth();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    final String uid = mAuth.getCurrentUser().getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("isAdmin").exists()) {
                                startActivity(new Intent(Login.this, AdminMainActivity.class));
                            } else if (dataSnapshot.exists()) {
                                Intent i=new Intent(Login.this, MainActivityNavigation.class);
                                i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            } else {
                                startActivity(new Intent(Login.this, UserInformation.class));
                                Intent i=new Intent(Login.this, UserInformation.class);
                                i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else{
                    Toast.makeText(Login.this, "Email or Password Information Is Incorrect", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    private int getFabWidth() {
        return (int) getResources().getDimension(R.dimen.fab_size);
    }
}
