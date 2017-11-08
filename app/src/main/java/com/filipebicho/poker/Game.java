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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Game extends AppCompatActivity {

    // Game title
    private TextView gameTitle;
    // Game action, where the last move is showed
    private TextView gameAction;
    // Opponent and player Chips
    private TextView opponentMoney;
    private TextView playerMoney;
    // Opponent and player name
    private TextView opponentName;
    private TextView playerName;
    // Opponent and player bet
    private TextView opponentBet;
    private TextView playerBet;
    // Opponent and player dealer
    private ImageView opponentDealer;
    private ImageView playerDealer;
    // Opponent and player hand ranking
    private TextView opponentRanking;
    private TextView playerRanking;
    // Opponent and player cards
    private ArrayList<ImageView> opponentCardsImg;
    private ArrayList<ImageView> playerCardsImg;
    // Table cards;
    private ArrayList<ImageView> tableCardsImg;
    // Fold call and bet buttons
    private Button foldButton;
    private Button callButton;
    private Button betButton;
    // Bet seekbar
    private SeekBar betSeekBar;
    // Bet edit text;
    private TextView betText;
    // Text Summary
    private TextView summary;
    // Animation for button
    private Animation buttonAnim;

    // Store opponent and player money
    int[] money = new int[2];

    // Stores opponent and player bets
    int[] bet = new int[2];

    // Small and bigBlind
    private int smallBlind = 20;
    private int bigBlind = 40;

    // Store opponent cards
    ArrayList<Cards> opponentCards;
    // Store player cards
    ArrayList<Cards> playerCards;
    // Store table cards
    ArrayList<Cards> tableCards;

    // bet
    private int betValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize gameTitle
        gameTitle = (TextView) findViewById(R.id.game_title);

        // Initialize gameAction
        gameAction = (TextView) findViewById(R.id.game_action);
        gameAction.setText("--");

        // Initialize opponent and player money
        money[0] = 1500;
        opponentMoney = (TextView) findViewById(R.id.opponent_money);
        opponentMoney.setText(money[0] + " €");

        money[1] = 1500;
        playerMoney = (TextView) findViewById(R.id.player_money);
        playerMoney.setText(money[1] + " €");

        // Initialize opponent and player names
        opponentName = (TextView) findViewById(R.id.opponent_name);
        playerName = (TextView) findViewById(R.id.player_name);

        // Initialize opponent and player bets
        opponentBet = (TextView) findViewById(R.id.opponent_bet);
        bet[0] = 0;
        opponentBet.setText(bet[0] + " €");

        playerBet = (TextView) findViewById(R.id.player_bet);
        bet[1] = 0;
        playerBet.setText(bet[1] + " €");

        // Initialize opponent and player dealer chip
        opponentDealer = (ImageView) findViewById(R.id.opponent_dealer);
        opponentDealer.setVisibility(View.INVISIBLE);
        playerDealer = (ImageView) findViewById(R.id.player_dealer);
        playerDealer.setVisibility(View.VISIBLE);

        // Initialize opponent and player hand ranking
        opponentRanking = (TextView) findViewById(R.id.opponent_ranking);
        opponentRanking.setText("");
        playerRanking = (TextView) findViewById(R.id.player_ranking);
        playerRanking.setText("");

        // Initialize opponent cards
        opponentCardsImg = new ArrayList<>();
        opponentCardsImg.add((ImageView) findViewById(R.id.card_opponent1));
        opponentCardsImg.add((ImageView) findViewById(R.id.card_opponent2));
        opponentCardsImg.get(0).setImageResource(R.drawable.back);
        opponentCardsImg.get(1).setImageResource(R.drawable.back);

        // Initialize player cards
        playerCardsImg = new ArrayList<>();
        playerCardsImg.add((ImageView) findViewById(R.id.card_player1));
        playerCardsImg.add((ImageView) findViewById(R.id.card_player2));
        playerCardsImg.get(0).setImageResource(R.drawable.back);
        playerCardsImg.get(1).setImageResource(R.drawable.back);

        // Initialize table cards
        tableCardsImg = new ArrayList<>();
        tableCardsImg.add((ImageView) findViewById(R.id.card_flop1));
        tableCardsImg.add((ImageView) findViewById(R.id.card_flop2));
        tableCardsImg.add((ImageView) findViewById(R.id.card_flop3));
        tableCardsImg.add((ImageView) findViewById(R.id.card_turn));
        tableCardsImg.add((ImageView) findViewById(R.id.card_river));

        // hide table cards
        for(ImageView card: tableCardsImg)
            card.setVisibility(View.INVISIBLE);

        // Initialize game summary
        summary = (TextView) findViewById(R.id.summary);

       // Animation for buttons
        buttonAnim = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.click_scale);

        // Initialize buttons
        foldButton = (Button) findViewById(R.id.fold);
        callButton = (Button) findViewById(R.id.call);
        betButton = (Button) findViewById(R.id.bet);

        // Initialize seek bar
        betSeekBar = (SeekBar) findViewById(R.id.seekBar);
        //set min value to betSeekBar
        if(betSeekBar.getProgress() < bigBlind)
            betSeekBar.setProgress(bigBlind);
        if(betSeekBar.getProgress() > bigBlind && betSeekBar.getProgress() < 2*bigBlind)
            betSeekBar.setProgress(bigBlind*2);
        betSeekBar.setMax(money[1]);


        // Initialize edittext for the poker chips value
        betText = (TextView) findViewById(R.id.poker_chips);

        // Fold button onClick
        foldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foldButton.startAnimation(buttonAnim);
                Toast.makeText(getApplication(), "Fold", Toast.LENGTH_LONG).show();
            }
        });

        // Call button onClick
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callButton.startAnimation(buttonAnim);
                Toast.makeText(getApplication(), "Call", Toast.LENGTH_LONG).show();
            }
        });

        // Bet button onCLick
        betButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                betButton.startAnimation(buttonAnim);
                Toast.makeText(getApplication(), "Bet", Toast.LENGTH_LONG).show();
            }
        });

        // perform seek bar change listener event used for getting the progress value
        betSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                //set min value to betSeekBar
                if(betSeekBar.getProgress() < bigBlind)
                    betSeekBar.setProgress(bigBlind);

                betValue = seekBar.getProgress();
                // Set the amount of poker chips with the seek bar
                betText.setText(betValue+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // HERE GOES AN IF STATEMENT TO CHECK WHICH KIND OF GAME IS!
        initializeGameUserVsComputer();
    }

    // Method to start a new game user VS computer
    public void initializeGameUserVsComputer()
    {
        initializeGameCards();

        for(int i = 0; i < 3; i++)
            tableCardsImg.get(i).setVisibility(View.VISIBLE);
    }

    // Method to initialize game cards
    public void initializeGameCards()
    {
        Deck deck = new Deck();
        Dealer dealer = new Dealer();

        opponentCards = new ArrayList<>();
        playerCards = new ArrayList<>();
        tableCards = new ArrayList<>();

        dealer.giveCards(deck,opponentCards, playerCards);
        dealer.giveFlop(deck,tableCards);

        // Get id's of opponent cards
        int opponentCardId1 = getResources().getIdentifier(
                Cards.getSrcCard(opponentCards.get(0)),
                "drawable",
                getPackageName()
        );
        opponentCardsImg.get(0).setImageResource(opponentCardId1);

        int opponentCardId2 = getResources().getIdentifier(
                Cards.getSrcCard(opponentCards.get(1)),
                "drawable",
                getPackageName()
        );
        opponentCardsImg.get(1).setImageResource(opponentCardId2);

        // Get id's of player cards
        int playerCardId1 = getResources().getIdentifier(
                Cards.getSrcCard(playerCards.get(0)),
                "drawable",
                getPackageName()
        );
        playerCardsImg.get(0).setImageResource(playerCardId1);

        int playerCardId2 = getResources().getIdentifier(
                Cards.getSrcCard(playerCards.get(1)),
                "drawable",
                getPackageName()
        );
        playerCardsImg.get(1).setImageResource(playerCardId2);

        // Get id's of table cards
        int tableCardId1 = getResources().getIdentifier(
                Cards.getSrcCard(tableCards.get(0)),
                "drawable",
                getPackageName()
        );
        tableCardsImg.get(0).setImageResource(tableCardId1);

        int tableCardId2 = getResources().getIdentifier(
                Cards.getSrcCard(tableCards.get(1)),
                "drawable",
                getPackageName()
        );
        tableCardsImg.get(1).setImageResource(tableCardId2);

        int tableCardId3 = getResources().getIdentifier(
                Cards.getSrcCard(tableCards.get(2)),
                "drawable",
                getPackageName()
        );
        tableCardsImg.get(2).setImageResource(tableCardId3);
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
