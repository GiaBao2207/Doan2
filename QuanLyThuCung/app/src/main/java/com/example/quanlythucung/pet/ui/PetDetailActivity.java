package com.example.quanlythucung.pet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.google.android.material.button.MaterialButton;

public class PetDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);

        setupTopBar();
        setupActions();
    }

    private void setupTopBar() {
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        ImageView btnMoreOptions = findViewById(R.id.btnMoreOptions);
        if (btnMoreOptions != null) {
            btnMoreOptions.setOnClickListener(v ->
                    Toast.makeText(this, "Tùy chọn", Toast.LENGTH_SHORT).show());
        }
    }

    private void setupActions() {
        MaterialButton btnBookForPet = findViewById(R.id.btnBookForPet);
        if (btnBookForPet != null) {
            btnBookForPet.setOnClickListener(v ->
                    Toast.makeText(this, "Tính năng đặt lịch hẹn đang được phát triển", Toast.LENGTH_SHORT).show());
        }

        MaterialButton btnEditPet = findViewById(R.id.btnEditPet);
        if (btnEditPet != null) {
            btnEditPet.setOnClickListener(v -> {
                Intent intent = new Intent(this, AddEditPetActivity.class);
                intent.putExtra(AddEditPetActivity.EXTRA_IS_EDIT_MODE, true);
                startActivity(intent);
            });
        }
    }
}
