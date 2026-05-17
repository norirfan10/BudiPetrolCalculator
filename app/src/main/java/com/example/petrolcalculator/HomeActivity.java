package com.example.petrolcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

/**
 * HomeActivity is the launch screen of the app.
 * It shows the app logo, name, brief description, and a button to navigate
 * to the Calculate page.
 */
public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set up toolbar with kebab menu
        setupToolbar(R.id.toolbar, getString(R.string.app_name));

        // Navigate to Calculate page when button is tapped
        Button btnGoCalculate = findViewById(R.id.btnGoCalculate);
        btnGoCalculate.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CalculateActivity.class);
            startActivity(intent);
        });
    }
}
