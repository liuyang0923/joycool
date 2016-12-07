<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();static final int COUNT_PRE_PAGE=5;%>
	<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	int totalCount = SqlUtil.getIntResult("select count(id) from flower_message where user_id = " + action.getLoginUserId() + " and type = 0", 5);
	PagingBean paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
	int pageNow = paging.getCurrentPageIndex();
	List logList = service.getMessageList("user_id = " + action.getLoginUserId() + " and type = 0 order by id desc limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
	FlowerMessageBean fm = null;
	UserBean user = null;
%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
<%	if (logList.size() == 0){
		%>尚无任何消息.<br/><%
	} else {
		for (int i=0;i<logList.size();i++){
			fm = (FlowerMessageBean)logList.get(i);
			user = UserInfoUtil.getUser(fm.getFromId());
			%><%=pageNow * COUNT_PRE_PAGE + i + 1%>.<a href="/user/ViewUserInfo.do?userId=<%=fm.getFromId()%>"><%=user.getNickNameWml()%></a>来你<%=fm.getContent()%><br/><%
		}
	}%><%=paging.shuzifenye("log.jsp", false, "|", response)%>
<a href="fgarden.jsp">返回我的花园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>