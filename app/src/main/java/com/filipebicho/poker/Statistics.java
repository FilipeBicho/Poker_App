package com.filipebicho.poker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


public class Statistics extends AppCompatActivity {

    private TextView mostPlayed;
    private TextView mostWinnings;
    private TextView biggestHand;
    private TextView numberOfGamesLabel;
    private TextView numberOfWinningGamesLabel;
    private TextView percentageWinningLabel;
    private TextView titleStatistics;

    private ArrayList<ImageView> mostPlayedCardsLabel;
    private ArrayList<ImageView> mostWinningCardsLabel;
    private ArrayList<ImageView> biggestHandCardsLabel;

    private ArrayList<Cards> mostPlayedCards1;
    private ArrayList<Cards> mostPlayedCards2;
    private ArrayList<Integer> mostPlayedCardsCounter;

    private ArrayList<Cards> mostWinningCards1;
    private ArrayList<Cards> mostWinningCards2;
    private ArrayList<Integer> mostWinningCardsCounter;

    private ArrayList<Cards> biggestHandCards;
    private String biggestHandRanking;
    private int biggestHandResult;

    private int numberOfGames;
    private int numberOfWinningGames;

    private String userName;

    SetStatistics statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Get the user name
        Intent intent = getIntent();
        userName = intent.getExtras().getString("userName");

        // Initialize title statistics with user name
        titleStatistics = (TextView) findViewById(R.id.title_statistics);
        titleStatistics.setText(userName + " Statistics");

        // Initialize SetStatistics class
        statistics = new SetStatistics(userName);

        // Initialize TextViews
        mostPlayed = (TextView) findViewById(R.id.most_played);
        mostWinnings = (TextView) findViewById(R.id.most_winning);
        biggestHand = (TextView) findViewById(R.id.biggest_hand);
        numberOfGamesLabel = (TextView) findViewById(R.id.number_games);
        numberOfWinningGamesLabel = (TextView) findViewById(R.id.number_wins);
        percentageWinningLabel = (TextView) findViewById(R.id.games_percentage);

        // Initialize ImageViews

        // Most played cards
        mostPlayedCardsLabel = new ArrayList<>();
        mostPlayedCardsLabel.add((ImageView) findViewById(R.id.most_played_card1));
        mostPlayedCardsLabel.add((ImageView) findViewById(R.id.most_played_card2));

        // Most winning cards
        mostWinningCardsLabel = new ArrayList<>();
        mostWinningCardsLabel.add((ImageView) findViewById(R.id.most_winning_card1));
        mostWinningCardsLabel.add((ImageView) findViewById(R.id.most_winning_card2));

        // Biggest hand cards
        biggestHandCardsLabel = new ArrayList<>();
        biggestHandCardsLabel.add((ImageView) findViewById(R.id.biggest_hand_card1));
        biggestHandCardsLabel.add((ImageView) findViewById(R.id.biggest_hand_card2));
        biggestHandCardsLabel.add((ImageView) findViewById(R.id.biggest_hand_card3));
        biggestHandCardsLabel.add((ImageView) findViewById(R.id.biggest_hand_card4));
        biggestHandCardsLabel.add((ImageView) findViewById(R.id.biggest_hand_card5));

        // Initialize the array that stores the most played cards
        mostPlayedCards1 = new ArrayList<>();
        mostPlayedCards2 = new ArrayList<>();
        mostPlayedCardsCounter = new ArrayList<>();

        // Initialize the array that stores the most winning cards
        mostWinningCards1 = new ArrayList<>();
        mostWinningCards2 = new ArrayList<>();
        mostWinningCardsCounter = new ArrayList<>();

        // Initialize the array that stores the biggest hand so far
        biggestHandCards = new ArrayList<>();
        biggestHandRanking = "";

        mostPlayedCards1 = statistics.getMostPlayedCards1();
        mostPlayedCards2 = statistics.getMostPlayedCards2();
        mostPlayedCardsCounter = statistics.getMostPlayedCardsCounter();

        mostWinningCards1 = statistics.getMostWinningCards1();
        mostWinningCards2 = statistics.getMostWinningCards2();
        mostWinningCardsCounter = statistics.getMostWinningCardsCounter();

        biggestHandCards = statistics.getBiggestHandCards();
        biggestHandRanking = statistics.getBiggestHandRanking();

        numberOfGames = statistics.getNumberOfGames();
        numberOfWinningGames = statistics.getNumberOfWinningGames();

        showStatistics();
    }


    public void showStatistics()
    {
        int max = 0, maxIndexMostPlayed = 0, maxIndexMostWinning = 0;



        // Get the index of the most played cards
        int i = 0;
        for(Integer counter : mostPlayedCardsCounter)
        {
            if(max < counter)
            {
                max = counter;
                maxIndexMostPlayed = i;
            }
            i++;
        }

        // Get the index of the most winning cards
        i = 0;
        max = 0;
        for(Integer counter : mostWinningCardsCounter)
        {
            if(max < counter)
            {
                max = counter;
                maxIndexMostWinning = i;
            }
            i++;
        }

        if(!mostPlayedCardsCounter.isEmpty())
        {
            // Show most played cards
            int mostPlayedCardId1 = getResources().getIdentifier(
                    Cards.getSrcCard(mostPlayedCards1.get(maxIndexMostPlayed)),
                    "drawable",
                    getPackageName()
            );
            mostPlayedCardsLabel.get(0).setImageResource(mostPlayedCardId1);
            int mostPlayedCardId2 = getResources().getIdentifier(
                    Cards.getSrcCard(mostPlayedCards2.get(maxIndexMostPlayed)),
                    "drawable",
                    getPackageName()
            );
            mostPlayedCardsLabel.get(1).setImageResource(mostPlayedCardId2);
            mostPlayed.setText(mostPlayedCardsCounter.get(maxIndexMostPlayed) + "");
        }


        if(!mostWinningCardsCounter.isEmpty())
        {
            // Show most winning cards
            int mostWinningCardId1 = getResources().getIdentifier(
                    Cards.getSrcCard(mostWinningCards1.get(maxIndexMostWinning)),
                    "drawable",
                    getPackageName()
            );
            mostWinningCardsLabel.get(0).setImageResource(mostWinningCardId1);
            int mostWinningCardId2 = getResources().getIdentifier(
                    Cards.getSrcCard(mostWinningCards2.get(maxIndexMostWinning)),
                    "drawable",
                    getPackageName()
            );
            mostWinningCardsLabel.get(1).setImageResource(mostWinningCardId2);
            mostWinnings.setText(mostWinningCardsCounter.get(maxIndexMostWinning) + "");
        }

        if(!biggestHandRanking.isEmpty())
        {
            // Show biggest hand
            for(i = 0; i < biggestHandCards.size(); i++)
            {
                int biggestCardID = getResources().getIdentifier(
                        Cards.getSrcCard(biggestHandCards.get(i)),
                        "drawable",
                        getPackageName()
                );
                biggestHandCardsLabel.get(i).setImageResource(biggestCardID);
            }
            biggestHand.setText(biggestHandRanking + "");
        }


        // Show total of games, winning games and percentage of winning
        numberOfGamesLabel.setText("You played " + numberOfGames + " games");
        numberOfWinningGamesLabel.setText("You won " + numberOfWinningGames + " games");
        percentageWinningLabel.setText(Math.round(((float)numberOfWinningGames / numberOfGames * 100) * 10) / 10.0 + " %");


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
