package com.fbla.atlas.atlas.pages.student;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.graphics.Matrix;
import android.widget.ImageView;
import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.view_holders.CartViewHolder;
import com.fbla.atlas.atlas.models.Book;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    RecyclerView list;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    Button back;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        find the recyclerview and initialize it
        list = (RecyclerView) view.findViewById(R.id.cart_list);
//        initialize firebase auth
        auth = FirebaseAuth.getInstance();
        String user_id = auth.getCurrentUser().getUid();
//        initialize firebase database and set the layout manager for recyclerview
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User_Cart").child(user_id);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
//        show progress dialog to indicate that data is being retrieved
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading User's Cart");
        progressDialog.setCancelable(false);
        progressDialog.show();
//        database event listener
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//               check if the data exists
                if (dataSnapshot.exists()){
//                      if the data does then put it into a firebaserecycleradapter
                    FirebaseRecyclerAdapter<Book, CartViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Book, CartViewHolder>
                            (Book.class,R.layout.book_layout_cart, CartViewHolder.class, databaseReference) {
                        @Override
                        protected void populateViewHolder(final CartViewHolder viewHolder, Book model, final int position) {
//                            dismiss the progress dialog when the data has been retrieved
                            progressDialog.cancel();
//                            set the text with data to populate the views
                            viewHolder.title.setText(model.getTitle());
                            viewHolder.description.setText(model.getDescription());
                            viewHolder.title.setTextColor(Color.parseColor("#000000"));
                            viewHolder.description.setTextColor(Color.parseColor("#000000"));
                            Picasso.with(getContext()).load(model.getImage()).into(viewHolder.image);

//                            string the book id
                            final String book_id = getRef(position).getKey();

                            viewHolder.title.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), BookPage.class);
                                    intent.putExtra("book_id", book_id);
                                    startActivity(intent);
                                }
                            });

                            viewHolder.description.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), BookPage.class);
                                    intent.putExtra("book_id", book_id);
                                    startActivity(intent);
                                }
                            });

                            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), BookPage.class);
                                    intent.putExtra("book_id", book_id);
                                    startActivity(intent);
                                }
                            });

//                            set the on click listener for the delete button
                            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            ObjectAnimator animator = ObjectAnimator.ofFloat(viewHolder.delete,"rotation",360f);
                                            animator.setDuration(1000);
                                            AnimatorSet animatorSet = new AnimatorSet();
                                            animatorSet.playTogether(animator);
                                            animatorSet.start();
                                            animatorSet.addListener(new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    super.onAnimationEnd(animation);
                                                    databaseReference.child(book_id).removeValue();
                                                }
                                            });

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                        }
                    };
//                        set the recyclerview adapter to the fireabase recycler adapter
                    list.setAdapter(firebaseRecyclerAdapter);


                }else{
//                    if the data doesn't exist dismiss the progress dialog and make a toast saying that it doesn't exist
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Your cart is empty", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        back = (Button) view.findViewById(R.id.cart_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.navigation_content, new HomeFragment()).addToBackStack(null).commit();
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
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
