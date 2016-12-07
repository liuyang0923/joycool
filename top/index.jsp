<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><%!
static String[] admins = {"chata","foruma","homea","maila","tonga","infoa","teama","fana"};
static boolean isAdmin(int id) {
	if(ForumAction.isSuperAdmin(id))	return true;
	for(int i = 0;i < admins.length;i++)
		if(ForbidUtil.isForbid(admins[i],id))	return true;
	return false;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷龙虎榜">
<p align="left">
上榜的都是在乐酷各个领域有突出贡献的名人，大家要向他们学习!<br/><br/>
<a href="moneyTop.jsp">富豪榜</a>|<a href="/top/socialTop.jsp">社交榜</a>|<a href="/top/credit.jsp">荣誉榜</a><br/>
<a href="blackList.jsp">恶人榜</a>|<a href="/top/photoWomanTop.jsp">美女榜</a>|<a href="/top/photoManTop.jsp">帅哥榜</a><br/>
<a href="charitarianTop.jsp">慈善榜</a>|<a href="/chat/lastRank.jsp">邀请榜</a>|<a href="/top/spiritTop.jsp">反倭榜</a><br/>
<a href="pkList.jsp">PK榜</a><br/><br/>
<%
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(loginUser!=null && isAdmin(loginUser.getId())){
%>
<a href="/admin/index.jsp">乐酷监察管理后台</a><br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
