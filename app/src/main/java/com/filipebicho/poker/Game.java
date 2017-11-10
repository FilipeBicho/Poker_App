package com.filipebicho.poker;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import java.lang.reflect.Method;
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
    // Bet seekbar
    private SeekBar betSeekBar;
    // Bet edit text;
    private TextView betText;
    // Text Summary
    private TextView summary;
    // Animation for button
    private Animation buttonAnim;

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
    private int[] pokerChips = new int[4];
    // Store the initial poker chip os each player
    private int[] initialPokerChips = new int[2];
    // Store previously pot money
    private int tempPot=0;
    // Stores opponent and player bets
    private int[] bet = new int[2];
    // Store confirmation of opponent and player to go to next round
    private int[] confirmation = new int[2];
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
    // Stores opponent and player odds
    private float[] odds = new float[2];
    // If a player made an initial bet
    private boolean initialBet = true;
    // If a player made an initial check
    private boolean initialCheck = false;

    // Store opponent cards
    private ArrayList<Cards> opponentCards;
    // Store player cards
    private ArrayList<Cards> playerCards;
    // Store table cards
    private ArrayList<Cards> tableCards;
    //Stores table cards temporarily
    private ArrayList<Cards> tempTableCards;
    // Store hands of both players
    private HashMap<Integer,ArrayList<Cards>> playersHands;
    private ArrayList<Cards> opponentHand = new ArrayList<>();
    private  ArrayList<Cards> playerHand = new ArrayList<>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // Initialize gameTitle
        gameTitle = (TextView) findViewById(R.id.game_title);

        // Initialize gameAction
        gameAction = (TextView) findViewById(R.id.game_action);

        // Initialize opponent and player money
        pokerChipsLabel = new ArrayList<>();
        pokerChipsLabel.add((TextView) findViewById(R.id.opponent_money));
        pokerChipsLabel.add((TextView) findViewById(R.id.player_money));

        // Initialize opponent and player names
        opponentName = (TextView) findViewById(R.id.opponent_name);
        playerName = (TextView) findViewById(R.id.player_name);

        // Initialize opponent and player bets
        betLabel = new ArrayList<>();
        betLabel.add((TextView) findViewById(R.id.opponent_bet));
        betLabel.add((TextView) findViewById(R.id.player_bet));

        // Initialize opponent and player odds
        oddsLabel = new ArrayList<>();
        oddsLabel.add((TextView) findViewById(R.id.opponent_odds));
        oddsLabel.add((TextView) findViewById(R.id.player_odds));

        // Initialize pot label
        potLabel = (TextView) findViewById(R.id.pot);

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

        // Min value to bet is bigblind
        if(betSeekBar.getProgress() < bigBlind)
            betSeekBar.setProgress(bigBlind);

        // Max value to bet is player money
        betSeekBar.setMax(pokerChips[1]);

        // Initialize textview for the poker chips value
        betText = (TextView) findViewById(R.id.poker_chips);

        // Fold button onClick
        foldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foldButton.startAnimation(buttonAnim);
                fold(1);
                hideButtons();
            }
        });

        // Call button onClick
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callButton.startAnimation(buttonAnim);
                call(1);
                hideButtons();
            }
        });

        // Bet button onCLick
        betButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                betButton.startAnimation(buttonAnim);
            }
        });

        // perform seek bar change listener event used for getting the progress value
        betSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                //set min value to betSeekBar
                if(betSeekBar.getProgress() < bigBlind)
                    betSeekBar.setProgress(bigBlind);
                // Min value to raise is 2 * bigblind
                if(betSeekBar.getProgress() > bigBlind && betSeekBar.getProgress() < (2*bigBlind))
                    betSeekBar.setProgress(bigBlind*2);

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

        // GET PLAYER AND OPPONENT NAME AND GAME MODE WITH GET INTENT

        gameMode = 1;

        Log.v("onCreate", "1");

        // HERE GOES AN IF STATEMENT TO CHECK WHICH KIND OF GAME IS!
        initializeLayouts();
        Log.v("onCreate", "2");

    }

    // Method to initialize Layout's
    public void initializeLayouts()
    {
        Log.v("initializeLayouts", "1");

        // 1. Change Title
        // 2. Change opponent and player name

        //There is still no action
        gameAction.setText("--");

        // Initialize opponent and player money
        pokerChips[0] = 30;
        pokerChips[1] = 1500;
        pokerChipsLabel.get(0).setText(pokerChips[0] + " €");
        pokerChipsLabel.get(1).setText(pokerChips[1] + " €");


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
        dealer = dealerRandom.nextInt((1-0)+1)+ 0; // random number between 0 and 1

        // Show dealer
        dealerLabel.get(dealer).setVisibility(View.VISIBLE);
        // Go to pre flop
        preFlop();
        Log.v("initializeLayouts", "2");
    }

    // Method to initialize Pre flop
    public void preFlop()
    {
        Log.v("preFlop", "1");
        // Get a deck
        Deck deck = new Deck();
        // Get a dealer
        Dealer dealer = new Dealer();


        // Initialize cards
        opponentCards = new ArrayList<>();
        playerCards = new ArrayList<>();
        tableCards = new ArrayList<>();
        tempTableCards = new ArrayList<>();

        // Players get cards
        dealer.giveCards(deck,opponentCards, playerCards);

        // Table gets already all the cards
        dealer.giveFlop(deck,tableCards);
        dealer.giveOneCard(deck,tableCards);
        dealer.giveOneCard(deck,tableCards);

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
        if(gameMode > 4)
        {
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
        }

        // Pre flop round
        round = 0;
        // Get opponent odds
        odds[0] = (float) oddsObj.preFlopOdds(opponentCards);

        // If game mode is computer vs computer calculate also player odds and show both odds
        if(gameMode > 4)
        {
            // Get opponent odds
            odds[1] = (float) oddsObj.preFlopOdds(playerCards);
            oddsLabel.get(0).setText(odds[0] + " %");
            oddsLabel.get(1).setText(odds[1] + " %");

        }

        // Increase number of games and show in summary
        nGames++;
        txt += "Game number: " + nGames + "\n";
        summary.setText(txt);

        // Go to pre Flop bets
        betsPreFlop();
        Log.v("preFlop", "2");

    }

    // Method to make bets in pre flop
    public void betsPreFlop()
    {
        Log.v("betsPreFlop", "1");

        // Check if is first that goes to pre flop
        // If is not dealer changes position
        if(!firstPlay)
            dealer = (dealer == 0) ? 1 : 0;
        firstPlay = false;
        dealer = 1;
        // Initialize blind
        int blind = (dealer == 0) ? 1 : 0;

        // Change dealer
        dealerLabel.get(dealer).setVisibility(View.VISIBLE);
        dealerLabel.get(blind).setVisibility(View.INVISIBLE);

        // Initialize values
        reset();

        // If blind doesn't have enough money makes all in
        if(pokerChips[blind] <= bigBlind) {
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

                if(blind == 0)
                    gameAction.setText("Opponent makes All in with " + pokerChips[blind] + " €");
                else
                    gameAction.setText("Player makes All in with" + pokerChips[blind] + " €");

                // Show all cards and calculate winner
                fiveCardsShowDown();
            }
            else
            {
                // Dealer pays small blind
                bet[dealer] = smallBlind;
                pokerChips[dealer] -= bet[dealer];

                // Set pot
                pokerChips[pot] = bet[dealer]+bet[blind];

                // Is not initial bet anymore
                initialBet = false;

                // Blind has to do all in
                allIn(blind);

            }
        }
        Log.v("betsPreFlop", "2");
    }

    // Method to bet all the poker chips
    public void allIn(int player)
    {

        int opponent = (player == 0) ? 1 : 0;

        int tempbet = bet[player];

        // Bet all the chips
        bet[player] = pokerChips[player] + tempbet;

        //Update action
        if(player == 0)
            gameAction.setText("Opponent makes All in with " + pokerChips[player] + " €");

        else
            gameAction.setText("Player makes All in with" + bet[player] + " €");

        pokerChips[player] -= (bet[player]-tempbet);
        pokerChips[pot] = (bet[player]+ bet[opponent]);

        potLabel.setText(pokerChips[pot] + " €");

        // Opponent has to talk
        confirmation[player] = 1;
        confirmation[opponent] = 0;

        initialBet=false;

        player = (player == 0) ? 1 : 0;
        Log.v("Player", pokerChips[player] +"");
        if(pokerChips[player] > 0)
        {
           // If is computer vs computer goes directly to fold_call menu
            if(gameMode > 4)
                fold_call(player);
            else if(player == 0)
                fold_call(player);
            else
            {
                // Show fold and call buttons
                foldButton.setVisibility(View.VISIBLE);
                callButton.setVisibility(View.VISIBLE);
            }

        }
        else
        {
            if(round == 0)
                fiveCardsShowDown();
        }
    }

    // Method when the player equals opponent bet
    public void call(int player)
    {
        int opponent = (player == 0) ? 1 : 0;

        // Stores old bet
        float tempBet;

        // If call is bigger then player money makes all in
        if((bet[opponent] - bet[player]) >= pokerChips[player] )
        {
            tempBet = bet[player];
            bet[player] = pokerChips[player];

            //Update action
            if(player == 0)
                gameAction.setText("Opponent makes All in with " + bet[player] + " €");

            else
                gameAction.setText("Player makes All in with " + bet[player] + " €");

            pokerChips[player] -= bet[player];
            bet[player] += tempBet;

            // Opponent gets difference
            pokerChips[opponent] = (pokerChips[opponent] + bet[opponent]) - bet[player];
            pokerChips[pot]= bet[player]*2;
        }
        else
        {
            tempBet = bet[player];

            // Bet is the difference of bets
            bet[player] = bet[opponent] - bet[player];

            //Update action
            if(player == 0)
                gameAction.setText("Opponent makes call with " + bet[player] + " €");

            else
                gameAction.setText("Player makes call with " + bet[player] + " €");

            pokerChips[player] -= bet[player];
            bet[player] += tempBet;
            pokerChips[pot]= bet[player] + bet[opponent];

        }

        // Update pote
        potLabel.setText(pokerChips[pot] + " €");

        // Update bets
        betLabel.get(0).setText(bet[0] + " €");
        betLabel.get(1).setText(bet[1] + " €");

        // If one of the players doesn't have enough money
        if(pokerChips[0] == 0 || pokerChips[1] == 0)
        {
            if(round == 0)
                fiveCardsShowDown();
        }

        initialCheck = false;

        // Bet cycle is finish
        confirmation[player] = 1;

        // Games continues
        pokerChips[3] = 0;
    }

    // Method when a player makes fold
    public void fold(int player)
    {

        //Update action
        if(player == 0)
            gameAction.setText("Opponent makes fold");
        else
            gameAction.setText("Player makes fold");

        int opponent = (player == 0) ? 1 : 0;

        // Opponent wins the pot
        pokerChips[opponent] += pokerChips[pot] + tempPot;

        txt += gameAction.getText() + "\n";
        txt += "Player " + (opponent+1) + " wins " + (pokerChips[pot]+tempPot) + "\n";

        //Update labels
        betLabel.get(0).setText("");
        betLabel.get(1).setText("");
        potLabel.setText("");
        summary.setText(txt);


        // End of game
        confirmation[0] = 1;
        confirmation[1] = 1;
        pokerChips[3] = 1;
        pokerChips[pot] = 0;
    }

    // Method get option in fold call menu in computer vs computer
    public void fold_call(int player)
    {

    }

    // Method to update labels
    public void updateLabels()
    {
        // Update opponent and player poker chips
        pokerChipsLabel.get(0).setText(pokerChips[0] + " €");
        pokerChipsLabel.get(1).setText(pokerChips[1] + " €");

        // Update bets
        betLabel.get(0).setText(bet[0] + " €");
        txt += "Opponent bets" + bet[0] + " €\n";
        betLabel.get(1).setText(bet[1] + " €");
        txt += "Player bets" + bet[1] + " €\n";
        // Update pot poker chips
        potLabel.setText(pokerChips[pot] + " €");

    }


    /* Method to show down all table cards
    *
    **/
    public void fiveCardsShowDown()
    {
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
        tempTableCards.addAll(tableCards.subList(0,3));

        // Get odds in flop
        flopOdds = oddsObj.oddsPlayerVSPlayerFlop(opponentCards,playerCards,tempTableCards);
        tempTableCards.remove(3);
        tempTableCards.remove(3);
        final float[] flopO = new float[2];
        flopO[0] = flopOdds[1];
        flopO[1] = flopOdds[2];

        // get ranking in flop
        // Opponent
        evaluate.evaluateHand(opponentCards,tempTableCards);
        flopRanking[0] = evaluate.getResult();
        evaluate.reset();
        // Player
        evaluate.evaluateHand(playerCards,tempTableCards);
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
        evaluate.evaluateHand(opponentCards,tempTableCards);
        turnRanking[0] = evaluate.getResult();
        evaluate.reset();
        // Player
        evaluate.evaluateHand(playerCards,tempTableCards);
        turnRanking[1] = evaluate.getResult();
        evaluate.reset();

        // Get rankin in river
        // Opponent
        resultHand[0] = evaluate.evaluateHand(opponentCards,tableCards);
        opponentHand.addAll(evaluate.getHand());            // Get final hand
        riverRanking[0] = evaluate.getResult();
        evaluate.reset();
        // Player
        resultHand[1] = evaluate.evaluateHand(playerCards,tableCards);
        playerHand.addAll(evaluate.getHand());            // Get final hand
        riverRanking[1] = evaluate.getResult();
        evaluate.reset();

        // Update summary
        txt += "Opponent: " + opponentHand + " - " + riverRanking[0] + "\n" +
                "player: " + playerHand + " - " + riverRanking[1] + "\n";

        // APAGAR
        gameMode = 5;
        // Show opponent cards
        if(gameMode > 4)
        {
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
        }

        // Hide all the buttons
        foldButton.setVisibility(View.INVISIBLE);
        callButton.setVisibility(View.INVISIBLE);
        betButton.setVisibility(View.INVISIBLE);

        // Hide bets
        betLabel.get(0).setText("");
        betLabel.get(1).setText("");

        // Loop through 5 cards and wait 1 second in each card
        for(int i = 0; i < 5; i++)
        {
            // Work around to access variable inside CountDowTimer
            final int j = i;

            new CountDownTimer((i+2) * 1000, (i+1) * 1000) {
                public void onTick(long millisUntilFinished) {}

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
                    if(j == 2)
                    {
                        // Show odds
                        oddsLabel.get(0).setText(flopO[0] + " %");
                        oddsLabel.get(1).setText(flopO[1] + " %");

                        // Show rankings
                        rankingLabel.get(0).setText(flopRanking[0]);
                        rankingLabel.get(1).setText(flopRanking[1]);
                    }

                    // Turn is out
                    if(j == 3)
                    {
                        // Show odds
                        oddsLabel.get(0).setText(turnO[0] + " %");
                        oddsLabel.get(1).setText(turnO[1] + " %");

                        // Show rankings
                        rankingLabel.get(0).setText(turnRanking[0]);
                        rankingLabel.get(1).setText(turnRanking[1]);
                    }


                    // River is out
                    if(j == 4)
                    {
                        // Calculate winner
                        calculateWinner();

                        // Hide odds
                        oddsLabel.get(0).setText("");
                        oddsLabel.get(1).setText("");

                        // Show final ranking
                        rankingLabel.get(0).setText(riverRanking[0]);
                        rankingLabel.get(1).setText(riverRanking[1]);

                        // Show summary
                        summary.setText(txt);
                    }

                }
            }.start();
        }


        Log.v("turnRanking",turnRanking[1]);
        Log.v("tempTable",tempTableCards.toString());
    }

    // Method to calculate the winner hand
    public void calculateWinner()
    {
        Log.v("calculateWinner", "1");
        Winner calcWinner = new Winner();
        int result;

        Log.v("hands", opponentHand.toString() + playerHand.toString());

        // Calculate winner hand
        result = calcWinner.calculateWinner(opponentHand, playerHand, resultHand);

        if(result == 1)
        {
            txt += "Opponent wins + " + pokerChips[pot] + " €\n";

            // Opponent gets the pot
            pokerChips[0] += pokerChips[pot];
            pokerChips[pot] = 0;

            // Update label
            pokerChipsLabel.get(0).setText(pokerChips[0] + "  €");
            potLabel.setText(pokerChips[pot] + " €");

        }
        else if(result == 2)
        {
            txt += "Opponent wins + " + pokerChips[pot] + " €\n";

            // Opponent gets the pot
            pokerChips[1] += pokerChips[pot];
            pokerChips[pot] = 0;

            // Update label
            pokerChipsLabel.get(1).setText(pokerChips[1] + "  €");
            potLabel.setText(pokerChips[pot] + " €");
        }

        else
        {
            // Split pot
            pokerChips[0] = Math.round(pokerChips[pot]/2);
            pokerChips[1] = Math.round(pokerChips[pot]/2);

            txt += "Draw\n";
            txt += "Each player wins + " +  pokerChips[0] + " €\n";

            // Update label
            pokerChipsLabel.get(1).setText(pokerChips[0] + "  €");
            pokerChipsLabel.get(1).setText(pokerChips[1] + "  €");
            potLabel.setText(pokerChips[pot] + " €");
        }
    }


    // Method to reset values
    public void reset()
    {
        // Its gonna be the first bet
        initialBet = true;
        bet[0] = 0;
        bet[1] = 0;
        confirmation[0] = 0;
        confirmation[1] = 0;
        pokerChips[pot] = 0;
        tempPot = 0;
        initialPokerChips[0] = pokerChips[0];
        initialPokerChips[1] =  pokerChips[1];
    }

    // Method to hide all the buttons
    public void hideButtons()
    {
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
