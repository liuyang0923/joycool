<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.spec.buyfriends.*,net.joycool.wap.bean.*,net.joycool.wap.util.*"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
	response.setHeader("Cache-Control","no-cache");
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	
	int cur = StringUtil.toId(request.getParameter("cur"));
	int limit = 5;
	ServiceMaster serviceMaster = ServiceMaster.getInstance();
	BeanMaster master = serviceMaster.getMasterByUid(loginUser.getId());
	List list = serviceMaster.getMastersICanBuyOfMyFriend(loginUser.getId(), master.getMoney(), cur, limit);
	int count = list.size();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="购买奴隶"><p><%=BaseAction.getTop(request, response)%>
【购买奴隶】<br/>
<%for(int i = 0; i<list.size(); i++){
		BeanMaster bean = (BeanMaster)(list.get(i));%>
<a href="<%=("/info.jsp?uid="+master.getUid() ) %>"><%=StringUtil.toWml(bean.getNickName()) %></a>|<a href="<%=("bFri.jsp?uid="+ bean.getUid() + "&amp;n=a&amp;nickName=" + StringUtil.toWml(bean.getNickName())) %>">购买</a>|￥<%=bean.getPrice() %><br/>
<%}%><%if(count >= 5) {%><a href="<%=("bFris.jsp?cur=" + (cur+1))%>">下一页</a>
<%}	%><%if(cur >= 1) {%>||<a href="<%=("bFris.jsp?cur=" + (cur-1))%>">上一页</a><%}%>
<br/><a href="myInfo.jsp">返回朋友买卖</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>

