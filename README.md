# PetrolCalculator – Smart Petrol Cost Calculator

A native Android application that estimates petrol costs in Malaysia and applies the **BUDI MADANI** fuel subsidy rebate for eligible users.

---

## Features

- ⛽ Supports **RON95**, **RON97**, and **Diesel** petrol types
- 💰 **BUDI MADANI** subsidy rebate (RM1.99/litre) for RON95 eligible users
- 📊 Instant cost breakdown: Total Cost, BUDI Rebate, Final Payable, and Total Saving
- 🎨 Clean, minimal blue-themed UI with Material 3 Design components
- 📱 Navigation via kebab menu (⋮) in the top-right corner

---

## Calculation Formula

| Output | Formula |
|---|---|
| Total Petrol Cost | Fuel Usage (L) × Petrol Price (RM/L) |
| BUDI MADANI Rebate | Fuel Usage (L) × RM1.99 *(RON95 + eligible only)* |
| Final Payable | Total Petrol Cost − BUDI Rebate |
| Total Saving | BUDI Rebate |

---

## Sample Calculation

| Input | Value |
|---|---|
| Petrol Type | RON95 |
| Petrol Price | RM 4.27/litre |
| Fuel Usage | 40 litres |
| BUDI MADANI | Yes |

**Results:**
- Total Petrol Cost = 40 × RM4.27 = **RM 170.80**
- BUDI MADANI Rebate = 40 × RM1.99 = **RM 79.60**
- Final Payable = RM170.80 − RM79.60 = **RM 91.20**
- Total Saving = **RM 79.60**

---

## Pages

1. **Home** – App logo, description, features, and button to navigate to Calculate
2. **Calculate** – Petrol type , price, usage, BUDI MADANI eligibility (RON95 only)
3. **About** – Author details, copyright notice, and clickable GitHub link

---

## Tech Stack

- **Language:** Java
- **UI:** XML with Material 3 Components
- **Build System:** Gradle KTS (AGP 9.1.1, Gradle 9.3.1)
- **IDE:** Android Studio Panda 2 (2025.3.2)
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 36

---

## Author

| Field | Value |
|---|---|
| Name | Nor Irfan Bin Nor Hisham |
| Matric No | 2025121363|
| Course | ICT602 – Mobile Technology |

---

## License

© 2025 Nor Irfan. All rights reserved.
