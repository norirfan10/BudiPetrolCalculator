package com.example.petrolcalculator;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * BaseActivity provides a shared kebab (overflow) menu and toolbar setup
 * for all activities in the app. Each activity extends this class.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Call this after setContentView() to set up the toolbar and kebab menu.
     *
     * @param toolbarId The resource ID of the Toolbar view in the layout.
     * @param title     The title to show in the toolbar.
     */
    protected void setupToolbar(int toolbarId, String title) {
        Toolbar toolbar = findViewById(toolbarId);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if (!(this instanceof HomeActivity)) {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
            return true;

        } else if (id == R.id.nav_calculate) {
            if (!(this instanceof CalculateActivity)) {
                Intent intent = new Intent(this, CalculateActivity.class);
                startActivity(intent);
            }
            return true;

        } else if (id == R.id.nav_about) {
            if (!(this instanceof AboutActivity)) {
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
