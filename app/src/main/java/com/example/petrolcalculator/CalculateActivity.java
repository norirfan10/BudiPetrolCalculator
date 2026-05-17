package com.example.petrolcalculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

/**
 * CalculateActivity handles all petrol cost calculations.
 *
 * Fixed fuel prices (Malaysian government rates):
 *  - RON95:  RM 2.05/litre  (BUDI MADANI eligible — always applied)
 *  - RON97:  RM 3.47/litre
 *  - Diesel: RM 2.15/litre
 *
 * Logic:
 *  - Total Petrol Cost = Fuel Usage x Price per Litre
 *  - BUDI Rebate       = Fuel Usage x RM1.99  (RON95 only, always eligible)
 *  - Final Payable     = Total Cost - BUDI Rebate
 *  - Total Saving      = BUDI Rebate
 *
 * BUDI MADANI subsidy rate: RM1.99 per litre, for RON95 users only.
 */
public class CalculateActivity extends BaseActivity {

    // Fixed fuel prices
    private static final double PRICE_RON95  = 3.87;
    private static final double PRICE_RON97  = 4.70;
    private static final double PRICE_DIESEL = 4.87;

    // Subsidy rate as defined by the BUDI MADANI programme
    private static final double BUDI_SUBSIDY_RATE = 1.99;

    // UI references
    private RadioGroup rgPetrolType;
    private RadioButton rbRon95, rbRon97, rbDiesel;
    private TextInputEditText etFuelUsage;
    private TextView tvPriceDisplay;

    // BUDI MADANI section
    private TextView tvBudiNotApplicable, tvBudiEligibleBadge, tvSubsidyBadge;

    // Result views
    private LinearLayout layoutResults, rowBudiRebate, rowFinalPayable;
    private View dividerFinal;
    private TextView tvTotalCost, tvBudiRebate, tvFinalPayable, tvTotalSaving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        setupToolbar(R.id.toolbar, getString(R.string.calculate));

        bindViews();
        setupPetrolTypeListener();
        setupButtons();

        // Default: RON95 selected
        updateBudiSection(true);
        updatePriceDisplay(R.id.rbRon95);
    }

    private void bindViews() {
        rgPetrolType = findViewById(R.id.rgPetrolType);
        rbRon95 = findViewById(R.id.rbRon95);
        rbRon97 = findViewById(R.id.rbRon97);
        rbDiesel = findViewById(R.id.rbDiesel);

        tvPriceDisplay = findViewById(R.id.tvPriceDisplay);
        etFuelUsage = findViewById(R.id.etFuelUsage);

        tvBudiNotApplicable = findViewById(R.id.tvBudiNotApplicable);
        tvBudiEligibleBadge = findViewById(R.id.tvBudiEligibleBadge);
        tvSubsidyBadge = findViewById(R.id.tvSubsidyBadge);

        layoutResults = findViewById(R.id.layoutResults);
        rowBudiRebate = findViewById(R.id.rowBudiRebate);
        rowFinalPayable = findViewById(R.id.rowFinalPayable);
        dividerFinal = findViewById(R.id.dividerFinal);
        tvTotalCost = findViewById(R.id.tvTotalCost);
        tvBudiRebate = findViewById(R.id.tvBudiRebate);
        tvFinalPayable = findViewById(R.id.tvFinalPayable);
        tvTotalSaving = findViewById(R.id.tvTotalSaving);
    }

    private void setupPetrolTypeListener() {
        rgPetrolType.setOnCheckedChangeListener((group, checkedId) -> {
            boolean isRon95 = (checkedId == R.id.rbRon95);
            updateBudiSection(isRon95);
            updatePriceDisplay(checkedId);
            layoutResults.setVisibility(View.GONE);
        });
    }

    private void updatePriceDisplay(int checkedId) {
        double price;
        if (checkedId == R.id.rbRon95) {
            price = PRICE_RON95;
        } else if (checkedId == R.id.rbRon97) {
            price = PRICE_RON97;
        } else {
            price = PRICE_DIESEL;
        }
        tvPriceDisplay.setText(String.format(Locale.US, "RM %.2f / litre", price));
    }

    private void updateBudiSection(boolean isRon95) {
        if (isRon95) {
            tvBudiEligibleBadge.setVisibility(View.VISIBLE);
            tvSubsidyBadge.setVisibility(View.VISIBLE);
            tvBudiNotApplicable.setVisibility(View.GONE);
        } else {
            tvBudiEligibleBadge.setVisibility(View.GONE);
            tvSubsidyBadge.setVisibility(View.GONE);
            tvBudiNotApplicable.setVisibility(View.VISIBLE);
        }
    }

    private void setupButtons() {
        @SuppressLint("WrongViewCast") MaterialButton btnCalculate = findViewById(R.id.btnCalculate);
        @SuppressLint("WrongViewCast") MaterialButton btnReset = findViewById(R.id.btnReset);

        btnCalculate.setOnClickListener(v -> performCalculation());
        btnReset.setOnClickListener(v -> resetAll());
    }

    private void performCalculation() {
        String usageStr = etFuelUsage.getText() != null
                ? etFuelUsage.getText().toString().trim() : "";

        if (TextUtils.isEmpty(usageStr)) {
            etFuelUsage.setError("Please enter fuel usage in litres");
            etFuelUsage.requestFocus();
            return;
        }

        double fuelUsage;
        try {
            fuelUsage = Double.parseDouble(usageStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fuelUsage <= 0) {
            Toast.makeText(this, "Fuel usage must be greater than zero", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isRon95 = rbRon95.isChecked();
        double pricePerLitre;
        if (isRon95) {
            pricePerLitre = PRICE_RON95;
        } else if (rbRon97.isChecked()) {
            pricePerLitre = PRICE_RON97;
        } else {
            pricePerLitre = PRICE_DIESEL;
        }

        double totalCost = fuelUsage * pricePerLitre;
        double budiRebate = isRon95 ? fuelUsage * BUDI_SUBSIDY_RATE : 0.0;
        double finalPayable = totalCost - budiRebate;

        displayResults(totalCost, budiRebate, finalPayable, isRon95);
    }

    private void displayResults(double totalCost, double budiRebate,
                                double finalPayable, boolean isBudiEligible) {
        tvTotalCost.setText(formatRm(totalCost));

        if (isBudiEligible) {
            rowBudiRebate.setVisibility(View.VISIBLE);
            dividerFinal.setVisibility(View.VISIBLE);
            rowFinalPayable.setVisibility(View.VISIBLE);

            tvBudiRebate.setText("- " + formatRm(budiRebate));
            tvFinalPayable.setText(formatRm(finalPayable));
            tvTotalSaving.setText(formatRm(budiRebate));

            findViewById(R.id.cardSavings).setVisibility(View.VISIBLE);
        } else {
            rowBudiRebate.setVisibility(View.GONE);
            dividerFinal.setVisibility(View.GONE);
            rowFinalPayable.setVisibility(View.GONE);
            findViewById(R.id.cardSavings).setVisibility(View.GONE);
        }

        layoutResults.setVisibility(View.VISIBLE);
    }

    private String formatRm(double amount) {
        return String.format(Locale.US, "RM %.2f", amount);
    }

    private void resetAll() {
        etFuelUsage.setText("");
        rbRon95.setChecked(true);
        updateBudiSection(true);
        updatePriceDisplay(R.id.rbRon95);
        layoutResults.setVisibility(View.GONE);
        etFuelUsage.setError(null);
    }
}
