package com.smallbil;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SaleActivity extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.sale_to_dash_navigation:
                    goToDashBord();
                    return true;
                case R.id.sale_to_prod_navigation:
                    goToProduct();
                    return true;

            }
            return false;
        }
    };

    private void goToDashBord() {
        startActivity(new Intent(this, MainActivity.class));
    }
    private void goToProduct() {
        startActivity(new Intent(this, ProductActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.sale_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
