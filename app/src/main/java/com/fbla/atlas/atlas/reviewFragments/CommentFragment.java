package com.fbla.atlas.atlas.reviewFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.models.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends Fragment {
    String user_id, book_id, Comment, imageURL, name;
    FirebaseAuth auth;
    DatabaseReference databaseReference, userDatabase;
    EditText comment;
    Button next;
    ImageView image;
    String Date;
    String rating;
    SimpleDateFormat simpleDateFormat;
    Calendar calendar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommentFragment newInstance(String param1, String param2) {
        CommentFragment fragment = new CommentFragment();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        book_id = bundle.getString("book_id");

        auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Reviews").child(user_id).child(book_id);
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        image = (ImageView) view.findViewById(R.id.user_image);
        next = (Button) view.findViewById(R.id.submitBtn);
        comment = (EditText) view.findViewById(R.id.comment_text);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rating = dataSnapshot.child("rating").getValue().toString().trim();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("pic").exists()) {
                    imageURL = dataSnapshot.child("pic").getValue().toString().trim();
                    Picasso.with(getContext()).load(imageURL).into(image);
                } else {
                    imageURL = "default";
                    Picasso.with(getContext()).load(R.drawable.logoatlas).into(image);
                }

                name = dataSnapshot.child("name").getValue().toString().trim();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        calendar = Calendar.getInstance();
        Date = simpleDateFormat.format(calendar.getTime());

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment = comment.getEditableText().toString().trim();
                if (Comment.isEmpty()){
                    comment.setError("A comment is required to continue");
                    comment.requestFocus();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("book_id", book_id);
                ReviewDoneFragment fragment = new ReviewDoneFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                transaction.replace(R.id.review_FrameLayout, fragment);
                transaction.commit();
                Review data = new Review(rating,Comment, Date,name,imageURL);
                databaseReference.setValue(data);
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Review_Comments").child(book_id).child(user_id);
                database.setValue(data);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment, container, false);
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
