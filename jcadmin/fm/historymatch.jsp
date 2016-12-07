<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,jc.family.game.*,java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	GameAction action = new GameAction(request, response);
	String[] weekGame = { "", "龙舟", "雪仗", "问答" };
	String[] state = { "未开玩", "已开玩", "已完赛" };
	List list = action.getHistoryMatch();
	PagingBean paging = null;
	if (list != null && list.size() > 0) {
		paging = new PagingBean(action, list.size(), 10, "p");
	}
%>
<html>
  <head>
    <title>历史赛事</title>
 <link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
   <table border="1">
    <tr><td align="center">序号</td><td align="center">比赛</td><td align="center">状态</td><td align="center">开始时间</td><td align="center">结束时间</td></tr>
		<%
			if(paging != null){
				for (int i = paging.getStartIndex(); i < paging.getEndIndex(); i++) {
					MatchBean bean = (MatchBean) list.get(i);
					if (bean != null) {
			%>
			<tr>
				<td align="center"><%=i + 1%></td>
				<td align="center"><%=weekGame[bean.getType()]%></td>
				<td align="center"><%=state[bean.getState()]%></td>
				<td align="center"><%=DateUtil.sformatTime(bean.getCreateTime())%></td>
				<td align="center"><%=DateUtil.sformatTime(bean.getEndTime())%></td>
			</tr>
			<%
					}
				}
			}
		%>
	</table>
	<%if(paging != null){%>
<%=paging.shuzifenye("historymatch.jsp",false,"|",response)%>
	<%}%>
 <a href="/jcadmin/fm/index.jsp">返回游戏管理主页面</a>
  </body>
</html>
