package com.example.kuntakatselu2;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout topNavigation;
    private boolean isInitialFragmentLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topNavigation = findViewById(R.id.topNavigation);

        // Add tabs
        topNavigation.addTab(topNavigation.newTab().setIcon(R.drawable.ic_weather).setText(R.string.weather));
        topNavigation.addTab(topNavigation.newTab().setIcon(R.drawable.ic_home).setText(R.string.home));
        topNavigation.addTab(topNavigation.newTab().setIcon(R.drawable.ic_comparison).setText(R.string.comparison));

        topNavigation.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        loadFragment(new WeatherFragment());
                        break;
                    case 1:
                        loadFragment(new SearchFragment());
                        break;
                    case 2:
                        loadFragment(new ComparisonFragment());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Reload the fragment when the tab is reselected
                switch (tab.getPosition()) {
                    case 0:
                        loadFragment(new WeatherFragment());
                        break;
                    case 1:
                        loadFragment(new SearchFragment());
                        break;
                    case 2:
                        loadFragment(new ComparisonFragment());
                        break;
                }
            }
        });

        // Select the default tab (Home/Search)
        TabLayout.Tab defaultTab = topNavigation.getTabAt(1);
        if (defaultTab != null) {
            defaultTab.select();
        }

        // Load the default fragment (Home/Search)
        if (savedInstanceState == null) {
            loadInitialFragment(new SearchFragment());
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void loadInitialFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        isInitialFragmentLoaded = true;
    }

    @Override
    public void onBackPressed() {
        if (isInitialFragmentLoaded && getSupportFragmentManager().getBackStackEntryCount() == 0) {
            // Exit the app if the initial fragment is loaded and back stack is empty
            super.onBackPressed();
        } else {
            // Otherwise, handle the back press normally
            getSupportFragmentManager().popBackStack();
        }
    }
}
