package com.fbla.atlas.atlas.pages.student;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.pages.books_lists.RomanceList;
import com.fbla.atlas.atlas.view_holders.BooksViewHolder;
import com.fbla.atlas.atlas.models.BookOrder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReserveFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReserveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReserveFragment extends Fragment {
    RecyclerView list;
    DatabaseReference database;
    ProgressDialog progressDialog;
    String DATE;
    SimpleDateFormat simpleDateFormat;
    Calendar calendar;
    Button back;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReserveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReserveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReserveFragment newInstance(String param1, String param2) {
        ReserveFragment fragment = new ReserveFragment();
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
//        find all of the views and initialize with firebase database and progressdialog
        list = (RecyclerView) view.findViewById(R.id.reserve_list);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.hasFixedSize();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String user_id = auth.getCurrentUser().getUid();

        database = FirebaseDatabase.getInstance().getReference().child("User_Reserved").child(user_id);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Reserved Books");
        progressDialog.setCancelable(false);
        progressDialog.show();
//       find today's date
        simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        calendar = Calendar.getInstance();
        DATE = simpleDateFormat.format(calendar.getTime());
        final String date = DATE.toString().trim();
//      database value event listener
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    FirebaseRecyclerAdapter<BookOrder, BooksViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BookOrder, BooksViewHolder>
                            (BookOrder.class,R.layout.book_row,BooksViewHolder.class,database) {
                        @Override
                        protected void populateViewHolder(BooksViewHolder viewHolder, final BookOrder model, int position) {
//                            populate the view
                            viewHolder.title.setText(model.getTitle());
                            viewHolder.title.setTextColor(Color.parseColor("#000000"));
                            viewHolder.description.setText("Due Date: " + model.getDate());
                            viewHolder.description.setTextColor(Color.parseColor("#000000"));
                            Picasso.with(getContext()).load(model.getImage()).into(viewHolder.image);
                            progressDialog.cancel();

                            final String book_id = getRef(position).getKey();

                            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), BookPage.class);
                                    intent.putExtra("book_id", book_id);
                                    startActivity(intent);
                                }
                            });



                        }
                    };
                    list.setAdapter(firebaseRecyclerAdapter);
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),"You Have No Reserved Books",Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        back = (Button) view.findViewById(R.id.reserve_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.navigation_content, new HomeFragment()).commit();
            }
        });


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reserve_books, container, false);
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
