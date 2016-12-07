<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.util.SqlUtil,net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.*,net.joycool.wap.util.StringUtil,java.util.List,net.joycool.wap.bean.PagingBean,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族"><p align="left"><%=BaseAction.getTop(request, response)%><%
int a=action.getParameterInt("a");
int c=action.service.selectIntResult("select count(id) from fm_home");
PagingBean paging=new PagingBean(action, c, 10, "p");
int p=action.getParameterInt("p");
List list=null;
%>家族游戏排行榜<br/><%
if(a==2){//问答排行榜
list=action.service.selectFmScoreList("1 order by ask_score desc limit "+paging.getStartIndex()+","+paging.getCountPerPage());
%>问答<%
}else{%><a href="topgame.jsp?a=2">问答</a><%}
if(a==3){//龙舟排行榜
list=action.service.selectFmScoreList("1 order by boat_score desc limit "+paging.getStartIndex()+","+paging.getCountPerPage());
%>|龙舟<%
}else{%>|<a href="topgame.jsp?a=3">龙舟</a><%}
if(a==4){//丛林排行榜
list=action.service.selectFmVSScoreList("game_id=1 order by score desc limit "+paging.getStartIndex()+","+paging.getCountPerPage());
%>|丛林<%
}else{%>|<a href="topgame.jsp?a=4">丛林</a><%}
if(a==5){//水果排行榜
list=action.service.selectFmVSScoreList("game_id=2 order by score desc limit "+paging.getStartIndex()+","+paging.getCountPerPage());
%>|水果<%
}else{%>|<a href="topgame.jsp?a=5">水果</a><%}
if(a==6){//庄园排行榜
list=action.service.selectFmVSScoreList("game_id=3 order by score desc limit "+paging.getStartIndex()+","+paging.getCountPerPage());
%>|庄园<%
}else{%>|<a href="topgame.jsp?a=6">庄园</a><%}
if(a==7){//帝王排行榜
list=action.service.selectFmVSScoreList("game_id=4 order by score desc limit "+paging.getStartIndex()+","+paging.getCountPerPage());
%>|帝王<%
}else{%>|<a href="topgame.jsp?a=7">帝王</a><%}
%><br/><%
if(list!=null&&list.size()>0){
	int count=0;
	for(int i=0;i<list.size();i++){
		FmScore fmScore=(FmScore)list.get(i);
		if(fmScore!=null){
			count++;
			%><%=count+p*10%>.<a href="myfamily.jsp?id=<%=fmScore.getId() %>"><%=fmScore.getFamilyName()%></a>|积分<%=fmScore.getScore(a)%><br/><%
		}
	}
}%><%=paging.shuzifenye("topgame.jsp?a="+a, true, "|", response)%>
<a href="topmore.jsp">家族排行榜</a><br/>
<a href="index.jsp">返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>