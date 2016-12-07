<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%>
<%@ page import="net.joycool.wap.framework.BaseAction,jc.news.nba.*,java.util.*,net.joycool.wap.util.*"%><%
	response.setHeader("Cache-Control","no-cache");
	NbaAction action = new NbaAction(request);
	List list = action.service.getRank(" del=0 order by id desc");
	String[] loc = {"大西洋赛区:","东南赛区:","中部赛区:","西南赛区:","西北赛区:","太平洋赛区:","东部赛区:","西部赛区:"};
	int p = action.getParameterInt("p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="常规赛排名"><p>
<%=BaseAction.getTop(request, response)%>
<a href="rule.jsp">常规赛排名规则</a><br/>排名|球队|胜|负|胜率|胜场差<br/><%
if(list != null && list.size() > 0){
	if(p == 2){
		for(int i = 0; i < list.size(); i++){
			BeanRank br = (BeanRank)list.get(i);
			if(br.getFlag() != 1){
		  		%><%=loc[br.getLoc()]%><br/>
		  		<%=StringUtil.toWmlIgnoreAnd(br.getCont())%><br/>———————————<br/><%
			}
		}
		%><a href="rank.jsp">东西部排名</a><br/><%
	}else{
		for(int i = 0; i < list.size(); i++){
			BeanRank br = (BeanRank)list.get(i);
			if(br.getFlag() == 1){
		  		%><%=loc[br.getLoc()]%><br/>
		  		<%=StringUtil.toWmlIgnoreAnd(br.getCont())%><br/>———————————<br/><%
			}
		}
		%><a href="rank.jsp?p=2">分区排名</a><br/><%
	}
}else{
%>暂无排名<br/><%
}%><a href="index.jsp">返回NBA专题</a><br/><%=BaseAction.getBottom(request, response)%></p>
</card>
</wml>