<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.news.nba.*,java.util.*,net.joycool.wap.bean.*,net.joycool.wap.util.*"%><%
	response.setHeader("Cache-Control","no-cache");
	NbaAction action = new NbaAction(request);
	List ml = NbaAction.service.getMatchList(" del=0 and (team1 like'"+ StringUtil.toSqlLike("'奇才'")+ "' or team2 like'"+ StringUtil.toSqlLike("'奇才'")
				+ "') and start_time > now() and del=0 order by start_time limit 5");
	List list = NbaAction.service.getNews(" flag=2 order by create_time desc");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="=易建联—奇才="><p>
<%=BaseAction.getTop(request, response)%>=球队赛程=<br/><% 
if(ml != null && ml.size() > 0){
	PagingBean paging = new PagingBean(action, ml.size(), 10, "p");
	for(int i=paging.getStartIndex();i<=paging.getEndIndex()-1;i++){
		BeanMatch bm = (BeanMatch)ml.get(i);
		%><%=action.format(bm.getStartTime(),"MM月-dd日 HH:mm")%>&#160;<%
		if(bm.getTeam1().contains("奇才")){
		%>VS<%=bm.getTeam2()%><br/><%
		}else{
		%>VS<%=bm.getTeam1()%><br/><%
		}
	}
	%><%=paging.shuzifenye("news2.jsp?jcfr=1", true, "|", response)%><%
}
%>=球队新闻=<br/><%
if(list != null && list.size() > 0){
	PagingBean paging = new PagingBean(action, list.size(), 10, "pg");
	for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
		BeanNews bn = (BeanNews)list.get(i);
		if(bn.getFlag() == 2){
			if(bn.getView().equals("")){
			%><a href="news.jsp?nid=<%=bn.getId()%>&amp;from=2"><%=bn.getName()%></a><br/><%
			}else{
			%><a href="news.jsp?nid=<%=bn.getId()%>&amp;from=2">[<%=bn.getView()%>]&#160;<%=bn.getName()%></a><br/><%
			}
		}
  	}
%><%=paging.shuzifenye("news2.jsp?jcfr=1", true, "|", response)%><%
}
 %><a href="index.jsp">返回NBA专题</a><br/><%=BaseAction.getBottom(request, response)%></p>
</card>
</wml>