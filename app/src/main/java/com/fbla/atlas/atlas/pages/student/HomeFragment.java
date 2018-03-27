package com.fbla.atlas.atlas.pages.student;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fbla.atlas.atlas.pages.books_lists.RomanceList;
import com.fbla.atlas.atlas.scrollListener.OnScrollListenerAll;
import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.models.Genre;
import com.fbla.atlas.atlas.pages.books_lists.ActionList;
import com.fbla.atlas.atlas.pages.books_lists.AllList;
import com.fbla.atlas.atlas.pages.books_lists.ComedyList;
import com.fbla.atlas.atlas.pages.books_lists.HistoryList;
import com.fbla.atlas.atlas.pages.books_lists.HorrorList;
import com.fbla.atlas.atlas.pages.books_lists.MangaList;
import com.fbla.atlas.atlas.pages.books_lists.NonFictionList;
import com.fbla.atlas.atlas.view_holders.GenreViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    RecyclerView all, comedy, horror, action,history, manga, nfiction, romance;
    DatabaseReference databaseReference;
    Button COMEDY,HORROR,ACTION,HISTORY,MANGA,NFICTION,ROMANCE;
    ProgressDialog progressDialog;
    Button ALL;

    private static final String TAG = "HomeFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

//            find all of the recyclerview
        all = view.findViewById(R.id.all_genre);
        action = view.findViewById(R.id.action_genre);
        comedy = view.findViewById(R.id.comedy_genre);
        history = view.findViewById(R.id.history_genre);
        horror = view.findViewById(R.id.horror_genre);
        manga = view.findViewById(R.id.manga_genre);
        nfiction = view.findViewById(R.id.nfiction_genre);
        romance = view.findViewById(R.id.romance_genre);
//          find all of the textviews
        ALL = view.findViewById(R.id.all_more);
        ACTION = view.findViewById(R.id.action_more);
        COMEDY = view.findViewById(R.id.comedy_more);
        HISTORY = view.findViewById(R.id.history_more);
        HORROR = view.findViewById(R.id.horror_more);
        MANGA = view.findViewById(R.id.manga_more);
        NFICTION = view.findViewById(R.id.nfiction_more);
        ROMANCE = view.findViewById(R.id.romance_more);
//          set the onclick listener for all of the textviews
        ALL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AllList.class));
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        ACTION.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ActionList.class));
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        COMEDY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ComedyList.class));
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        HISTORY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HistoryList.class));
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        HORROR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HorrorList.class));
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        MANGA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MangaList.class));
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        NFICTION.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NonFictionList.class));
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        ROMANCE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RomanceList.class));
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

//          initialize the firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Books").child("AllBooks");

//          query the database
        Query allQuery = databaseReference.limitToLast(10).orderByChild("stitle");
        Query actionQuery = databaseReference.orderByChild("genre").equalTo("Action").limitToLast(7);
        Query comedyQuery = databaseReference.orderByChild("genre").equalTo("Comedy").limitToLast(7);
        Query historyQuery = databaseReference.orderByChild("genre").equalTo("History").limitToLast(7);
        Query mangaQuery = databaseReference.orderByChild("genre").equalTo("Manga").limitToLast(7);
        Query horrorQuery = databaseReference.orderByChild("genre").equalTo("Horror").limitToLast(7);
        Query nFictionQuery = databaseReference.orderByChild("genre").equalTo("Non-Fiction").limitToLast(7);
        Query RomanceQuery = databaseReference.orderByChild("genre").equalTo("Romance").limitToLast(7);

//          set the recyclerview layout manager to horizontal
        all.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        action.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        comedy.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        horror.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        history.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        nfiction.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        manga.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        romance.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
//          set the recyclerview to have a fixedsize
        all.hasFixedSize();
        action.hasFixedSize();
        comedy.hasFixedSize();
        horror.hasFixedSize();
        history.hasFixedSize();
        nfiction.hasFixedSize();
        manga.hasFixedSize();
        romance.hasFixedSize();
//          show a progress dialog to show that data is being retrieved
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setMessage("Loading Books");
        progressDialog.setCancelable(false);
//          create a firebase recycler adapter
        FirebaseRecyclerAdapter<Genre, GenreViewHolder> allAdapter = new FirebaseRecyclerAdapter<Genre, GenreViewHolder>
                (Genre.class, R.layout.genre_design, GenreViewHolder.class, allQuery) {
            @Override
            protected void populateViewHolder(final GenreViewHolder viewHolder, Genre model, int position) {
//                populate the view with all of the datea
                viewHolder.title.setText(model.getTitle());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.image);
                progressDialog.dismiss();
                final String book_id = getRef(position).getKey();

//                set a book onclick listener and send the book id through a bundle and arguments
                viewHolder.ripple.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), BookPage.class);
                                intent.putExtra("book_id", book_id);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                        }, 100);

                    }
                });
            }
        };

        all.setAdapter(allAdapter);

        all.addOnScrollListener(new OnScrollListenerAll(view, ALL));

//                set a book onclick listener and send the book id through a bundle and arguments
        FirebaseRecyclerAdapter<Genre, GenreViewHolder> actionAdapter = new FirebaseRecyclerAdapter<Genre, GenreViewHolder>
                (Genre.class, R.layout.genre_design, GenreViewHolder.class, actionQuery) {
            @Override
            protected void populateViewHolder(GenreViewHolder viewHolder, Genre model, int position) {
                viewHolder.title.setText(model.getTitle());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.image);
                progressDialog.dismiss();
                final String book_id = getRef(position).getKey();
                viewHolder.ripple.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), BookPage.class);
                                intent.putExtra("book_id", book_id);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                        }, 100);

                    }
                });

            }
        };
        action.setAdapter(actionAdapter);
        action.addOnScrollListener(new OnScrollListenerAll(view, ACTION));
        //        set a book onclick listener and send the book id through a bundle and arguments
        FirebaseRecyclerAdapter<Genre, GenreViewHolder> comedyAdapter = new FirebaseRecyclerAdapter<Genre, GenreViewHolder>
                (Genre.class, R.layout.genre_design, GenreViewHolder.class, comedyQuery) {
            @Override
            protected void populateViewHolder(GenreViewHolder viewHolder, Genre model, int position) {
                viewHolder.title.setText(model.getTitle());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.image);
                progressDialog.dismiss();
                final String book_id = getRef(position).getKey();
                viewHolder.ripple.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), BookPage.class);
                                intent.putExtra("book_id", book_id);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                        }, 100);

                    }
                });
            }
        };
        comedy.setAdapter(comedyAdapter);
        comedy.addOnScrollListener(new OnScrollListenerAll(view, COMEDY));

        //                set a book onclick listener and send the book id through a bundle and arguments
        FirebaseRecyclerAdapter<Genre, GenreViewHolder> historyAdapter = new FirebaseRecyclerAdapter<Genre, GenreViewHolder>
                (Genre.class, R.layout.genre_design, GenreViewHolder.class, historyQuery) {
            @Override
            protected void populateViewHolder(GenreViewHolder viewHolder, Genre model, int position) {
                viewHolder.title.setText(model.getTitle());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.image);
                progressDialog.dismiss();
                final String book_id = getRef(position).getKey();
                viewHolder.ripple.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), BookPage.class);
                                intent.putExtra("book_id", book_id);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                        }, 100);

                    }
                });
            }
        };
        history.setAdapter(historyAdapter);
        history.addOnScrollListener(new OnScrollListenerAll(view, HISTORY));

        //                set a book onclick listener and send the book id through a bundle and arguments
        FirebaseRecyclerAdapter<Genre, GenreViewHolder> mangaAdapter = new FirebaseRecyclerAdapter<Genre, GenreViewHolder>
                (Genre.class, R.layout.genre_design, GenreViewHolder.class, mangaQuery) {
            @Override
            protected void populateViewHolder(GenreViewHolder viewHolder, Genre model, int position) {
                viewHolder.title.setText(model.getTitle());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.image);
                progressDialog.dismiss();
                final String book_id = getRef(position).getKey();
                viewHolder.ripple.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), BookPage.class);
                                intent.putExtra("book_id", book_id);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                        }, 100);

                    }
                });
            }
        };
        manga.setAdapter(mangaAdapter);
        manga.addOnScrollListener(new OnScrollListenerAll(view, MANGA));

        //                set a book onclick listener and send the book id through a bundle and arguments
        FirebaseRecyclerAdapter<Genre, GenreViewHolder> nFictionAdapter = new FirebaseRecyclerAdapter<Genre, GenreViewHolder>
                (Genre.class, R.layout.genre_design, GenreViewHolder.class, nFictionQuery) {
            @Override
            protected void populateViewHolder(GenreViewHolder viewHolder, Genre model, int position) {
                viewHolder.title.setText(model.getTitle());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.image);
                progressDialog.dismiss();
                final String book_id = getRef(position).getKey();
                viewHolder.ripple.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), BookPage.class);
                                intent.putExtra("book_id", book_id);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                        }, 100);

                    }
                });
            }
        };
        nfiction.setAdapter(nFictionAdapter);
        nfiction.addOnScrollListener(new OnScrollListenerAll(view, NFICTION));

        //                set a book onclick listener and send the book id through a bundle and arguments
        FirebaseRecyclerAdapter<Genre, GenreViewHolder> romanceAdapter = new FirebaseRecyclerAdapter<Genre, GenreViewHolder>
                (Genre.class, R.layout.genre_design, GenreViewHolder.class, RomanceQuery) {
            @Override
            protected void populateViewHolder(GenreViewHolder viewHolder, Genre model, int position) {
                viewHolder.title.setText(model.getTitle());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.image);
                progressDialog.dismiss();
                final String book_id = getRef(position).getKey();
                viewHolder.ripple.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), BookPage.class);
                                intent.putExtra("book_id", book_id);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                        }, 100);

                    }
                });
            }
        };
        romance.setAdapter(romanceAdapter);
        romance.addOnScrollListener(new OnScrollListenerAll(view, ROMANCE));

        //                set a book onclick listener and send the book id through a bundle and arguments
        FirebaseRecyclerAdapter<Genre, GenreViewHolder> horrorAdapter = new FirebaseRecyclerAdapter<Genre, GenreViewHolder>
                (Genre.class, R.layout.genre_design, GenreViewHolder.class, horrorQuery) {
            @Override
            protected void populateViewHolder(GenreViewHolder viewHolder, Genre model, int position) {
                viewHolder.title.setText(model.getTitle());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.image);
                progressDialog.dismiss();
                final String book_id = getRef(position).getKey();
                viewHolder.ripple.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), BookPage.class);
                                intent.putExtra("book_id", book_id);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                        }, 100);

                    }
                });
            }
        };
        horror.setAdapter(horrorAdapter);
        horror.addOnScrollListener(new OnScrollListenerAll(view, HORROR));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        return v;
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
