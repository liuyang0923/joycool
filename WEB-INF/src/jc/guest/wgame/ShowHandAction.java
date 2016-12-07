/*
 * Created on 2006-1-11
 *
 */
package jc.guest.wgame;

import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import jc.guest.GuestHallAction;
import jc.guest.GuestUserInfo;
import jc.guest.wgame.Cards;
import jc.guest.wgame.ShowHandBean;

import net.joycool.wap.bean.wgame.CardBean;
import net.joycool.wap.bean.wgame.HistoryBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.util.StringUtil;

/**
 * @author lbj
 *  
 */
public class ShowHandAction extends WGameAction {
    public String shSessionName = "showhand";

    public String winsSessionName = "showhandWins";

    public int NUMBER_PAGE = 10;

    /**
     * 取得系统牌
     * 
     * @param cards
     * @return
     */
    public Vector getNewCards(Cards cards, Vector originCards, boolean isOpen) {
        if (originCards == null) {
            originCards = new Vector();
        }
        CardBean card = cards.getCard();
        card.setOpen(isOpen);
        originCards.add(card);
        return originCards;
    }

    /**
     * 处理下注
     * 
     * @param request
     */
    public void deal1(HttpServletRequest request,GuestUserInfo guestUser) {
        if (guestUser == null) {
            return;
        }
//		fanys2006-08-11
//        UserStatusBean status = getUserStatus(loginUser.getId());

        String tip = null;
        String result = "success";
        //取得参数
        int wager = StringUtil.toInt(request.getParameter("wager"));
        if (wager <= 0) {
            tip = "赌注不能小于等于零!";
            result = "failure";
        } else if (wager > guestUser.getMoney()) {
            tip = "您的游币不够了!";
            result = "failure";
        } else if (wager < 100) {
            tip = "梭哈一局最少10游币!";
            result = "failure";
        } else if (wager > 10000) {
            tip = "乐酷提示:您最多能下注10000个游币!";
            result = "failure";
        }

        //有错
        if ("failure".equals(result)) {
            request.setAttribute("tip", tip);
            request.setAttribute("result", result);
            return;
        } else {
            //选择美女
            if (getSessionObject(request, winsSessionName) == null) {
                int girlId = StringUtil.toInt(request.getParameter("girlId"));
                if (girlId <= 0) {
                    girlId = 1;
                }
                WGameBean wins = new WGameBean();
                wins.setGirlId(girlId);
                setSessionObject(request, winsSessionName, wins);
            }

            ShowHandBean sh = new ShowHandBean();
            sh.setMaxWager(wager);
            sh.setCards(new Cards(1));
            Vector systemCards = getNewCards(sh.getCards(), null, false);
            Vector userCards = getNewCards(sh.getCards(), null, false);
            sh.setSystemCards(systemCards);
            sh.setUserCards(userCards);
            sendCard(sh);

            sh.setRound(1);
            sh.setTotalWager(5);

            //系统下注
            if (compareOpen(sh.getSystemCards(), sh.getUserCards()) > 0) {
                int rw = (sh.getMaxWager() / 40) * 10;
                sh.setRoundWager(rw);
                sh.setFlag(0);
            }
            //用户下注
            else {
                sh.setFlag(1);
            }

            setSessionObject(request, shSessionName, sh);
            request.setAttribute("result", result);
            return;
        }
    }

    /**
     * 第一轮
     * 
     * @param request
     */
    public void round1(HttpServletRequest request) {
        String action = request.getParameter("action");
        if (action == null) {
            return;
        }

        //放弃
        if (action.equals("quit")) {
            request.setAttribute("next", "quit");
            return;
        }

        ShowHandBean sh = (ShowHandBean) getSessionObject(request,
                shSessionName);
        if (sh == null) {
            return;
        }
        //跟牌
        if (action.equals("follow")) {
            sh.setTotalWager(sh.getTotalWager() + sh.getRoundWager());
            sh.setRoundWager(0);
            sendCard(sh);

            //系统下注
            if (compareOpen(sh.getSystemCards(), sh.getUserCards()) > 0) {
                int rw = (sh.getMaxWager() / 20) * 10;
                sh.setRoundWager(rw);
                sh.setFlag(0);
            }
            //用户下注
            else {
                sh.setFlag(1);
            }

            //到第二轮
            request.setAttribute("next", "continue");
            return;
        }
        //下注
        else if (action.equals("add")) {
            String tip = null;
            int roundMaxWager = sh.getMaxWager() / 4;
            int wager = StringUtil.toInt(request.getParameter("wager"));
            if (wager < 0) {
                tip = "您的下注不对!";
            } else if (wager > roundMaxWager) {
                tip = "这轮最多能下注" + roundMaxWager + "个游币!";
            }
            if (sh.getRoundWager() != 0) {
                if (wager < sh.getRoundWager()) {
                    tip = "你下的注不能小于系统下的注!";
                }
            }
            if (tip != null) {
                request.setAttribute("tip", tip);
                return;
            }

            sh.setTotalWager(sh.getTotalWager() + wager);
            sh.setRoundWager(0);
            sendCard(sh);

            //系统下注
            if (compareOpen(sh.getSystemCards(), sh.getUserCards()) > 0) {
                int rw = (sh.getMaxWager() / 20) * 10;
                sh.setRoundWager(rw);
                sh.setFlag(0);
            }
            //用户下注
            else {
                sh.setFlag(1);
            }

            //到第二轮
            request.setAttribute("next", "continue");
            return;
        }

    }

    /**
     * 第二轮
     * 
     * @param request
     */
    public void round2(HttpServletRequest request) {
        String action = request.getParameter("action");
        if (action == null) {
            return;
        }

        //放弃
        if (action.equals("quit")) {
            request.setAttribute("next", "quit");
            return;
        }

        ShowHandBean sh = (ShowHandBean) getSessionObject(request,
                shSessionName);
        if (sh == null) {
            return;
        }
        //跟牌
        if (action.equals("follow")) {
            sh.setTotalWager(sh.getTotalWager() + sh.getRoundWager());
            sh.setRoundWager(0);
            sendCard(sh);

            //系统下注
            if (compareOpen(sh.getSystemCards(), sh.getUserCards()) > 0) {
                int rw = (sh.getMaxWager() - sh.getTotalWager()) / 2;
                if (rw == 0) {
                    rw += 1;
                }
                sh.setRoundWager(rw);
                sh.setFlag(0);
            }
            //用户下注
            else {
                sh.setFlag(1);
            }

            //到第三轮
            request.setAttribute("next", "continue");
            return;
        }
        //下注
        else if (action.equals("add")) {
            String tip = null;
            int roundMaxWager = sh.getMaxWager() / 2;
            if (roundMaxWager == 0) {
                roundMaxWager += 1;
            }
            int wager = StringUtil.toInt(request.getParameter("wager"));
            if (wager < 0) {
                tip = "您的下注不对!";
            } else if (wager > roundMaxWager) {
                tip = "这轮最多能下注" + roundMaxWager + "个游币!";
            }
            if (sh.getRoundWager() != 0) {
                if (wager < sh.getRoundWager()) {
                    tip = "你下的注不能小于系统下的注!";
                }
            }
            if (tip != null) {
                request.setAttribute("tip", tip);
                return;
            }

            sh.setTotalWager(sh.getTotalWager() + wager);
            sh.setRoundWager(0);
            sendCard(sh);

            //系统下注
            if (compareOpen(sh.getSystemCards(), sh.getUserCards()) > 0) {
                int rw = (sh.getMaxWager() - sh.getTotalWager()) / 2;
                if (rw == 0) {
                    rw += 1;
                }
                sh.setRoundWager(rw);
                sh.setFlag(0);
            }
            //用户下注
            else {
                sh.setFlag(1);
            }

            //到第三轮
            request.setAttribute("next", "continue");
            return;
        }

    }

    /**
     * 第三轮
     * 
     * @param request
     */
    public void round3(HttpServletRequest request) {
        String action = request.getParameter("action");
        if (action == null) {
            return;
        }

        //放弃
        if (action.equals("quit")) {
            request.setAttribute("next", "quit");
            return;
        }

        ShowHandBean sh = (ShowHandBean) getSessionObject(request,
                shSessionName);
        if (sh == null) {
            return;
        }
        //跟牌
        if (action.equals("follow")) {
            sh.setTotalWager(sh.getTotalWager() + sh.getRoundWager());
            sendCard(sh);
            sh.setRoundWager(0);

            ((CardBean) sh.getSystemCards().get(sh.getSystemCards().size() - 1))
                    .setOpen(false);

            //到第四轮前开牌
            request.setAttribute("next", "continue");
            return;
        }
        //下注
        else if (action.equals("add")) {
            String tip = null;
            int roundMaxWager = sh.getMaxWager() - sh.getTotalWager();
            int wager = StringUtil.toInt(request.getParameter("wager"));
            if (wager < 0) {
                tip = "您的下注不对!";
            } else if (wager > roundMaxWager) {
                tip = "这轮最多能下注" + roundMaxWager + "个游币!";
            }
            if (sh.getRoundWager() != 0) {
                if (wager < sh.getRoundWager()) {
                    tip = "你下的注不能小于系统下的注!";
                }
            }
            if (tip != null) {
                request.setAttribute("tip", tip);
                return;
            }

            sh.setTotalWager(sh.getTotalWager() + wager);
            sendCard(sh);
            sh.setRoundWager(0);

            ((CardBean) sh.getSystemCards().get(sh.getSystemCards().size() - 1))
                    .setOpen(false);

            //到第四轮
            request.setAttribute("next", "continue");
            return;
        }

    }

    /**
     * 第四轮前开牌
     * 
     * @param request
     */
    public void round40(HttpServletRequest request) {
        String action = request.getParameter("action");
        if (action == null) {
            return;
        }

        ShowHandBean sh = (ShowHandBean) getSessionObject(request,
                shSessionName);
        if (sh == null) {
            return;
        }

        ((CardBean) sh.getSystemCards().get(sh.getSystemCards().size() - 1))
                .setOpen(true);
        //开第一张
        if (action.equals("open0")) {
            CardBean card0 = (CardBean) sh.getUserCards().get(0);
            card0.setOpen(true);
            sh.getUserCards().remove(0);
            sh.getUserCards().add(card0);
        }
        //下注
        else if (action.equals("open4")) {
            ((CardBean) sh.getUserCards().get(sh.getUserCards().size() - 1))
                    .setOpen(true);
        }

        //系统下注
        if (compareOpen(sh.getSystemCards(), sh.getUserCards()) > 0) {
            int rw = (sh.getMaxWager() - sh.getTotalWager()) / 2;
            if (rw == 0) {
                rw += 1;
            }
            sh.setRoundWager(rw);
        }
        //用户下注
        else {
            sh.setFlag(1);
        }

        //到第四轮
        request.setAttribute("next", "continue");
        return;
    }

    /**
     * 第四轮
     * 
     * @param request
     */
    public void round4(HttpServletRequest request) {
        String action = request.getParameter("action");
        if (action == null) {
            return;
        }

        //放弃
        if (action.equals("quit")) {
            request.setAttribute("next", "quit");
            return;
        }

        ShowHandBean sh = (ShowHandBean) getSessionObject(request,
                shSessionName);
        if (sh == null) {
            return;
        }
        //跟牌
        if (action.equals("follow")) {
            sh.setTotalWager(sh.getTotalWager() + sh.getRoundWager());
            sh.setRoundWager(0);

            //到结束
            request.setAttribute("next", "continue");
            return;
        }
        //下注
        else if (action.equals("add")) {
            String tip = null;
            int roundMaxWager = sh.getMaxWager() - sh.getTotalWager();
            int wager = StringUtil.toInt(request.getParameter("wager"));
            if (wager < 0) {
                tip = "您的下注不对!";
            } else if (wager > roundMaxWager) {
                tip = "这轮最多能下注" + roundMaxWager + "个游币!";
            }
            if (sh.getRoundWager() != 0) {
                if (wager < sh.getRoundWager()) {
                    tip = "你下的注不能小于系统下的注!";
                }
            }
            if (tip != null) {
                request.setAttribute("tip", tip);
                return;
            }

            sh.setTotalWager(sh.getTotalWager() + wager);
            sh.setRoundWager(0);

            //到结束
            request.setAttribute("next", "continue");
            return;
        }

    }

    /**
     * 结束
     * 
     * @param request
     */
    public void end(HttpServletRequest request,GuestUserInfo guestUser) {
        ShowHandBean sh = (ShowHandBean) getSessionObject(request,
                shSessionName);
        if (sh == null) {
            return;
        }
        ((CardBean) sh.getSystemCards().get(0)).setOpen(true);

        if (guestUser == null) {
            return;
        }

        String result = null;

        //赢了
        if (compareCards(sh.getUserCards(), sh.getSystemCards()) > 0) {
            WGameBean wgame = (WGameBean) getSessionObject(request,
                    winsSessionName);
            if (wgame != null) {
                wgame.setWins(wgame.getWins() + 1);
            }
            
            // 加钱
			GuestHallAction.updateMoney(guestUser.getId(), sh.getTotalWager());
			//记录
            HistoryBean history = new HistoryBean();
            history.setUserId(guestUser.getId());
            history.setGameType(WGameBean.GT_DC);
            history.setGameId(WGameBean.DC_SHOWHAND);
            history.setWinCount(1);
            history.setMoney(sh.getTotalWager());
            updateHistory(history);
            result = "win";
        } else {
        	// 减钱
        	GuestHallAction.updateMoney(guestUser.getId(), 0-sh.getTotalWager());
			//mcq_end
            HistoryBean history = new HistoryBean();
            history.setUserId(guestUser.getId());
            history.setGameType(WGameBean.GT_DC);
            history.setGameId(WGameBean.DC_SHOWHAND);
            history.setLoseCount(1);
            history.setMoney(-sh.getTotalWager());
            updateHistory(history);

            result = "lose";
        }

        request.setAttribute("result", result);
        request.setAttribute("showhand", sh);
        request.getSession().removeAttribute(shSessionName);
    }

    /**
     * 放弃
     * 
     * @param request
     */
    public void quit(HttpServletRequest request,GuestUserInfo guestUser) {
        //request.getSession().removeAttribute(winsSessionName);

        ShowHandBean sh = (ShowHandBean) getSessionObject(request,
                shSessionName);
        if (sh == null) {
            return;
        }
        ((CardBean) sh.getSystemCards().get(0)).setOpen(true);

        if (guestUser == null) {
            return;
        }

        String result = null;

        // 扣钱
        GuestHallAction.updateMoney(guestUser.getId(), 0-sh.getTotalWager());
        HistoryBean history = new HistoryBean();
        history.setUserId(guestUser.getId());
        history.setGameType(WGameBean.GT_DC);
        history.setGameId(WGameBean.DC_SHOWHAND);
        history.setLoseCount(1);
        history.setMoney(-sh.getTotalWager());
        updateHistory(history);

        result = "lose";

        request.setAttribute("result", result);
        request.setAttribute("showhand", sh);
        request.getSession().removeAttribute(shSessionName);
    }

    public int compareCards(Vector cards1, Vector cards2) {
        if (cards1 == null || cards1.size() < 5 || cards2 == null
                || cards2.size() < 5) {
            return 0;
        }
        int t1 = getCardsType(cards1);
        int t2 = getCardsType(cards2);
        if (t1 != t2) {
            return t1 - t2;
        }

        CardBean card11 = (CardBean) cards1.get(0);
        CardBean card12 = (CardBean) cards1.get(1);
//        CardBean card13 = (CardBean) cards1.get(2);
//        CardBean card14 = (CardBean) cards1.get(3);
//        CardBean card15 = (CardBean) cards1.get(4);

        CardBean card21 = (CardBean) cards2.get(0);
        CardBean card22 = (CardBean) cards2.get(1);
//        CardBean card23 = (CardBean) cards2.get(2);
//        CardBean card24 = (CardBean) cards2.get(3);
//        CardBean card25 = (CardBean) cards2.get(4);

        //同花顺
        if (t1 == 9) {
            return compareCard(card11, card21);
        }
        //铁支
        if (t1 == 8) {
            return compareCard(card11, card21);
        }
        //葫芦
        if (t1 == 7) {
            return compareCard(card11, card21);
        }
        //同花
        if (t1 == 6) {
            return compareCard(card11, card21);
        }
        //顺子
        if (t1 == 5) {
            return compareCard(card11, card21);
        }
        //三条
        if (t1 == 4) {
            return compareCard(card11, card21);
        }
        //两对
        if (t1 == 3) {
            if (card11.getSHValue() != card21.getSHValue()) {
                return card11.getSHValue() - card21.getSHValue();
            } else {
                if (card12.getSHValue() != card22.getSHValue()) {
                    return card12.getSHValue() - card22.getSHValue();
                } else {
                    return card11.getType() - card21.getType();
                }
            }
        }
        if (t1 == 2) {
            return compareCard(card11, card21);
        }
        //单牌
        return compareCard(card11, card21);

    }

    /**
     * 取得牌型。
     * 
     * @param cards
     * @return
     */
    public int getCardsType(Vector cards) {
        if (cards == null || cards.size() < 5) {
            return 0;
        }
        //排序
        orderCards(cards);

        CardBean card1 = (CardBean) cards.get(0);
        CardBean card2 = (CardBean) cards.get(1);
        CardBean card3 = (CardBean) cards.get(2);
        CardBean card4 = (CardBean) cards.get(3);
        CardBean card5 = (CardBean) cards.get(4);

        //同花
        if (card1.getType() == card2.getType()
                && card1.getType() == card3.getType()
                && card1.getType() == card4.getType()
                && card1.getType() == card5.getType()) {
            //同花顺
            if (card1.getSHValue() == card2.getSHValue() + 1
                    && card1.getSHValue() == card3.getSHValue() + 2
                    && card1.getSHValue() == card4.getSHValue() + 3
                    && card1.getSHValue() == card5.getSHValue() + 4) {
                return 9;
            }
            //普通同花
            else {
                return 6;
            }
        }
        //铁支
        if (card1.getSHValue() == card2.getSHValue()
                && card1.getSHValue() == card3.getSHValue()
                && card1.getSHValue() == card4.getSHValue()) {
            return 8;
        } else if (card2.getSHValue() == card3.getSHValue()
                && card2.getSHValue() == card4.getSHValue()
                && card2.getSHValue() == card5.getSHValue()) {
            cards.add(card1);
            cards.remove(0);
            return 8;
        }
        //葫芦
        if (card1.getSHValue() == card2.getSHValue()
                && card1.getSHValue() == card3.getSHValue()
                && card4.getSHValue() == card5.getSHValue()) {
            return 7;
        } else if (card1.getSHValue() == card2.getSHValue()
                && card3.getSHValue() == card4.getSHValue()
                && card3.getSHValue() == card5.getSHValue()) {
            cards.add(card1);
            cards.add(card2);
            cards.remove(0);
            cards.remove(0);
            return 7;
        }
        //顺子
        if (card1.getSHValue() == card2.getSHValue() + 1
                && card1.getSHValue() == card3.getSHValue() + 2
                && card1.getSHValue() == card4.getSHValue() + 3
                && card1.getSHValue() == card5.getSHValue() + 4) {
            return 5;
        }
        //三条
        if (card1.getSHValue() == card2.getSHValue()
                && card1.getSHValue() == card3.getSHValue()) {
            return 4;
        } else if (card2.getSHValue() == card3.getSHValue()
                && card3.getSHValue() == card4.getSHValue()) {
            cards.insertElementAt(card1, 4);
            cards.remove(0);
            return 4;
        } else if (card3.getSHValue() == card4.getSHValue()
                && card4.getSHValue() == card5.getSHValue()) {
            cards.add(card1);
            cards.add(card2);
            cards.remove(0);
            cards.remove(0);
            return 4;
        }
        //两对
        if (card1.getSHValue() == card2.getSHValue()
                && card3.getSHValue() == card4.getSHValue()) {
            return 3;
        } else if (card2.getSHValue() == card3.getSHValue()
                && card4.getSHValue() == card5.getSHValue()) {
            cards.add(card1);
            cards.remove(0);
            return 3;
        } else if (card1.getSHValue() == card2.getSHValue()
                && card4.getSHValue() == card5.getSHValue()) {
            cards.add(card3);
            cards.remove(2);
            return 3;
        }
        //对子
        if (card1.getSHValue() == card2.getSHValue()) {
            return 2;
        } else if (card2.getSHValue() == card3.getSHValue()) {
            cards.insertElementAt(card1, 3);
            cards.remove(0);
            return 2;
        } else if (card3.getSHValue() == card4.getSHValue()) {
            cards.insertElementAt(card2, 4);
            cards.insertElementAt(card1, 4);
            cards.remove(0);
            cards.remove(0);
            return 2;
        } else if (card4.getSHValue() == card5.getSHValue()) {
            cards.add(card1);
            cards.add(card2);
            cards.add(card3);
            cards.remove(0);
            cards.remove(0);
            cards.remove(0);
            return 2;
        }

        return 1;
    }

    /**
     * 从大到小排序。
     * 
     * @param cards
     */
    public void orderCards(Vector cards) {
        if (cards == null || cards.size() < 5) {
            return;
        }

        Vector newCards = new Vector();
        newCards.addAll(cards);
        CardBean card = max(newCards);
        while (card != null) {
            cards.add(card);
            newCards.remove(card);
            card = max(newCards);
        }
        cards.remove(0);
        cards.remove(0);
        cards.remove(0);
        cards.remove(0);
        cards.remove(0);
    }

    public CardBean max(Vector cards) {
        if (cards.size() == 0) {
            return null;
        }
        CardBean maxCard = null;
        CardBean card = null;
        Iterator itr = cards.iterator();
        while (itr.hasNext()) {
            card = (CardBean) itr.next();
            if (maxCard == null) {
                maxCard = card;
            } else if (compareCard(maxCard, card) < 0) {
                maxCard = card;
            }
        }
        return maxCard;
    }

    /**
     * 发牌。
     * 
     * @param sh
     */
    public void sendCard(ShowHandBean sh) {
        Vector systemCards = sh.getSystemCards();
        Vector userCards = sh.getUserCards();
        CardBean card = null;
        card = (CardBean) sh.getCards().getCard();
        card.setOpen(true);
        systemCards.add(card);
        card = (CardBean) sh.getCards().getCard();
        card.setOpen(true);
        userCards.add(card);
        sh.setRound(sh.getRound() + 1);
    }

    /**
     * 判断明牌谁大。
     * 
     * @param systemCards
     * @param userCards
     * @return
     */
    public int compareOpen(Vector systemCards, Vector userCards) {
        CardBean card1 = (CardBean) systemCards.get(systemCards.size() - 1);
        CardBean card2 = (CardBean) userCards.get(userCards.size() - 1);
        return compareCard(card1, card2);
    }

    /**
     * 比较牌大。
     * 
     * @param card1
     * @param card2
     * @return
     */
    public int compareCard(CardBean card1, CardBean card2) {
        int v1 = card1.getValue() == 1 ? 14 : card1.getValue();
        int v2 = card2.getValue() == 1 ? 14 : card2.getValue();
        if (v1 == v2) {
            return card1.getType() - card2.getType();
        } else {
            return v1 - v2;
        }
    }

//    public static void main(String[] args) {
//        ShowHandAction toa = new ShowHandAction();
//    }
    
    /**
     * @param request
     */
    public void history(HttpServletRequest request,GuestUserInfo guestUser) {
        if (guestUser == null) {
            return;
        }

        HistoryBean history = getHistory(guestUser.getId(), WGameBean.GT_DC,
                WGameBean.DC_SHOWHAND);
        request.setAttribute("history", history);
    }
}
