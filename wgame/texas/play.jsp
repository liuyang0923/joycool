<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.farm.SimpleChatLog"%><%@ page import="net.joycool.wap.util.UserInfoUtil,java.util.List"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.game.texas.*"%><%
response.setHeader("Cache-Control","no-cache");
TexasAction action = new TexasAction(request);
int id = action.getParameterIntS("id");
if(id>=0&&id<5){
	session.setAttribute("texas", TexasAction.boards[id]);
}
//if(action.getParameterInt("w")>=0)	// 避免下负数
action.play();
TexasGame game = (TexasGame)session.getAttribute("texas");
if(game == null||action.getParameterInt("leave")==2) {
	response.sendRedirect("index.jsp");
	return;
}
TexasUser user = action.getUser();
boolean in = (user != null);	// 是否已经在游戏中
int boardId = TexasGame.getUserBoardId(action.getLoginUser().getId());
boolean canSit = (boardId == -1);		// 没有在任何游戏中则可以坐下
boolean isCurrent = (game.getStatus()==1&&game.getCurrentUserId()==action.getLoginUser().getId());	// 当前玩家
//in = false;	// 用于测试
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=game.getBoardId() + 1%>号<%=game.getGameTypeName()%>桌" <%if(!isCurrent){%>ontimer="<%=response.encodeURL("play.jsp")%>"<%}%>><%if(!isCurrent){%><timer value="300"/><%}%>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(boardId != -1 && game.getBoardId()!=boardId){%><a href="play.jsp?id=<%=boardId%>">回到当前<%=boardId+1%>号桌</a><br/><%}%>
<%if(game.getUserCount()>1 && in && game.getStatus()!=1){
int leftTime=game.getTimeLeft();
%><%if(leftTime > 0){%>重新开始(剩余<%=leftTime%>秒)<a href="play.jsp">刷新</a><%}else{%><a href="play.jsp?start=1">开始新一局德州扑克</a><%}%><br/><%}%>
<%switch(game.getRound()){
case 0:{	// 等待开始
%>


<%
	} break;
	case 1:{	// 盲注
%><%
TexasUser[] users = game.getUsers();
for(int i=0;i<users.length;i++){
	TexasUser tu = users[i];
	if(tu==null) continue;
%><%=i+1%>座(<%=tu.getMoney()%>)<%=UserInfoUtil.getUser(tu.getUserId()).getNickNameWml()%><br/><%
}%>

==游戏准备==<br/>
<%
	} break;
	case 2:		// 口袋牌
	case 3:		// 三张牌
	case 4:		// 四张牌
	case 5:{	// 五张牌
%>

==<%=game.getRoundName()%>==<br/>
台面:<%if(game.getCards()[0]==-1){%>(未开牌)<%}else{%><%=TexasGame.toString(game.getCards())%><%}%><br/>
<%if(in&&user.isStatusPlay()){%>底牌:<%=TexasGame.toString(user.getUserCards())%><br/><%}%>

<%
TexasUser[] users = game.getUsers();
for(int i=0;i<users.length;i++){
	TexasUser tu = users[i];
	if(tu==null || tu.getStatus()==0) continue;
%>(<%=i+1%>座<%if(user!=null&&user.getSeat()==i){%>]<%}else{%>)<%}%>下注<%=tu.getRoundWager()%>(<%=tu.getMoney()%>)<%if(i!=game.getCurrent()){%><%=tu.getAction()%><%}else{%>思考中<%}%><br/><%
}%>
+池底总筹码<%=game.getWager()%>+<br/>

<%if(isCurrent){
int more = user.getMoney() - (game.getRoundWager() - user.getRoundWager());
%><%if(more>0){%><a href="play.jsp?a=1">跟<%=game.getRoundWager()-user.getRoundWager()%></a><%}else{%>跟<%}%>|<%if(game.getRoundWager()==0){%><a href="play.jsp?a=4">看牌</a><%}else{%>看牌<%}%>|<a href="play.jsp?a=3">全下<%=user.getMoney()%></a>|<a href="play.jsp?a=5">放弃</a><br/>
<%if(more>0){%><input name="amoney" maxlength="4" format="*N"/><br/>
<anchor title="加注">加注<go href="play.jsp?a=2"><postfield name="w" value="$amoney"/></go></anchor>|<%if(more>2){%><a href="play.jsp?a=2&amp;w=2">+2</a><%}else{%>+2<%}%>|<%if(more>5){%><a href="play.jsp?a=2&amp;w=5">+5</a><%}else{%>+5<%}%>|<%if(more>10){%><a href="play.jsp?a=2&amp;w=10">+10</a><%}else{%>+50<%}%>|<%if(more>50){%><a href="play.jsp?a=2&amp;w=50">+50</a><%}else{%>+50<%}%><br/><%}%>
<%}else{%>等待<%=game.getCurrent()+1%>座玩家(<%=game.getWaitTimeLeft()%>秒)-<a href="play.jsp">刷新</a><br/><%}%>


<%
	} break;
	case 6:{	// 结果
%>

==本局结果==<br/>
台面:<%=TexasGame.toString(game.getCards())%><br/>
总下注:<%=game.getWager()%><br/>
===本局赢家===<br/>
<%
List winUsers = game.getWinUsers();
for(int i=0;i<winUsers.size();i++){
	TexasUser tu = (TexasUser)winUsers.get(i);
	if(tu.getWinWager()==0) continue;
%><%=tu.getSeat()+1%>座[<%=tu.getTypeName()%>]赢得了<%=tu.getWinWager()%><br/><%
}%>
===本局详情===<br/>
<%
List users = game.getRoundUsers();
for(int i = 0;i < users.size();i++) {
	TexasUser tu = (TexasUser)users.get(i);
	if(tu==null || tu.getStatus()==0) continue;
%><%=tu.getSeat()+1%>座<%if(tu.getStatus()!=3){%>[<%=tu.getTypeName()%>]<%=TexasGame.toString(tu.getUserCards())%><%if(tu.getType()!=0){%>-&gt;<br/><%=TexasGame.toString(tu.getFinalCards())%><%}%><%}else{%>[放弃]<%}%><br/>
<%
}%>

<%}}%>
<%if(!in||game.getRound()==6||game.getRound()==0){%>==座位列表==<br/><%
TexasUser[] users = game.getUsers();
for(int i=0;i<users.length;i++){
	TexasUser tu = users[i];
	if(tu==null){
%><%=i+1%>座<%if(!canSit){%>(空)<%}else{%>-(<a href="sit.jsp?sit=<%=i%>">坐下</a>)<%}%><br/><%
	}else{
%><%=i+1%>座-<a href="/chat/post.jsp?toUserId=<%=tu.getUserId()%>"><%=UserInfoUtil.getUser(tu.getUserId()).getNickNameWml()%></a>(<%=tu.getMoney()%>)<br/><%
	}
}%>
<%}%>
<%if(in){%><a href="leave.jsp">站起</a>|<a href="leave.jsp?a=1">站起回大厅</a><br/><%}%>

<%
if(game.getLog().size()>0){
%>==历史信息<%if(game.getLog().size()>5){%>(<a href="log.jsp">更多</a>)<%}%>==<br/>
<%=game.getLog().getLogString(5)%>
<%}%>

<%SimpleChatLog sc = SimpleChatLog.getChatLog("tx"+game.getBoardId());%>
==最近评论<a href="chat.jsp">(更多)</a>==<br/>
<%if(sc.size()>0){%><%=sc.getChatString(0, 2)%><%}%>
<input name="cchat"  maxlength="100"/>
<anchor title="确定">发表
<go href="chat.jsp">
    <postfield name="content" value="$cchat"/>
</go></anchor><br/>
<a href="index.jsp">返回德州扑克大厅</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>

</card>
</wml>