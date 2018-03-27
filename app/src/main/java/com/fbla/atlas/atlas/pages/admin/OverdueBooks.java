package com.fbla.atlas.atlas.pages.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.models.Book;
import com.fbla.atlas.atlas.view_holders.OverdueViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OverdueBooks.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OverdueBooks#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverdueBooks extends Fragment {
    RecyclerView list;
    DatabaseReference databaseReference;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public OverdueBooks() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OverdueBooks.
     */
    // TODO: Rename and change types and number of parameters
    public static OverdueBooks newInstance(String param1, String param2) {
        OverdueBooks fragment = new OverdueBooks();
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

        String user_id = getArguments().getString("user_id");
        System.out.println("The user id: " + user_id);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Overdue_Books").child(user_id);
        list = (RecyclerView) view.findViewById(R.id.over_due_list);
        list.hasFixedSize();
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Overdue Books");
        progressDialog.setCancelable(false);
        progressDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    FirebaseRecyclerAdapter<Book, OverdueViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Book, OverdueViewHolder>
                            (Book.class, R.layout.overdue_design, OverdueViewHolder.class, databaseReference) {
                        @Override
                        protected void populateViewHolder(OverdueViewHolder viewHolder, Book model, final int position) {

                            viewHolder.title.setText(model.getTitle());
                            Picasso.with(getContext()).load(model.getImage()).into(viewHolder.image);

                            viewHolder.returned.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String book_id = getRef(position).getKey();
                                    databaseReference.child(book_id).removeValue();
                                }
                            });

                            progressDialog.dismiss();

                        }
                    };
                    list.setAdapter(firebaseRecyclerAdapter);
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "This person doesn't have any overdue books", Toast.LENGTH_LONG).show();
                }
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
        return inflater.inflate(R.layout.fragment_overdue_books, container, false);
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
