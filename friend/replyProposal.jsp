<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.bean.friend.FriendRingBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
String noticeId=request.getParameter("noticeId");
String reply=request.getParameter("reply");
String ringId=request.getParameter("ringId");
int fromId=StringUtil.toInt(request.getParameter("user"));
UserBean loginUser=(UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
UserBean fromUser=(UserBean)UserInfoUtil.getUser(fromId);
UserStatusBean user=(UserStatusBean)UserInfoUtil.getUserStatus(loginUser.getId());
UserStatusBean from=(UserStatusBean)UserInfoUtil.getUserStatus(fromId);
if("1".equals(reply))
{	FriendAction action=new FriendAction(request);
	boolean flag = action.replyYes(fromId,StringUtil.toInt(ringId));
	if(flag){
%>
<card title="求婚结果" ontimer="<%=response.encodeURL("/friend/marriage.jsp")%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
您答应了<%=StringUtil.toWml(fromUser.getNickName())%>的求婚，选个时间举行婚礼吧！<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
	}else{
%>
<card title="求婚结果" ontimer="<%=response.encodeURL("/friend/friendCenter.jsp")%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%= (((String)request.getAttribute("tip")==null))?"":(String)request.getAttribute("tip") %><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%		
	}
}
else if ("0".equals(reply))
{
	FriendAction action=new FriendAction(request);
    if(action.replyNo(fromId))
    {%>
    <card title="求婚结果" ontimer="<%=response.encodeURL("/friend/friendCenter.jsp")%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
您已经拒绝了<%=StringUtil.toWml(fromUser.getNickName())%>的求婚，不能再拒绝了！<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
  <%  }
%>
<card title="求婚结果" ontimer="<%=response.encodeURL("/friend/friendCenter.jsp")%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
您拒绝了<%=StringUtil.toWml(fromUser.getNickName())%>的求婚，哎……<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
else
{
FriendAction action=new FriendAction(request);
FriendRingBean ring=action.getFriendRing(StringUtil.toInt(ringId));
String ringName=ring.getName();
%>
<card title="求婚">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(fromUser.getNickName())%>用<%=ringName%>向您求婚了！<br/>
<a href="/friend/replyProposal.jsp?reply=1&amp;user=<%=fromId%>&amp;ringId=<%=ringId%>&amp;noticeId=<%=noticeId%>">答应</a><br/>
<a href="/friend/replyProposal.jsp?reply=0&amp;user=<%=fromId%>&amp;ringId=<%=ringId%>&amp;noticeId=<%=noticeId%>">拒绝</a><br/>
<a href="/friend/friendCenter.jsp">再考虑考虑</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>