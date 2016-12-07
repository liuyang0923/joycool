<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.action.user.UserAction"%><%
response.setHeader("Cache-Control","no-cache");
UserAction action = new UserAction(request);
action.shortcut();
UserBean loginUser = action.getLoginUser();
String[] ss = loginUser.getUserSetting().getShortcut().split(",");
HashMap sMap=(HashMap)request.getAttribute("sMap");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="快捷通道设置">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="shortcutList.jsp">添加链接到快捷通道&gt;&gt;</a><br/>
<%
LinkBuffer lb = new LinkBuffer(response);
for(int i = 0;i < ss.length;i++){
	int ssid = StringUtil.toInt(ss[i]);
    ShortcutBean bean = (ShortcutBean)sMap.get(Integer.valueOf(ssid));
    if(bean != null) {
    	lb.appendLink("shortcutRes.jsp?id=" + bean.getId(), bean.getShortName());
}}
String shortcut = lb.toString();%>
<%if(shortcut.length() > 0){%>
已添加链接:(点击删除)<br/><%}%>
<%=shortcut%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>