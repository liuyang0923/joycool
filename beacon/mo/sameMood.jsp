<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page	import="java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%!
	static MoodService service=new MoodService();
	static final int COUNT_PRE_PAGE=10;
%><%
	MoodAction action=new MoodAction(request);
	int uid=action.getLoginUser().getId();
	//int type=action.getParameterInt("type");
	MoodUserBean lmb=service.getLastMoodBean(uid);
	int type=0;
	if (lmb==null) {
		//最新心情不存在
		action.sendRedirect("operate.jsp?f=null2",response);
		return;
	} else {
		type=lmb.getType();
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="心情">
<p><%=BaseAction.getTop(request, response)%>
<% int totalCount = SqlUtil.getIntResult("select count(*) from mood_user where type=" + type, 5);
   PagingBean paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
     	   
   int pageNow = paging.getCurrentPageIndex();
    
   List list=service.getSameMood(type,pageNow,COUNT_PRE_PAGE);

   lmb=null;
   for(int i=0;i<list.size();i++){
   	  lmb=(MoodUserBean)list.get(i);
	  //user2是回复者的信息
	  UserBean user2 = UserInfoUtil.getUser(lmb.getUserId());
	  %><%=pageNow * COUNT_PRE_PAGE + i + 1%>.<a href="/user/ViewUserInfo.do?userId=<%=user2.getId() %>"><%=user2.getNickNameWml()%></a><br/><%
   }
%><%=paging.shuzifenye("sameMood.jsp?uid=" + uid, true, "|", response)%>
<a href="mood.jsp?uid=<%=uid%>">返回上级</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>