<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.FriendRingBean"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
int toId=StringUtil.toInt(request.getParameter("toId"));
int gender=StringUtil.toInt(request.getParameter("gender"));
FriendAction action=new FriendAction(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
UserStatusBean tous=UserInfoUtil.getUserStatus(toId);
FriendRingBean ring=null;
Vector	vecRing=null;
vecRing=action.getFriendRing();%>
<card title="购买求婚戒指">
<p align="left">
<%=BaseAction.getTop(request, response)%>
 <%if(us.getMark()!=0 )
{%>
您处于结婚或者求婚状态，不能再向其他人求婚<br/>
<% }
else if(tous.getMark()==2 )
{%>
<%=gender == 1? "他" : "她"%>处于结婚状态，您不能再向<%=gender == 1? "他" : "她"%>求婚<br/>
<% }%>
<%if(us.getMark()==0 && tous.getMark()!=2)
{if(vecRing!=null)%>
选择求婚戒指，向<%=gender == 1? "他" : "她"%>表达您的爱吧<br/>
<%{
for(int j=0;j<vecRing.size();j++){
	ring=(FriendRingBean)vecRing.get(j);%>
<a href="/friend/proposalResult.jsp?toId=<%=toId%>&amp;ringId=<%=ring.getId()%>">
<%=ring.getName()%>(<%=ring.getPrice()%>)
<img src="<%=ring.getImageUrl()%>" alt="ring" /><br/>
</a>
<%}}%>
<a href="/friend/friendCenter.jsp">再考虑考虑</a><br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>