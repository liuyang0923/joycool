<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.RandomUtil"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.service.impl.JcForumServiceImpl"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.game.texas.*"%><%!
	static JcForumServiceImpl forumService = new JcForumServiceImpl();
	static int[] games = {0,1,2,3};
	static void prepareForum(int id, String name) {	// 准备一个论坛
		ForumBean forum = ForumCacheUtil.getForumCache(id);
		if(forum!=null)
			return;
		forum = new ForumBean();
		forum.setId(id);
		forum.setUserId("");
		forum.setTitle(name);
		forum.setDescription(name);
		forumService.addForum2(forum);
	}
	static int forumId = 9389;
	static {
		prepareForum(forumId, "德州扑克交流区");
	}
	static String[] sayings = {"如果你在三十分钟还找不到桌子上的鱼，那么你就是那条鱼。",
"忍是为了下一次全下。",
"不会从失败中找寻教训的人，他们的成功之路是遥远的。",
"打牌要打出拳手的风范，进可攻退可守",
"别指望狗屎运能带给你胜利。",
"去赢下该赢的，别去输不该输的。",
"有时输钱是一种赢钱的策略，拌猪吃虎有时很有效果。",
"不要惧怕任何人，更不要小视任何人。",
"做一个绅士，即使输掉所有的筹码任然可以向对手致敬。",
"冷静的头脑加出众的判断",
"应该懂得在什么时候放弃。",
"输钱是从赢钱开始的。",
"没有一种胜利比战胜自己及自己的冲动情绪更伟大。",
"尊重自己，尊重别人是长久胜利的口粮。",
"帮你赢钱的都是些普通的牌，起手的好牌负责送你下地狱。",
"定力和眼力是德州扑克的两大法宝。",
"冲动不是魔鬼，犹豫才是。",
"不要看人家有多少钱，要看自己有什么牌。",
"机会面前人人平等，等赢了再说这句话不迟",
"冷静，冷静，还是冷静",
"心态起决定性作用"};
%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="德州扑克" ontimer="<%=response.encodeURL("index.jsp")%>"><timer value="200"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="img/logo.gif" alt="德州扑克"/><br/>
<%
UserBean loginUser = (UserBean) session.getAttribute("loginUser");
TexasUserBean tub = null;
if(loginUser != null){
tub =	TexasGame.getUserBean(loginUser.getId());
int boardId = TexasGame.getUserBoardId(loginUser.getId());
if(boardId != -1){%><a href="play.jsp?id=<%=boardId%>">回到当前<%=boardId+1%>号桌</a><br/><%}}%>
德州扑克(测)<a href="help.jsp">[新手必读]</a><br/>
<%if(tub!=null){%>我的积分:<%=tub.getMoney()%><br/>等级:[<%=tub.getRankName()%>]<br/><%}%>
<a href="/jcforum/forum.jsp?forumId=<%=forumId%>">+玩家交流区+</a><br/>
<%for(int i=0;i<games.length;i++){
TexasGame game = TexasAction.boards[games[i]];

%><a href="play.jsp?id=<%=games[i]%>"><%=games[i]+1%>号<%=game.getGameTypeName()%>桌</a>(<%=game.getUserCount()%>/<%=game.getMaxUser()%>人)盲注<%=game.getBaseWager()%>|<%=game.getBaseWager()/2%><br/>

<%}%>5号实战桌(0/9人)盲注10|20<br/>
[名言]<%=sayings[RandomUtil.nextInt(sayings.length)]%><br/>
*<a href="sav.jsp">救济金发放处</a>*<br/>
==擂台大赛(暂未开放)==<br/>
**<a href="recs.jsp">最近历史战局</a>**<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>