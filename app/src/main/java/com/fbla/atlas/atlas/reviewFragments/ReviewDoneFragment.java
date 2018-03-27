package com.fbla.atlas.atlas.reviewFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fbla.atlas.atlas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReviewDoneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReviewDoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewDoneFragment extends Fragment {

    DatabaseReference databaseReference;
    FirebaseAuth auth;
    TextView date, rating, comment;
    String user_id, book_id;
    ImageView image;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReviewDoneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewDoneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewDoneFragment newInstance(String param1, String param2) {
        ReviewDoneFragment fragment = new ReviewDoneFragment();
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
        System.out.println(book_id);
        auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Reviews").child(user_id).child(book_id);

        image = (ImageView) view.findViewById(R.id.user_image);
        rating = (TextView) view.findViewById(R.id.review_rating);
        comment = (TextView) view.findViewById(R.id.review_comment);
        date = (TextView) view.findViewById(R.id.review_date);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageURL = dataSnapshot.child("image").getValue().toString().trim();
                String Comment = dataSnapshot.child("comment").getValue().toString().trim();
                String Date = dataSnapshot.child("date").getValue().toString().trim();
                String Rating = dataSnapshot.child("rating").getValue().toString().trim();

                if (imageURL.equals("default")){
                    Picasso.with(getContext()).load(R.drawable.logoatlas).into(image);
                }else{
                    Picasso.with(getContext()).load(imageURL).into(image);
                }

                date.setText("Rated on: " + Date);
                rating.setText("Rating: " + Rating);
                comment.setText(Comment);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_done, container, false);
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
