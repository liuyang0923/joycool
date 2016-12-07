/*
 * Created on 2006-1-11
 *
 */
package net.joycool.wap.action.wgamefree;

import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.wgame.CardBean;
import net.joycool.wap.bean.wgame.Cards;
import net.joycool.wap.bean.wgame.G21Bean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * @author lbj
 *  
 */
public class G21Action extends WGameAction {
    public String g21SessionName = "g21";

    public String winsSessionName = "g21Wins";

    public int NUMBER_PAGE = 10;

    /**
     * 取得系统牌
     * 
     * @param cards
     * @return
     */
    public Vector getSystemCards(Cards cards) {
        Vector cs = new Vector();
        int sum = 0;
        CardBean card = cards.getCard();
        cs.add(card);
        sum = getSum(cs);

        //随机数
        while (true) {
            //            System.out.println(card.getValue() + "_" + card.getType() + "\t"
            //                    + card.getPicUrl());
            //牌数已到五张
            if (cs.size() == 5) {
                break;
            }
            //10以下，肯定要牌
            else if (sum <= 10) {
                card = cards.getCard();
                cs.add(card);
                sum = getSum(cs);
            }
            //11-13，有70%的可能要牌
            else if (sum > 10 && sum <= 13) {
                if (RandomUtil.nextInt(100) > 30) {
                    card = cards.getCard();
                    cs.add(card);
                    sum = getSum(cs);
                } else {
                    break;
                }
            }
            //14-16，有30%的可能要牌
            else if (sum > 14 && sum <= 16) {
                if (RandomUtil.nextInt(100) > 70) {
                    card = cards.getCard();
                    cs.add(card);
                    sum = getSum(cs);
                } else {
                    break;
                }
            }
            //16以上，肯定不要牌
            else {
                break;
            }
        }
        //System.out.println(sum);
        return cs;
    }

    /**
     * 处理下注
     * 
     * @param request
     */
    public void deal1(HttpServletRequest request) {
        String tip = null;
        String result = "success";
        //取得参数
        int wager = StringUtil.toInt(request.getParameter("wager"));
        if (wager <= 0) {
            tip = "赌注不能小于等于零！";
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

            G21Bean g21 = new G21Bean();
            g21.setWager(wager);
            g21.setCards(new Cards());
            g21.setSystemCards(getSystemCards(g21.getCards()));
            setSessionObject(request, g21SessionName, g21);
            request.setAttribute("result", result);
            return;
        }
    }

    /**
     * 给用户发牌，用户要牌。
     * 
     * @param request
     */
    public void deal2(HttpServletRequest request) {
        G21Bean g21 = (G21Bean) getSessionObject(request, g21SessionName);
        if (g21 == null) {
            return;
        }

        //第一次发牌
        if (g21.getUserCards() == null) {
            Vector userCards = new Vector();
            userCards.add(g21.getCards().getCard());
            userCards.add(g21.getCards().getCard());
            g21.setUserCards(userCards);
            request.setAttribute("next", "continue");
            return;
        }
        //用户要牌
        else {
            g21.getUserCards().add(g21.getCards().getCard());
            //如果大于21点，退出
            int sum = getSum(g21.getUserCards());
            if (sum > 21) {
                request.setAttribute("next", "breakLose");
                return;
            }
            //够了五张
            else if (g21.getUserCards().size() >= 5) {
                request.setAttribute("next", "breakWin");
                return;
            }
            request.setAttribute("next", "continue");
            return;
        }
    }

    /**
     * 判断输赢，显示结果
     * 
     * @param request
     */
    public void deal3(HttpServletRequest request) {

        G21Bean g21 = (G21Bean) getSessionObject(request, g21SessionName);
        if (g21 == null) {
            return;
        }

        int systemSum = getSum(g21.getSystemCards());
        int userSum = getSum(g21.getUserCards());

        String result = null;
        String tip = null;

        boolean sysHasBlackJack = hasBlackJack(g21.getSystemCards());
        boolean userHasBlackJack = hasBlackJack(g21.getUserCards());

        //系统大于21点
        if (systemSum > 21) {
            //打平
            if (userSum > 21) {
                result = "draw";
                tip = "系统大于21点，您也大于21点。";
            }
            //用户赢
            else {
                result = "userWin";
                tip = "系统大于21点，您" + userSum + "点。";
            }
        }
        //系统小于等于21点
        else {
            //用户输
            if (userSum > 21) {
                result = "userLose";
                tip = "系统" + systemSum + "点，您大于21点。";
            } else {
                //如果系统牌为BlackJack，无敌牌
                if (sysHasBlackJack) {
                    //打平
                    if (userHasBlackJack) {
                        result = "draw";
                        tip = "您有Blackjack，可惜系统也有。";
                    }
                    //用户输
                    else {
                        result = "userLose";
                        tip = "系统有Blackjack，您" + userSum + "点。";
                    }
                }
                //系统牌为普通牌
                else {
                    //如果用户牌为BlackJack，无敌牌
                    if (userHasBlackJack) {
                        result = "userWin";
                        tip = "系统" + systemSum + "点，您有Blackjack！";
                    }
                    //打平
                    else if (systemSum == userSum) {
                        result = "draw";
                        tip = "系统" + systemSum + "点，您" + userSum + "点。";
                    }
                    //用户赢
                    else if (userSum > systemSum) {
                        result = "userWin";
                        tip = "系统" + systemSum + "点，您" + userSum + "点。";
                    }
                    //用户输
                    else if (userSum < systemSum) {
                        result = "userLose";
                        tip = "系统" + systemSum + "点，您" + userSum + "点。";
                    }
                }
            }
        }
        
        //统计连赢次数
        if("userWin".equals(result)){
            WGameBean wgame = (WGameBean) getSessionObject(request,
                    winsSessionName);
            if(wgame != null){
                wgame.setWins(wgame.getWins() + 1);
            }
        } else if("userLose".equals(result)){
            request.getSession().removeAttribute(winsSessionName);
        }

        if (userHasBlackJack) {
            request.setAttribute("userHasBlackJack", "true");
        } else {
            request.setAttribute("userHasBlackJack", "false");
        }
        request.setAttribute("result", result);
        request.setAttribute("tip", tip);
        request.setAttribute("g21", g21);
        request.getSession().removeAttribute(g21SessionName);
    }

    /**
     * 取得总点数。
     * 
     * @param userCards
     * @return
     */
    public int getSum(Vector userCards) {
        int sum = 0;
        if (userCards == null) {
            return sum;
        }
        Iterator itr = userCards.iterator();
        int value = 0;
        boolean hasAce = false;
        while (itr.hasNext()) {
            value = ((CardBean) itr.next()).getValue();
            if (value == 1) {
                hasAce = true;
            } else if (value > 10) {
                value = 10;
            }
            sum += value;
        }

        //有Ace
        if (sum <= 11 && hasAce) {
            sum += 10;
        }
        return sum;
    }

    /**
     * 判断是否有黑杰克。
     * 
     * @param userCards
     * @return
     */
    public boolean hasBlackJack(Vector userCards) {
        if (userCards == null) {
            return false;
        }

        if (userCards.size() != 2) {
            return false;
        }

        CardBean card1 = (CardBean) userCards.get(0);
        CardBean card2 = (CardBean) userCards.get(1);

        if (card1.getValue() == 1 && card2.getValue() >= 10) {
            return true;
        } else if (card2.getValue() == 1 && card1.getValue() >= 10) {
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        G21Action toa = new G21Action();
        Cards cards = new Cards();
        toa.getSystemCards(cards);
    }
}
