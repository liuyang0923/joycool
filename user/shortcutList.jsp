<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.user.UserBagAction" %><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.user.UserAction"%><%@ page import="net.joycool.wap.util.*" %><%
response.setHeader("Cache-Control","no-cache");
UserAction action = new UserAction(request);
action.shortcutList();
UserBean loginUser = action.getLoginUser();
String s = loginUser.getUserSetting().getShortcut();
PagingBean pagingBean = (PagingBean)request.getAttribute("page");
List sList=(List)request.getAttribute("sList");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="快捷通道设置">
<p align="left">
<%=BaseAction.getTop(request, response)%>
点击以下链接，把它加入到快捷通道：<br/>
<%
int i = 0;
LinkBuffer lb = new LinkBuffer(response);
Iterator iter = sList.iterator();
while(iter.hasNext()){
    ShortcutBean bean = (ShortcutBean)iter.next();
    if(bean == null||bean.getHide()==1) continue;
    if(s.indexOf(','+String.valueOf(bean.getId())+',') == -1) {
    	lb.appendLink("shortcutRes.jsp?id=" + bean.getId(), bean.getShortName());
}else{lb.appendWml(bean.getShortName());}}%>
<%=lb%><br/>
<%=PageUtil.shuzifenye(pagingBean, "shortcutList.jsp", false, "|", response)%>
<a href="shortcut.jsp">查看已添加链接&gt;&gt;</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>