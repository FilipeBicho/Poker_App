package com.filipebicho.poker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Get animation for start button
        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.click_translate);

        // Get the user name
        Intent intent = getIntent();
        String name = intent.getExtras().getString("userName");

        // Create an explicit intent to go to game activity
        final Intent game_intent = new Intent(Menu.this, Game.class);

        // Create and initialize the list of game options
        final ArrayList<MenuOption> menuItem = new ArrayList<>();

        menuItem.add(new MenuOption(
                R.drawable.user,
                name + " vs Passive",
                R.drawable.uservspassive));

        menuItem.add(new MenuOption(
                R.drawable.user,
                name + " vs Agressive",
                R.drawable.uservsaggressive));

        menuItem.add(new MenuOption(
                R.drawable.user,
                name + " vs Mix",
                R.drawable.uservsmix));

        menuItem.add(new MenuOption(
                R.drawable.user,
                name + " vs MFS",
                R.drawable.uservsmfs));

        menuItem.add(new MenuOption(
                R.drawable.computer,
                "Passive vs Aggressive ",
                R.drawable.passivevsaggressive));

        menuItem.add(new MenuOption(
                R.drawable.computer,
                "Passive vs Mix",
                R.drawable.passivevsmix));

        menuItem.add(new MenuOption(
                R.drawable.computer,
                "Passive vs MFS",
                R.drawable.passivevsmfs));

        menuItem.add(new MenuOption(
                R.drawable.computer,
                "Aggressive vs Mix",
                R.drawable.aggressivevsmix));

        menuItem.add(new MenuOption(
                R.drawable.computer,
                "Aggressive vs MFS",
                R.drawable.aggressivevsmfs));

        menuItem.add(new MenuOption(
                R.drawable.computer,
                "Mix vs MFS",
                R.drawable.mixvsmfs));

        menuItem.add(new MenuOption(
                R.drawable.statistics,
                "Statistics",
                R.drawable.userstatistics));

        // Create the adapter to convert the array to views
        MenuOptionAdapter adapter = new MenuOptionAdapter(this, menuItem);

        // Attach the adapter to a list
        ListView listView = (ListView) findViewById(R.id.menu_list);

        // Set the adapter
        listView.setAdapter(adapter);

        //Set a click listener when a button from the list view is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                // Get the MenuOption object at the given position the user clicked on
                final MenuOption item = menuItem.get(position);

                // Get MenuOption LinearLayout
                final LinearLayout item_layout = (LinearLayout)view.findViewById(R.id.menu_layout);
                item_layout.startAnimation(animation);

                // Wait 0.3s before go the the new activity
                Runnable r = new Runnable()
                {
                    public void run()
                    {
                        // Pass the game option that the user choosed
                        game_intent.putExtra("game_mode",position + "");
                        startActivity(game_intent);
                    }
                };
                view.postDelayed(r, 300);

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




