<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();
	static int COUNT_PRE_PAGE = 10;%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	int totalCount = SqlUtil.getIntResult("select count(user_id) from flower_user", 5);
	PagingBean paging = new PagingBean(action, totalCount,COUNT_PRE_PAGE, "p");
	int pageNow = paging.getCurrentPageIndex();
	List list = service.getFlowerUser(pageNow,COUNT_PRE_PAGE);
%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
更多花园玩家.<br/>
<% FlowerUserBean fub = null;
   for (int i = 0;i < list.size();i++){
   		fub = (FlowerUserBean)list.get(i);
		%><a href="/user/ViewUserInfo.do?userId=<%=fub.getUserId()%>"><%=UserInfoUtil.getUser(fub.getUserId()).getNickNameWml()%></a><br/><%
   }%><%=paging.shuzifenye("more.jsp", false, "|",response)%>
<%%><a href="fgarden.jsp">返回我的花园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>