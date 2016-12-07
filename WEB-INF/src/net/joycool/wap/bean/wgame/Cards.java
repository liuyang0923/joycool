/*
 * Created on 2006-1-11
 *
 */
package net.joycool.wap.bean.wgame;

import java.util.Vector;

import net.joycool.wap.util.RandomUtil;

/**
 * @author lbj
 *  
 */
public class Cards {
    public Vector cards;

    public Cards() {
        init();
    }

    public Cards(int showhand) {
        initShowhand();
    }

    public void init() {
        cards = new Vector();
        CardBean card = null;
        for (int i = 1; i <= 13; i++) {
            for (int j = 1; j <= 4; j++) {
                card = new CardBean(i, j,
                        "../cardImg/" + i + "_" + j
                                + ".gif");
                cards.add(card);
            }
        }
    }

    public void initShowhand() {
        cards = new Vector();
        CardBean card = null;
        for (int i = 7; i <= 13; i++) {
            for (int j = 1; j <= 4; j++) {
                card = new CardBean(i % 13 + 1, j,
                        "../cardImg/" + (i % 13 + 1)
                                + "_" + j + ".gif");
                cards.add(card);
            }
        }
    }

    public CardBean getCard() {
        if (cards == null || cards.size() == 0) {
            init();
        }

        int count = cards.size();
        int index = RandomUtil.nextInt(count);
        CardBean card = (CardBean) cards.get(index);

        cards.remove(index);
        return card;
    }
}
