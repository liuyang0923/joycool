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
<card title="图片显示设置">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="picMark.jsp?mark=0">logo图片</a>(<%if(set!=null&&set.isFlagHideLogo()){%>隐藏<%}else{%>显示<%}%>)<br/>
<a href="picMark.jsp?mark=1">星星图片</a>(<%if(set!=null&&set.isFlagHideStar()){%>隐藏<%}else{%>显示<%}%>)<br/>
<a href="picMark.jsp?mark=2">帽子图片</a>(<%if(set!=null&&set.isFlagHideHat()){%>隐藏<%}else{%>显示<%}%>)<br/>
<a href="picMark.jsp?mark=3">表情图片</a>(<%if(set!=null&&set.isFlagHideFace()){%>隐藏<%}else{%>显示<%}%>)<br/>
<a href="picMark.jsp?mark=4">用户贴图</a>(<%if(set!=null&&set.isFlagHideSend()){%>隐藏<%}else{%>显示<%}%>)<br/>
<a href="userInfo.jsp">返回用户设置</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>