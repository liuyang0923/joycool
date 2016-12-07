<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="jc.util.SimpleChatLog2"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.framework.*,java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.snow.*"%><%@ page import="jc.family.game.*"%><%@ page import="jc.family.*"%><%!
static int NUMBER_OF_PAGE = 6;%><%
SnowGameAction snowAction=new SnowGameAction(request,response);
int mid=snowAction.getParameterInt("mid");
if(mid==0){
response.sendRedirect("index.jsp");return;
}
int isGameOver=snowAction.isGameOver(mid);// 比赛结束跳到别的页面
if(isGameOver==2||isGameOver==0){
	out.clearBuffer();
	response.sendRedirect("fight.jsp?mid="+mid);return;
}else if(isGameOver==3||isGameOver==4){
	out.clearBuffer();
	response.sendRedirect("/fm/game/fmgame.jsp");return;
}
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="聊天记录"><p>
<%
String ct="f";
if(request.getParameter("ct")!=null&&(request.getParameter("ct").equals("a")||request.getParameter("ct").equals("f"))){
	session.setAttribute("ct",request.getParameter("ct"));
}
if(session.getAttribute("ct")!=null){ct=(String)session.getAttribute("ct");}
net.joycool.wap.bean.UserBean user = snowAction.getLoginUser();
FamilyUserBean fmUser = snowAction.getFmUser();
SimpleChatLog2 sc =null;%>
<%if(ct.equals("f")){%>家族
<%}else{%><a href="chatmore.jsp?mid=<%=mid%>&amp;ct=f">家族</a><%} %>|
<%if(ct.equals("a")){%>所有
<%}else{%><a href="chatmore.jsp?mid=<%=mid%>&amp;ct=a">所有</a><%} %>|<a href="chatmore.jsp?mid=<%=mid %>&amp;ct=<%=ct %>" >刷新</a><br/>
<%
String content = snowAction.getParameterNoEnter("content");
	if(content != null&&!"".equals(content)) {		// 发言
		String c="f";
		if(request.getParameter("c")!=null){
			c=request.getParameter("c");
			}
		SimpleChatLog2 cc =null;
		if("f".equals(c)){
			cc = SimpleChatLog2.getChatLog(new Integer(mid+""+fmUser.getFm_id()).intValue(),"match_fm");
			}
		if("a".equals(c)){
			cc = SimpleChatLog2.getChatLog(mid,"match");
			}
		cc.add(user.getId(),user.getNickNameWml(),StringUtil.toWml(content));
	}
	if(ct.equals("f")){sc = SimpleChatLog2.getChatLog(new Integer(mid+""+fmUser.getFm_id()).intValue(),"match_fm");}else if (ct.equals("a")){sc = SimpleChatLog2.getChatLog(mid,"match");}
	PagingBean paging = new PagingBean(snowAction, sc.size(),NUMBER_OF_PAGE,"p");
%>
<%=sc.getChatString(paging.getStartIndex(), NUMBER_OF_PAGE)%>
<%=paging.shuzifenye("chatmore.jsp?mid="+mid+"&amp;ct="+ct,true, "|", response)%>
<input name="gchat"  maxlength="100"/><br/>
<anchor title="ok">发送给家族
<go href="chatmore.jsp?mid=<%=mid%>&amp;c=f&amp;ct=<%=ct %>" method="post">
    <postfield name="content" value="$gchat"/>
</go></anchor><br/>
<anchor title="ok">发送给所有
<go href="chatmore.jsp?mid=<%=mid%>&amp;c=a&amp;ct=<%=ct %>" method="post">
    <postfield name="content" value="$gchat"/>
</go></anchor><br/>
<a href="fight.jsp?mid=<%=mid %>">返回游戏</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>