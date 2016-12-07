<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.spec.buyfriends.BeanVisit"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.user.UserAction"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(loginUser==null){
	response.sendRedirect("/user/login.jsp");
	return;
}
List historyList = PositionUtil.getUserPosition(loginUser.getId()).getHistory();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="ME">
<p align="left">
<%if(loginUser!=null){
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
%>
<a href="/user/ViewFriends.do">好友<%int friendCount=UserInfoUtil.getUserOnlineFriendsCount(loginUser.getId());if(friendCount>0){%><%=friendCount%><%}%></a>|<a href="/user/userInfo.jsp">设置</a>|<a href="/mycart.jsp">收藏</a>|<a href="/user/userBag.jsp">行囊</a><br/>
<a href="/pet/info.jsp">宠物</a>|<%if(loginUser.getHome()==1){%><a href="/home/home.jsp">家园</a><%}else{%><a href="/home/viewAllHome.jsp">家园</a><%}%>|<%if(us.getTong()>=20000){%><a href="/fm/myfamily.jsp?id=<%=us.getTong()%>">家族</a><%}else if(us.getTong()>0){%><a href="/tong/tong.jsp?tongId=<%=us.getTong()%>">帮会</a><%}else{%><a href="/fm/index.jsp">家族</a><%}%>|<a href="/team/index.jsp">圈子</a><br/>
<a href="/user/messageIndex.jsp">信箱</a>|<a href="/user/ViewUserInfo.do?userId=<%=loginUser.getId()%>">资料</a>|<a href="/bank/bank.jsp">银行</a>|<a href="/user/logout.jsp">注销</a><br/>
&gt;&gt;<a href="/jcforum/userTopic.jsp?u=<%=loginUser.getId()%>">我的帖子</a><%--|<a href="/apps/my.jsp">我的组件</a>--%><br/>
<%=BeanVisit.getUserShortcut2String(loginUser, response) %><br/>
&gt;&gt;<a href="/user/shortcut2.jsp">编辑ME</a><br/>
<%}%>
最近访问:<%if(historyList.size()==0){%>(无记录)<%}else{
	Iterator iter = historyList.iterator();
	while(iter.hasNext()){
		ModuleBean module = (ModuleBean)iter.next();
%><a href="<%=module.getEntryUrl()%>"><%=module.getShortName()%></a><%if(iter.hasNext()){%>.<%}%><%
	}
}%><br/>
<%=BaseAction.separator%>
ME|<a href="/lswjs/index.jsp">导航</a>|<a href="/wapIndex.jsp">乐酷首页</a><br/>
<%=DateUtil.getCurrentDatetimeAsStr()%><br/>
</p>
</card>
</wml>
