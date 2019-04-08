package com.smallbil.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smallbil.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity
implements ScannerFragment.OnFragmentInteractionListener {



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {

                case R.id.dashboard_fragment:
                    fragment = new DashbordFragment();
                    break;
                case R.id.product_fragment:
                    fragment = new ProductFragment();
                    break;
                case R.id.sale_fragment:
                    fragment = new SaleFragment();
                    break;
                case R.id.settings_fragment:
                    fragment = new SettingsFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new ScannerFragment());
       // mTextMessage = (TextView) findViewById(R.id.mess);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.dashboard_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }



    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

public void onFragmentInteraction(Uri uril) {

}
}
