<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,java.util.*"%><%
	
	
	CastleAction action = new CastleAction(request);
	int uid = action.getParameterInt("uid");
	
	CastleUserBean castleUserBean = CastleUtil.getCastleUser(uid);
	if(castleUserBean==null){
		response.sendRedirect("s.jsp");
		return;
	}
	List list = action.getCastleService().getCastleList(uid);
	TongBean tongBean = null;
	
	if(castleUserBean.getTong() > 0) {
		tongBean = CastleUtil.getTong(castleUserBean.getTong());
	}
	
	
	CastleBean myCastleBean = action.getCastle();
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="城主资料"><p><%@include file="top.jsp"%>
【<%=StringUtil.toWml(castleUserBean.getName())%>】<br/>
种族:<%=castleUserBean.getRaceName()%><br/>
总人口:<%=castleUserBean.getPeople()%><br/>
<%if(tongBean != null) {%>所属联盟:<a href="tong/tong.jsp?id=<%=tongBean.getId()%>"><%=StringUtil.toWml(tongBean.getName()) %></a><br/><%}%>
<%if(castleUserBean.getInfo2().length()!=0){%><%=castleUserBean.getInfo2()%><br/><%}%>
<%if(castleUserBean.getUid()!=100){%><a href="/chat/post.jsp?toUserId=<%=castleUserBean.getUid() %>">发送信息</a><br/><%}%>
==城堡列表(<%=castleUserBean.getCastleCount()%>)==<br/>
<%for(int i = 0; i < list.size() ; i++) {
	CastleBean castleBean = (CastleBean)list.get(i);
	UserResBean userRes = (UserResBean)CastleUtil.getUserResBeanById(castleBean.getId());
%><%if(castleUserBean.getCastleCount()!=1&&castleUserBean.getMain()==castleBean.getId()){%>*<%}
%><%=i+1 %>.<a href="search.jsp?x=<%=castleBean.getX() %>&amp;y=<%=castleBean.getY() %>"><%=StringUtil.toWml(castleBean.getCastleName()) %>(<%=castleBean.getX() %>|<%=castleBean.getY() %>)</a>人口<%=userRes.getPeople() %><br/>
<%} %>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>