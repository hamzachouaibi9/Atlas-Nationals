package com.fbla.atlas.atlas.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.models.UserData;
import com.fbla.atlas.atlas.pages.student.AccountPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UserInfoUpdate extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String user_id, Name, Phone, ImageURL, School, Grade, Age, Gender, PreviousImage;
    EditText name, phone, school;
    ImageView image;
    Spinner grade, age, gender;
    Button updateInformation;
    ArrayAdapter<CharSequence> adapter, adapter2, adapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_update);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Update Information");

        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        storageReference = FirebaseStorage.getInstance().getReference().child("User_Images");

        name = (EditText) findViewById(R.id.nameUpdate);
        phone = (EditText) findViewById(R.id.phoneUpdate);
        school = (EditText) findViewById(R.id.schoolUpdate);
        image = (ImageView) findViewById(R.id.imageUpdate);
        grade = (Spinner) findViewById(R.id.gradeUpdate);
        age = (Spinner) findViewById(R.id.ageUpdate);
        gender = (Spinner) findViewById(R.id.sexUpdate);
        updateInformation = (Button) findViewById(R.id.updateInfoBTN);

        adapter= ArrayAdapter.createFromResource(this, R.array.grade,android.R.layout.simple_spinner_dropdown_item);
        adapter2=ArrayAdapter.createFromResource(this, R.array.sex,android.R.layout.simple_spinner_dropdown_item);
        adapter3=ArrayAdapter.createFromResource(this, R.array.age,android.R.layout.simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        grade.setAdapter(adapter);

        gender.setAdapter(adapter2);

        age.setAdapter(adapter3);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("pic").exists()){

                    Picasso.with(UserInfoUpdate.this).load(dataSnapshot.child("pic").getValue().toString().trim()).into(image);
                    PreviousImage = dataSnapshot.child("pic").getValue().toString().trim();

                }else{

                    Picasso.with(UserInfoUpdate.this).load(R.drawable.logoatlas).into(image);

                }

                name.setText(dataSnapshot.child("name").getValue().toString().trim());
                school.setText(dataSnapshot.child("school").getValue().toString().trim());
                phone.setText(dataSnapshot.child("phone").getValue().toString().trim());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateUserInformation();

            }
        });

    }

    private void showImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200);
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
                    ImageURL = task.getResult().getDownloadUrl().toString();

                }
            });
            image.setImageURI(selectedImage);

        }

    }

    private void updateUserInformation() {
        Name = name.getText().toString().trim();
        Phone = phone.getText().toString().trim();
        School = school.getText().toString().trim();
        Grade = grade.getSelectedItem().toString().trim();
        Age = age.getSelectedItem().toString().trim();
        Gender = gender.getSelectedItem().toString().trim();
        Boolean haserror = false;

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
            Toast.makeText(UserInfoUpdate.this, "Please Pick Your Age", Toast.LENGTH_SHORT).show();
            haserror = true;
        }

        if (Gender.equals("Gender")){
            Toast.makeText(UserInfoUpdate.this, "Please Pick Your Gender", Toast.LENGTH_SHORT).show();
            haserror = true;
        }

        if (Grade.equals("Grade")){
            Toast.makeText(UserInfoUpdate.this, "Please Pick Your Grade", Toast.LENGTH_SHORT).show();
            haserror = true;
        }

        if (haserror){
            return;
        }


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating User Information");
        progressDialog.show();

        if (ImageURL == null){

            UserData userData = new UserData(Name,Phone,School,Grade,Gender,Age,PreviousImage);
            databaseReference.setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(new Intent(UserInfoUpdate.this, AccountPage.class));
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserInfoUpdate.this, "Something Unexpected Has Occured, Please Try Again!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });


        }else{
            UserData userData = new UserData(Name,Phone,School,Grade,Gender,Age,ImageURL);
            databaseReference.setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(new Intent(UserInfoUpdate.this, AccountPage.class));
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserInfoUpdate.this, "Something Unexpected Has Occured, Please Try Again!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

        }

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
