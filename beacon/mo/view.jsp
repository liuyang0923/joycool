<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page	import="java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%!
	static MoodService service=new MoodService();
	static int COUNT_PRE_PAGE=6; 
%><%
	MoodAction action = new MoodAction(request);
	int id = action.getParameterInt("id");
	List moodType = action.getMoodType();
	MoodBean mb = service.getMoodById(id);
	if (mb == null) {
		response.sendRedirect("operate.jsp?f=null");
		return;
	}
	int uid = mb.getUserId();
	UserBean user = UserInfoUtil.getUser(uid);
	//这是登陆者的id
	int logoUid = action.getLoginUser().getId();
	
	int totalCount = SqlUtil.getIntResult("select count(*) from mood_reply where mood_id=" + mb.getId(), 5);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="心情">
<p><%=BaseAction.getTop(request, response)%>
<%=user.getNickNameWml()%>在<%=DateUtil.sformatTime(mb.getCreateTime())%>记录了自己的心情:<img src="img/<%=mb.getType()%>.gif" alt="<%=moodType.get(mb.getType())%>" /><%=StringUtil.toWml(mb.getMood())%><br/>
==回复<%=totalCount%>条==<br/>
<% PagingBean paging = new PagingBean(action, totalCount,COUNT_PRE_PAGE, "p");
   int pageNow = paging.getCurrentPageIndex();
   //浏览次数加1
   //service.increaseViewCount(mb.getId());
   MoodReply mr;
   ArrayList list = service.getReplyById(mb.getId(), pageNow, COUNT_PRE_PAGE);

   for (int i = 0; i < list.size(); i++) {
   		mr = (MoodReply) list.get(i);
		//user2是回复者的信息
		UserBean user2 = UserInfoUtil.getUser(mr.getUserId());
%><%=pageNow * COUNT_PRE_PAGE + i + 1%>.<a href="/user/ViewUserInfo.do?userId=<%=user2.getId()%>"><%=user2.getNickNameWml()%></a>说:<%=StringUtil.toWml(mr.getReply())%>(<%=DateUtil.sformatTime(mr.getCreateTime())%>)
<% if (logoUid == uid || logoUid == user2.getId()) {		//logoUid == user2.getId()
%><a href="operate.jsp?id=<%=mr.getId()%>&amp;f=rdel">删</a>
<%}%><br/>
<%}%><%=paging.shuzifenye("view.jsp?id=" + id, true, "|",response)%>
=回复(最多50字)=<br/>
<input name="Reply" value="" maxlength="100" /><br/>
<anchor>发表回复
<go href="operate.jsp?id=<%=mb.getId()%>&amp;f=reply" method="post">
	<postfield name="reply" value="$Reply" />
</go>
</anchor><br/>
<a href="mood.jsp?id=<%=mb.getId()%>">返回心情列表</a><br/>
<%if (logoUid == uid) {
//相等就返回自己的资料
%><a href="../../user/ViewUserInfo.do?userId=<%=logoUid%>">返回我的资料</a><br/>
<%} else {%><a href="../../user/ViewUserInfo.do?userId=<%=user.getId()%>">返回<%=user.getNickNameWml()%>的资料</a><br/>
<%}%><%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>