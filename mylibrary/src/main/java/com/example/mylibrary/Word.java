package com.example.mylibrary;

import android.util.Log;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Class to handle vocab words
 */
public class Word implements Comparable {
    //email of the user
    public static String username = "Anonymous User";

    //This is how difficult (as defined by the user) a word is. The scale is from 0 to 5. Here it is the multiple of the reciprocal of mastery.
    public static float quality;

    //checker variable for loading
    public static boolean loaded = false;

    //tag for log messages
    static String TAG = "brett Word";

    //Pulled from https://www.prepscholar.com/toefl/blog/wp-content/uploads/sites/13/2017/08/TOEFL-Vocab-Word-List.pdf
    //This is the default class arraylist of words
    public static ArrayList<Word> allWords = new ArrayList<Word>();


    //The name of the word
    private String word;

    //How recently the word appeared on the screen
    private int staleCount;

    //Product of staleCount and mastery- used to order the arraylist
    private int priority;

    //definition of the word
    private String definition;

    //This is the assumed mastery of the word- based on if the user asks for the definition or not
    private int mastery;


    /**
     *
     * @param newWord name of the word in question
     * @param newDefinition definition of the word in question
     */
    public Word(String newWord, String newDefinition, int mast) {
        word = newWord;
        definition = newDefinition;
        mastery = mast;
        staleCount = 1;
        priority = mast;
    }

    /**
     *
     * @return returns the name of the word
     */
    public String getWord() {
        return word;
    }

    /**
     *
     * @return returns the definition of the word
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * @return the mastery of a word
     */
    public int getMastery() {
        return mastery;
    }

    /**
     * Increases the mastery of a word and updates priority
     */
    public void incMastery() {
        mastery++;
        updatePriority();
    }

    /**
     * Decreases the mastery of a word (can't go below 1) and updates priority
     */
    public void decMastery() {
        if (mastery == 1) {
            return;
        } else {
            mastery--;
            updatePriority();
        }
    }

    /**
     * Decreases the staleCount of a word (can't go below 1)
     */
    public void decStale() {
        if (staleCount == 1) {
            return;
        } else {
            staleCount--;
            updatePriority();
        }
    }

    /**
     * @param other The Word to be searched
     * @return The index of the word, -1 if not found
     */
    public static int find(Word other) {
        for (int i = 0; i < allWords.size(); i++) {
            if (allWords.get(i).equals(other)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * function used in initialization
     * @param value new value for priority
     */
    public void pUpdate(int value) {
        priority = value;
    }



    /**
     * Updates the priority of a word
     * Priority is the product of the mastery and staleCount
     */
    private void updatePriority() {
        priority = mastery*staleCount;
    }

    /**
     * @return The priority of a word
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sorts the Arraylist of words by their priority
     * Decrements the stale count of all of the words before sorting
     */
    public static void updateWords() {
        decAllStale();
        for (int i = 0; i < allWords.size(); i++) {
            allWords.get(i).updatePriority();
        }
        Collections.sort(allWords);
    }

    /**
     * @param newStaleCount sets StaleCount
     */
    public void setStaleCount(int newStaleCount) {
        staleCount = newStaleCount;
    }

    /**
     *
     * @param other object to be compared to
     * @return int - positive if priority of first is greater than second
     * negative if second is greater than first
     * 0 if equal
     */
    public int compareTo(Object other) {
        if (!(other instanceof Word)) {
            return -1;
        }
        Word wOther = (Word) other;
        if (priority > wOther.getPriority()) {
            return 1;
        } else if (priority < wOther.getPriority()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     *
     * @param other an object
     * @return returns a boolean depending if the two objects are equal
     */
    public boolean equals(Object other) {
        if (!(other instanceof Word)) {
            return false;
        } else {
            if (((Word) other).getWord().equals(word) && ((Word) other).getDefinition().equals(definition)){
                return true;
            }
            return false;
        }
    }

    /**
     * https://stackoverflow.com/questions/5969447/java-random-integer-with-non-uniform-distribution
     * @param maxSize Maximum size of index number
     * @return a random non-uniformly distributed number between 1 and max number
     * It is a linear distribution- may want to update to be a quadratic in the future
     */
    public static int getLinearRandomNumber(int maxSize){
        //Get a linearly multiplied random number
        int randomMultiplier = maxSize * (maxSize + 1) / 2;
        Random r=new Random();
        int randomInt = r.nextInt(randomMultiplier);

        //Linearly iterate through the possible values to find the correct one
        int linearRandomNumber = 0;
        for(int i=maxSize; randomInt >= 0; i--){
            randomInt -= i;
            linearRandomNumber++;
        }

        return linearRandomNumber;
    }

    /**
     * Decrements the staleCount of all words
     */
    public static void decAllStale() {
        for (int i = 0; i < allWords.size(); i++) {
            allWords.get(i).decStale();
        }
    }

    /**
     * A function used for debugging and Log messages.
     * @return A string of all of the priorities of the words
     */
    public static String printPriorities() {
        String toRet = "";
        for (int i = 0; i < allWords.size(); i++) {
            toRet += (Integer.toString(allWords.get(i).getPriority()) + ", ");
        }
        return toRet;
    }


    /**
     * Pulls 5 random words using the non-uniform probability distribution.
     * This allows words with a lower priority to appear on the screen more often.
     * @return A list of 5 words to be displayed on the screen.
     */
    public static List<Word> pullRandom() {
        Log.d(TAG, "pullRandom: entered");
        //updates words (sorts Allwords and decrements stalecount of all words)
        updateWords();
        Log.d(TAG, printPriorities());
        Log.d(TAG, "pullRandom: Updated words");

        List<Word> toRet = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            int rand = getLinearRandomNumber(allWords.size()) - 1;

            Log.d(TAG, "pullRandom: random number = " + rand);

            //Makes sure no duplicates appear on the screen
            if (toRet.contains(allWords.get(rand))) {
                i--;
            } else {
                toRet.add(allWords.get(rand));
                allWords.get(rand).setStaleCount(6);
                allWords.get(rand).incMastery();
            }
        }
        Log.d(TAG, "pullRandom: Exiting loop");
        return toRet;
    }
}
