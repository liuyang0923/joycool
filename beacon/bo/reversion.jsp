<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.bottle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.PagingBean"%><%!static BottleService service = new BottleService();
	static final int COUNT_PRE_PAGE = 5;%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="漂流瓶">
<p><%=BaseAction.getTop(request, response)%>
	<%
		BottleAction action = new BottleAction(request);

		//这个ID是用户ID。要用这个ID查看他放出的所有瓶子。
		//要先经过判断
		int uid = action.getLoginUser().getId();

		//注意这里要输入每页显示的数据量
		int totalCount = SqlUtil
				.getIntResult("select count(distinct bottle_id) from bottle_viewer where user_id="
						+ uid, 5);
		PagingBean paging = new PagingBean(action, totalCount,
				COUNT_PRE_PAGE, "p");

		//取得要显示的页数。如果第一次访问，则从第1页开始
		int pageNow = paging.getCurrentPageIndex();

		//取得分页
		ArrayList al = service.getRevBottleById(uid, pageNow,
				COUNT_PRE_PAGE);
	%>
	在你身边曾经划过的尘缘，追忆？或是重新拾起？
	<br />
	<%
		if (al.size() != 0) {
			for (int i = 0; i < al.size(); i++) {
				BottleBean bb = (BottleBean) al.get(i);
	%><%=pageNow * COUNT_PRE_PAGE + i + 1%>.
	<a href="read.jsp?id=<%=bb.getId()%>&amp;f=reve"><%=StringUtil.toWml(bb.getTitle())%></a>
	<br />
	<%
		}
	%><%=paging.shuzifenye("reversion.jsp?id=" + uid, true,
								"|", response)%>
	<%
		} else {
	%>我还没回复过漂流瓶~<br/><%
		}
	%><a href="bottles.jsp">继续捞取</a><br/>
	<a href="list.jsp">我的漂流瓶历程</a><br/>
	<a href="index.jsp">返回漂流瓶首页</a><br/>
	<a href="../../friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>