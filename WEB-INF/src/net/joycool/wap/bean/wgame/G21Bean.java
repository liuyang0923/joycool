/*
 * Created on 2006-1-11
 *
 */
package net.joycool.wap.bean.wgame;

import java.util.Vector;

/**
 * @author lbj
 *
 */
public class G21Bean {
    int wager;
    Vector systemCards;
    Vector userCards;
    Cards cards;
    
    
        
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
