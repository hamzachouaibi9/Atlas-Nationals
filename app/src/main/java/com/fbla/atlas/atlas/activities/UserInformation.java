package com.fbla.atlas.atlas.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
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

public class UserInformation extends AppCompatActivity{

    Button register2;
    ImageView pic;

    EditText name, school, phone;
    Spinner grade, sex, age;

    StorageReference storageReference;

    String imageURL = null;
    TextView text;

    ArrayAdapter<CharSequence> adapter2;
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> adapter3;

    DatabaseReference databaseReference;

    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        progressDialog= new ProgressDialog(this);
        register2=(Button) findViewById(R.id.google_register);
        pic = (ImageView) findViewById(R.id.google_pic);
        name = (EditText) findViewById(R.id.google_name);
        school = (EditText) findViewById(R.id.google_school);
        phone = (EditText) findViewById(R.id.google_phone);
        grade = (Spinner) findViewById(R.id.google_grade);
        sex = (Spinner) findViewById(R.id.google_sex);
        age = (Spinner) findViewById(R.id.google_age);
        text = (TextView) findViewById(R.id.text);

        mAuth= FirebaseAuth.getInstance();

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

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
                text.setVisibility(View.GONE);
            }
        });

        register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo();
            }
        });

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
    private void showImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200);
    }


    public void UserInfo(){
        String Name = name.getText().toString().trim();
        String Phone = phone.getText().toString().trim();
        String School = school.getText().toString().trim();
        String Grade = grade.getSelectedItem().toString().trim();
        String Sex = sex.getSelectedItem().toString().trim();
        String Age = age.getSelectedItem().toString().trim();
        String uid = mAuth.getCurrentUser().getUid();

        if (Name.isEmpty()){
            name.setError("This field is required");
            name.requestFocus();
            return;
        }

        if (Phone.isEmpty()){
            phone.setError("This field is required");
            phone.requestFocus();
            return;
        }

        if (School.isEmpty()){
            school.setError("This field is required");
            school.requestFocus();
            return;
        }

        if (Grade.equals("Grade")){
            Toast.makeText(this, "Grade can't be Grade", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Sex.equals("Gender")){
            Toast.makeText(this, "Gender can't be Gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Age.equals("Age")){
            Toast.makeText(this, "Age can't be Age", Toast.LENGTH_SHORT).show();
            return;
        }

        UserData userData = new UserData(Name,Phone, School, Grade, Sex, Age, imageURL);

        databaseReference.child(uid).setValue(userData);
        startActivity(new Intent(UserInformation.this, MainActivityNavigation.class));

    }

}
