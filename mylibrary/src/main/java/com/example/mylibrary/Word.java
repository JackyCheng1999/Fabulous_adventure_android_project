package com.example.mylibrary;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Word implements Comparable {
    public static String username;
    static String TAG = "brett Word";

    //Pulled from https://www.prepscholar.com/toefl/blog/wp-content/uploads/sites/13/2017/08/TOEFL-Vocab-Word-List.pdf
    public static ArrayList<Word> allWords = new ArrayList<Word>(){
        {
            add(new Word("abundant","adj. present in large quantities"));
            add(new Word("accumulate","v. to gradually collect"));
            add(new Word("berate","v. to scold"));
            add(new Word("bestow","v. to give as a gift"));
            add(new Word("cease","v. to stop"));
            add(new Word("clarify","v. to make clear; to remove confusion"));
            add(new Word("cohesion","n. uniting; becoming one"));
            add(new Word("core","adj. central; of main importance"));
            add(new Word("decay","v. to decline in health or excellence"));
            add(new Word("demonstrate","v. to show"));
            add(new Word("diatribe","n. a sharp criticism or attack"));
            add(new Word("divert","v. to cause a change of course"));
            add(new Word("efficient","adj. maximizing productivity"));
            add(new Word("eloquent","adj. moving speech or writing"));
            add(new Word("evade","v. to avoid or escape"));
            add(new Word("exclusive","adj. not admitting the majority"));
            add(new Word("feasible","adj. possible to do"));
            add(new Word("flaw","n. a feature that ruins the perfection of something"));
            add(new Word("fortify","v. to strengthen"));
            add(new Word("frivolous","adj. unnecessary; of little importance"));
            add(new Word("garbled","adj. communication that is distorted and unclear"));
            add(new Word("grandiose","adj. pompous; overly important"));
            add(new Word("haphazard","adj. lacking planning"));
            add(new Word("hesitate","v. to pause, often due to reluctance"));
            add(new Word("hostile","adj. extremely unfriendly"));
            add(new Word("illiterate","adj. unable to read"));
            add(new Word("impact","n. effect or influence"));
            add(new Word("imply","v. to strongly suggest"));
            add(new Word("incessant","adj. continuing without pause"));
            add(new Word("inclination","n. a preference"));
            add(new Word("indefatigable","adj. untiring"));
            add(new Word("inhibit","v. to hinder or restrain"));
            add(new Word("interpret","v. to explain the meaning of something"));
            add(new Word("jargon","n. words specific to a certain job or group"));
            add(new Word("knack","n. a special talent or skill"));
            add(new Word("lag","v. to fall behind"));
            add(new Word("leery","adj. wary"));
            add(new Word("lenient","adj. merciful; less harsh"));
            add(new Word("major","adj. very important"));
            add(new Word("mend","v. to fix"));
            add(new Word("migrate","v. to move from one place to another"));
            add(new Word("morose","adj. gloomy, depressed"));
            add(new Word("nonchalant","adj. indifferent, unexcited"));
            add(new Word("obtain","v. to get"));
            add(new Word("oppress","v. to unfairly burden"));
            add(new Word("peak","n. the highest or most important point"));
            add(new Word("pertain","v. to relate"));
            add(new Word("prior","adj. previous or earlier"));
            add(new Word("proximity","n. nearness in time or space"));
            add(new Word("rank","n. an official position or station"));
            add(new Word("recapitulate","to give a brief summary"));
            add(new Word("reinforce","v. to strengthen with added support"));
            add(new Word("resist","v. to withstand the effect of"));
            add(new Word("scrutinize","v. to very carefully examine"));
            add(new Word("sparse","adj. thinly scattered"));
            add(new Word("squalid","adj. filthy and unpleasant"));
        }
    };


    private String word;
    private int staleCount;
    private int priority;
    private String definition;
    private int mastery;

    public Word(String newWord, String newDefinition) {
        word = newWord;
        definition = newDefinition;
        mastery = 1;
        staleCount = 1;
        priority = 1;
    }

    public String getWord() {
        return word;
    }

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
     * Decreases the mastery of a word (can't go below zero) and updates priority
     */
    public void decMastery() {
        if (mastery == 1) {
            return;
        } else {
            mastery--;
            updatePriority();
        }
    }

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
     * Sorts the Arraylist of words
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
     */
    public static int getLinnearRandomNumber(int maxSize){
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

    public static void decAllStale() {
        for (int i = 0; i < allWords.size(); i++) {
            allWords.get(i).decStale();
        }
    }

    public static String printPriorities() {
        String toRet = "";
        for (int i = 0; i < allWords.size(); i++) {
            toRet += (Integer.toString(allWords.get(i).getPriority()) + ", ");
        }
        return toRet;
    }

    public static List<Word> pullRandom() {
        Log.d(TAG, "pullRandom: entered");
        updateWords();
        Log.d(TAG, printPriorities());
        Log.d(TAG, "pullRandom: Udpated words");

        List<Word> toRet = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            int rand = getLinnearRandomNumber(allWords.size()) - 1;

            Log.d(TAG, "pullRandom: random number = " + rand);

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
