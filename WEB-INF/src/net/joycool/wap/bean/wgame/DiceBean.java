/*
 * Created on 2006-1-10
 *
 */
package net.joycool.wap.bean.wgame;

/**
 * @author lbj
 *
 */
public class DiceBean {
    int wager;
    int guess;
    
    
    /**
     * @return Returns the guess.
     */
    public int getGuess() {
        return guess;
    }
    /**
     * @param guess The guess to set.
     */
    public void setGuess(int guess) {
        this.guess = guess;
    }
    /**
     * @return Returns the wager.
     */
    public int getWager() {
        return wager;
    }
    /**
     * @param wager The wager to set.
     */
    public void setWager(int wager) {
        this.wager = wager;
    }
}
