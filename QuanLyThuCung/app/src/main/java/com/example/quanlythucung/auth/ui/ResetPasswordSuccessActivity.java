package com.example.quanlythucung.auth.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.google.android.material.button.MaterialButton;

public class ResetPasswordSuccessActivity extends AppCompatActivity {

    private View cardSuccessBadge;
    private MaterialButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_success);

        initViews();
        setupListeners();
        playSuccessAnimation();
    }

    private void initViews() {
        cardSuccessBadge = findViewById(R.id.cardSuccessBadge);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ResetPasswordSuccessActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void playSuccessAnimation() {
        if (cardSuccessBadge == null) return;

        float density = getResources().getDisplayMetrics().density;
        float startOffsetY = -28f * density;

        cardSuccessBadge.setTranslationY(startOffsetY);
        cardSuccessBadge.setScaleX(0.7f);
        cardSuccessBadge.setScaleY(0.7f);
        cardSuccessBadge.setAlpha(0.0f);

        ObjectAnimator translateY = ObjectAnimator.ofFloat(cardSuccessBadge, "translationY", startOffsetY, 0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(cardSuccessBadge, "scaleX", 0.7f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(cardSuccessBadge, "scaleY", 0.7f, 1.0f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(cardSuccessBadge, "alpha", 0.0f, 1.0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translateY, scaleX, scaleY, alpha);
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new OvershootInterpolator(1.2f));
        animatorSet.setStartDelay(100);
        animatorSet.start();
    }
}
