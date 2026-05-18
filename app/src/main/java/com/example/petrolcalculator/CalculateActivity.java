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


public class CalculateActivity extends BaseActivity {

    // Fixed fuel prices
    private static final double PRICE_RON95  = 3.87;
    private static final double PRICE_RON97  = 4.70;
    private static final double PRICE_DIESEL = 4.87;

    // BUDI MADANI subsidy rate
    private static final double BUDI_SUBSIDY_RATE = 1.99;

    // Petrol type
    private RadioGroup rgPetrolType;
    private RadioButton rbRon95, rbRon97, rbDiesel;
    private TextView tvPriceDisplay;

    // Input mode toggle
    private RadioGroup rgInputMode;
    private RadioButton rbByLitres, rbByAmount;
    private LinearLayout layoutInputLitres, layoutInputAmount;
    private TextInputEditText etFuelUsage, etAmountRm;

    // BUDI section
    private TextView tvBudiNotApplicable, tvBudiEligibleBadge, tvSubsidyBadge;

    // Results
    private LinearLayout layoutResults, rowBudiRebate, rowFinalPayable, rowPetrolLitres;
    private View dividerFinal;
    private TextView tvTotalCost, tvPetrolLitres, tvBudiRebate, tvFinalPayable, tvTotalSaving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        setupToolbar(R.id.toolbar, getString(R.string.calculate));

        bindViews();
        setupPetrolTypeListener();
        setupInputModeListener();
        setupButtons();

        // Default state
        updateBudiSection(true);
        updatePriceDisplay(R.id.rbRon95);
    }

    private void bindViews() {
        rgPetrolType    = findViewById(R.id.rgPetrolType);
        rbRon95         = findViewById(R.id.rbRon95);
        rbRon97         = findViewById(R.id.rbRon97);
        rbDiesel        = findViewById(R.id.rbDiesel);
        tvPriceDisplay  = findViewById(R.id.tvPriceDisplay);

        rgInputMode       = findViewById(R.id.rgInputMode);
        rbByLitres        = findViewById(R.id.rbByLitres);
        rbByAmount        = findViewById(R.id.rbByAmount);
        layoutInputLitres = findViewById(R.id.layoutInputLitres);
        layoutInputAmount = findViewById(R.id.layoutInputAmount);
        etFuelUsage       = findViewById(R.id.etFuelUsage);
        etAmountRm        = findViewById(R.id.etAmountRm);

        tvBudiNotApplicable = findViewById(R.id.tvBudiNotApplicable);
        tvBudiEligibleBadge = findViewById(R.id.tvBudiEligibleBadge);
        tvSubsidyBadge      = findViewById(R.id.tvSubsidyBadge);

        layoutResults   = findViewById(R.id.layoutResults);
        rowBudiRebate   = findViewById(R.id.rowBudiRebate);
        rowFinalPayable = findViewById(R.id.rowFinalPayable);
        rowPetrolLitres = findViewById(R.id.rowPetrolLitres);
        dividerFinal    = findViewById(R.id.dividerFinal);
        tvTotalCost     = findViewById(R.id.tvTotalCost);
        tvPetrolLitres  = findViewById(R.id.tvPetrolLitres);
        tvBudiRebate    = findViewById(R.id.tvBudiRebate);
        tvFinalPayable  = findViewById(R.id.tvFinalPayable);
        tvTotalSaving   = findViewById(R.id.tvTotalSaving);
    }

    private void setupPetrolTypeListener() {
        rgPetrolType.setOnCheckedChangeListener((group, checkedId) -> {
            updateBudiSection(checkedId == R.id.rbRon95);
            updatePriceDisplay(checkedId);
            layoutResults.setVisibility(View.GONE);
        });
    }

    /** Show/hide the correct input field when the user switches input mode. */
    private void setupInputModeListener() {
        rgInputMode.setOnCheckedChangeListener((group, checkedId) -> {
            boolean byLitres = (checkedId == R.id.rbByLitres);
            layoutInputLitres.setVisibility(byLitres ? View.VISIBLE : View.GONE);
            layoutInputAmount.setVisibility(byLitres ? View.GONE   : View.VISIBLE);
            // Clear both fields and hide results on mode switch
            etFuelUsage.setText("");
            etAmountRm.setText("");
            layoutResults.setVisibility(View.GONE);
        });
    }

    private void updatePriceDisplay(int checkedId) {
        double price;
        if (checkedId == R.id.rbRon95)       price = PRICE_RON95;
        else if (checkedId == R.id.rbRon97)  price = PRICE_RON97;
        else                                  price = PRICE_DIESEL;
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
        @SuppressLint("WrongViewCast") MaterialButton btnReset     = findViewById(R.id.btnReset);
        btnCalculate.setOnClickListener(v -> performCalculation());
        btnReset.setOnClickListener(v -> resetAll());
    }

    private void performCalculation() {
        boolean isRon95 = rbRon95.isChecked();
        double pricePerLitre;
        if (isRon95)              pricePerLitre = PRICE_RON95;
        else if (rbRon97.isChecked()) pricePerLitre = PRICE_RON97;
        else                          pricePerLitre = PRICE_DIESEL;

        double fuelUsage; // resolved litres, regardless of input mode

        if (rbByLitres.isChecked()) {
            // --- Mode: user entered litres ---
            String usageStr = etFuelUsage.getText() != null
                    ? etFuelUsage.getText().toString().trim() : "";
            if (TextUtils.isEmpty(usageStr)) {
                etFuelUsage.setError("Please enter fuel usage in litres");
                etFuelUsage.requestFocus();
                return;
            }
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
        } else {
            // --- Mode: user entered RM amount ---
            String amountStr = etAmountRm.getText() != null
                    ? etAmountRm.getText().toString().trim() : "";
            if (TextUtils.isEmpty(amountStr)) {
                etAmountRm.setError("Please enter an amount in RM");
                etAmountRm.requestFocus();
                return;
            }
            double amountRm;
            try {
                amountRm = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                return;
            }
            if (amountRm <= 0) {
                Toast.makeText(this, "Amount must be greater than zero", Toast.LENGTH_SHORT).show();
                return;
            }
            // Derive litres from the RM amount
            fuelUsage = amountRm / pricePerLitre;
        }

        // Step 1
        double totalCost  = fuelUsage * pricePerLitre;
        // Step 2
        double budiRebate = isRon95 ? fuelUsage * BUDI_SUBSIDY_RATE : 0.0;
        // Step 3
        double finalPayable = totalCost - budiRebate;

        displayResults(totalCost, fuelUsage, budiRebate, finalPayable, isRon95);
    }

    private void displayResults(double totalCost, double fuelUsage,
                                double budiRebate, double finalPayable,
                                boolean isBudiEligible) {
        tvTotalCost.setText(formatRm(totalCost));

        // Show petrol amount
        tvPetrolLitres.setText(String.format(Locale.US, "%.2f L", fuelUsage));
        rowPetrolLitres.setVisibility(View.VISIBLE);

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
        etAmountRm.setText("");
        rbRon95.setChecked(true);
        rbByLitres.setChecked(true);
        layoutInputLitres.setVisibility(View.VISIBLE);
        layoutInputAmount.setVisibility(View.GONE);
        updateBudiSection(true);
        updatePriceDisplay(R.id.rbRon95);
        layoutResults.setVisibility(View.GONE);
        etFuelUsage.setError(null);
        etAmountRm.setError(null);
    }
}
