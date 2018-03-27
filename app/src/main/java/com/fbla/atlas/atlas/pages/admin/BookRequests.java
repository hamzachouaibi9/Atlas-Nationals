package com.fbla.atlas.atlas.pages.admin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.models.UserRequest;
import com.fbla.atlas.atlas.view_holders.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookRequests.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookRequests#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookRequests extends Fragment {

    DatabaseReference databaseReference;
    RecyclerView list;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BookRequests() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookRequests.
     */
    // TODO: Rename and change types and number of parameters
    public static BookRequests newInstance(String param1, String param2) {
        BookRequests fragment = new BookRequests();
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

        list = (RecyclerView) view.findViewById(R.id.user_request);
        list.hasFixedSize();
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    FirebaseRecyclerAdapter<UserRequest, UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserRequest, UserViewHolder>
                            (UserRequest.class, R.layout.user_design, UserViewHolder.class, databaseReference) {
                        @Override
                        protected void populateViewHolder(UserViewHolder viewHolder, UserRequest model, int position) {
                            viewHolder.name.setText(model.getName());
                            viewHolder.school.setText(model.getSchool());
                            Picasso.with(getContext()).load(model.getPic()).into(viewHolder.image);

                            final String user_id = getRef(position).getKey();

                            viewHolder.checked.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CheckoutRequest secondFrag = new CheckoutRequest();
                                    Bundle args = new Bundle();
                                    args.putString("user_id", user_id);

                                    secondFrag.setArguments(args);
                                    getFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.admin_container, secondFrag)
                                            .addToBackStack(null)
                                            .commit();
                                }
                            });

                            viewHolder.reserved.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ReserveRequest secondFrag = new ReserveRequest();
                                    Bundle args = new Bundle();
                                    args.putString("user_id", user_id);

                                    secondFrag.setArguments(args);
                                    getFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.admin_container, secondFrag)
                                            .addToBackStack(null)
                                            .commit();
                                }
                            });

                            viewHolder.overdue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    OverdueBooks secondFrag = new OverdueBooks();
                                    Bundle args = new Bundle();
                                    args.putString("user_id", user_id);

                                    secondFrag.setArguments(args);
                                    getFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.admin_container, secondFrag)
                                            .addToBackStack(null)
                                            .commit();
                                }
                            });

                        }
                    };
                    list.setAdapter(firebaseRecyclerAdapter);

                }else{
                    Toast.makeText(getContext(), "No One Lives", Toast.LENGTH_SHORT).show();
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
        return inflater.inflate(R.layout.fragment_book_requests, container, false);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter  adapter = new ViewPagerAdapter (getChildFragmentManager());
        adapter.addFragment(new CheckoutRequest(), "Book Checkout");
        adapter.addFragment(new ReserveRequest(), "Book Request");
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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
