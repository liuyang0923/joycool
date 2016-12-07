<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.action.user.SendAction" %><%
response.setHeader("Cache-Control","no-cache");
if(request.getParameter("mark")!=null){
SendAction action = new SendAction(request);
action.picMark(request);
}
UserBean loginUser=(UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
net.joycool.wap.bean.UserSettingBean set = loginUser.getUserSetting();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="其他设置">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="otherSet.jsp?mark=16">好友列表中的备注</a>(<%if(set!=null&&set.isFlag(16)){%>显示<%}else{%>隐藏<%}%>)<br/>
<a href="otherSet.jsp?mark=14">单个赠送物品时</a>(<%if(set!=null&&set.isFlagFriendConfirm()){%>需要确认<%}else{%>无需确认<%}%>)<br/>
<a href="otherSet.jsp?mark=15">拍卖大厅竞价</a>(<%if(set!=null&&set.isFlagAuctionConfirm()){%>都需要确认<%}else{%>超过50亿的需要确认<%}%>)<br/>
<a href="userInfo.jsp">返回用户设置</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>