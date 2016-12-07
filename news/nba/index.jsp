<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.news.nba.*,java.util.*"%><%
	response.setHeader("Cache-Control","no-cache");
	NbaAction action = new NbaAction(request);
	List huojian = NbaAction.service.getNews(" flag=1 order by create_time desc limit 3");
	List lanwang = NbaAction.service.getNews(" flag=2 order by create_time desc limit 3");
	List huren = NbaAction.service.getNews(" flag=3 order by create_time desc limit 3");
	List more = NbaAction.service.getNews(" flag=4 order by create_time desc limit 3");
	List hot = NbaAction.service.getNews(" flag=5 order by create_time desc limit 3");
	List livelist = NbaAction.service.getMatchList(" static_value=2");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="NBA首页">
<p>
<%=BaseAction.getTop(request, response)%>
<a href="match.jsp">赛程查询</a>|<a href="rank.jsp">常规赛排名</a><br/>
=比赛直播间=<br/><%
if(livelist != null && livelist.size() > 0){
	for(int i = 0; i < livelist.size(); i++){
		BeanMatch bm = (BeanMatch)livelist.get(i);
	%><a href="alive.jsp?mid=<%=bm.getId()%>"><%=bm.getTeam1()%>VS<%=bm.getTeam2()%></a>&#160;<%=action.format(bm.getStartTime(),"HH:mm")%><br/><%
	}
}else{
%>(暂无)<a href="match.jsp">查看最近赛事</a><br/><%
}
%>=火热战报=<br/><%
if(hot != null && hot.size() > 0){
	for(int i = 0; i < hot.size(); i++){
		BeanNews bn = (BeanNews)hot.get(i);
		if(bn.getFlag() == 5){
			%><a href="news.jsp?nid=<%=bn.getId()%>"><%=bn.getName()%></a><br/><%
		}
	}
}
%>=<a href="news1.jsp">姚明—火箭</a>=<br/><%
if(huojian != null && huojian.size() > 0){
	for(int i = 0; i < huojian.size(); i++){
		BeanNews bn = (BeanNews)huojian.get(i);
		if(bn.getFlag() == 1){
			if(bn.getView().equals("")){
			%><a href="news.jsp?nid=<%=bn.getId()%>&amp;from=1"><%=bn.getName()%></a><br/><%
			}else{
			%><a href="news.jsp?nid=<%=bn.getId()%>&amp;from=1">[<%=bn.getView()%>]<%=bn.getName()%></a><br/><%
			}			
		}
	}
}
%>=<a href="news2.jsp">易建联—奇才</a>=<br/><%
if(lanwang != null && lanwang.size() > 0){
	for(int i = 0; i < lanwang.size(); i++){
		BeanNews bn = (BeanNews)lanwang.get(i);
		if(bn.getFlag() == 2){
			if(bn.getView().equals("")){
			%><a href="news.jsp?nid=<%=bn.getId()%>&amp;from=2"><%=bn.getName()%></a><br/><%
			}else{
			%><a href="news.jsp?nid=<%=bn.getId()%>&amp;from=2">[<%=bn.getView()%>]<%=bn.getName()%></a><br/><%
			}			
		}
	}
}
%>=<a href="news3.jsp">科比—湖人</a>=<br/><%
if(huren != null && huren.size() > 0){
	for(int i = 0; i < huren.size(); i++){
		BeanNews bn = (BeanNews)huren.get(i);
		if(bn.getFlag() == 3){
			if(bn.getView().equals("")){
			%><a href="news.jsp?nid=<%=bn.getId()%>&amp;from=3"><%=bn.getName()%></a><br/><%
			}else{
			%><a href="news.jsp?nid=<%=bn.getId()%>&amp;from=3">[<%=bn.getView()%>]<%=bn.getName()%></a><br/><%
			}			
		}
	}
}
%>=<a href="news4.jsp">诸强动态</a>=<br/><%
if(more != null && more.size() > 0){
	for(int i = 0; i < more.size(); i++){
		BeanNews bn = (BeanNews)more.get(i);
		if(bn.getFlag() == 4){
			if(bn.getView().equals("")){
			%><a href="news.jsp?nid=<%=bn.getId()%>&amp;from=4"><%=bn.getName()%></a><br/><%
			}else{
			%><a href="news.jsp?nid=<%=bn.getId()%>&amp;from=4">[<%=bn.getView()%>]<%=bn.getName()%></a><br/><%
			}			
		}
	}
}
%><a href="../../jcforum/forum.jsp?forumId=1268">体坛风云&gt;进入论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>