package com.filipebicho.poker;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Flip on 25/11/2017.
 */

class SetStatistics {

    private final ArrayList<Cards> mostPlayedCards1;
    private final ArrayList<Cards> mostPlayedCards2;
    private final ArrayList<Integer> mostPlayedCardsCounter;

    private final ArrayList<Cards> mostWinningCards1;
    private final ArrayList<Cards> mostWinningCards2;
    private final ArrayList<Integer> mostWinningCardsCounter;

    private final ArrayList<Cards> biggestHandCards;
    private String biggestHandRanking;
    private int biggestHandResult;

    private int numberOfGames;
    private int numberOfWinningGames;

    private final String userName;

    public SetStatistics( String userName)
    {

        this.userName = userName;

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
        biggestHandResult = 0;

        // Initialize number of games and winning games
        numberOfGames = 0;
        numberOfWinningGames = 0;

        readMostPlayedCards();
        readMostWinningCards();
        readBiggestHandCards();
        readTotalOfGames();
    }

    // Save most played cards in a file
    public void saveMostPlayedCardsIntoFile() throws IOException
    {

        // Create Folder
        File folder = new File("/data/data/com.filipebicho.poker/files/" + userName );
        if(!folder.exists())
            folder.mkdirs();

        File file= new File(folder, "MostPlayedCards.txt");
        FileOutputStream fileout=new FileOutputStream(file);
        OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
        String newLine = System.getProperty("line.separator");
        // Save cards into file
        try {

            for(int i = 0; i < mostPlayedCards1.size();i++)
            {
                outputWriter.write(
                        " " + mostPlayedCards1.get(i).getRank() + " " +
                                mostPlayedCards1.get(i).getSuit() + " " +
                                mostPlayedCards2.get(i).getRank() + " " +
                                mostPlayedCards2.get(i).getSuit() + " " +
                                mostPlayedCardsCounter.get(i) + newLine
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(outputWriter != null)
            {
                try {
                    outputWriter.close();
                }catch (IOException e){
                    Log.e("Error", e.toString());
                }
            }
        }
    }

    // Save most played cards in a file
    public void saveMostWinningCardsIntoFile() throws IOException
    {

        // Create Folder
        File folder = new File("/data/data/com.filipebicho.poker/files/" + userName );
        if(!folder.exists())
            folder.mkdirs();

        File file= new File(folder, "MostWinningCards.txt");
        FileOutputStream fileout = new FileOutputStream(file);
        OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
        String newLine = System.getProperty("line.separator");
        // Save cards into file
        try {

            for(int i = 0; i < mostWinningCards1.size();i++)
            {
                outputWriter.write(
                        " " + mostWinningCards1.get(i).getRank() + " " +
                                mostWinningCards1.get(i).getSuit() + " " +
                                mostWinningCards2.get(i).getRank() + " " +
                                mostWinningCards2.get(i).getSuit() + " " +
                                mostWinningCardsCounter.get(i) + newLine
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(outputWriter != null)
            {
                try {
                    outputWriter.close();
                }catch (IOException e){
                    Log.e("Error", e.toString());

                }
            }
        }
    }

    // Save best hand cards in a file
    public void saveBiggestHandCardsIntoFile() throws IOException
    {

        // Create Folder
        File folder = new File("/data/data/com.filipebicho.poker/files/" + userName );
        if(!folder.exists())
            folder.mkdirs();

        File file= new File(folder, "BiggestHandCards.txt");
        FileOutputStream fileout=new FileOutputStream(file);
        OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
        String newLine = System.getProperty("line.separator");
        // Save cards into file
        try {

            for(int i = 0; i < biggestHandCards.size();i++)
            {
                outputWriter.write(
                        " " + biggestHandCards.get(i).getRank() + " " +
                                biggestHandCards.get(i).getSuit() + newLine
                );
            }
            outputWriter.write(" " + biggestHandRanking + newLine);
            outputWriter.write(" " + biggestHandResult);

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(outputWriter != null)
            {
                try {
                    outputWriter.close();
                }catch (IOException e){
                    Log.e("Error", e.toString());
                }
            }
        }
    }

    // Save total of game and total of wins in a file
    public void saveTotalOfGamesIntoFile() throws IOException
    {
        // Create Folder
        File folder = new File("/data/data/com.filipebicho.poker/files/" + userName );
        if(!folder.exists())
            folder.mkdirs();

        File file= new File(folder, "TotalOfGames.txt");
        FileOutputStream fileout=new FileOutputStream(file);
        OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
        String newLine = System.getProperty("line.separator");
        // Save cards into file
        try {
            outputWriter.write(" " + numberOfGames + " " + numberOfWinningGames);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(outputWriter != null)
            {
                try {
                    outputWriter.close();
                }catch (IOException e){
                    Log.e("Error", e.toString());
                }
            }
        }
    }

    // Save cards in an ArrayList, if cards already exists then increment one value
    public void saveMostPlayedCards(ArrayList<Cards> cards)
    {
        boolean flag = false;
        // Go through all the cards
        for(int i = 0; i < mostPlayedCards1.size(); i++)
        {
            // If the first is the same
            if(cards.get(0).getRank() == mostPlayedCards1.get(i).getRank() &&
                    cards.get(0).getSuit() == mostPlayedCards1.get(i).getSuit())
            {
                // If the second card is also the same
                if(cards.get(1).getRank() == mostPlayedCards2.get(i).getRank() &&
                        cards.get(1).getSuit() == mostPlayedCards2.get(i).getSuit())
                {
                    // Increment counter of this cards
                    int inc = mostPlayedCardsCounter.get(i);
                    inc++;
                    mostPlayedCardsCounter.set(i, inc);
                    flag = true;
                    return;
                }

            }
            // Same thing but inverse
            else if(cards.get(0).getRank() == mostPlayedCards2.get(i).getRank() &&
                    cards.get(0).getSuit() == mostPlayedCards2.get(i).getSuit())
            {
                if(cards.get(1).getRank() == mostPlayedCards1.get(i).getRank() &&
                        cards.get(1).getSuit() == mostPlayedCards1.get(i).getSuit())
                {
                    int inc = mostPlayedCardsCounter.get(i);
                    inc++;
                    mostPlayedCardsCounter.set(i, inc);
                    flag = true;
                    return;
                }

            }

        }

        // If cards don't exists already then add them
        if(!flag)
        {
            mostPlayedCards1.add(cards.get(0));
            mostPlayedCards2.add(cards.get(1));
            mostPlayedCardsCounter.add(1);
        }
    }

    // Save cards in an ArrayList, if cards already exists then increment one value
    public void saveMostWinningCards(ArrayList<Cards> cards)
    {
        boolean flag = false;
        // Go through all the cards
        for(int i = 0; i < mostWinningCards1.size(); i++)
        {
            // If the first is the same
            if(cards.get(0).getRank() == mostWinningCards1.get(i).getRank() &&
                    cards.get(0).getSuit() == mostWinningCards1.get(i).getSuit())
            {
                // If the second card is also the same
                if(cards.get(1).getRank() == mostWinningCards2.get(i).getRank() &&
                        cards.get(1).getSuit() == mostWinningCards2.get(i).getSuit())
                {
                    // Increment counter of this cards
                    int inc = mostWinningCardsCounter.get(i);
                    inc++;
                    mostWinningCardsCounter.set(i, inc);
                    flag = true;
                    return;
                }

            }
            // Same thing but inverse
            else if(cards.get(0).getRank() == mostWinningCards2.get(i).getRank() &&
                    cards.get(0).getSuit() == mostWinningCards2.get(i).getSuit())
            {
                if(cards.get(1).getRank() == mostWinningCards1.get(i).getRank() &&
                        cards.get(1).getSuit() == mostWinningCards1.get(i).getSuit())
                {
                    int inc = mostWinningCardsCounter.get(i);
                    inc++;
                    mostWinningCardsCounter.set(i, inc);
                    flag = true;
                    return;
                }

            }

        }

        // If cards don't exists already then add them
        if(!flag)
        {
            mostWinningCards1.add(cards.get(0));
            mostWinningCards2.add(cards.get(1));
            mostWinningCardsCounter.add(1);
        }
    }

    // Save best hand in an ArrayList
    public void saveBiggestHandCards(ArrayList<Cards> hand)
    {
        Evaluate calculateRank = new Evaluate();
        ArrayList<Cards> twoCards = new ArrayList<>();
        ArrayList<Cards> threeCards = new ArrayList<>();
        int[] result = new  int[2];

        // If doesn't exists a biggest hand
        if(biggestHandCards.isEmpty())
        {
            twoCards.addAll(hand.subList(0,2));
            threeCards.addAll(hand.subList(2,5));

            biggestHandResult = calculateRank.evaluateHand(twoCards,threeCards);
            biggestHandRanking = calculateRank.getResult();
            biggestHandCards.addAll(calculateRank.getHand());
        }
        else
        {
            ArrayList<Cards> newHand = new ArrayList<>();
            Winner calculateWinner = new Winner();
            twoCards.addAll(hand.subList(0,2));
            threeCards.addAll(hand.subList(2,5));

            result[0] = biggestHandResult;
            result[1] = calculateRank.evaluateHand(twoCards,threeCards);
            newHand.addAll(calculateRank.getHand());

            // If it is a better hand then the actual hand
            if(calculateWinner.calculateWinner(biggestHandCards,newHand, result) == 2)
            {
                biggestHandCards.clear();
                biggestHandCards.addAll(newHand);
                biggestHandResult = result[1];
                biggestHandRanking = calculateRank.getResult();
            }

        }


    }

    // Increment and save total of games and winning games
    public void saveTotalOfGames(int nGames, int nWins)
    {
        numberOfGames += nGames;
        numberOfWinningGames += nWins;
    }

    // Read most played cards from file
    private void readMostPlayedCards()
    {

        // Get path of the folder
        String path = "/data/data/com.filipebicho.poker/files/" + userName;

        // /Get the text file
        File file = new File(path,"MostPlayedCards.txt");

        //Read text from file
        StringBuilder text = new StringBuilder();

        BufferedReader readTextFile = null;

        try {
            readTextFile = new BufferedReader(new FileReader(file));
            String line;

            while ((line = readTextFile.readLine()) != null) {
                text.append(line);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("File Not Fourd", e.toString());
        }
        catch (IOException e)
        {
            Log.e("Execption", e.toString());
        }
        finally {
            if(readTextFile != null)
            {
                try {
                    readTextFile.close();
                }
                catch (IOException e)
                {
                    Log.e("Execption", e.toString());
                }
            }
        }

        if(file.exists())
        {
            // Gets an array of strings
            String[] valuesStr = text.toString().split(" ");

            int j = 1;
            int rank1=0;
            int suit1=0;
            int rank2=0;
            int suit2=0;
            int counter=0;
            for(int i = 1; i < valuesStr.length; i++)
            {
                if(j == 1)
                    rank1 = Integer.valueOf(valuesStr[i]);
                if(j == 2)
                    suit1 = Integer.valueOf(valuesStr[i]);
                if(j == 3)
                    rank2 = Integer.valueOf(valuesStr[i]);
                if(j == 4)
                    suit2 = Integer.valueOf(valuesStr[i]);
                if(j == 5)
                    counter = Integer.valueOf(valuesStr[i]);

                if(j == 5)
                {
                    j=1;
                    mostPlayedCards1.add(new Cards(rank1,suit1));
                    mostPlayedCards2.add(new Cards(rank2,suit2));
                    mostPlayedCardsCounter.add(counter);
                }
                else
                    j++;
            }
        }
    }

    // Read most played cards from file
    private void readMostWinningCards()
    {
        // Get path of the folder
        String path = "/data/data/com.filipebicho.poker/files/" + userName;

        // /Get the text file
        File file = new File(path,"MostWinningCards.txt");

        //Read text from file
        StringBuilder text = new StringBuilder();

        BufferedReader readTextFile = null;

        try {
            readTextFile = new BufferedReader(new FileReader(file));
            String line;

            while ((line = readTextFile.readLine()) != null) {
                text.append(line);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("File Not Fourd", e.toString());
        }
        catch (IOException e)
        {
            Log.e("Execption", e.toString());
        }
        finally {
            if(readTextFile != null) {
                try {
                    readTextFile.close();
                }
                catch (IOException e) {
                    Log.e("Execption", e.toString());
                }
            }
        }

        if (file.exists())
        {
            // Gets an array of strings
            String[] valuesStr = text.toString().split(" ");

            int j = 1;
            int rank1=0;
            int suit1=0;
            int rank2=0;
            int suit2=0;
            int counter=0;
            for(int i = 1; i < valuesStr.length; i++)
            {
                if(j == 1)
                    rank1 = Integer.valueOf(valuesStr[i]);
                if(j == 2)
                    suit1 = Integer.valueOf(valuesStr[i]);
                if(j == 3)
                    rank2 = Integer.valueOf(valuesStr[i]);
                if(j == 4)
                    suit2 = Integer.valueOf(valuesStr[i]);
                if(j == 5)
                    counter = Integer.valueOf(valuesStr[i]);

                if(j == 5)
                {
                    j=1;
                    mostWinningCards1.add(new Cards(rank1,suit1));
                    mostWinningCards2.add(new Cards(rank2,suit2));
                    mostWinningCardsCounter.add(counter);
                }
                else
                    j++;
            }
        }

    }

    // Read biggest hand from file
    private void readBiggestHandCards()
    {
        // Get path of the folder
        String path = "/data/data/com.filipebicho.poker/files/" + userName;

        // /Get the text file
        File file = new File(path,"BiggestHandCards.txt");

        //Read text from file
        StringBuilder text = new StringBuilder();

        BufferedReader readTextFile = null;

        try {
            readTextFile = new BufferedReader(new FileReader(file));
            String line;

            while ((line = readTextFile.readLine()) != null) {
                text.append(line);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("File Not Fourd", e.toString());
        }
        catch (IOException e)
        {
            Log.e("Execption", e.toString());
        }
        finally {
            if(readTextFile != null) {
                try {
                    readTextFile.close();
                }
                catch (IOException e) {
                    Log.e("Execption", e.toString());
                }
            }
        }

        if(file.exists())
        {
            // Gets an array of strings
            String[] valuesStr = text.toString().split(" ");

            int rank = 0;
            int suit = 0;

            for(int i= 1; i < 11; i++)
            {
                rank = Integer.valueOf(valuesStr[i]);
                i++;
                suit = Integer.valueOf(valuesStr[i]);
                biggestHandCards.add(new Cards(rank,suit));
            }

            biggestHandResult = Integer.valueOf(valuesStr[valuesStr.length-1]);

            if(biggestHandResult == 8 || biggestHandResult == 4)
                biggestHandRanking = valuesStr[valuesStr.length-5] + " " + valuesStr[valuesStr.length-4]
                        + " " + valuesStr[valuesStr.length-3] + " " + valuesStr[valuesStr.length-2];

            if(biggestHandResult == 10)
                biggestHandRanking = valuesStr[valuesStr.length-4] + " " +
                        valuesStr[valuesStr.length-3] + " " + valuesStr[valuesStr.length-2];

            if(biggestHandResult == 9 || biggestHandResult == 7 ||
                    biggestHandResult == 3 || biggestHandResult == 1)
                biggestHandRanking = valuesStr[valuesStr.length-3] + " " + valuesStr[valuesStr.length-2];

            if(biggestHandResult == 6 || biggestHandResult == 5 || biggestHandResult == 2)
                biggestHandRanking = valuesStr[valuesStr.length-2];
        }



    }

    // Read total of games and winning games
    private void readTotalOfGames()
    {
        // Get path of the folder
        String path = "/data/data/com.filipebicho.poker/files/" + userName;

        // /Get the text file
        File file = new File(path,"TotalOfGames.txt");

        //Read text from file
        StringBuilder text = new StringBuilder();

        BufferedReader readTextFile = null;

        try {
            readTextFile = new BufferedReader(new FileReader(file));
            text.append(readTextFile.readLine());
        }
        catch (FileNotFoundException e) {
            Log.e("File Not Fourd", e.toString());
        }
        catch (IOException e)
        {
            Log.e("Execption", e.toString());
        }
        finally {
            if(readTextFile != null) {
                try {
                    readTextFile.close();
                }
                catch (IOException e) {
                    Log.e("Execption", e.toString());
                }
            }
        }
        if(file.exists())
        {
            // Gets an array of strings
            String[] valuesStr = text.toString().split(" ");
            numberOfGames = Integer.valueOf(valuesStr[1]);
            numberOfWinningGames = Integer.valueOf(valuesStr[2]);
        }
    }

    // Define getters
    public ArrayList<Cards> getMostPlayedCards1(){ return mostPlayedCards1;}
    public ArrayList<Cards> getMostPlayedCards2(){ return mostPlayedCards2;}
    public ArrayList<Integer> getMostPlayedCardsCounter(){ return mostPlayedCardsCounter;}

    public ArrayList<Cards> getMostWinningCards1(){ return mostWinningCards1;}
    public ArrayList<Cards> getMostWinningCards2(){ return mostWinningCards2;}
    public ArrayList<Integer> getMostWinningCardsCounter(){ return mostWinningCardsCounter;}

    public ArrayList<Cards> getBiggestHandCards() {return biggestHandCards;}
    public String getBiggestHandRanking(){ return biggestHandRanking;}

    public int getNumberOfGames() { return  numberOfGames;}
    public  int getNumberOfWinningGames() { return numberOfWinningGames;}
}
