package com.filipebicho.poker;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get animation for start button
        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.click_start);

        // Get EditText with user name
        final EditText userName = (EditText) findViewById(R.id.name);

        // Create the action when start button is clicked
        final Button start = findViewById(R.id.start);

        // Create an explicit intent to go to menu activity
        final Intent menu_intent = new Intent(MainActivity.this, Menu.class);

        // Get the audio final for start button
        final MediaPlayer start_audio = MediaPlayer.create(getApplicationContext(), R.raw.start_click);

        // When start button is clicked goes to menu activity if userName isn't empty
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                // Start animation
                start.startAnimation(animation);

                //Check if user name is not empty
                if(userName.getText().toString().equals(""))
                    Toast.makeText(getApplication(), "You have to insert your name", Toast.LENGTH_LONG).show();
                else
                {

                    // Wait 0.5s before go the the new activity
                    Runnable r = new Runnable()
                    {
                        public void run()
                        {
                            start_audio.start();
                            // Pass the  userName string to menu activity
                            menu_intent.putExtra("userName", userName.getText().toString());
                            startActivity(menu_intent);
                        }
                    };
                    v.postDelayed(r, 500);
                }
            }
         }
        );
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}
