<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.*"%><%!
	static TeamService service = new TeamService();
	static String getChatString(int id,List chatList, int start, int limit) {
		if(start >= chatList.size())
			return "";
		StringBuilder sb = new StringBuilder();
		int i=0;
		ListIterator iter = chatList.listIterator(start);
		while(iter.hasNext()) {
			TeamChatBean chat = (TeamChatBean)iter.next();
			sb.append(++i);
			sb.append(".\t");
			if(!chat.isFlagAct()) {
				if(chat.getFromId() != 0) {		// 0 表示加入等信息
					sb.append("<a href=\"");
					sb.append(("queryUserInfo.jsp?id=" + chat.getFromId()));
					sb.append("\">");
					sb.append(StringUtil.toWml(chat.getFromName()));
					sb.append("</a>");
				} else {
					sb.append(StringUtil.toWml(chat.getFromName()));
				}
				sb.append(':');
			}
			sb.append(StringUtil.toWml(chat.getContent()));
			sb.append('(');
			sb.append(DateUtil.sformatTime(chat.getTime()));
			sb.append(')');
			sb.append("<a href=\"team.jsp?id="+id+"&d="+chat.getId()+"&dt="+chat.getTeamId()+"\" onclick=\"return confirm('确定删除?')\">删除</a><br/>");
			if(limit <= 1)
				break;
			limit--;
		}
		return sb.toString();
	}
	static int NUMBER_OF_PAGE = 30;
%><%

CustomAction action = new CustomAction(request);
int id = action.getParameterInt("id");	// team id

int del = action.getParameterIntS("d");
if(del>=0){
	int delt = action.getParameterIntS("dt");
	SqlUtil.executeUpdate("delete from team_chat where id=" + del, 3);
	TeamAction.getTeam(delt).setChatList(null);
}


TeamBean team = TeamAction.getTeam(id);

PagingBean paging;
List chatList = null;
if(team==null){
	paging = new PagingBean(action, 9999,NUMBER_OF_PAGE,"p");
	chatList = service.getChatList("1 order by id desc limit " + paging.getStartIndex()+"," + NUMBER_OF_PAGE);
} else {
	chatList = team.getChatList();
	paging = new PagingBean(action, chatList.size(),NUMBER_OF_PAGE,"p");
}

%>
<html>
<head><link href="../farm/common.css" rel="stylesheet" type="text/css">
</head>
<body>
<%if(team==null){%>
<%=getChatString(id,chatList, 0, NUMBER_OF_PAGE)%>
<%}else{%>
<%=getChatString(id,chatList, paging.getStartIndex(), NUMBER_OF_PAGE)%>
<%}%>
<br/><%=paging.shuzifenye("team.jsp?id=" + id, true, "|", response)%>
<body>
</html>