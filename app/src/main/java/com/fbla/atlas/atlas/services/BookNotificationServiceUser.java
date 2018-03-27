package com.fbla.atlas.atlas.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.pages.student.CheckoutPage;
import com.fbla.atlas.atlas.pages.student.ReservedPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookNotificationServiceUser extends Service {

    DatabaseReference checkoutDatabase, reservedDatabase, overdueDatabase;
    FirebaseAuth firebaseAuth;
    String user_id;

    public BookNotificationServiceUser() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();

        checkoutDatabase = FirebaseDatabase.getInstance().getReference().child("User_Checkout").child(user_id);
        reservedDatabase = FirebaseDatabase.getInstance().getReference().child("User_Reserved").child(user_id);
        overdueDatabase = FirebaseDatabase.getInstance().getReference().child("Overdue_Books").child(user_id);


    }

    public void onDataChangedCheckout(String book_id, DataSnapshot dataSnapshot){
        if (!dataSnapshot.child("date").equals("Pending")){

            Intent resultIntent = new Intent(this, CheckoutPage.class);
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
                            .setContentTitle("Your Book "+ dataSnapshot.child("title").getValue().toString().trim()+ " is ready for pickup")
                            .setContentText(dataSnapshot.child("title").getValue().toString().trim()+ " is ready for pickup")
                            .setAutoCancel(true)
                            .setContentIntent(resultPendingIntent);

            int mNotificationId = 001;
    // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());

        }
        System.out.println("Something has been done: " + dataSnapshot);

    }

    public void onDataRemovedCheckout(DataSnapshot dataSnapshot){
        if (!dataSnapshot.child("date").equals("Pending")){

            Intent resultIntent = new Intent(this, CheckoutPage.class);
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
                            .setContentTitle("Your Book "+ dataSnapshot.child("title").getValue().toString().trim()+ " has been rejected")
                            .setContentText(dataSnapshot.child("title").getValue().toString().trim()+ " has been rejected")
                            .setAutoCancel(true)
                            .setContentIntent(resultPendingIntent);

            int mNotificationId = 001;
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());




        }
        System.out.println("Something has been done: " + dataSnapshot);

    }

    public void onDataChanged(String book_id, DataSnapshot dataSnapshot){
        if (!dataSnapshot.child("date").equals("Pending")){

            Intent resultIntent = new Intent(this, ReservedPage.class);
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
                            .setContentTitle("Your Book "+ dataSnapshot.child("title").getValue().toString().trim()+ " is ready for pickup")
                            .setContentText(dataSnapshot.child("title").getValue().toString().trim()+ " is ready for pickup")
                            .setAutoCancel(true)
                            .setContentIntent(resultPendingIntent);

            int mNotificationId = 001;
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());

        }
        System.out.println("Something has been done: " + dataSnapshot);

    }

    public void onDataRemoved(DataSnapshot dataSnapshot){
        if (!dataSnapshot.child("date").equals("Pending")){

            Intent resultIntent = new Intent(this, ReservedPage.class);
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
                            .setContentTitle("Your Book "+ dataSnapshot.child("title").getValue().toString().trim()+ " has been rejected")
                            .setContentText(dataSnapshot.child("title").getValue().toString().trim()+ " has been rejected")
                            .setAutoCancel(true)
                            .setContentIntent(resultPendingIntent);

            int mNotificationId = 001;
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());




        }
        System.out.println("Something has been done: " + dataSnapshot);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {

        reservedDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                onDataChanged(s,dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                onDataRemoved(dataSnapshot);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        checkoutDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                onDataChanged(s,dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                onDataRemoved(dataSnapshot);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        overdueDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                onDataChanged(s, dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                onDataChanged(s, dataSnapshot);

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
