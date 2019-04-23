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
    public static String username;

    //tag for log messages
    static String TAG = "brett Word";

    //Pulled from https://www.prepscholar.com/toefl/blog/wp-content/uploads/sites/13/2017/08/TOEFL-Vocab-Word-List.pdf
    //This is the default class arraylist of words
    public static ArrayList<Word> allWords = new ArrayList<Word>();

    /*
    add(new Word("abundant","adj. present in large quantities", 1));
            add(new Word("accumulate","v. to gradually collect", 1));
            add(new Word("berate","v. to scold", 1));
            add(new Word("bestow","v. to give as a gift", 1));
            add(new Word("cease","v. to stop", 1));
            add(new Word("clarify","v. to make clear; to remove confusion", 1));
            add(new Word("cohesion","n. uniting; becoming one", 1));
            add(new Word("core","adj. central; of main importance", 1));
            add(new Word("decay","v. to decline in health or excellence", 1));
            add(new Word("demonstrate","v. to show", 1));
            add(new Word("diatribe","n. a sharp criticism or attack", 1));
            add(new Word("divert","v. to cause a change of course", 1));
            add(new Word("efficient","adj. maximizing productivity", 1));
            add(new Word("eloquent","adj. moving speech or writing", 1));
            add(new Word("evade","v. to avoid or escape", 1));
            add(new Word("exclusive","adj. not admitting the majority", 1));
            add(new Word("feasible","adj. possible to do", 1));
            add(new Word("flaw","n. a feature that ruins the perfection of something", 1));
            add(new Word("fortify","v. to strengthen", 1));
            add(new Word("frivolous","adj. unnecessary; of little importance", 1));
            add(new Word("garbled","adj. communication that is distorted and unclear", 1));
            add(new Word("grandiose","adj. pompous; overly important", 1));
            add(new Word("haphazard","adj. lacking planning", 1));
            add(new Word("hesitate","v. to pause, often due to reluctance", 1));
            add(new Word("hostile","adj. extremely unfriendly", 1));
            add(new Word("illiterate","adj. unable to read", 1));
            add(new Word("impact","n. effect or influence", 1));
            add(new Word("imply","v. to strongly suggest", 1));
            add(new Word("incessant","adj. continuing without pause", 1));
            add(new Word("inclination","n. a preference", 1));
            add(new Word("indefatigable","adj. untiring", 1));
            add(new Word("inhibit","v. to hinder or restrain", 1));
            add(new Word("interpret","v. to explain the meaning of something", 1));
            add(new Word("jargon","n. words specific to a certain job or group", 1));
            add(new Word("knack","n. a special talent or skill", 1));
            add(new Word("lag","v. to fall behind", 1));
            add(new Word("leery","adj. wary", 1));
            add(new Word("lenient","adj. merciful; less harsh", 1));
            add(new Word("major","adj. very important", 1));
            add(new Word("mend","v. to fix", 1));
            add(new Word("migrate","v. to move from one place to another", 1));
            add(new Word("morose","adj. gloomy, depressed", 1));
            add(new Word("nonchalant","adj. indifferent, unexcited", 1));
            add(new Word("obtain","v. to get", 1));
            add(new Word("oppress","v. to unfairly burden", 1));
            add(new Word("peak","n. the highest or most important point", 1));
            add(new Word("pertain","v. to relate", 1));
            add(new Word("prior","adj. previous or earlier", 1));
            add(new Word("proximity","n. nearness in time or space", 1));
            add(new Word("rank","n. an official position or station", 1));
            add(new Word("recapitulate","to give a brief summary", 1));
            add(new Word("reinforce","v. to strengthen with added support", 1));
            add(new Word("resist","v. to withstand the effect of", 1));
            add(new Word("scrutinize","v. to very carefully examine", 1));
            add(new Word("sparse","adj. thinly scattered", 1));
            add(new Word("squalid","adj. filthy and unpleasant", 1));
     */

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
     * Updates the priority of a word
     * Priority is the product of the mastery and staleCount
     */
    private void updatePriority() {
        priority = mastery*staleCount;
    }

    /**
     *
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
        updateWords();
        Log.d(TAG, printPriorities());
        Log.d(TAG, "pullRandom: Udpated words");

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
