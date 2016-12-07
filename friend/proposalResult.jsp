<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.friend.FriendRingBean"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%
response.setHeader("Cache-Control","no-cache");
FriendAction action=new FriendAction(request);
String ringId=(String)request.getParameter("ringId");
FriendRingBean friendRing=null;
int toId=StringUtil.toInt(request.getParameter("toId"));
UserBean toUser=(UserBean)UserInfoUtil.getUser(toId);
int friendRingid=StringUtil.toInt(ringId);
friendRing=(FriendRingBean)action.getFriendRing(friendRingid);
String ringName=friendRing.getName();
int point=friendRing.getPrice();%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="求婚提示" ontimer="<%=response.encodeURL("/friend/friendCenter.jsp")%>">
<timer value="30"/>
<p align="left" >
<%=BaseAction.getTop(request, response)%>
<%
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
if(us.getMark()!=0 ){
%>您处于结婚或者求婚状态，不能再向其他人求婚<br/>
<% 
} 
else{
%>
<%if(friendRing==null){%>
该戒指不存在！<br/>
<%}
else if((null!=friendRing)&&(action.haveEnoughpoint(friendRing.getPrice())))
{%>
您已经用<%=friendRing.getName()%>向<%=StringUtil.toWml(toUser.getNickName())%>求婚了，成功还是失败，请静候爱神的裁决吧！<br/>
<%
action.deductUserpoint(friendRing.getPrice(),friendRing.getName(),toId,friendRing.getId());}
else{%>
穷鬼也想结婚？！你不会挑个便宜的求婚戒指啊，充什么有钱人，婚后日子还过不过了？！！<br/>
<%}%>
(3秒钟跳转回主页面)<br/>
<% } %>
<a href="<%=("/friend/friendCenter.jsp") %>">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>