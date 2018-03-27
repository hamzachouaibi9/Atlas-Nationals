package com.fbla.atlas.atlas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.pages.admin.AddBook;
import com.fbla.atlas.atlas.pages.admin.AdminBooks;
import com.fbla.atlas.atlas.pages.admin.BookRequests;
import com.fbla.atlas.atlas.services.PushNotificationAdminService;
import com.google.firebase.auth.FirebaseAuth;

public class AdminMainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchToFragment2();
                    return true;
                case R.id.add_books_nav:
                    switchToFragment1();
                    return true;
                case R.id.navigation_notifications:
                    switchToFragment4();
                    return true;
                case  R.id.account:
                    switchToFragment3();
                    return true;
            }
            return false;
        }
    };

    public void switchToFragment1() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.admin_container, new AddBook()).commit();
    }
    public void switchToFragment2() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.admin_container, new AdminBooks()).commit();
    }
    public void switchToFragment3() {
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(AdminMainActivity.this, Login.class));
    }
    public void switchToFragment4() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.admin_container, new BookRequests()).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        startService(new Intent(AdminMainActivity.this, PushNotificationAdminService.class));

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

    }

}
