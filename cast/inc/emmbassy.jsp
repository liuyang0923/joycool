<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="java.util.*"%>
<%
String url = "fun.jsp?pos="+request.getParameter("pos");

if(building.getGrade() >= 3 && castleUser.getTong() == 0) {%>
<a href="tong/cTong.jsp?pos=<%=request.getParameter("pos") %>">创建联盟</a><br/>
<%}
TongBean tong = CastleUtil.getTong(castleUser.getTong());
if(tong == null) {
	int total = castleService.getTongInviteCount(action.getUserBean().getId());
	PagingBean paging = new PagingBean(action, total, 5, "p1");
	List list = castleService.getTongInvite(action.getUserBean().getId(), paging.getStartIndex(), paging.getCountPerPage());
%>你还没有加入联盟<br/>
<%
	for(int i = 0; i < list.size();i++) {
		TongInviteBean bean = (TongInviteBean)list.get(i);
		CastleUserBean cUser = CastleUtil.getCastleUser(bean.getFromUid());
%>
	<a href="user.jsp?uid=<%=cUser.getUid() %>"><%=StringUtil.toWml(cUser.getName())%></a>邀请您<a href="tong/cTong.jsp?a=a&amp;id=<%=bean.getId()%>&amp;pos=<%=request.getParameter("pos") %>">加入联盟</a>|<a href="tong/cTong.jsp?a=d&amp;id=<%=bean.getId()%>&amp;pos=<%=request.getParameter("pos") %>">删除</a><br/>
<%
	}
%><%=paging.shuzifenye(url, true, "|", response)%>
<%
} else {
	int grade = action.getUserResBean().getBuildingGrade(ResNeed.EMMBASSY_BUILD);
%>
联盟:<a href="tong/tong.jsp?id=<%=tong.getId()%>"><%=StringUtil.toWml(tong.getName()) %></a><br/>
人数上限:<%=ResNeed.getBuildingT(ResNeed.EMMBASSY_BUILD, grade).getValue()%><br/>

<%if(castleUser.getUid() != tong.getUid()) {%>
<a href="tong/quitTong.jsp?pos=<%=request.getParameter("pos") %>">退出联盟</a><br/><%}else { %>
<a href="tong/quitTong.jsp?pos=<%=request.getParameter("pos") %>">移除联盟</a><br/>
<%} %>
<%
	int total = tong.getCount();
	PagingBean paging = new PagingBean(action, total, 5, "p");
	
	List list = castleService.getTongUser(tong.getId(), paging.getStartIndex(), paging.getCountPerPage());
%>
【联盟成员】人数:<%=tong.getCount() %><br/>
<%
	
	for(int i = 0; i < list.size(); i ++) {
		CastleUserBean user = CastleUtil.getCastleUser(((Integer)list.get(i)).intValue());
%>
<a href="user.jsp?uid=<%=user.getUid()%>"><%=user.getNameWml()%></a><br/>
<%} %>
<%=paging.shuzifenye(url, true, "|", response)%>
<%if(tong.getUid() == action.getUserBean().getId()) {%>

<%}} %>
