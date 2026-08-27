package com.example.quanlythucung.customer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.example.quanlythucung.pet.ui.MyPetsActivity;
import com.example.quanlythucung.service.ui.ServiceDiscoveryActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class CustomerHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        setupBottomNavigation();
        setupQuickActions();
        setupSectionActions();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavCustomer);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_customer_home);
            bottomNav.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_customer_home) {
                    return true;
                } else if (itemId == R.id.nav_customer_pets) {
                    startActivity(new Intent(this, MyPetsActivity.class));
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

    private void setupQuickActions() {
        MaterialCardView btnActionBookAppointment = findViewById(R.id.btnActionBookAppointment);
        if (btnActionBookAppointment != null) {
            btnActionBookAppointment.setOnClickListener(v ->
                    Toast.makeText(this, "Tính năng đặt lịch đang được phát triển", Toast.LENGTH_SHORT).show());
        }

        MaterialCardView btnActionMyPets = findViewById(R.id.btnActionMyPets);
        if (btnActionMyPets != null) {
            btnActionMyPets.setOnClickListener(v ->
                    startActivity(new Intent(this, MyPetsActivity.class)));
        }

        MaterialCardView btnActionServices = findViewById(R.id.btnActionServices);
        if (btnActionServices != null) {
            btnActionServices.setOnClickListener(v ->
                    startActivity(new Intent(this, ServiceDiscoveryActivity.class)));
        }

        MaterialCardView btnActionShop = findViewById(R.id.btnActionShop);
        if (btnActionShop != null) {
            btnActionShop.setOnClickListener(v ->
                    Toast.makeText(this, "Cửa hàng sản phẩm đang được phát triển", Toast.LENGTH_SHORT).show());
        }
    }

    private void setupSectionActions() {
        MaterialButton btnBookAppointmentCta = findViewById(R.id.btnBookAppointmentCta);
        if (btnBookAppointmentCta != null) {
            btnBookAppointmentCta.setOnClickListener(v ->
                    Toast.makeText(this, "Tính năng đặt lịch đang được phát triển", Toast.LENGTH_SHORT).show());
        }

        MaterialButton btnAddPetCta = findViewById(R.id.btnAddPetCta);
        if (btnAddPetCta != null) {
            btnAddPetCta.setOnClickListener(v ->
                    startActivity(new Intent(this, MyPetsActivity.class)));
        }

        TextView btnViewAllServices = findViewById(R.id.btnViewAllServices);
        if (btnViewAllServices != null) {
            btnViewAllServices.setOnClickListener(v ->
                    startActivity(new Intent(this, ServiceDiscoveryActivity.class)));
        }

        MaterialCardView cardFeaturedSpa = findViewById(R.id.cardFeaturedSpa);
        if (cardFeaturedSpa != null) {
            cardFeaturedSpa.setOnClickListener(v ->
                    startActivity(new Intent(this, ServiceDiscoveryActivity.class)));
        }

        MaterialCardView cardFeaturedHealth = findViewById(R.id.cardFeaturedHealth);
        if (cardFeaturedHealth != null) {
            cardFeaturedHealth.setOnClickListener(v ->
                    startActivity(new Intent(this, ServiceDiscoveryActivity.class)));
        }

        MaterialCardView cardFeaturedBoarding = findViewById(R.id.cardFeaturedBoarding);
        if (cardFeaturedBoarding != null) {
            cardFeaturedBoarding.setOnClickListener(v ->
                    startActivity(new Intent(this, ServiceDiscoveryActivity.class)));
        }
    }
}
