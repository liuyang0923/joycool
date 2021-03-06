<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.UserInfoUtil,java.util.List"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.game.texas.*"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="德州扑克">
<p align="left">
使用道具：一副标准扑克牌去掉大小王后的52张牌进行游戏。<br/>
游戏人数：最少两个玩家，最多10个玩家，个别情况有12个玩家的。<br/>
游戏的目的：赢取其他玩家筹码，<br/>
下注宗旨：玩家之间同时继续看牌或比牌需要下同样注额筹码，筹码不足的玩家allin全下后可以看到底并参与比牌。<br/>
发牌下注步骤：发牌一般分为5个步骤，分别为，<br/>
Perflop—先下大小盲注，然后给每个玩家发2张底牌，大盲注后面第一个玩家选择跟注、加注或者盖牌放弃，按照顺时针方向，其他玩家依次表态，大盲注玩家最后表态，如果玩家有加注情况，前面已经跟注的玩家需要再次表态甚至多次表态。<br/>
Flop—同时发三张公牌，由小盲注开始（如果小盲注以盖牌，由后面最近的玩家开始，以此类推），按照顺时针方向依次表态，玩家可以选择下注、加注、或者盖牌放弃。<br/>
Turn—发第4张牌，由小盲注开始，按照顺时针方向依次表态，玩家可以选择下注、加注、或者盖牌放弃。<br/>
River—发第五张牌，由小盲注开始，按照顺时针方向依次表态，玩家可以选择下注、加注、或者盖牌放弃。<br/>
比牌—经过前面4轮发牌和下注，剩余的玩家开始亮牌比大小，成牌最大的玩家赢取池底。<br/>
比牌方法：<br/>
用自己的2张底牌和5张公共牌结合在一起，选出5张牌，不论手中的牌使用几张（甚至可以不用手中的底牌），凑成最大的成牌，跟其他玩家比大小。<br/>
比牌先比牌型，大的牌型大于小的牌型，牌型一般分为10种，从大到小为：<br/>
皇家同花顺(royal flush)：由AKQJ10五张组成，并且这5张牌花色相同<br/>
同花顺(straight flush)：由五张连张同花色的牌组成<br/>
4条(four of a kind)：4张同点值的牌加上一张其他任何牌<br/>
葫芦(full house)：3张同点值加上另外一对<br/>
同花(flush)：5张牌花色相同，但是不成顺子<br/>
顺子(straight)：五张牌连张，至少一张花色不同<br/>
3条(three of a kind)：三张牌点值相同，其他两张各异<br/>
两对(two pairs)：两对加上一个杂牌<br/>
一对(one pair)：一对加上3张杂牌<br/>
散牌(high card)：不符合上面任何一种牌型的牌型<br/>
相同牌型比点值，4种花色不分大小，点值以A为最大点，2为最小点。比点值本着牌型优先和最大点优先的原则，先比主要部分，再比较次要部分，先比最大点，再比次大点。例如先比较葫芦的3条部分，3条大的胜出，如果3条部分相等，再比较1对部分。顺子比牌中，A2345的顶张为5，A算1，属于最小的顺子。对于散牌比大小，先比较各自的最大牌，如果最大牌的点值不同，则立刻分出胜负，如果最大牌点值相同，再比次大牌，以此类推，最终比出大小，如果所有点值相等，则不分胜负，平分池底。<br/>
池底分配方法：<br/>
由于玩家筹码不同，池底可能由主池和边池构成，有时候有多个边池。主池由allin全下的玩家和其他玩家下注组成，边池由超出allin玩家下注的筹码部分组成。<br/>
比牌之后开始分配池底，最大成牌的玩家赢取主池，如果玩家下注的筹码参与了边池，就同时赢取边池，如果没有参与边池，边池由剩余玩家再次比牌分配。如果玩家之间存在成牌大小相等，则平分池底（不能平分的单数筹码由前面的牌手获得）。<br/>
<a href="help.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>