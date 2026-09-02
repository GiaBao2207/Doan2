package com.example.quanlythucung.pet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.example.quanlythucung.customer.ui.CustomerHomeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class MyPetsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pets);

        setupBottomNavigation();
        setupActions();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavCustomer);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_customer_pets);
            bottomNav.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_customer_pets) {
                    return true;
                } else if (itemId == R.id.nav_customer_home) {
                    startActivity(new Intent(this, CustomerHomeActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_customer_appointments) {
                    Toast.makeText(this, "Tính năng lịch hẹn đang được phát triển", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (itemId == R.id.nav_customer_shop) {
                    Toast.makeText(this, "Cửa hàng sản phẩm đang được phát triển", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (itemId == R.id.nav_customer_account) {
                    Toast.makeText(this, "Tài khoản đang được phát triển", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return false;
            });
        }
    }

    private void setupActions() {
        MaterialButton btnAddPetHeader = findViewById(R.id.btnAddPetHeader);
        if (btnAddPetHeader != null) {
            btnAddPetHeader.setOnClickListener(v ->
                    startActivity(new Intent(this, AddEditPetActivity.class)));
        }

        MaterialButton btnAddPetEmptyState = findViewById(R.id.btnAddPetEmptyState);
        if (btnAddPetEmptyState != null) {
            btnAddPetEmptyState.setOnClickListener(v ->
                    startActivity(new Intent(this, AddEditPetActivity.class)));
        }
    }
}
