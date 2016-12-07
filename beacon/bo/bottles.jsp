<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.bottle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%!static BottleService service = new BottleService();
	static ArrayList topThree = null;
	static final int COUNT_PRE_PAGE = 5;%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="漂流瓶">
<p><%=BaseAction.getTop(request, response)%>
	茫茫大海中有许多承载着人们梦想的漂流瓶，捞起一个，追寻属于你的缘分吧<br />
	<%
		BottleAction action = new BottleAction(request);
		
		//随机取出5个瓶子
		List bbs = action.getFiveBottles();
		
		//ArrayList al = service.getBottles(pageNow, COUNT_PRE_PAGE);
		for (int i = 0; i < bbs.size(); i++) {
			BottleBean bb = (BottleBean)bbs.get(i);
	%><%=i+1%>.
	<%-- <%=bbs[i].getTitle()%> --%>玻璃瓶<a href="read.jsp?id=<%=bb.getId()%>">捞取</a>
	<br />(在<%=DateUtil.sformatTime(bb.getLastPickupTime())%>查看后又放回了海里)
	<br />
	<%
		}
	%>
	<a href="bottles.jsp">继续捞取</a><br/>
	<a href="create.jsp">&gt;我要放一个漂流瓶</a><br/>
	<a href="reversion.jsp">我捞到的漂流瓶</a><br/>
	<a href="list.jsp">我的漂流瓶历程</a><br/>
	<a href="index.jsp">返回漂流瓶首页</a><br/>
	<a href="../../friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>

