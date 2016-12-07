<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.forum.*,net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.friendadver.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.impl.FriendAdverServiceImpl"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%!
	static int FRIEND_ADVER_PER_PAGE = 20;
	FriendAdverServiceImpl service = new FriendAdverServiceImpl();
%><%
    response.setHeader("Cache-Control","no-cache");
    CustomAction action = new CustomAction(request);
	String backTo=request.getParameter("backTo");
	int deleteId = action.getParameterInt("del");
	if (deleteId > 0){
		service.deleteFriendAdverMessage(" id="+deleteId);
	}

    String condition = null;

	PagingBean paging = new PagingBean(action,10000,FRIEND_ADVER_PER_PAGE,"p");
	int pageIndex = paging.getCurrentPageIndex();
	int totalPageCount = paging.getTotalPageCount();

	String cond = " 1 order by id desc limit " + paging.getStartIndex() + ",20";
//	OsCacheUtil.flushGroup(OsCacheUtil.FRIEND_ADVER_CACHE_GROUP,"SELECT * FROM jc_friend_adver WHERE "+cond);
    List friendAdverList = service.getFriendAdverMessageList(cond);
%>
<html>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body onkeydown="if(event.keyCode==39){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()+1%>';return false;}else if(event.keyCode==37){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()-1%>';return false;}else return true;">
交友广告回复列表<br/>
<table align="center" border="1" width="100%">
<%
int count = friendAdverList.size();
boolean hasAttach;
for(int i = 0; i < count; i ++){
	FriendAdverMessageBean fa = (FriendAdverMessageBean) friendAdverList.get(i);
	hasAttach = false;
	if(fa.getAttachment() != null && !fa.getAttachment().equals("")){
		hasAttach = true;
	}
%><tr><td width="50"><%=fa.getId()%></td>
<td width="80"><a href="../user/queryUserInfo.jsp?id=<%=fa.getUserId()%>"><%=fa.getUserId()%></a></td>
<td>
<%if(hasAttach){%>[图]<%}%><%=StringUtil.toWml(fa.getContent())%></td>
<td width="80"><%=fa.getCreateDatetime()%></td>
<td width="60">|<a href="friendAdverMessage.jsp?del=<%=fa.getId()%>&p=<%=paging.getCurrentPageIndex()%>" onclick="return confirm('确认删除？')">删除</a>
</td></tr>
<%
}
%></table>
<%=paging.shuzifenye("friendAdverMessage.jsp",false,"|",response,20)%><br/>
</body>
</html>