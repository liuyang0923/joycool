<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.RandomUtil"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.service.impl.JcForumServiceImpl"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.game.texas.*"%><%
%><html>
	<head>
	</head>
	<body>
<%
UserBean loginUser = null;
TexasUserBean tub = null;
if(loginUser != null){
tub =	TexasGame.getUserBean(loginUser.getId());
int boardId = TexasGame.getUserBoardId(loginUser.getId());
if(boardId != -1){%><a href="play.jsp?id=<%=boardId%>">回到当前<%=boardId+1%>号桌</a><br/><%}}%>
<%if(tub!=null){%>我的积分:<%=tub.getMoney()%><br/>等级:[<%=tub.getRankName()%>]<br/><%}%>

<%for(int i=0;i<TexasAction.boards.length;i++){
TexasGame game = TexasAction.boards[i];

%><a href="play.jsp?id=<%=i%>"><%=i+1%>号<%=game.getGameTypeName()%>桌</a>(<%=game.getUserCount()%>/<%=game.getMaxUser()%>人)盲注<%=game.getBaseWager()%>|<%=game.getBaseWager()/2%><%if(game.getStatus()==1){%>(<%=game.getWaitTimeLeft()%>)<%}%><br/>

<%}%>
	</body>
</html>
