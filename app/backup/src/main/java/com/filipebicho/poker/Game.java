package com.filipebicho.poker;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Game extends AppCompatActivity {

    // Game title
    private TextView gameTitle;
    // Game action, where the last move is showed
    private TextView gameAction;
    // Opponent and player Chips
    private ArrayList<TextView> pokerChipsLabel;
    // Opponent and player name
    private TextView opponentName;
    private TextView playerName;
    // Opponent and player bet
    private ArrayList<TextView> betLabel;
    // Opponent and player odds
    private ArrayList<TextView> oddsLabel;
    // Pot money
    private TextView potLabel;
    // Opponent and player dealer
    private ArrayList<ImageView> dealerLabel;
    // Opponent and player hand ranking
    private ArrayList<TextView> rankingLabel;
    // Opponent and player cards
    private ArrayList<ImageView> opponentCardsImg;
    private ArrayList<ImageView> playerCardsImg;
    // Table cards;
    private ArrayList<ImageView> tableCardsImg;
    // Fold call and bet buttons
    private Button foldButton;
    private Button callButton;
    private Button betButton;
    // Boolean for checkButton
    private Boolean checkButton = false;
    // Bet seekbar
    private SeekBar betSeekBar;
    // Bet edit text;
    private TextView betText;
    // Text Summary
    private TextView summary;
    // Animation for button
    private Animation buttonAnim;
    // Choose the type of opponent
    private Simulator computer = new Simulator();
    // Object to store game statistics
    private SetStatistics statistics;

    // Store opponent cards
    private ArrayList<Cards> opponentCards;
    // Store player cards
    private ArrayList<Cards> playerCards;
    // Store table cards
    private ArrayList<Cards> tableCards;
    //Stores table cards temporarily
    private ArrayList<Cards> tempTableCards;
    // Stores player and opponent hands
    private ArrayList<Cards> opponentHand = new ArrayList<>();
    private ArrayList<Cards> playerHand = new ArrayList<>();

    // Stores player and opponent names
    private String userName, pcName;
    // Stores the game mode
    private int gameMode;
    // Stores the dealer
    private int dealer;
    // Boolean to check if is first play in the game
    private Boolean firstPlay = true;
    /* 0 - Player 1 poker chips
     * 1 - Player 2 poker chips
	 * 2 - Pot poker chips
	 * 3 - Checks if games continues
	 */
    private float[] pokerChips = new float[4];
    // Stores opponent and player bets
    private float[] bet = new float[2];
    // Small and bigBlind
    private int smallBlind = 20;
    private int bigBlind = 40;
    /* 1- bet options
	 * 2- bet value
	 */
    private int[] betOption = new int[2];
    // Pot money
    private int pot = 2;
    //Stores in which round the game is
    private int round;
    // Count the number of games
    private int nGames = 0;
    // Count the number of Victories
    private int nWinningGames = 0;
    // Stores opponent and player odds in flop
    private float oddsFlopOpponent;
    private float oddsFlopPlayer;
    // Stores opponent and player odds in turn
    private float oddsTurnOpponent;
    private float oddsTurnPlayer;
    // Stores opponent and player odds in river
    private float oddsRiverOpponent;
    private float oddsRiverPlayer;

    // Stores opponent and player odds
    private float[] odds = new float[2];
    // Check if its possible to make check
    private Boolean checkFlag = true;
    // Store the result of hand evaluation
    private int[] resultHand = new int[2];
    // Store the text that goes into summary
    private String txt = "";
    // bet
    private int betValue;
    // Store ranking string
    private ArrayList<String> ranking;
    // Get odds
    Odds oddsObj = new Odds();
    // Stores pot temporarily
    float tempPot;
    // Check if the game play is already finish
    Boolean finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get intent from menus
        Intent intent = getIntent();
        // Get game mode
        gameMode = Integer.valueOf(intent.getExtras().getString("game_mode"));
        gameMode++;

        // Set player and opponent names
        if (gameMode < 5) {
            // Get user name
            userName = intent.getExtras().getString("userName");
            if (gameMode == 1)
                pcName = "Passive";
            if (gameMode == 2)
                pcName = "Aggressive";
            if (gameMode == 3)
                pcName = "Mix";
            if (gameMode == 4)
                pcName = "MFS";
        } else {
            if (gameMode >= 5 && gameMode < 8) {
                userName = "Passive";
                if (gameMode == 5)
                    pcName = "Aggressive";
                if (gameMode == 6)
                    pcName = "Mix";
                if (gameMode == 7)
                    pcName = "MFS";
            } else if (gameMode >= 8 && gameMode < 10) {
                userName = "Aggressive";
                if (gameMode == 8)
                    pcName = "Mix";
                if (gameMode == 9)
                    pcName = "MFS";
            } else {
                userName = "Mix";
                pcName = "MFS";
            }
        }

        // Initialize statistics object
        statistics = new SetStatistics(userName);

        // Initialize gameTitle
        gameTitle = findViewById(R.id.game_title);

        // Initialize gameAction
        gameAction = findViewById(R.id.game_action);

        // Initialize opponent and player money
        pokerChipsLabel = new ArrayList<>();
        pokerChipsLabel.add((TextView) findViewById(R.id.opponent_money));
        pokerChipsLabel.add((TextView) findViewById(R.id.player_money));

        // Initialize opponent and player names
        opponentName = findViewById(R.id.opponent_name);
        opponentName.setText(pcName);
        playerName = findViewById(R.id.player_name);
        playerName.setText(userName);

        // Initialize opponent and player bets
        betLabel = new ArrayList<>();
        betLabel.add((TextView) findViewById(R.id.opponent_bet));
        betLabel.add((TextView) findViewById(R.id.player_bet));

        // Initialize opponent and player odds
        oddsLabel = new ArrayList<>();
        oddsLabel.add((TextView) findViewById(R.id.opponent_odds));
        oddsLabel.add((TextView) findViewById(R.id.player_odds));

        // Initialize pot label
        potLabel = findViewById(R.id.pot);

        // Initialize opponent and player dealer chip
        dealerLabel = new ArrayList<>();
        dealerLabel.add((ImageView) findViewById(R.id.opponent_dealer));
        dealerLabel.add((ImageView) findViewById(R.id.player_dealer));

        // Initialize opponent and player hand ranking
        ranking = new ArrayList<>();
        ranking.add("");
        ranking.add("");
        rankingLabel = new ArrayList<>();
        rankingLabel.add((TextView) findViewById(R.id.opponent_ranking));
        rankingLabel.add((TextView) findViewById(R.id.player_ranking));

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

        // Initialize game summary
        summary = findViewById(R.id.summary);

        // Animation for buttons
        buttonAnim = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.click_scale);

        // Initialize buttons
        foldButton = findViewById(R.id.fold);
        callButton = findViewById(R.id.call);
        betButton = findViewById(R.id.bet);

        // Initialize seek bar
        betSeekBar = findViewById(R.id.seekBar);

        // Initialize textview for the poker chips value
        betText = findViewById(R.id.poker_chips);

        // Fold button onClick
        foldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideButtons();
                fold(1);
            }
        });

        // Call button onClick
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideButtons();

                if (!checkButton) {
                    Runnable r = new Runnable() {
                        public void run() {
                            call(1);
                        }
                    };
                    view.postDelayed(r, 0);
                } else {
                    Runnable r = new Runnable() {
                        public void run() {
                            check(1);
                        }
                    };
                    view.postDelayed(r, 0);
                }
            }
        });

        // Bet button onCLick
        betButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideButtons();
                Runnable r = new Runnable() {
                    public void run() {
                        bet(1);
                    }
                };
                view.postDelayed(r, 0);
            }
        });

        // perform seek bar change listener event used for getting the progress value
        betSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                //set min value to betSeekBar
                if (betSeekBar.getProgress() < bigBlind)
                    betSeekBar.setProgress(bigBlind);
                // Min value to raise is 2 * bigblind
                if (betSeekBar.getProgress() > bigBlind && betSeekBar.getProgress() < (2 * bigBlind))
                    betSeekBar.setProgress(bigBlind * 2);

                // Set Max value
                betSeekBar.setMax(Math.round(pokerChips[1]));

                betValue = seekBar.getProgress();
                // Set the amount of poker chips with the seek bar
                betText.setText(betValue + "€");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Start creating layouts
        initializeLayouts();
    }

    // Method to initialize Layout's
    public void initializeLayouts() {
        // Initialize game title and Action
        gameTitle.setText(userName + " VS " + pcName);
        gameAction.setText("Loading...");

        // Initialize opponent and player money
        pokerChips[0] = 1500;
        pokerChips[1] = 1500;
        pokerChipsLabel.get(0).setText(pokerChips[0] + " €");
        pokerChipsLabel.get(1).setText(pokerChips[1] + " €");

        // Initialize bet text
        betText.setText(bigBlind + " €");

        // Initialize opponent and player bets
        bet[0] = 0;
        bet[1] = 0;
        betLabel.get(0).setText(bet[0] + " €");
        betLabel.get(1).setText(bet[1] + " €");

        // Initialize pot money
        pokerChips[pot] = 0;
        potLabel.setText(pokerChips[pot] + " €");

        // Choose dealer with random
        Random dealerRandom = new Random();
        dealer = dealerRandom.nextInt((1 - 0) + 1) + 0; // random number between 0 and 1

        // Show dealer
        dealerLabel.get(dealer).setVisibility(View.VISIBLE);
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {

                // Go to pre flop
                preFlop();

            }
        }.start();


    }

    // Method to initialize Pre flop
    public void preFlop() {

        // Get a deck
        Deck deck = new Deck();
        // Get a dealer
        Dealer dealer = new Dealer();

        float[] tempOdds = new float[3];

        // Restart tempPot
        tempPot = 0;

        // Check possibility is true
        checkFlag = true;

        // Initialize cards
        opponentCards = new ArrayList<>();
        playerCards = new ArrayList<>();
        tableCards = new ArrayList<>();
        tempTableCards = new ArrayList<>();

        // Players get cards
        dealer.giveCards(deck, opponentCards, playerCards);

        // Save player cards
        statistics.saveMostPlayedCards(playerCards);

        // Get Flop
        dealer.giveFlop(deck, tableCards);

        // Calculate odds in the flop
        tempTableCards.addAll(tableCards);
        tempOdds = oddsObj.oddsFlop(opponentCards, tempTableCards);
        oddsFlopOpponent = tempOdds[1];
        tempTableCards.clear();
        if (gameMode > 4) {
            // Get odds
            tempTableCards.addAll(tableCards);
            tempOdds = oddsObj.oddsFlop(playerCards, tempTableCards);
            oddsFlopPlayer = tempOdds[1];
            tempTableCards.clear();
        }

        // Get turn
        dealer.giveOneCard(deck, tableCards);

        // Calculate odds in the turn
        tempTableCards.addAll(tableCards);
        tempOdds = oddsObj.oddsTurn(opponentCards, tempTableCards);
        oddsTurnOpponent = tempOdds[1];
        tempTableCards.clear();
        if (gameMode > 4) {
            // Get odds
            tempTableCards.addAll(tableCards);
            tempOdds = oddsObj.oddsTurn(playerCards, tempTableCards);
            oddsTurnPlayer = tempOdds[1];
            tempTableCards.clear();
        }

        // Get River
        dealer.giveOneCard(deck, tableCards);

        // Calculate odds in the turn
        tempTableCards.addAll(tableCards);
        tempOdds = oddsObj.oddsRiver(opponentCards, tempTableCards);
        oddsRiverOpponent = tempOdds[1];
        tempTableCards.clear();
        if (gameMode > 4) {
            // Get odds
            tempTableCards.addAll(tableCards);
            tempOdds = oddsObj.oddsRiver(playerCards, tempTableCards);
            oddsRiverPlayer = tempOdds[1];
            tempTableCards.clear();
        }

        Log.v("Flop opo", oddsFlopOpponent + "");
        Log.v("Flop pl", oddsFlopPlayer + "");
        Log.v("Turn opo", oddsTurnOpponent + "");
        Log.v("Turn pl", oddsTurnPlayer + "");
        Log.v("River opo", oddsRiverOpponent + "");
        Log.v("River pl", oddsRiverPlayer + "");

        // Show player cards
        int playerCard1Id = getResources().getIdentifier(
                Cards.getSrcCard(playerCards.get(0)),
                "drawable",
                getPackageName()
        );
        playerCardsImg.get(0).setImageResource(playerCard1Id);
        int playerCard2Id = getResources().getIdentifier(
                Cards.getSrcCard(playerCards.get(1)),
                "drawable",
                getPackageName()
        );
        playerCardsImg.get(1).setImageResource(playerCard2Id);

        // If game mode is computer vs computer show also opponent cards
        if (gameMode > 4) {
            // Show opponent cards
            int opponentCard1Id = getResources().getIdentifier(
                    Cards.getSrcCard(opponentCards.get(0)),
                    "drawable",
                    getPackageName()
            );
            opponentCardsImg.get(0).setImageResource(opponentCard1Id);

            int opponentCard2Id = getResources().getIdentifier(
                    Cards.getSrcCard(opponentCards.get(1)),
                    "drawable",
                    getPackageName()
            );
            opponentCardsImg.get(1).setImageResource(opponentCard2Id);
        } else {
            opponentCardsImg.get(0).setImageResource(R.drawable.back);
            opponentCardsImg.get(1).setImageResource(R.drawable.back);
        }

        // Hide action
        gameAction.setText("");

        // hide ranking
        rankingLabel.get(0).setVisibility(View.INVISIBLE);
        rankingLabel.get(1).setVisibility(View.INVISIBLE);

        // Pre flop round
        round = 0;

        // Variable to check if game keeps going
        finish = false;

        // Get opponent odds
        odds[0] = (float) oddsObj.preFlopOdds(opponentCards);
        // If game mode is computer vs computer calculate also player odds and show both odds
        if (gameMode > 4) {
            // Get player odds
            odds[1] = (float) oddsObj.preFlopOdds(playerCards);
            oddsLabel.get(0).setText(odds[0] + " %");
            oddsLabel.get(1).setText(odds[1] + " %");
        }

        // Initialize opponent and player bets
        bet[0] = 0;
        bet[1] = 0;
        betLabel.get(0).setText(bet[0] + " €");
        betLabel.get(1).setText(bet[1] + " €");

        // Initialize pot money
        pokerChips[pot] = 0;
        potLabel.setText(pokerChips[pot] + " €");

        // Increase number of games and show in summary
        nGames++;
        txt += "\n------Game number: " + nGames + "-------\n\n";
        summary.setText(txt);

        if (gameMode > 4) {
            new CountDownTimer(1000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {

                    // Go to bets before the flop
                    betsPreFlop();

                }
            }.start();
        } else
            // Go to bets before the flop
            betsPreFlop();
    }

    // Method to initialize Flop
    public void flop() {
        // flop round
        round = 1;

        // It's possible to check again
        checkFlag = true;

        int player = (dealer == 0) ? 1 : 0;
        reset();
        // If its computer turn
        if (player == 0 || gameMode > 4)
            check_bet(player);
        else {
            checkButton = true;
            callButton.setText("Check");
            callButton.setVisibility(View.VISIBLE);
            betButton.setVisibility(View.VISIBLE);
            betSeekBar.setVisibility(View.VISIBLE);
            betText.setVisibility(View.VISIBLE);
        }
    }

    // Method to initialize Turn
    public void turn() {

        // turn round
        round = 2;

        // It's possible to check again
        checkFlag = true;

        int player = (dealer == 0) ? 1 : 0;

        reset();
        // If its computer turn
        if (player == 0 || gameMode > 4)
            check_bet(player);
        else {
            checkButton = true;
            callButton.setText("Check");
            callButton.setVisibility(View.VISIBLE);
            betButton.setVisibility(View.VISIBLE);
            betSeekBar.setVisibility(View.VISIBLE);
            betText.setVisibility(View.VISIBLE);
        }

    }

    // Method to initialize River
    public void river() {

        // river round
        round = 3;

        // It's possible to check again
        checkFlag = true;

        int player = (dealer == 0) ? 1 : 0;

        reset();
        // If its computer turn
        if (player == 0 || gameMode > 4)
            check_bet(player);
        else {
            checkButton = true;
            callButton.setText("Check");
            callButton.setVisibility(View.VISIBLE);
            betButton.setVisibility(View.VISIBLE);
            betSeekBar.setVisibility(View.VISIBLE);
            betText.setVisibility(View.VISIBLE);
        }
    }

    // Method to make bets in pre flop
    public void betsPreFlop() {

        // Check if is first that goes to pre flop
        // If is not dealer changes position
        if (!firstPlay)
            dealer = (dealer == 0) ? 1 : 0;
        firstPlay = false;

        // Initialize blind
        final int blind = (dealer == 0) ? 1 : 0;

        // Change dealer
        dealerLabel.get(dealer).setVisibility(View.VISIBLE);
        dealerLabel.get(blind).setVisibility(View.INVISIBLE);

        // Initialize values
        reset();

        // If blind doesn't have enough money makes all in
        if (pokerChips[blind] <= bigBlind) {
            // If blind has less money than small blind makes all in
            if (pokerChips[blind] <= smallBlind) {

                // Blind has to bet all the money
                bet[blind] = pokerChips[blind];
                pokerChips[blind] -= bet[blind];

                // Dealer just pay blind all in
                bet[dealer] = bet[blind];
                pokerChips[dealer] -= bet[dealer];

                // Calculate pot
                pokerChips[pot] = 2 * bet[blind];

                // Update labels
                updateLabels();

                if (blind == 0)
                    gameAction.setText(pcName + " bets " + bet[blind] + " €");
                else
                    gameAction.setText(userName + " bets " + bet[blind] + " €");

                // Update bets
                betLabel.get(0).setText(bet[0] + " €");
                txt += pcName + " bets " + bet[0] + " €\n";
                betLabel.get(1).setText(bet[1] + " €");
                txt += userName + " bets" + bet[1] + " €\n";

                // Update summary
                summary.setText(txt);

                new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        // Show all cards and calculate winner
                        fiveCardsShowDown();
                    }
                }.start();


            } else {
                // Dealer pays small blind
                bet[dealer] = smallBlind;
                pokerChips[dealer] -= bet[dealer];

                // Update bets
                betLabel.get(0).setText(bet[0] + " €");
                txt += pcName + " bets " + bet[0] + " €\n";
                betLabel.get(1).setText(bet[1] + " €");
                txt += userName + " bets" + bet[1] + " €\n";
                // Set pot
                pokerChips[pot] = bet[dealer] + bet[blind];

                //Update action
                if (blind == 0) {
                    betLabel.get(1).setText(bet[1] + " €");
                    txt += userName + " pays small blind (20€)\n";
                } else {
                    betLabel.get(0).setText(bet[0] + " €");
                    txt += pcName + " pays small blind (20€)\n";
                }

                summary.setText(txt);

                new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        // Blind has to do all in
                        allIn(blind);
                    }
                }.start();


            }
        }
        // If dealer has less than small blind makes all in
        else if (pokerChips[dealer] <= smallBlind) {
            // All in
            bet[dealer] = pokerChips[dealer];
            pokerChips[dealer] -= bet[dealer];

            // Blind pays all in
            bet[blind] = bet[dealer];
            pokerChips[blind] -= bet[blind];

            // Set pot
            pokerChips[pot] = bet[blind] + bet[dealer];

            // Update labels
            updateLabels();

            if (blind == 0)
                gameAction.setText(pcName + " makes All in with " + bet[blind] + " €");
            else
                gameAction.setText(userName + " makes All in with " + bet[blind] + " €");

            // Update bets
            betLabel.get(0).setText(bet[0] + " €");
            txt += pcName + " bets" + bet[0] + " €\n";
            betLabel.get(1).setText(bet[1] + " €");
            txt += userName + " bets" + bet[1] + " €\n";

            // Update summary
            summary.setText(txt);

            new CountDownTimer(1000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    // Show all cards and calculate winner
                    fiveCardsShowDown();
                }
            }.start();

        }
        // If both players have enough money
        else if (pokerChips[blind] > bigBlind && pokerChips[dealer] > smallBlind) {
            // Dealer pays small blind
            bet[dealer] = smallBlind;
            bet[blind] = bigBlind;

            // Blind pays big blind
            pokerChips[dealer] -= bet[dealer];
            pokerChips[blind] -= bet[blind];

            // Set pot
            pokerChips[pot] = bet[dealer] + bet[blind];

            // Update labels
            updateLabels();

            //Update action
            if (blind == 0) {
                betLabel.get(1).setText(bet[1] + " €");
                txt += pcName + " pays big blind (" + bet[blind] + "€)\n";
                txt += userName + " pays small blind (" + bet[dealer] + "€)\n";

            } else {
                betLabel.get(0).setText(bet[0] + " €");
                txt += pcName + " pays small  blind (" + bet[dealer] + "€)\n";
                txt += userName + " pays big blind (" + bet[blind] + "€)\n";
            }

            if (blind == 0)
                gameAction.setText(pcName + " paid " + bet[blind] + " €");
            else
                gameAction.setText(userName + " paid " + bet[blind] + " €");

            // Update summary
            summary.setText(txt);

            // Update bets
            betLabel.get(0).setText(bet[0] + " €");
            betLabel.get(1).setText(bet[1] + " €");

            if (gameMode > 4 || dealer == 0) {
                new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        // Dealer choose his bet option
                        fold_call_bet(dealer);
                    }
                }.start();
            } else {
                foldButton.setVisibility(View.VISIBLE);
                callButton.setVisibility(View.VISIBLE);
                callButton.setText("Call");
                checkButton = false;
                betButton.setVisibility(View.VISIBLE);
                betSeekBar.setVisibility(View.VISIBLE);
                betSeekBar.isEnabled();
                betText.setVisibility(View.VISIBLE);
            }
        }
    }

    // Method to bet all the poker chips
    public void allIn(int player) {

        final int opponent = (player == 0) ? 1 : 0;

        float tempbet = bet[player];

        // Bet all the chips
        bet[player] = pokerChips[player] + tempbet;

        //Update action
        if (player == 0) {
            gameAction.setText(pcName + " makes All in with " + pokerChips[player] + " €");
            betLabel.get(0).setText(bet[0] + " €");
            txt += pcName + " makes allin with " + bet[0] + " €\n";
        } else {
            gameAction.setText(userName + " makes All in with " + bet[player] + " €");
            betLabel.get(1).setText(bet[1] + " €");
            txt += userName + " makes allin with " + bet[1] + " €\n";
        }

        pokerChips[player] -= (bet[player] - tempbet);
        pokerChips[pot] = (bet[player] + bet[opponent]);

        // Update summary
        summary.setText(txt);

        // Update Labels
        updateLabels();

        // Game its finish
        finish = true;

        // Check possibility is false
        checkFlag = false;

        if (pokerChips[opponent] > 0) {
            // If is computer vs computer goes directly to fold_call menu
            if (gameMode > 4 || player == 1) {
                new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        fold_allin(opponent);
                    }
                }.start();
            } else {
                // Show fold and call buttons
                foldButton.setVisibility(View.VISIBLE);
                callButton.setText("Call");
                checkButton = false;
                callButton.setVisibility(View.VISIBLE);
            }
        } else {
            if (round == 0)
                fiveCardsShowDown();
            else if (round == 1)
                twoCardsShowDown();
            else if (round == 2)
                oneCardShowDown();
            else if (round == 3)
                showDown();
        }
    }

    // Method when exists a bet
    public void bet(int player) {

        final int opponent = (player == 0) ? 1 : 0;

        // Check possibility is false
        checkFlag = false;

        // If its computer turn or is computer vs computer
        if (player == 0 || gameMode > 4) {
            // Check if its all in
            if (betOption[1] >= pokerChips[player])
                allIn(player);
            else {
                // tempBet stores old bet
                float tempBet = bet[player];

                bet[player] = betOption[1] + bet[opponent];

                if (tempBet + bet[player] >= pokerChips[player]) {
                    bet[player] = tempBet;
                    allIn(player);
                } else {
                    // Update poker chips
                    pokerChips[player] -= bet[player];

                    //Update action
                    if (player == 0) {
                        gameAction.setText(pcName + " bets " + bet[player] + " €");
                        txt += pcName + " bets " + bet[player] + " €\n";
                    } else {
                        gameAction.setText(userName + " bets " + bet[player] + " €");
                        txt += userName + " bets " + bet[player] + " €\n";
                    }

                    // Sum old bet and new bet
                    bet[player] += tempBet;

                    // Update pot
                    pokerChips[pot] = bet[player] + bet[opponent];

                    updateLabels();

                    // Update bets
                    betLabel.get(0).setText(bet[0] + " €");
                    betLabel.get(1).setText(bet[1] + " €");

                    // Update summary
                    summary.setText(txt);

                    if (gameMode > 4 || player == 1) {
                        // If opponent doesn't have enough money to pay the bet
                        if (pokerChips[opponent] + bet[opponent] <= bet[player]) {
                            new CountDownTimer(1000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {
                                    fold_allin(opponent);
                                }
                            }.start();
                        } else {
                            new CountDownTimer(1000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {
                                    fold_call_bet(opponent);
                                }
                            }.start();
                        }
                    } else {
                        if (pokerChips[opponent] + bet[opponent] <= bet[player]) {
                            // Show fold and call button
                            foldButton.setVisibility(View.VISIBLE);
                            callButton.setVisibility(View.VISIBLE);
                            callButton.setText("Call");
                            checkButton = false;
                            finish = true;

                        } else {
                            // Show fold, call and bet
                            foldButton.setVisibility(View.VISIBLE);
                            callButton.setText("Call");
                            checkButton = false;
                            callButton.setVisibility(View.VISIBLE);
                            betButton.setVisibility(View.VISIBLE);
                            betSeekBar.setVisibility(View.VISIBLE);
                            betText.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        } else {
            // tempBet stores old bet
            float tempBet = bet[player];

            bet[player] = betSeekBar.getProgress() + bet[opponent];

            if (tempBet + bet[player] >= pokerChips[player]) {
                bet[player] = tempBet;
                allIn(player);
            } else {
                if (bet[player] == 0)
                    bet[player] = 40;

                // Update poker chips
                pokerChips[player] -= bet[player];

                gameAction.setText(userName + " bets " + bet[player] + " €");
                txt += userName + " bets " + bet[player] + " €\n";

                // Sum old bet and new bet
                bet[player] += tempBet;

                // Update pot
                pokerChips[pot] = bet[player] + bet[opponent];

                updateLabels();

                // Update bets
                betLabel.get(0).setText(bet[0] + " €");
                betLabel.get(1).setText(bet[1] + " €");

                // Update summary
                summary.setText(txt);

                // If opponent doesn't have enough money to pay the bet
                if (pokerChips[opponent] + bet[opponent] <= bet[player])
                    fold_allin(opponent);
                else
                    fold_call_bet(opponent);
            }
        }
    }

    // Method when the player equals opponent bet
    public void call(int player) {

        final int opponent = (player == 0) ? 1 : 0;

        // Stores old bet
        float tempBet;

        // If call is bigger then player money makes all in
        if ((bet[opponent] - bet[player]) >= pokerChips[player]) {
            tempBet = bet[player];
            bet[player] = pokerChips[player];

            //Update action
            if (player == 0) {
                gameAction.setText(pcName + " makes All in with " + bet[player] + " €");
                txt += pcName + " makes All in with " + bet[player] + " €\n";
            } else {
                gameAction.setText(userName + " makes All in with " + bet[player] + " €");
                txt += userName + " makes All in with " + bet[player] + " €\n";
            }

            pokerChips[player] -= bet[player];
            bet[player] += tempBet;

            // Opponent gets difference
            pokerChips[opponent] = (pokerChips[opponent] + bet[opponent]) - bet[player];
            pokerChips[pot] = bet[player] * 2;
        } else {
            // Stores old bet
            tempBet = bet[player];

            // Bet is the difference of bets
            bet[player] = bet[opponent] - bet[player];

            // Update poker chips
            pokerChips[player] -= bet[player];

            //Update action
            if (player == 0) {
                txt += pcName + " makes call with " + bet[player] + " €\n";
                gameAction.setText(pcName + " makes call with " + bet[player] + " €");
            } else {
                txt += userName + " makes call with " + bet[player] + " €\n";
                gameAction.setText(userName + " makes call with " + bet[player] + " €");
            }

            // Sum of bets
            bet[player] += tempBet;

            // Update pot
            pokerChips[pot] = bet[player] + bet[opponent];
        }

        updateLabels();

        // Update bets
        betLabel.get(0).setText(bet[0] + " €");
        betLabel.get(1).setText(bet[1] + " €");

        // Update summary
        summary.setText(txt);

        // If one of the players doesn't have enough money
        if (pokerChips[0] == 0 || pokerChips[1] == 0) {
            if (round == 0)
                fiveCardsShowDown();
            else if (round == 1)
                twoCardsShowDown();
            else if (round == 2)
                oneCardShowDown();
            else if (round == 3)
                showDown();
        }

        // If is initial call the other player has to choose an option
        if (checkFlag && round == 0) {

            // If its computer turn or is computer vs computer
            if (player == 1 || gameMode > 4) {
                new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        check_bet(opponent);
                    }
                }.start();


            } else {
                checkButton = true;
                callButton.setText("Check");
                callButton.setVisibility(View.VISIBLE);
                betButton.setVisibility(View.VISIBLE);
                betSeekBar.setVisibility(View.VISIBLE);
                betText.setVisibility(View.VISIBLE);
            }
        } else if (!finish) {

            if (round == 0)
                showFlop();
            if (round == 1)
                showTurn();
            if (round == 2)
                showRiver();
            if (round == 3)
                showDown();
        }
    }

    // Method to make check
    public void check(int player) {

        final int opponent = (player == 0) ? 1 : 0;

        //Update action
        if (player == 0) {
            gameAction.setText(pcName + " makes check");
            txt += pcName + " makes check\n";
        } else {
            gameAction.setText(userName + " makes check");
            txt += userName + " makes check\n";
        }

        // Update summary
        summary.setText(txt);

        // If it was the first check the opponent has to talk
        if (checkFlag && round > 0) {
            // It's not possible to check again
            checkFlag = false;

            // if its computer
            if (player == 1 || gameMode > 4) {
                new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        check_bet(opponent);
                    }
                }.start();
            } else {
                callButton.setText("Check");
                checkButton = true;
                callButton.setVisibility(View.VISIBLE);
                betButton.setVisibility(View.VISIBLE);
                betSeekBar.setVisibility(View.VISIBLE);
                betText.setVisibility(View.VISIBLE);
            }
        } else {
            if (round == 0)
                showFlop();
            if (round == 1)
                showTurn();
            if (round == 2)
                showRiver();
            if (round == 3)
                showDown();
        }
    }

    // Method when a player makes fold
    public void fold(int player) {

        //Update action
        if (player == 0)
            gameAction.setText(pcName + " makes fold");
        else
            gameAction.setText(userName + " makes fold");

        int opponent = (player == 0) ? 1 : 0;

        // Opponent wins the pot
        pokerChips[opponent] += pokerChips[pot] + tempPot;

        txt += gameAction.getText() + "\n";

        //Update summary
        if (player == 0) {
            txt += userName + " wins " + (pokerChips[pot] + tempPot) + "\n";
            statistics.saveMostWinningCards(playerCards);
            nWinningGames++;

        } else
            txt += pcName + " wins " + (pokerChips[pot] + tempPot) + "\n";

        pokerChips[pot] = 0;
        finish = true;

        //Update labels
        updateLabels();

        // Update bets
        betLabel.get(0).setText(bet[0] + "€");
        betLabel.get(1).setText(bet[1] + "€");

        // Update summary
        summary.setText(txt);

        new CountDownTimer(2000, 2000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                finalMethod();

            }
        }.start();
    }

    // Method to get option check or bet with computer
    public void check_bet(int player) {
        betOption = gameMode(player, gameMode, 4);


        if (betOption[0] == 2)
            betOption[1] = bigBlind;

        switch (betOption[0]) {
            case 1:
                check(player);
                break;
            case 2:
                bet(player);
                break;
            case 3:
                bet(player);
                break;
            case 4:
                allIn(player);
                break;
        }
    }

    // Method to get option in fold call or bet with computer
    public void fold_call_bet(int player) {

        betOption = gameMode(player, gameMode, 5);

        if (betOption[0] == 3)
            betOption[1] = bigBlind;

        summary.setText(txt);
        switch (betOption[0]) {
            case 1:
                fold(player);
                break;
            case 2:
                call(player);
                break;
            case 3:
                bet(player);
                break;
            case 4:
                bet(player);
                break;
            case 5:
                allIn(player);
                break;
        }
    }

    // Method to get option in fold call or bet with computer
    public void fold_allin(int player) {

        betOption = gameMode(player, gameMode, 2);

        switch (betOption[0]) {
            case 1:
                fold(player);
                break;
            case 2:
                call(player);
                break;
        }
    }

    // Method to update labels
    public void updateLabels() {
        // Update opponent and player poker chips
        pokerChipsLabel.get(0).setText(pokerChips[0] + " €");
        pokerChipsLabel.get(1).setText(pokerChips[1] + " €");

        // Update pot poker chips
        potLabel.setText((pokerChips[pot] + tempPot) + " €");
    }

    // Method to show flop cards
    public void showFlop() {
        txt += "------- Flop -----\n";

        // tempPote stores pot
        tempPot += pokerChips[pot];

        // temp table cards gets flop cards
        tempTableCards.clear();
        tempTableCards.addAll(tableCards.subList(0, 3));

        txt += tempTableCards + "\n";
        summary.setText(txt);

        // Opponent get odds
        odds[0] = oddsFlopOpponent;

        if (gameMode > 4) {
            // Player get's odds
            odds[1] = oddsFlopPlayer;

            // Show odds
            oddsLabel.get(0).setText(odds[0] + "%");
            oddsLabel.get(1).setText(odds[1] + "%");

        }

        // Reset bets
        bet[0] = 0;
        bet[1] = 0;

        betLabel.get(0).setText(bet[0] + "€");
        betLabel.get(1).setText(bet[1] + "€");

        // Loop through 3 cards and wait 1 second in each card
        for (int i = 0; i < 4; i++) {
            // Work around to access variable inside CountDowTimer
            final int j = i;

            new CountDownTimer((i + 2) * 500, (i + 1) * 500) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {

                    if (j != 3) {
                        // Show flop card
                        int tableCard1Id = getResources().getIdentifier(
                                Cards.getSrcCard(tableCards.get(j)),
                                "drawable",
                                getPackageName()
                        );
                        tableCardsImg.get(j).setImageResource(tableCard1Id);
                    }
                    if (j == 3)
                        flop();
                }
            }.start();
        }
    }

    // Method to show turn card
    public void showTurn() {

        txt += "------- Turn -----\n";
        // tempPote stores pot
        tempPot += pokerChips[pot];

        // temp table cards gets flop cards
        tempTableCards.clear();
        tempTableCards.addAll(tableCards.subList(0, 4));

        txt += tempTableCards + "\n";
        summary.setText(txt);

        // Opponent get odds
        odds[0] = oddsTurnOpponent;

        if (gameMode > 4) {
            // Opponent get odds
            odds[1] = oddsTurnPlayer;

            // Show odds
            oddsLabel.get(0).setText(odds[0] + "%");
            oddsLabel.get(1).setText(odds[1] + "%");

        }

        // Reset bets
        bet[0] = 0;
        bet[1] = 0;

        betLabel.get(0).setText(bet[0] + "€");
        betLabel.get(1).setText(bet[1] + "€");

        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                // Show Turn card
                int tableCard1Id = getResources().getIdentifier(
                        Cards.getSrcCard(tableCards.get(3)),
                        "drawable",
                        getPackageName()
                );
                tableCardsImg.get(3).setImageResource(tableCard1Id);

                turn();

            }
        }.start();

    }

    // Method to show river card
    public void showRiver() {

        txt += "------- River -----\n";
        txt += tableCards + "\n";
        summary.setText(txt);

        // tempPote stores pot
        tempPot += pokerChips[pot];

        // Opponent get odds
        odds[0] = oddsRiverOpponent;

        if (gameMode > 4) {
            // Opponent get odds
            odds[1] = oddsRiverPlayer;

            // Show odds
            oddsLabel.get(0).setText(odds[0] + "%");
            oddsLabel.get(1).setText(odds[1] + "%");
        }

        // Reset bets
        bet[0] = 0;
        bet[1] = 0;

        betLabel.get(0).setText(bet[0] + "€");
        betLabel.get(1).setText(bet[1] + "€");

        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                // Show flop card
                int tableCard1Id = getResources().getIdentifier(
                        Cards.getSrcCard(tableCards.get(4)),
                        "drawable",
                        getPackageName()
                );
                tableCardsImg.get(4).setImageResource(tableCard1Id);

                river();

            }
        }.start();
    }

    // Method to the final show down
    public void showDown() {

        pokerChips[pot] += tempPot;

        // Object to evaluate hands
        Evaluate evaluate = new Evaluate();

        // Hand rankings will be visible
        rankingLabel.get(0).setVisibility(View.VISIBLE);
        rankingLabel.get(1).setVisibility(View.VISIBLE);
        tempTableCards.clear();

        tempTableCards.clear();
        // Temp table cards gets flop
        tempTableCards.addAll(tableCards);

        // get ranking
        // Opponent
        resultHand[0] = evaluate.evaluateHand(opponentCards, tempTableCards);
        opponentHand.addAll(evaluate.getHand());
        ranking.set(0, evaluate.getResult() + "");
        evaluate.reset();
        // Player
        resultHand[1] = evaluate.evaluateHand(playerCards, tempTableCards);
        playerHand.addAll(evaluate.getHand());
        statistics.saveBiggestHandCards(playerHand);
        ranking.set(1, evaluate.getResult() + "");
        evaluate.reset();

        // Show final ranking
        rankingLabel.get(0).setText(ranking.get(0));
        rankingLabel.get(1).setText(ranking.get(1));

        // Show opponent cards
        int opponentCard1Id = getResources().getIdentifier(
                Cards.getSrcCard(opponentCards.get(0)),
                "drawable",
                getPackageName()
        );
        opponentCardsImg.get(0).setImageResource(opponentCard1Id);

        int opponentCard2Id = getResources().getIdentifier(
                Cards.getSrcCard(opponentCards.get(1)),
                "drawable",
                getPackageName()
        );
        opponentCardsImg.get(1).setImageResource(opponentCard2Id);

        // Hide all the buttons
        foldButton.setVisibility(View.INVISIBLE);
        callButton.setVisibility(View.INVISIBLE);
        betButton.setVisibility(View.INVISIBLE);

        // Calculate winner
        calculateWinner();

    }

    // Method to show down 1 card
    public void oneCardShowDown() {

        pokerChips[pot] += tempPot;

        // Array to store odds in turn
        float[] turnOdds = new float[3];

        // String to store ranking in turn and river
        final String[] turnRanking = new String[2];
        final String[] riverRanking = new String[2];

        // Object to evaluate hands
        Evaluate evaluate = new Evaluate();

        // Hand rankings will be visible
        rankingLabel.get(0).setVisibility(View.VISIBLE);
        rankingLabel.get(1).setVisibility(View.VISIBLE);

        tempTableCards.clear();

        // Temp table cards gets flop and river
        tempTableCards.addAll(tableCards.subList(0, 4));

        // Get odds in turn
        turnOdds = oddsObj.oddsPlayerVSPlayerTurn(opponentCards, playerCards, tempTableCards);
        tempTableCards.remove(4);
        final float[] turnO = new float[2];
        turnO[0] = turnOdds[1];
        turnO[1] = turnOdds[2];

        // Get ranking in turn
        // Opponent
        evaluate.evaluateHand(opponentCards, tempTableCards);
        turnRanking[0] = evaluate.getResult();
        evaluate.reset();
        // Player
        evaluate.evaluateHand(playerCards, tempTableCards);
        turnRanking[1] = evaluate.getResult();
        evaluate.reset();

        // Get ranking in river
        // Opponent
        resultHand[0] = evaluate.evaluateHand(opponentCards, tableCards);
        opponentHand.addAll(evaluate.getHand());            // Get final hand
        riverRanking[0] = evaluate.getResult();
        evaluate.reset();
        // Player
        resultHand[1] = evaluate.evaluateHand(playerCards, tableCards);
        playerHand.addAll(evaluate.getHand());            // Get final hand
        statistics.saveBiggestHandCards(playerHand);
        riverRanking[1] = evaluate.getResult();
        evaluate.reset();

        ranking.set(0, riverRanking[0] + "");
        ranking.set(1, riverRanking[1] + "");

        // Show opponent cards
        int opponentCard1Id = getResources().getIdentifier(
                Cards.getSrcCard(opponentCards.get(0)),
                "drawable",
                getPackageName()
        );
        opponentCardsImg.get(0).setImageResource(opponentCard1Id);

        int opponentCard2Id = getResources().getIdentifier(
                Cards.getSrcCard(opponentCards.get(1)),
                "drawable",
                getPackageName()
        );
        opponentCardsImg.get(1).setImageResource(opponentCard2Id);

        // Hide all the buttons
        foldButton.setVisibility(View.INVISIBLE);
        callButton.setVisibility(View.INVISIBLE);
        betButton.setVisibility(View.INVISIBLE);

        // Show odds
        oddsLabel.get(0).setText(turnO[0] + " %");
        oddsLabel.get(1).setText(turnO[1] + " %");

        // Show rankings
        rankingLabel.get(0).setText(turnRanking[0]);
        rankingLabel.get(1).setText(turnRanking[1]);


        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                // Show River card
                int tableCard1Id = getResources().getIdentifier(
                        Cards.getSrcCard(tableCards.get(4)),
                        "drawable",
                        getPackageName()
                );
                tableCardsImg.get(4).setImageResource(tableCard1Id);

                tempTableCards.add(tableCards.get(4));

                // Hide bets
                betLabel.get(0).setText("");
                betLabel.get(1).setText("");

                // Hide odds
                oddsLabel.get(0).setText("");
                oddsLabel.get(1).setText("");

                // Show final ranking
                rankingLabel.get(0).setText(riverRanking[0]);
                rankingLabel.get(1).setText(riverRanking[1]);

                // Show summary
                summary.setText(txt);

                // Calculate winner
                calculateWinner();
            }
        }.start();
    }

    // Method to show down 2 cards
    public void twoCardsShowDown() {
        pokerChips[pot] += tempPot;

        // Array to store odds and flop and turn
        float[] flopOdds = new float[3];
        float[] turnOdds = new float[3];

        // String to store ranking in flop, turn and river
        final String[] flopRanking = new String[2];
        final String[] turnRanking = new String[2];
        final String[] riverRanking = new String[2];

        // Object to evaluate hands
        Evaluate evaluate = new Evaluate();

        // Hand rankings will be visible
        rankingLabel.get(0).setVisibility(View.VISIBLE);
        rankingLabel.get(1).setVisibility(View.VISIBLE);
        tempTableCards.clear();
        // Temp table cards gets flop
        tempTableCards.addAll(tableCards.subList(0, 3));

        // Get odds in flop
        flopOdds = oddsObj.oddsPlayerVSPlayerFlop(opponentCards, playerCards, tempTableCards);
        tempTableCards.remove(3);
        tempTableCards.remove(3);
        final float[] flopO = new float[2];
        flopO[0] = flopOdds[1];
        flopO[1] = flopOdds[2];

        // get ranking in flop
        // Opponent
        evaluate.evaluateHand(opponentCards, tempTableCards);
        flopRanking[0] = evaluate.getResult();
        evaluate.reset();
        // Player
        evaluate.evaluateHand(playerCards, tempTableCards);
        flopRanking[1] = evaluate.getResult();
        evaluate.reset();

        // Temp table cards get turn
        tempTableCards.add(tableCards.get(3));

        // Get odds in turn
        turnOdds = oddsObj.oddsPlayerVSPlayerTurn(opponentCards, playerCards, tempTableCards);
        tempTableCards.remove(4);
        final float[] turnO = new float[2];
        turnO[0] = turnOdds[1];
        turnO[1] = turnOdds[2];

        // Get ranking in turn
        // Opponent
        evaluate.evaluateHand(opponentCards, tempTableCards);
        turnRanking[0] = evaluate.getResult();
        evaluate.reset();
        // Player
        evaluate.evaluateHand(playerCards, tempTableCards);
        turnRanking[1] = evaluate.getResult();
        evaluate.reset();

        // Get ranking in river
        // Opponent
        resultHand[0] = evaluate.evaluateHand(opponentCards, tableCards);
        opponentHand.addAll(evaluate.getHand());            // Get final hand
        riverRanking[0] = evaluate.getResult();
        evaluate.reset();
        // Player
        resultHand[1] = evaluate.evaluateHand(playerCards, tableCards);
        playerHand.addAll(evaluate.getHand());            // Get final hand
        statistics.saveBiggestHandCards(playerHand);
        riverRanking[1] = evaluate.getResult();
        evaluate.reset();

        ranking.set(0, riverRanking[0] + "");
        ranking.set(1, riverRanking[1] + "");

        // Show opponent cards
        int opponentCard1Id = getResources().getIdentifier(
                Cards.getSrcCard(opponentCards.get(0)),
                "drawable",
                getPackageName()
        );
        opponentCardsImg.get(0).setImageResource(opponentCard1Id);

        int opponentCard2Id = getResources().getIdentifier(
                Cards.getSrcCard(opponentCards.get(1)),
                "drawable",
                getPackageName()
        );
        opponentCardsImg.get(1).setImageResource(opponentCard2Id);

        // Hide all the buttons
        foldButton.setVisibility(View.INVISIBLE);
        callButton.setVisibility(View.INVISIBLE);
        betButton.setVisibility(View.INVISIBLE);

        // Show flop odds
        oddsLabel.get(0).setText(flopO[0] + " %");
        oddsLabel.get(1).setText(flopO[1] + " %");

        // Show flop rankings
        rankingLabel.get(0).setText(flopRanking[0]);
        rankingLabel.get(1).setText(flopRanking[1]);

        // Loop through 5 cards and wait 1 second in each card
        for (int i = 3; i < 5; i++) {
            // Work around to access variable inside CountDowTimer
            final int j = i;

            new CountDownTimer((i + 2) * 1000, (i + 1) * 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {

                    // Show flop card
                    int tableCard1Id = getResources().getIdentifier(
                            Cards.getSrcCard(tableCards.get(j)),
                            "drawable",
                            getPackageName()
                    );
                    tableCardsImg.get(j).setImageResource(tableCard1Id);

                    tempTableCards.add(tableCards.get(j));

                    // Turn is out
                    if (j == 3) {
                        // Show odds
                        oddsLabel.get(0).setText(turnO[0] + " %");
                        oddsLabel.get(1).setText(turnO[1] + " %");

                        // Show rankings
                        rankingLabel.get(0).setText(turnRanking[0]);
                        rankingLabel.get(1).setText(turnRanking[1]);
                    }


                    // River is out
                    if (j == 4) {

                        // Hide bets
                        betLabel.get(0).setText("");
                        betLabel.get(1).setText("");

                        // Hide odds
                        oddsLabel.get(0).setText("");
                        oddsLabel.get(1).setText("");

                        // Show final ranking
                        rankingLabel.get(0).setText(riverRanking[0]);
                        rankingLabel.get(1).setText(riverRanking[1]);

                        // Show summary
                        summary.setText(txt);

                        // Calculate winner
                        calculateWinner();
                    }

                }
            }.start();
        }
    }

    // Method to show down all table cards
    public void fiveCardsShowDown() {

        pokerChips[pot] += tempPot;

        // Array to store odds and flop and turn
        float[] flopOdds = new float[3];
        float[] turnOdds = new float[3];

        // String to store ranking in flop, turn and river
        final String[] flopRanking = new String[2];
        final String[] turnRanking = new String[2];
        final String[] riverRanking = new String[2];

        // Object to evaluate hands
        Evaluate evaluate = new Evaluate();

        // Hand rankings will be visible
        rankingLabel.get(0).setVisibility(View.VISIBLE);
        rankingLabel.get(1).setVisibility(View.VISIBLE);

        // Temp table cards gets flop
        tempTableCards.addAll(tableCards.subList(0, 3));

        // Get odds in flop
        flopOdds = oddsObj.oddsPlayerVSPlayerFlop(opponentCards, playerCards, tempTableCards);
        tempTableCards.remove(3);
        tempTableCards.remove(3);
        final float[] flopO = new float[2];
        flopO[0] = flopOdds[1];
        flopO[1] = flopOdds[2];

        // get ranking in flop
        // Opponent
        evaluate.evaluateHand(opponentCards, tempTableCards);
        flopRanking[0] = evaluate.getResult();
        evaluate.reset();
        // Player
        evaluate.evaluateHand(playerCards, tempTableCards);
        flopRanking[1] = evaluate.getResult();
        evaluate.reset();

        // Temp table cards get turn
        tempTableCards.add(tableCards.get(3));

        // Get odds in turn
        turnOdds = oddsObj.oddsPlayerVSPlayerTurn(opponentCards, playerCards, tempTableCards);
        tempTableCards.remove(4);
        final float[] turnO = new float[2];
        turnO[0] = turnOdds[1];
        turnO[1] = turnOdds[2];

        // Get ranking in turn
        // Opponent
        evaluate.evaluateHand(opponentCards, tempTableCards);
        turnRanking[0] = evaluate.getResult();
        evaluate.reset();
        // Player
        evaluate.evaluateHand(playerCards, tempTableCards);
        turnRanking[1] = evaluate.getResult();
        evaluate.reset();

        // Get ranking in river
        // Opponent
        resultHand[0] = evaluate.evaluateHand(opponentCards, tableCards);
        opponentHand.addAll(evaluate.getHand());            // Get final hand
        riverRanking[0] = evaluate.getResult();
        evaluate.reset();
        // Player
        resultHand[1] = evaluate.evaluateHand(playerCards, tableCards);
        playerHand.addAll(evaluate.getHand());            // Get final hand
        statistics.saveBiggestHandCards(playerHand);
        riverRanking[1] = evaluate.getResult();
        evaluate.reset();

        ranking.set(0, riverRanking[0] + "");
        ranking.set(1, riverRanking[1] + "");

        // Show opponent cards
        int opponentCard1Id = getResources().getIdentifier(
                Cards.getSrcCard(opponentCards.get(0)),
                "drawable",
                getPackageName()
        );
        opponentCardsImg.get(0).setImageResource(opponentCard1Id);

        int opponentCard2Id = getResources().getIdentifier(
                Cards.getSrcCard(opponentCards.get(1)),
                "drawable",
                getPackageName()
        );
        opponentCardsImg.get(1).setImageResource(opponentCard2Id);

        // Hide all the buttons
        foldButton.setVisibility(View.INVISIBLE);
        callButton.setVisibility(View.INVISIBLE);
        betButton.setVisibility(View.INVISIBLE);

        // Loop through 5 cards and wait 1 second in each card
        for (int i = 0; i < 5; i++) {
            // Work around to access variable inside CountDowTimer
            final int j = i;

            new CountDownTimer((i + 2) * 1000, (i + 1) * 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    // Show flop card
                    int tableCard1Id = getResources().getIdentifier(
                            Cards.getSrcCard(tableCards.get(j)),
                            "drawable",
                            getPackageName()
                    );
                    tableCardsImg.get(j).setImageResource(tableCard1Id);

                    tempTableCards.add(tableCards.get(j));

                    // Flop is out
                    if (j == 2) {
                        // Show odds
                        oddsLabel.get(0).setText(flopO[0] + " %");
                        oddsLabel.get(1).setText(flopO[1] + " %");

                        // Show rankings
                        rankingLabel.get(0).setText(flopRanking[0]);
                        rankingLabel.get(1).setText(flopRanking[1]);
                    }

                    // Turn is out
                    if (j == 3) {
                        // Show odds
                        oddsLabel.get(0).setText(turnO[0] + " %");
                        oddsLabel.get(1).setText(turnO[1] + " %");

                        // Show rankings
                        rankingLabel.get(0).setText(turnRanking[0]);
                        rankingLabel.get(1).setText(turnRanking[1]);
                    }


                    // River is out
                    if (j == 4) {
                        // Hide odds
                        oddsLabel.get(0).setText("");
                        oddsLabel.get(1).setText("");
                        // Calculate winner
                        calculateWinner();
                    }
                }
            }.start();
        }

    }

    // Method final
    public void finalMethod() {
        reset();
        potLabel.setText(pokerChips[pot] + "€");

        rankingLabel.get(0).setText("");
        rankingLabel.get(1).setText("");

        pokerChipsLabel.get(0).setText(pokerChips[0] + "€");
        pokerChipsLabel.get(1).setText(pokerChips[1] + "€");

        betLabel.get(0).setText(bet[0] + "€");
        betLabel.get(1).setText(bet[1] + "€");

        oddsLabel.get(0).setText("");
        oddsLabel.get(1).setText("");

        gameAction.setText("New Game");

        for (ImageView card : opponentCardsImg)
            card.setImageResource(R.drawable.back);

        for (ImageView card : tableCardsImg)
            card.setImageResource(0);

        tableCards.clear();

        opponentCards.clear();
        playerCards.clear();
        opponentHand.clear();
        playerHand.clear();

        if (pokerChips[0] > 0 && pokerChips[1] > 0)
            preFlop();
        else {
            statistics.saveTotalOfGames(nGames, nWinningGames);

            // Save statistics into file
            try {
                statistics.saveMostPlayedCardsIntoFile();
                statistics.saveMostWinningCardsIntoFile();
                statistics.saveBiggestHandCardsIntoFile();
                statistics.saveTotalOfGamesIntoFile();
            } catch (IOException e) {
                Log.e("Error", e.toString());
            }
        }
    }

    // Method to calculate the winner hand
    public void calculateWinner() {

        Winner calcWinner = new Winner();
        int result;

        txt += "------- Show Down -----\n";
        txt += pcName + ": " + opponentCards + "\n";
        txt += userName + ": " + playerCards + "\n";
        txt += "Table: " + tableCards + "\n";
        txt += pcName + ": " + opponentHand.toString() + " - " + ranking.get(0) + "\n";
        txt += userName + ": " + playerHand.toString() + " - " + ranking.get(1) + "\n";

        // Calculate winner hand
        result = calcWinner.calculateWinner(opponentHand, playerHand, resultHand);

        if (result == 1) {
            txt += pcName + " wins " + pokerChips[pot] + " €\n";

            gameAction.setText(pcName + " wins " + pokerChips[pot] + " €\n");

            // Opponent gets the pot
            pokerChips[0] += pokerChips[pot];

            // Update label
            pokerChipsLabel.get(0).setText(pokerChips[0] + "  €");
            potLabel.setText(pokerChips[pot] + " €");

        } else if (result == 2) {
            // Save winning cards
            statistics.saveMostWinningCards(playerCards);
            nWinningGames++;

            txt += userName + " wins " + pokerChips[pot] + " €\n";
            gameAction.setText(userName + " wins " + pokerChips[pot] + " €\n");

            // Opponent gets the pot
            pokerChips[1] += pokerChips[pot];

            // Update label
            pokerChipsLabel.get(1).setText(pokerChips[1] + "  €");
            potLabel.setText(pokerChips[pot] + " €");
        } else {
            // Split pot
            pokerChips[0] += pokerChips[pot] / 2;
            pokerChips[1] += pokerChips[pot] / 2;

            txt += "Draw\n";
            txt += "Each player wins " + pokerChips[pot] / 2 + " €\n";

            gameAction.setText("Each player wins " + pokerChips[pot] / 2 + " €\n");


            // Update label
            pokerChipsLabel.get(1).setText(pokerChips[0] + "  €");
            pokerChipsLabel.get(1).setText(pokerChips[1] + "  €");

            potLabel.setText(pokerChips[pot] + " €");
        }

        summary.setText(txt);

        new CountDownTimer(3000, 3000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                finalMethod();
            }
        }.start();
    }

    // Get the game option
    private int[] gameMode(int player, int gameMode, int menu) {

        // Store players cards
        HashMap<Integer, ArrayList<Cards>> cards = new HashMap<>();

        cards.put(0, opponentCards);
        cards.put(1, playerCards);
        cards.put(2, tempTableCards);

        Random botRandom = new Random();
        int bot = 0;
        switch (gameMode) {
            // User vs Passive
            case 1:
                betOption = computer.computer_passive(player, dealer, pokerChips, bet, tempPot, round, menu, cards, odds);
                break;
            // User vs Aggressive
            case 2:
                betOption = computer.computer_aggressive(player, dealer, pokerChips, bet, tempPot, round, menu, cards, odds);
                break;
            // User vs Mix
            case 3:
                bot = botRandom.nextInt((1 - 0) + 1) + 0;
                if (bot == 0)
                    betOption = computer.computer_passive(player, dealer, pokerChips, bet, tempPot, round, menu, cards, odds);
                else
                    betOption = computer.computer_aggressive(player, dealer, pokerChips, bet, tempPot, round, menu, cards, odds);
                break;
            // User vs MFS
            case 4:
                betOption = computer.MFS(player, pokerChips, tempPot, odds, bet, menu);
                break;
            // Passive vs Aggressive
            case 5:
                if (player == 0)
                    betOption = computer.computer_passive(player, dealer, pokerChips, bet, tempPot, round, menu, cards, odds);
                else
                    betOption = computer.computer_aggressive(player, dealer, pokerChips, bet, tempPot, round, menu, cards, odds);
                break;
            // Passive vs Mix
            case 6:
                if (player == 0)
                    betOption = computer.computer_passive(player, dealer, pokerChips, bet, tempPot, round, menu, cards, odds);
                else {
                    bot = botRandom.nextInt((1 - 0) + 1) + 0;
                    if (bot == 0)
                        betOption = computer.computer_passive(player, dealer, pokerChips, bet, tempPot, round, menu, cards, odds);
                    else
                        betOption = computer.computer_aggressive(player, dealer, pokerChips, bet, tempPot, round, menu, cards, odds);
                }
                break;
            // Passive vs MFS
            case 7:
                if (player == 0)
                    betOption = computer.computer_passive(player, dealer, pokerChips, bet, tempPot, round, menu, cards, odds);
                else
                    betOption = computer.MFS(player, pokerChips, tempPot, odds, bet, menu);
                break;
            // Aggressive vs Mix
            case 8:
                if (player == 0)
                    betOption = computer.computer_aggressive(player, dealer, pokerChips, bet, tempPot, round, menu, cards, odds);
                else {
                    bot = botRandom.nextInt((1 - 0) + 1) + 0;
                    if (bot == 0)
                        betOption = computer.computer_passive(player, dealer, pokerChips, bet, tempPot, round, menu, cards, odds);
                    else
                        betOption = computer.computer_aggressive(player, dealer, pokerChips, bet, tempPot, round, menu, cards, odds);
                }
                break;
            // Aggressive vs MFS
            case 9:
                if (player == 0)
                    betOption = computer.computer_aggressive(player, dealer, pokerChips, bet, tempPot, round, menu, cards, odds);
                else
                    betOption = computer.MFS(player, pokerChips, tempPot, odds, bet, menu);
                break;
            // Mix vs MFS
            case 10:
                if (player == 0) {
                    bot = botRandom.nextInt((1 - 0) + 1) + 0;
                    if (bot == 0)
                        betOption = computer.computer_passive(player, dealer, pokerChips, bet, tempPot, round, menu, cards, odds);
                    else
                        betOption = computer.computer_aggressive(player, dealer, pokerChips, bet, tempPot, round, menu, cards, odds);
                } else
                    betOption = computer.MFS(player, pokerChips, tempPot, odds, bet, menu);
        }


        return betOption;
    }

    // Method to reset values
    public void reset() {

        betSeekBar.setProgress(bigBlind);
        bet[0] = 0;
        bet[1] = 0;

        pokerChips[pot] = 0;
    }

    // Method to hide all the buttons
    public void hideButtons() {
        foldButton.setVisibility(View.INVISIBLE);
        callButton.setVisibility(View.INVISIBLE);
        betButton.setVisibility(View.INVISIBLE);
        betSeekBar.setVisibility(View.INVISIBLE);
        betText.setVisibility(View.INVISIBLE);
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
