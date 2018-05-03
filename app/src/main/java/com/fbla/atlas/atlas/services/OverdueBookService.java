package com.fbla.atlas.atlas.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.models.Book;
import com.fbla.atlas.atlas.pages.student.OverduePage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OverdueBookService extends Service {
    
    DatabaseReference reserved, checkout, overdue;
    FirebaseAuth firebaseAuth;
    SimpleDateFormat simpleDateFormat;
    Calendar calendar;
    String DATE;
    Date afterDate;

    public OverdueBookService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

            firebaseAuth = FirebaseAuth.getInstance();
            String user_id = firebaseAuth.getCurrentUser().getUid();

            reserved = FirebaseDatabase.getInstance().getReference().child("User_Reserved").child(user_id);
            checkout = FirebaseDatabase.getInstance().getReference().child("User_Checkout").child(user_id);
            overdue = FirebaseDatabase.getInstance().getReference().child("Overdue_Books").child(user_id);

            simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
            calendar = Calendar.getInstance();
            DATE = simpleDateFormat.format(calendar.getTime());
        }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        overdue.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                onOverdueAdd(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                onOverdueAdd(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                onOverdueReturned(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        reserved.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String i = dataSnapshot.getRef().getKey().trim();
                String date = dataSnapshot.child("date").getValue().toString().trim();
                String title = dataSnapshot.child("title").getValue().toString().trim();
                String author = dataSnapshot.child("author").getValue().toString().trim();
                String description = dataSnapshot.child("description").getValue().toString().trim();
                String genre = dataSnapshot.child("genre").getValue().toString().trim();
                String image = dataSnapshot.child("image").getValue().toString().trim();

                System.out.println("Title: " + title);
                System.out.println("Author: " + author);
                System.out.println("Description: " + description);
                System.out.println("Genre: " + genre);
                System.out.println("Image: " + image);

                System.out.println("The id is: " + i);
                if (date.equals(DATE)){


                    Book book =  new Book(title,description,image,genre,author);
                    overdue.child(title).setValue(book);
                    System.out.println("Working on it");
                    reserved.child(i).removeValue();

                    onOverdueAdd(dataSnapshot);

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        checkout.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String i = dataSnapshot.getRef().getKey().trim();
                String date = dataSnapshot.child("date").getValue().toString().trim();
                String title = dataSnapshot.child("title").getValue().toString().trim();
                String author = dataSnapshot.child("author").getValue().toString().trim();
                String description = dataSnapshot.child("description").getValue().toString().trim();
                String genre = dataSnapshot.child("genre").getValue().toString().trim();
                String image = dataSnapshot.child("image").getValue().toString().trim();

                System.out.println("Title: " + title);
                System.out.println("Author: " + author);
                System.out.println("Description: " + description);
                System.out.println("Genre: " + genre);
                System.out.println("Image: " + image);

                System.out.println("The id is: " + i);
                if (date.equals(DATE)){

                    Book book =  new Book(title,description,image,genre,author);
                    overdue.child(i).setValue(book);
                    System.out.println("Working on it");
                    checkout.child(i).removeValue();

                    onOverdueAdd(dataSnapshot);

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    private void onOverdueReturned(DataSnapshot dataSnapshot) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logoatlas)
                        .setContentTitle(dataSnapshot.child("title").getValue().toString().trim()+ " has been returned")
                        .setContentText("This book has been returned");

        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        Intent resultIntent = new Intent(this, OverduePage.class);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);


    }

    private void onOverdueAdd(DataSnapshot dataSnapshot) {

        Intent resultIntent = new Intent(this, OverduePage.class);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logoatlas)
                        .setContentTitle(dataSnapshot.child("title").getValue().toString().trim()+ " is overdue")
                        .setContentText("Please return this book to the library")
                        .setAutoCancel(true)
                        .setContentIntent(resultPendingIntent);

        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
