package com.filipebicho.poker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Game extends AppCompatActivity {

    // Game title
    TextView gameTitle;
    // Game action, where the last move is showed
    TextView gameAction;
    // Opponent and player Chips
    TextView opponentChips;
    TextView playerChips;
    // Opponent and player name
    TextView opponentName;
    TextView playerName;
    // Opponent and player bet
    TextView


    Button foldButton;


    TextView summary;


    private int betValue = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Animation for buttons
        final Animation animation = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.click_scale);

        // Initialize buttons
        foldButton = (Button) findViewById(R.id.fold);
        final Button callButton = (Button) findViewById(R.id.call);
        final Button betButton = (Button) findViewById(R.id.bet);

        // Initialize seek bar
        final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);

        // Initialize edittext for the poker chips value
        final EditText pokerChips = (EditText) findViewById(R.id.poker_chips);

        // Fold button onClick
        foldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foldButton.startAnimation(animation);
                Toast.makeText(getApplication(), "Fold", Toast.LENGTH_LONG).show();
            }
        });

        // Call button onClick
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callButton.startAnimation(animation);
                Toast.makeText(getApplication(), "Call", Toast.LENGTH_LONG).show();
            }
        });

        // Bet button onCLick
        betButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                betButton.startAnimation(animation);
                Toast.makeText(getApplication(), "Bet", Toast.LENGTH_LONG).show();
            }
        });

        // perform seek bar change listener event used for getting the progress value
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                betValue = seekBar.getProgress();
                // Set the amount of poker chips with the seek bar
                pokerChips.setText(betValue+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // perform text change listener event used for getting text from poker chips edittext
        pokerChips.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                betValue = Integer.parseInt(editable.toString());
                // set the postion of the seek bar with the value of poker chips edittext
                seekBar.setProgress(betValue);
            }
        });
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
