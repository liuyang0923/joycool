/*
 * Created on 2006-1-17
 *
 */
package net.joycool.wap.bean.wgame;

import java.util.Vector;

/**
 * @author lbj
 *
 */
public class ShowHandBean {
    int maxWager;
    Cards cards;
    Vector systemCards;
    Vector userCards;
    int totalWager;
    int roundWager;    
    int round;
    int flag;//0则系统下注，1则用户下注
    
    /**
     * @return Returns the flag.
     */
    public int getFlag() {
        return flag;
    }
    /**
     * @param flag The flag to set.
     */
    public void setFlag(int flag) {
        this.flag = flag;
    }
    /**
     * @return Returns the roundWager.
     */
    public int getRoundWager() {
        return roundWager;
    }
    /**
     * @param roundWager The roundWager to set.
     */
    public void setRoundWager(int roundWager) {
        this.roundWager = roundWager;
    }
    /**
     * @return Returns the cards.
     */
    public Cards getCards() {
        return cards;
    }
    /**
     * @param cards The cards to set.
     */
    public void setCards(Cards cards) {
        this.cards = cards;
    }
    /**
     * @return Returns the maxWager.
     */
    public int getMaxWager() {
        return maxWager;
    }
    /**
     * @param maxWager The maxWager to set.
     */
    public void setMaxWager(int maxWager) {
        this.maxWager = maxWager;
    }
    /**
     * @return Returns the round.
     */
    public int getRound() {
        return round;
    }
    /**
     * @param round The round to set.
     */
    public void setRound(int round) {
        this.round = round;
    }
    /**
     * @return Returns the systemCards.
     */
    public Vector getSystemCards() {
        return systemCards;
    }
    /**
     * @param systemCards The systemCards to set.
     */
    public void setSystemCards(Vector systemCards) {
        this.systemCards = systemCards;
    }
    /**
     * @return Returns the totalWager.
     */
    public int getTotalWager() {
        return totalWager;
    }
    /**
     * @param totalWager The totalWager to set.
     */
    public void setTotalWager(int totalWager) {
        this.totalWager = totalWager;
    }
    /**
     * @return Returns the userCards.
     */
    public Vector getUserCards() {
        return userCards;
    }
    /**
     * @param userCards The userCards to set.
     */
    public void setUserCards(Vector userCards) {
        this.userCards = userCards;
    }
}
