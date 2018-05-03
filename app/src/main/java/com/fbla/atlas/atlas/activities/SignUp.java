package com.fbla.atlas.atlas.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.models.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    Button register2;
    TextView login2;
    EditText editTextEmail;
    EditText editTextPassword;

    ImageView pic;

    EditText name, school, phone;
    Spinner grade, sex, age;

    StorageReference storageReference;

    TextView text;

    String imageURL;

    private static final String TAG = "SignUp";

    ArrayAdapter<CharSequence> adapter2;
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> adapter3;

    DatabaseReference databaseReference;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog= new ProgressDialog(this);

        register2=(Button) findViewById(R.id.register2);

        text = (TextView) findViewById(R.id.text);

        login2=(TextView) findViewById(R.id.login2);

        pic = (ImageView) findViewById(R.id.pic);

        editTextEmail=(EditText) findViewById(R.id.register_email);
        editTextPassword=(EditText) findViewById(R.id.register_pass);


        mAuth= FirebaseAuth.getInstance();

        findViewById(R.id.pic).setOnClickListener(this);
        findViewById(R.id.register2).setOnClickListener(this);
        findViewById(R.id.login2).setOnClickListener(this);

        name = (EditText) findViewById(R.id.register_name);
        school = (EditText) findViewById(R.id.register_school);
        phone = (EditText) findViewById(R.id.register_phone);

        grade = (Spinner) findViewById(R.id.grade);
        sex = (Spinner) findViewById(R.id.sex);
        age = (Spinner) findViewById(R.id.age);

        adapter=ArrayAdapter.createFromResource(this, R.array.grade,android.R.layout.simple_spinner_dropdown_item);
        adapter2=ArrayAdapter.createFromResource(this, R.array.sex,android.R.layout.simple_spinner_dropdown_item);
        adapter3=ArrayAdapter.createFromResource(this, R.array.age,android.R.layout.simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        grade.setAdapter(adapter);

        sex.setAdapter(adapter2);

        age.setAdapter(adapter3);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        storageReference = FirebaseStorage.getInstance().getReference().child("User_Image");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            UploadTask uploadTask = storageReference.child(selectedImage.getLastPathSegment()).putFile(selectedImage);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    imageURL = task.getResult().getDownloadUrl().toString();

                }
            });
            pic.setImageURI(selectedImage);

        }

    }

    public void registerUser(){
        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();
        String Phone = phone.getText().toString().trim();
        String Name = name.getText().toString().trim();
        String School = school.getText().toString().trim();
        String Grade = grade.getSelectedItem().toString().trim();
        String Age = age.getSelectedItem().toString().trim();
        String Sex = sex.getSelectedItem().toString().trim();
        boolean haserror=false;


        if (email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            haserror=true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email");
            haserror=true;
        }

        if (phone.length()<0){
            phone.setError("Phone Number is Required");
            phone.requestFocus();
            haserror=true;
        }

        if (phone.length()<10){
            phone.setError("Minimum length of numbers is 10");
            phone.requestFocus();
            haserror=true;
        }

        if (password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            haserror=true;
        }

        if (password.length()<6){
            editTextPassword.setError("Minimum length of characters is 6");
            editTextPassword.requestFocus();
            haserror=true;
        }
        if (Name.isEmpty()){
            name.setError("Full Name Is Required");
            name.requestFocus();
            haserror = true;
        }

        if (Phone.isEmpty()){
            phone.setError("Please Enter a 10 Digit Phone Number");
            phone.requestFocus();
            haserror = true;
        }
        if (Phone.length()<10){
            phone.setError("Please Enter a 10 Digit Phone Number");
            phone.requestFocus();
            haserror = true;
        }

        if (School.isEmpty()){
            school.setError("Please Enter Your School Name");
            school.requestFocus();
            haserror = true;
        }

        if (Age.equals("Age")){
            Toast.makeText(SignUp.this, "Please Pick Your Age", Toast.LENGTH_SHORT).show();
            haserror = true;
        }

        if (Sex.equals("Gender")){
            Toast.makeText(SignUp.this, "Please Pick Your Gender", Toast.LENGTH_SHORT).show();
            haserror = true;
        }

        if (Grade.equals("Grade")){
            Toast.makeText(SignUp.this, "Please Pick Your Grade", Toast.LENGTH_SHORT).show();
            haserror = true;
        }


        if (haserror){
            return;
        }

        progressDialog.setMessage("Registering User To Atlas Book Records");
        progressDialog.show();


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()){

                     Intent i = new Intent(SignUp.this, MainActivityNavigation.class);
                     i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                     UserInfo(task.getResult().getUser().getUid());
                     startActivity(i);
                     Log.d(TAG, "onComplete: USERID" + task.getResult().getUser().getUid());

                }else{

                    if (task.getException()instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"Something Unexpected Occurred Please Try Again",Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getApplicationContext(),"You are already registered, Please Login",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register2:
                registerUser();
                break;

            case R.id.login2:
                startActivity(new Intent(this, Login.class));
                break;

            case R.id.pic:
                showImage();
                break;
        }

    }

/*
This will get the image action
 */
    private void showImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200);
    }

    public void UserInfo(String uid) {
        final String Name = name.getText().toString().trim();
        String Phone = phone.getText().toString().trim();
        String School = school.getText().toString().trim();
        String Grade = grade.getSelectedItem().toString().trim();
        String Sex = sex.getSelectedItem().toString().trim();
        String Age = age.getSelectedItem().toString().trim();

        UserData userData = new UserData(Name, Phone, School, Grade, Sex, Age, imageURL);

        databaseReference.child(uid).setValue(userData);
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
