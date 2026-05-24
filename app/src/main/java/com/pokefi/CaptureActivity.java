package com.pokefi;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CaptureActivity extends AppCompatActivity {

    private boolean captured = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        String name    = getIntent().getStringExtra("name");
        String rarity  = getIntent().getStringExtra("rarity");
        String element = getIntent().getStringExtra("element");
        String emoji   = getIntent().getStringExtra("emoji");

        ((TextView) findViewById(R.id.captureEmoji)).setText(emoji);
        ((TextView) findViewById(R.id.captureName)).setText(name);
        ((TextView) findViewById(R.id.captureDetails)).setText(element + "  •  " + rarity);

        Button throwBtn  = findViewById(R.id.throwButton);
        TextView gotcha  = findViewById(R.id.gotchaText);
        TextView hint    = findViewById(R.id.hintText);
        View    ball     = findViewById(R.id.pokeballView);

        throwBtn.setOnClickListener(v -> {
            if (captured) return;

            // Animate the ball: fly up, wiggle, bounce back
            ObjectAnimator moveUp   = ObjectAnimator.ofFloat(ball, "translationY", 0f, -400f);
            moveUp.setDuration(400);

            ObjectAnimator wiggle   = ObjectAnimator.ofFloat(ball, "rotation", 0f, 20f, -20f, 10f, 0f);
            wiggle.setDuration(500);
            wiggle.setStartDelay(400);

            ObjectAnimator bounceDown = ObjectAnimator.ofFloat(ball, "translationY", -400f, 0f);
            bounceDown.setDuration(500);
            bounceDown.setInterpolator(new BounceInterpolator());
            bounceDown.setStartDelay(900);

            // Pulse the emoji
            ObjectAnimator pulseX = ObjectAnimator.ofFloat(
                    findViewById(R.id.captureEmoji), "scaleX", 1f, 1.6f, 1f);
            ObjectAnimator pulseY = ObjectAnimator.ofFloat(
                    findViewById(R.id.captureEmoji), "scaleY", 1f, 1.6f, 1f);
            pulseX.setDuration(600); pulseX.setStartDelay(900);
            pulseY.setDuration(600); pulseY.setStartDelay(900);
            pulseX.setInterpolator(new AccelerateDecelerateInterpolator());
            pulseY.setInterpolator(new AccelerateDecelerateInterpolator());

            AnimatorSet set = new AnimatorSet();
            set.playTogether(moveUp, wiggle, bounceDown, pulseX, pulseY);
            set.start();

            // Show gotcha after animation
            ball.postDelayed(() -> {
                gotcha.setVisibility(View.VISIBLE);
                hint.setVisibility(View.VISIBLE);
                throwBtn.setText("✅ Caught!");
                throwBtn.setEnabled(false);
                captured = true;

                // Bounce the gotcha text in
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(gotcha, "scaleX", 0f, 1.2f, 1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(gotcha, "scaleY", 0f, 1.2f, 1f);
                AnimatorSet pop = new AnimatorSet();
                pop.playTogether(scaleX, scaleY);
                pop.setDuration(400);
                pop.start();
            }, 1500);
        });

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }
}
