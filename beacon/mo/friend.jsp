<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page	import="java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%!
	static MoodService service=new MoodService();
	static final int COUNT_PRE_PAGE=6; 
%><%
	MoodAction action=new MoodAction(request);
	int uid=action.getLoginUser().getId();
	List moodList = MoodAction.getFriendMoodList(uid);
	if (moodList == null || moodList.size() == 0){
		action.sendRedirect("error.jsp?t=4",response);
		return;
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="好友的心情">
<p><%=BaseAction.getTop(request, response)%>
<%  
	PagingBean paging = new PagingBean(action,moodList.size(),COUNT_PRE_PAGE,"p");
	for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
		MoodUserBean mub=(MoodUserBean)moodList.get(i);
		//user2是回复者的信息
		UserBean user2 = UserInfoUtil.getUser(mub.getUserId());
%><%=i + 1%>.<a href="mood.jsp?uid=<%=user2.getId() %>"><%=user2.getNickNameWml()%></a>:
<%=StringUtil.toWml(mub.getMood())%><br/><%
	}
%><%=paging.shuzifenye("friend.jsp", false, "|", response)%>
<a href="mood.jsp?uid=<%=uid%>">返回上级</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>