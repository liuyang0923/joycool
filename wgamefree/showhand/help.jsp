<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="梭哈-帮助">
<p align="left">
<%=BaseAction.getTop(request, response)%>
梭哈游戏说明<br/>
-------------------<br/>
“梭哈”是一种扑克游戏，五张牌比大小，同花顺最大，主要流行于我国广东、香港、澳门，由于此游戏简单，激烈，既含有技巧也有很大的运气成分，所以流传非常广泛。游戏用的是扑克牌，各门花色的牌只取「8、9、10、J、Q、K、A」，有黑桃、红桃、草花、方片四种，因此游戏中共有28张牌。<br/>
一开始用户设定本局最大乐币数，最少100个。五个乐币做底。然后发牌，按最后亮的那张牌的大小判定系统还是用户下注，第一次下注最多能下本局最大乐币数的四分一，第二次下注最多能下二分一，之后不限。一方下注后，另一方如果想继续，要按「跟了」键，或可选择加注，若不想跟牌，请按「放弃」键，先前跟过的乐币，就输掉了。<br/>
比较大小：<br/>
牌型比较：同花顺＞铁支＞葫芦＞同花＞顺子＞三条＞两对＞对子＞散牌。<br/>
数字比较：A>K>Q>J>10>9>8<br/>
花式比较：黑桃>红桃>草花>方片<br/>
牌型说明：<br/>
同花顺为同一种花色的顺子，比如全部是黑桃的A,K,Q,J,10。但8和A之间不算顺子。<br/>
铁支为四张同样大小的牌，比如4个A。<br/>
葫芦为三张一样的带2张一样的，比如3个A带2个8。大家都是3带2时，比较三个头的大小。<br/>
同花为五张牌为一种花式，如果大家都是同花，比最大的一张牌，如果大小还一样就比这张牌的花式。<br/>
顺子为5张牌是顺子但不同花，如果大家都是顺子，比最大的一张牌，如果大小还一样就比这张牌的花式。<br/>
三条是指有三个头，如果大家都是三条，比三条的大小。<br/>
两对是指有两个对子，如果大家都是两对，比大对子的大小，如果大对子也一样，比小对子的大小，如果还是一样，比大对子中的最大花式。<br/>
对子是指有一个对子，如果大家都是对子，比对子的大小，如果对子也一样，比这个对子中的最大花色。<br/>
散牌是没有上面说的任何情况，比最大一张牌的大小，如果大小一样，比这张牌的花色。<br/>
<br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="../index.jsp">返回赌场</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>