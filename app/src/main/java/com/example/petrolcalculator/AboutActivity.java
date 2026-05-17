package com.example.petrolcalculator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

/**
 * AboutActivity displays information about the application:
 *  - Application icon
 *  - Author information (Name, Matric No, Course)
 *  - GitHub Repository (read-only, hardcoded URL)
 *  - Clickable "Open in Browser" link
 *  - Copyright notice
 */
public class AboutActivity extends BaseActivity {

    // Hardcoded GitHub repository URL — not editable by users
    private static final String GITHUB_URL = "https://github.com/yourusername/PetrolCalculator";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setupToolbar(R.id.toolbar, getString(R.string.about));

        setupGithubLink();
    }

    /**
     * Make the "Open in Browser" label open the hardcoded GitHub URL.
     */
    private void setupGithubLink() {
        TextView tvGithubUrl = findViewById(R.id.tvGithubUrl);
        tvGithubUrl.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL));
            startActivity(browserIntent);
        });
    }
}
