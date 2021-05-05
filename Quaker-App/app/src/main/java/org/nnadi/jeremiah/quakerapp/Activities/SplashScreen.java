package org.nnadi.jeremiah.quakerapp.Activities;

/*
 - Name: Jeremiah Nnadi
 - StudentID: S1903336
*/

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import org.nnadi.jeremiah.quakerapp.R;


/**
 * The splashscreen class extends the Activity class
 * It is the first startup screen which appears when App is opened which contains the student information and app logo
 */
public class SplashScreen extends Activity {

    int SPLASH_SCREEN_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        View relativeLayout = findViewById(R.id.splash_screen);
        relativeLayout.setBackgroundColor(Color.WHITE);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashScreen.this,
                    MainActivity.class);
            startActivity(i);
            // Starts the second activity
            finish();
            // Finishes the current activity
        }, SPLASH_SCREEN_TIME_OUT);
    }
}
