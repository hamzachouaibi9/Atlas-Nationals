package com.fbla.atlas.atlas.activities;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fbla.atlas.atlas.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordReset extends AppCompatActivity {

    EditText email;
    Button btn;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Password Reset");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.password_reset_email);
        btn = (Button) findViewById(R.id.passwordReset_BTN);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String EMAIl = email.getText().toString().trim();

                if (EMAIl.isEmpty()){
                    email.setError("Email is required");
                    email.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(EMAIl).matches()){
                    email.setError("Email address is not formatted correctly");
                    email.requestFocus();
                    return;
                }

                progressDialog.setMessage("Sending email to " + EMAIl);
                progressDialog.show();

                auth.sendPasswordResetEmail(EMAIl).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(PasswordReset.this, "An Password reset email has been sent to your email. " +
                                "Follow the steps and come back to login with your new password", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });

            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
