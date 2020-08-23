package com.ninjacart.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.ninjacart.Extra.Common;
import com.ninjacart.Fragment.MyCart;
import com.ninjacart.Fragment.Home;
import com.ninjacart.Fragment.OrderHistory;
import com.ninjacart.Fragment.Setting;
import com.ninjacart.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class MainPage extends AppCompatActivity {

    public static ImageView menu, back;
    public static DrawerLayout drawerLayout;
    public static TextView title, userNameTxt, userMobileNumberTxt;
    public static LinearLayout toolbarContainer;
    public static String userId, currency = "₹", userName, userNumber, userAddress;
    boolean doubleBackToExitPressedOnce = false;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    public static BottomNavigationView bottomNavigationView;
    public static ProgressBar progressBar;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbarContainer = findViewById(R.id.toolbar_container);
        initViews();

        userId = Common.getSavedUserData(MainPage.this, "userId");
        userName = Common.getSavedUserData(MainPage.this, "userName");
        userNumber = Common.getSavedUserData(MainPage.this, "userNumber");

        loadFragment(new Home(), false);

        NavigationView navigationView = findViewById(R.id.navigationView);
        View header = navigationView.getHeaderView(0);
        userNameTxt = header.findViewById(R.id.userName);
        userMobileNumberTxt = header.findViewById(R.id.userMobileNumber);

        userNameTxt.setText(userName);
        userMobileNumberTxt.setText(userNumber);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        loadFragment(new Home(), false);
                        break;

                    case R.id.cart:
                        loadFragment(new MyCart(), true);
                        break;

                    case R.id.history:
                        loadFragment(new OrderHistory(), true);
                        break;

                    case R.id.profile:
                        loadFragment(new Setting(), true);
                        break;

                    case R.id.share:
                    case R.id.rateus:
                        // perform click on Rate Category
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                        }
                        break;

                }

                return false;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        loadFragment(new Home(), false);
                        break;
                    case R.id.cart:
                        loadFragment(new MyCart(), true);
                        break;
                    case R.id.history:
                        loadFragment(new OrderHistory(), true);
                        break;
                    case R.id.profile:
                        loadFragment(new Setting(), true);
                        break;
                }
                return true;
            }
        });



    }

    @SuppressLint("CutPasteId")
    private void initViews() {

        drawerLayout = findViewById(R.id.drawer_layout);
        title = findViewById(R.id.title);
        menu = findViewById(R.id.menu);
        progressBar = findViewById(R.id.progressBar);
        back = findViewById(R.id.back);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

    }

    @SuppressLint("RtlHardcoded")
    @OnClick({R.id.menu, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                if (!MainPage.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    MainPage.drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.back:
                removeCurrentFragmentAndMoveBack();
                break;

        }
    }


    @Override
    public void onBackPressed() {
        // double press to exit
        if (menu.getVisibility() == View.VISIBLE) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
        } else {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toasty.normal(MainPage.this, "Press back once more to exit", Toasty.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    public void lockUnlockDrawer(int lockMode) {
        drawerLayout.setDrawerLockMode(lockMode);
        if (lockMode == DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            menu.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
        } else {
            menu.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            back.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
        }

    }

    public void removeCurrentFragmentAndMoveBack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    public void loadFragment(Fragment fragment, Boolean bool) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        if (bool) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}