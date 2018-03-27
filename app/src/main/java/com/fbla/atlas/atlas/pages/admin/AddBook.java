package com.fbla.atlas.atlas.pages.admin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.models.AdminBook;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddBook.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddBook#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBook extends Fragment {

    TextInputEditText title, author, description;
    ImageButton image;
    Spinner genre;
    Button add;
    ArrayAdapter<CharSequence> adapter;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String imageURL;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddBook() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddBook.
     */
    // TODO: Rename and change types and number of parameters
    public static AddBook newInstance(String param1, String param2) {
        AddBook fragment = new AddBook();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_books, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = (TextInputEditText) view.findViewById(R.id.book_add_title);
        author = (TextInputEditText) view.findViewById(R.id.book_add_author);
        description = (TextInputEditText) view.findViewById(R.id.book_add_description);
        image = (ImageButton) view.findViewById(R.id.book_add_image);
        genre = (Spinner) view.findViewById(R.id.book_add_genre);
        add = (Button) view.findViewById(R.id.book_add_btn);

        adapter=ArrayAdapter.createFromResource(getContext(), R.array.book_add_genre,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genre.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Books").child("AllBooks");
        storageReference = FirebaseStorage.getInstance().getReference().child("Book_Images");


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();

            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            image.setImageURI(selectedImage);

        }

    }

    private void showImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200);
    }

    public void addBook(){
        String Title = title.getEditableText().toString().trim();
        String Description = description.getEditableText().toString().trim();
        String Author = author.getEditableText().toString().trim();
        String Genre = genre.getSelectedItem().toString().trim();
        String sTitle = title.getEditableText().toString().toLowerCase().trim();
        AdminBook book = new AdminBook(Title,sTitle,Description,imageURL,Genre,Author);

        databaseReference.child(Title).setValue(book)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(),"Book Has Successfully been added",Toast.LENGTH_SHORT).show();
                        title.setText("");
                        description.setText("");
                        author.setText("");
                        genre.setSelection(0);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Book Has Failed To Upload",Toast.LENGTH_SHORT).show();
                    }
                });
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
