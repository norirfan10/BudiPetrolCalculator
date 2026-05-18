package com.example.petrolcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set up toolbar menu
        setupToolbar(R.id.toolbar, getString(R.string.app_name));

        // Navigate to Calculate page when button is tapped
        Button btnGoCalculate = findViewById(R.id.btnGoCalculate);
        btnGoCalculate.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CalculateActivity.class);
            startActivity(intent);
        });
    }
}
