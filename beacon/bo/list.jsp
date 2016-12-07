<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.bottle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.PagingBean"%><%!static BottleService service = new BottleService();
	static final int COUNT_PRE_PAGE = 5;%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="漂流瓶">
<p><%=BaseAction.getTop(request, response)%><%
		BottleAction action = new BottleAction(request);

		//这个ID是用户ID。要用这个ID查看他放出的所有瓶子。
		int uid = action.getLoginUser().getId();

		//注意这里要输入每页显示的数据量
		int totalCount = SqlUtil
				.getIntResult("select count(id) from bottle_info where user_id="
						+ uid, 5);
		PagingBean paging = new PagingBean(action, totalCount,
				COUNT_PRE_PAGE, "p");

		//取得要显示的页数。如果第一次访问，则从第1页开始
		int pageNow = paging.getCurrentPageIndex();

		//取得分页的SQL语句，注意这里也要输入每页显示的数据量，要与上面一至才可

		ArrayList al = service.getBottlesById(uid, pageNow, COUNT_PRE_PAGE);
	%>
	在缘分的指引中你投放的漂流瓶漂到了何处？经历了什么？查看下吧
	<br />
	<%
		if (al != null && al.size() != 0) {
			for (int i = 0; i < al.size(); i++) {
				BottleBean bb = (BottleBean) al.get(i);
	%><%=pageNow * COUNT_PRE_PAGE + i + 1%>.
	<a href="read.jsp?id=<%=bb.getId()%>&amp;f=list"><%=StringUtil.toWml(bb.getTitle())%></a>
	<br /><%=DateUtil.sformatTime(bb.getCreateTime())%>投放&nbsp;回复:<%=bb.getReplyCount()%><br />
	<%
		}
	%><%=paging.shuzifenye("list.jsp?id=" + 0, true, "|",
								response)%>
	<%
		} else {
	%>我还没放过漂流瓶~<br/><%
		}
	%><a href="bottles.jsp">继续捞取</a><br/>
	<a href="reversion.jsp">我捞到的漂流瓶</a><br/>
	<a href="index.jsp">返回漂流瓶首页</a><br/>
	<a href="../../friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>

