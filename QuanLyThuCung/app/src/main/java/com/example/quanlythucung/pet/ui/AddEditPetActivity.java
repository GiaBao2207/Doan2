package com.example.quanlythucung.pet.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

public class AddEditPetActivity extends AppCompatActivity {

    public static final String EXTRA_IS_EDIT_MODE = "EXTRA_IS_EDIT_MODE";

    private boolean isEditMode = false;

    private TextView tvScreenTitle;
    private LinearLayout layoutPetPhoto;
    private TextView btnChangePhoto;
    private TextInputEditText etPetName;

    private MaterialButton btnSpeciesDog;
    private MaterialButton btnSpeciesCat;
    private MaterialButton btnSpeciesOther;

    private LinearLayout layoutOtherSpecies;
    private TextInputEditText etOtherSpecies;
    private TextInputEditText etPetBreed;

    private MaterialButton btnGenderMale;
    private MaterialButton btnGenderFemale;
    private MaterialButton btnGenderUnknown;

    private TextInputEditText etPetDob;
    private TextInputEditText etPetWeight;
    private TextInputEditText etPetNotes;
    private MaterialButton btnDeletePet;
    private MaterialButton btnSavePet;

    private String selectedSpecies = "";
    private String selectedGender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_pet);

        isEditMode = getIntent().getBooleanExtra(EXTRA_IS_EDIT_MODE, false);

        initViews();
        setupMode();
        setupListeners();
    }

    private void initViews() {
        tvScreenTitle = findViewById(R.id.tvScreenTitle);
        layoutPetPhoto = findViewById(R.id.layoutPetPhoto);
        btnChangePhoto = findViewById(R.id.btnChangePhoto);
        etPetName = findViewById(R.id.etPetName);

        btnSpeciesDog = findViewById(R.id.btnSpeciesDog);
        btnSpeciesCat = findViewById(R.id.btnSpeciesCat);
        btnSpeciesOther = findViewById(R.id.btnSpeciesOther);

        layoutOtherSpecies = findViewById(R.id.layoutOtherSpecies);
        etOtherSpecies = findViewById(R.id.etOtherSpecies);
        etPetBreed = findViewById(R.id.etPetBreed);

        btnGenderMale = findViewById(R.id.btnGenderMale);
        btnGenderFemale = findViewById(R.id.btnGenderFemale);
        btnGenderUnknown = findViewById(R.id.btnGenderUnknown);

        etPetDob = findViewById(R.id.etPetDob);
        etPetWeight = findViewById(R.id.etPetWeight);
        etPetNotes = findViewById(R.id.etPetNotes);
        btnDeletePet = findViewById(R.id.btnDeletePet);
        btnSavePet = findViewById(R.id.btnSavePet);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    private void setupMode() {
        if (isEditMode) {
            if (tvScreenTitle != null) {
                tvScreenTitle.setText("Chỉnh sửa thú cưng");
            }
            if (btnSavePet != null) {
                btnSavePet.setText("Lưu thay đổi");
            }
            if (btnDeletePet != null) {
                btnDeletePet.setVisibility(View.VISIBLE);
            }
        } else {
            if (tvScreenTitle != null) {
                tvScreenTitle.setText("Thêm thú cưng");
            }
            if (btnSavePet != null) {
                btnSavePet.setText("Lưu thú cưng");
            }
            if (btnDeletePet != null) {
                btnDeletePet.setVisibility(View.GONE);
            }
        }
    }

    private void setupListeners() {
        // Species selection
        if (btnSpeciesDog != null) {
            btnSpeciesDog.setOnClickListener(v -> selectSpecies(btnSpeciesDog, "Chó"));
        }
        if (btnSpeciesCat != null) {
            btnSpeciesCat.setOnClickListener(v -> selectSpecies(btnSpeciesCat, "Mèo"));
        }
        if (btnSpeciesOther != null) {
            btnSpeciesOther.setOnClickListener(v -> selectSpecies(btnSpeciesOther, "Khác"));
        }

        // Gender selection
        if (btnGenderMale != null) {
            btnGenderMale.setOnClickListener(v -> selectGender(btnGenderMale, "Đực"));
        }
        if (btnGenderFemale != null) {
            btnGenderFemale.setOnClickListener(v -> selectGender(btnGenderFemale, "Cái"));
        }
        if (btnGenderUnknown != null) {
            btnGenderUnknown.setOnClickListener(v -> selectGender(btnGenderUnknown, "Không rõ"));
        }

        View.OnClickListener photoClickListener = v ->
                Toast.makeText(this, "Chọn ảnh thú cưng", Toast.LENGTH_SHORT).show();

        if (layoutPetPhoto != null) {
            layoutPetPhoto.setOnClickListener(photoClickListener);
        }
        if (btnChangePhoto != null) {
            btnChangePhoto.setOnClickListener(photoClickListener);
        }

        if (etPetDob != null) {
            etPetDob.setOnClickListener(v -> showDatePicker());
        }

        View tilDob = findViewById(R.id.tilPetDob);
        if (tilDob != null) {
            tilDob.setOnClickListener(v -> showDatePicker());
        }

        if (btnDeletePet != null) {
            btnDeletePet.setOnClickListener(v -> {
                Toast.makeText(this, "Đã xóa thú cưng", Toast.LENGTH_SHORT).show();
                finish();
            });
        }

        if (btnSavePet != null) {
            btnSavePet.setOnClickListener(v -> handleSave());
        }
    }

    private void selectSpecies(MaterialButton selectedBtn, String species) {
        if (btnSpeciesDog != null) btnSpeciesDog.setChecked(selectedBtn == btnSpeciesDog);
        if (btnSpeciesCat != null) btnSpeciesCat.setChecked(selectedBtn == btnSpeciesCat);
        if (btnSpeciesOther != null) btnSpeciesOther.setChecked(selectedBtn == btnSpeciesOther);

        selectedSpecies = species;
        if (layoutOtherSpecies != null) {
            layoutOtherSpecies.setVisibility("Khác".equals(species) ? View.VISIBLE : View.GONE);
        }
    }

    private void selectGender(MaterialButton selectedBtn, String gender) {
        if (btnGenderMale != null) btnGenderMale.setChecked(selectedBtn == btnGenderMale);
        if (btnGenderFemale != null) btnGenderFemale.setChecked(selectedBtn == btnGenderFemale);
        if (btnGenderUnknown != null) btnGenderUnknown.setChecked(selectedBtn == btnGenderUnknown);

        selectedGender = gender;
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                            selectedDay, selectedMonth + 1, selectedYear);
                    if (etPetDob != null) {
                        etPetDob.setText(formattedDate);
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void handleSave() {
        String petName = etPetName != null && etPetName.getText() != null
                ? etPetName.getText().toString().trim()
                : "";

        if (petName.isEmpty()) {
            if (etPetName != null) {
                etPetName.setError("Vui lòng nhập tên thú cưng");
                etPetName.requestFocus();
            }
            Toast.makeText(this, "Vui lòng nhập tên thú cưng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedSpecies.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn loài thú cưng", Toast.LENGTH_SHORT).show();
            return;
        }

        if ("Khác".equals(selectedSpecies)) {
            String otherSpecies = etOtherSpecies != null && etOtherSpecies.getText() != null
                    ? etOtherSpecies.getText().toString().trim()
                    : "";
            if (otherSpecies.isEmpty()) {
                if (etOtherSpecies != null) {
                    etOtherSpecies.setError("Vui lòng nhập loài thú cưng");
                    etOtherSpecies.requestFocus();
                }
                Toast.makeText(this, "Vui lòng nhập loài thú cưng", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String successMsg = isEditMode ? "Đã cập nhật thông tin thú cưng" : "Đã thêm thú cưng thành công";
        Toast.makeText(this, successMsg, Toast.LENGTH_SHORT).show();
        finish();
    }
}
