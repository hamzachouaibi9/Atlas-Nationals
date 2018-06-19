package com.fbla.atlas.atlas.pages.student;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import com.fbla.atlas.atlas.R;

import com.fbla.atlas.atlas.activities.Login;
import com.fbla.atlas.atlas.help.HelpHome;
import com.fbla.atlas.atlas.models.Book;
import com.fbla.atlas.atlas.models.BookOrder;
import com.fbla.atlas.atlas.models.Review;
import com.fbla.atlas.atlas.reviewFragments.CommentFragment;
import com.fbla.atlas.atlas.reviewFragments.RateFragment;
import com.fbla.atlas.atlas.reviewFragments.ReviewDoneFragment;
import com.fbla.atlas.atlas.view_holders.ReviewViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import static io.fabric.sdk.android.services.network.UrlUtils.urlEncode;

public class BookPage extends AppCompatActivity {

    DatabaseReference databaseReference, cartDatabase, reserveDatabase, checkoutDatabase, userDatabase, reviewDatabase, ratingData;
    Button checkout, cart,reserve;

    GoogleSignInClient mGoogleSignInClient;

    FloatingActionButton facebook, twitter, email;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;


    TextView title,description,author,genre, date, viewMore, review_more, countTV, moreDescription;
    String count;
    ImageView image;
    String DATE;
    String IMAGE;
    String book_id, book_title, book_image;
    String book_desc;

    FirebaseAuth mauth = FirebaseAuth.getInstance();
    String user_id = mauth.getCurrentUser().getUid();

    ProgressDialog progressDialog;

    RecyclerView reviewList;

    @Override
    protected void onStart() {
        super.onStart();

        ratingData = FirebaseDatabase.getInstance().getReference().child("Reviews").child(user_id).child(book_id);
        ratingData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("rating").exists() && dataSnapshot.child("comment").exists()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("book_id", book_id);
                    ReviewDoneFragment fragment = new ReviewDoneFragment();
                    fragment.setArguments(bundle);
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.review_FrameLayout, fragment, fragment.getTag()).commit();
                }else if (dataSnapshot.child("rating").exists() && !dataSnapshot.child("comment").exists()){
                    Bundle bundle = new Bundle();
                    bundle.putString("book_id", book_id);
                    CommentFragment fragment = new CommentFragment();
                    fragment.setArguments(bundle);
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.review_FrameLayout, fragment, fragment.getTag()).commit();
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString("book_id", book_id);
                    RateFragment fragment = new RateFragment();
                    fragment.setArguments(bundle);
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.review_FrameLayout, fragment, fragment.getTag()).commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_page);

        FacebookSdk.sdkInitialize(BookPage.this);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(BookPage.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        book_id = getIntent().getStringExtra("book_id");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Book Data");
        progressDialog.show();
        progressDialog.setCancelable(false);

//        find all of the views and initialize them
        genre = (TextView) findViewById(R.id.book_genre);
        author = (TextView) findViewById(R.id.book_author);
        image = (ImageView) findViewById(R.id.book_page_image);
        title = (TextView) findViewById(R.id.book_page_title);
        description = (TextView) findViewById(R.id.book_page_description);
        date = (TextView) findViewById(R.id.currentDate);
        reserve = (Button) findViewById(R.id.reserve_button);
        reviewList = (RecyclerView) findViewById(R.id.review_list);
        date.setVisibility(View.GONE);
        viewMore = (TextView) findViewById(R.id.more_reviews);
        review_more = (TextView) findViewById(R.id.reviews_text);
        moreDescription = (TextView) findViewById(R.id.view_more_Description);
        moreDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDescriptionLength();
            }
        });
        facebook = (FloatingActionButton) findViewById(R.id.fab_facebook);
        twitter = (FloatingActionButton) findViewById(R.id.fab_twitter);
        email = (FloatingActionButton) findViewById(R.id.fab_email);

        viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BookPage.this, MoreReviews.class);
                i.putExtra("book_id", book_id);
                startActivity(i);
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });

//        get the string from previous page and call a database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        cartDatabase = FirebaseDatabase.getInstance().getReference().child("User_Cart").child(user_id).child(book_id);
        reserveDatabase = FirebaseDatabase.getInstance().getReference().child("User_Reserved").child(user_id).child(book_id);
        checkoutDatabase = FirebaseDatabase.getInstance().getReference().child("User_Checkout").child(user_id).child(book_id);
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        reviewDatabase = FirebaseDatabase.getInstance().getReference().child("Review_Comments").child(book_id);
        ratingData = FirebaseDatabase.getInstance().getReference().child("Reviews").child(user_id).child(book_id);


        checkoutDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    checkout.setVisibility(View.GONE);
                    checkout.setClickable(false);
                    reserve.setVisibility(View.GONE);
                    reserve.setClickable(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        reserveDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    checkout.setVisibility(View.GONE);
                    checkout.setClickable(false);
                    reserve.setVisibility(View.GONE);
                    reserve.setClickable(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cartDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    cart.setVisibility(View.GONE);
                    cart.setClickable(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        call a value event listener and string the data for the database
        databaseReference.child("Books").child("AllBooks").child(book_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                book_title = dataSnapshot.child("title").getValue().toString().trim();
                book_desc = dataSnapshot.child("description").getValue().toString().trim();
                final String book_author = dataSnapshot.child("author").getValue().toString().trim();
                final String book_genre = dataSnapshot.child("genre").getValue().toString().trim();
                book_image = dataSnapshot.child("image").getValue().toString().trim();

                getSupportActionBar().setTitle(book_title);

//                Set the text with the string above
                if (book_desc.length()>250) {
                    description.setText(book_desc.substring(0, 250) + "...");
                }else{
                    description.setText(book_desc);
                    moreDescription.setVisibility(View.GONE);
                }
                title.setText(book_title);
                author.setText(book_author);
                genre.setText(book_genre);
                Picasso.with(BookPage.this).load(book_image).into(image);
                IMAGE = book_image;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareBookEmail(book_title);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareBookTwitter(book_title, book_image);
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareBookFacebook(book_title);
            }
        });


//        Set the on click listener for the buttons and call the function that is required
        cart = (Button) findViewById(R.id.add_cart_button);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BookPage.this, "Your Book Has Been Placed In Your Cart",Toast.LENGTH_SHORT).show();
                        addCart();
                        startActivity(new Intent(BookPage.this, CartPage.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                }, 300);


            }
        });

        checkout = (Button) findViewById(R.id.order_button);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BookPage.this, "Your Book Has Been Checked Out",Toast.LENGTH_SHORT).show();
                        addCheckout();
                        startActivity(new Intent(BookPage.this, CheckoutPage.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);                    }
                }, 300);

            }
        });


        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BookPage.this, "Your Book Has Been Reserved",Toast.LENGTH_SHORT).show();
                        addReserve();
                        startActivity(new Intent(BookPage.this, ReservedPage.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);                    }
                }, 300);



            }
        });

        simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,14);
        DATE = simpleDateFormat.format(calendar.getTime());

        reviewList.hasFixedSize();
        reviewList.setLayoutManager(new LinearLayoutManager(this));
        ratingData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("comment").exists()){
                    FirebaseRecyclerAdapter<Review, ReviewViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Review, ReviewViewHolder>
                            (Review.class, R.layout.review_design, ReviewViewHolder.class, reviewDatabase) {
                        @Override
                        protected void populateViewHolder(ReviewViewHolder viewHolder, Review model, int position) {

                            viewHolder.rating.setText("Rating: " + model.getRating());
                            viewHolder.comment.setText(model.getComment());
                            viewHolder.name.setText(model.getName());
                            viewHolder.date.setText(model.getDate());
                            progressDialog.dismiss();

                            if (model.getImage().equals("default")){
                                Picasso.with(BookPage.this).load(R.drawable.logoatlas).into(viewHolder.image);
                            }else{
                                Picasso.with(BookPage.this).load(model.getImage()).into(viewHolder.image);
                            }

                        }
                    };

                    reviewList.setAdapter(firebaseRecyclerAdapter);
                    viewMore.setVisibility(View.VISIBLE);
                    reviewList.setVisibility(View.VISIBLE);
                    review_more.setVisibility(View.VISIBLE);
                }else{
                    reviewList.setVisibility(View.GONE);
                    viewMore.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    review_more.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void checkDescriptionLength() {
        final String view_text = moreDescription.getText().toString().trim();
        if (view_text.equals("View More")){
            description.setText(book_desc);
            moreDescription.setText("View Less");
        }else{
            description.setText(book_desc.substring(0, 250) + "...");
            moreDescription.setText("View More");
        }
    }


    //      String all of the data and put them into the database
    private void addCheckout() {
        String Title = title.getText().toString().trim();
        String Description = description.getText().toString().trim();
        String Genre = genre.getText().toString().trim();
        String Author = author.getText().toString().trim();
        String Image = IMAGE.toString().trim();
        String date = "Pending";
        BookOrder books = new BookOrder(Title,Image,Author,Description,Genre,date,null);

        databaseReference.child("User_Checkout").child(user_id).child(book_id).setValue(books);
        DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("History").child(user_id);
        data.child(book_id).setValue(books);
    }

    //      String all of the data and put them into the database
    private void addReserve() {
        String Title = title.getText().toString().trim();
        String Description = description.getText().toString().trim();
        String Genre = genre.getText().toString().trim();
        String Author = author.getText().toString().trim();
        String Image = IMAGE.toString().trim();
        String date = "Pending";
        BookOrder books = new BookOrder(Title,Image,Author,Description,Genre,date,null);

        databaseReference.child("User_Reserved").child(user_id).child(book_id).setValue(books);
        DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("History").child(user_id);
        data.child(book_id).setValue(books);
    }

    //      String all of the data and put them into the database
    private void addCart() {
        String Title = title.getText().toString().trim();
        String Description = description.getText().toString().trim();
        String Genre = genre.getText().toString().trim();
        String Author = author.getText().toString().trim();
        String Image = IMAGE.toString().trim();

        Book books = new Book(Title, Description, Image, Genre, Author);

        databaseReference.child("User_Cart").child(user_id).child(book_id).setValue(books);
    }


    public void shareBookTwitter(String text, String image) {
        String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                urlEncode(text),
                urlEncode("Checkout book image: "+image));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

        // Narrow down to official Twitter app, if available:
        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                intent.setPackage(info.activityInfo.packageName);
            }
        }

        startActivity(intent);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();
        Fragment fragment = null;

        //noinspection SimplifiableIfStatement

        if (id == R.id.help_button) {
            startActivity(new Intent(BookPage.this, HelpHome.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.nav_account){
            startActivity(new Intent(BookPage.this, CartPage.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if (id == R.id.app_bar_sign_out) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();

            // [START config_signin]
            // Configure Google Sign In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            // [END config_signin]
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(new Intent(BookPage.this, Login.class));
                    finish();
                }
            });
            return true;
        }

        if (id == R.id.app_bar_search){
            startActivity(new Intent(BookPage.this, SearchPage.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            return true;
        }

        if (fragment == fragment){
            // Do nothing
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        return super.onOptionsItemSelected(item);
    }

    public void shareBookEmail(String book_title){
            String email = mauth.getCurrentUser().getEmail();
        String[] CC = {
                email
        };

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, book_title);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Check Out this book on Atlas" + "https://play.google.com/store/apps/details?id=com.atlas.fbla");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(BookPage.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

    }

    public void shareBookFacebook(String book_title){
        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                                .setQuote("Checkout This Book: "+book_title)
                                .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.atlas.fbla"))
                                .build();
                        if (shareDialog.canShow(ShareLinkContent.class)){
                            shareDialog.show(shareLinkContent);
                        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    public static String fmt(double d) {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_navigation, menu);
        final MenuItem cart = menu.findItem(R.id.app_bar_cart);
        View actionView = MenuItemCompat.getActionView(cart);
        countTV = (TextView) actionView.findViewById(R.id.count_text);
        countTV.setText(count);
        countTV.setTextColor(Color.WHITE);
        countTV.setVisibility(View.VISIBLE);
        final DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("User_Cart").child(user_id);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int count = (int) dataSnapshot.getChildrenCount();
                    String COUNT = fmt(count);
                    countTV.setText(COUNT);

                }else{
                    countTV.setText("0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookPage.this, CartPage.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                System.out.println("THis is what the textview says: " + countTV.getText().toString().trim());
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
