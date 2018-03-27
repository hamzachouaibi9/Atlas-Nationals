package com.fbla.atlas.atlas.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.activities.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PushNotificationAdminService extends Service {

    FirebaseAuth auth;
    String user_id;
    DatabaseReference reservedDatabase, overdueDatabase, checkedOutDatabase;

    public PushNotificationAdminService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid();

        checkedOutDatabase = FirebaseDatabase.getInstance().getReference().child("User_Checkout");
        reservedDatabase = FirebaseDatabase.getInstance().getReference().child("User_Reserved");
        overdueDatabase = FirebaseDatabase.getInstance().getReference().child("Overdue_Books");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        checkedOutDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                checkoutAdded(s,dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                checkoutAdded(s,dataSnapshot);
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

        reservedDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                reservedAdded(s,dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                reservedAdded(s,dataSnapshot);
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

        overdueDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                overdueAdded(s,dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                overdueAdded(s,dataSnapshot);
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

    private void checkoutAdded(String book_id,DataSnapshot dataSnapshot) {

        String id = dataSnapshot.getKey().trim();

        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(id);

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String NAME = dataSnapshot.child("name").getValue().toString().trim();

                Intent resultIntent = new Intent(PushNotificationAdminService.this, Login.class);
                // Because clicking the notification opens a new ("special") activity, there's
                // no need to create an artificial back stack.
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                PushNotificationAdminService.this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(PushNotificationAdminService.this)
                                .setSmallIcon(R.drawable.logoatlas)
                                .setContentTitle(NAME)
                                .setContentText( "This user is wanting to checkout a book")
                                .setAutoCancel(true)
                                .setContentIntent(resultPendingIntent);

                int mNotificationId = 001;
                // Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(mNotificationId, mBuilder.build());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void reservedAdded(String book_id,DataSnapshot dataSnapshot) {
        String id = dataSnapshot.getKey().trim();

        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(id);

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String NAME = dataSnapshot.child("name").getValue().toString().trim();

                Intent resultIntent = new Intent(PushNotificationAdminService.this, Login.class);
                // Because clicking the notification opens a new ("special") activity, there's
                // no need to create an artificial back stack.
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                PushNotificationAdminService.this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(PushNotificationAdminService.this)
                                .setSmallIcon(R.drawable.logoatlas)
                                .setContentTitle(NAME)
                                .setContentText( "This user is wanting to reserve a book")
                                .setAutoCancel(true)
                                .setContentIntent(resultPendingIntent);

                int mNotificationId = 001;
                // Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(mNotificationId, mBuilder.build());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void overdueAdded(String book_id,DataSnapshot dataSnapshot) {
        String id = dataSnapshot.getKey().trim();

        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(id);

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String NAME = dataSnapshot.child("name").getValue().toString().trim();

                Intent resultIntent = new Intent(PushNotificationAdminService.this, Login.class);
                // Because clicking the notification opens a new ("special") activity, there's
                // no need to create an artificial back stack.
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                PushNotificationAdminService.this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(PushNotificationAdminService.this)
                                .setSmallIcon(R.drawable.logoatlas)
                                .setContentTitle(NAME)
                                .setContentText("Has an overdue book")
                                .setAutoCancel(true)
                                .setContentIntent(resultPendingIntent);

                int mNotificationId = 001;
                // Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(mNotificationId, mBuilder.build());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }
            @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
