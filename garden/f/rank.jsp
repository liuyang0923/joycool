<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();
	static final int COUNT_PRE_PAGE=10;%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	boolean result = false;
	FlowerAction action = new FlowerAction(request);
	int loginUid = action.getLoginUserId();
	int totalCount = SqlUtil.getIntResult("select count(user_id) from flower_rank", 5);
	PagingBean paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
	int pageNow = paging.getCurrentPageIndex();

	//每过一段时间更新排行榜
	FlowerUtil.statCompose();
	List rankList = service.getRank("1 limit " + (pageNow * COUNT_PRE_PAGE) + "," + COUNT_PRE_PAGE);
	int myRank = SqlUtil.getIntResult("select id from flower_rank where user_id =" + action.getLoginUserId(),5);
%><wml>
<card title="花之境">
<p><%=BaseAction.getTop(request, response)%>
超级花匠<br/>
<% if (myRank > 0){
	%>你当前排名为:第<%=myRank%>名<br/><%
}%>==姓名/成就值==<br/>
<%if (rankList.size() > 0){
		UserBean user = null;
		FlowerRankBean frb = null;
		for (int i=0;i<rankList.size();i++){
			frb = (FlowerRankBean)rankList.get(i);
			user = UserInfoUtil.getUser(frb.getUserId());
			%><%=frb.getId() %>.<a href="/user/ViewUserInfo.do?userId=<%=user.getId() %>"><%=user.getNickNameWml()%></a>/<%=frb.getExp() %><br/><%
		}
  }%><%=paging.shuzifenye("rank.jsp", false, "|", response)%>
<a href="fgarden.jsp">返回我的花园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>