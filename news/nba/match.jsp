<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.news.nba.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.bean.*"%><%
	response.setHeader("Cache-Control","no-cache");
	NbaAction action = new NbaAction(request);
	int y = action.getParameterInt("y");
	int m = action.getParameterInt("m");
	int d = action.getParameterInt("d");
	int show = action.getShow();
	int mid = action.getParameterInt("maid");
	if(mid > 0){
		BeanMatch bm = action.getMatchById(mid);
			if(bm != null){
				Date date = new Date(bm.getStartTime());
				y = date.getYear()+1900;
				m = date.getMonth()+1;
				d = date.getDate();
			}
	}
	String[] ss = {"","年","月","日",""};
	if(y/100<1)
		y = 2000 + y;
	List list = action.getMLByDate();
	String[] sta = {"未赛","已完赛","直播中"};
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="=NBA赛程查询="><p>
<%=BaseAction.getTop(request, response)%><%
if(show > 0){
%>请正确输入<%=ss[show]%>!<br/><%
}%><%
if(show < 0){
%><%=y%>年<%=m%>月<%=d%>日赛事:<br/><%
}else{
	Calendar cal = Calendar.getInstance();
	cal.setTimeInMillis(System.currentTimeMillis());
%><%=cal.get(Calendar.YEAR)%>年<%=cal.get(Calendar.MONTH)+1%>月<%=cal.get(Calendar.DAY_OF_MONTH)%>日赛事:<br/><%
}
if (list.size() == 0) {
	list = action.getMLByDate2();
	%>本日没有赛事!<br/>=以往赛事=<br/><%
} 
if(list != null && list.size() > 0){
	for(int i=0;i<list.size();i++){
		BeanMatch bm = (BeanMatch) list.get(i);
		if(bm.getSumLive() > 1){
	%><a href="alive.jsp?mid=<%=bm.getId()%>"><%=bm.getTeam1()%>VS<%=bm.getTeam2()%></a>&#160;<%
		}else{
	%><%=bm.getTeam1()%>VS<%=bm.getTeam2()%>&#160;<%
		}
		if(bm.getStaticValue() == 0){
	%><%=DateUtil.formatDate2(bm.getStartTime())%><%
		}else{
	%><%=StringUtil.toWml(bm.getCode())%><%
		}
	%>&#160;<%=sta[bm.getStaticValue()]%><br/><%
	}
}else{
%>当天没有赛事!<br/><%
}
if(show < 0){
	if(d - 1 <= 0){
		if(m - 1 <= 0){
			if(y <= 1900){
			%>上一日|<%
			}else{
			%><a href="match.jsp?y=<%=y-1%>&amp;m=<%=12%>&amp;d=<%=action.totalday(y-1,12)%>">上一日</a>|<%
			}
		}else{
		%><a href="match.jsp?y=<%=y%>&amp;m=<%=m-1%>&amp;d=<%=action.totalday(y,m-1)%>">上一日</a>|<%
		}
	%><a href="match.jsp?y=<%=y%>&amp;m=<%=m%>&amp;d=<%=d+1%>">下一日</a><br/><%
	}else if(d + 1 > action.totalday(y,m)){
	%><a href="match.jsp?y=<%=y%>&amp;m=<%=m%>&amp;d=<%=d-1%>">上一日</a>|<%
		if(m+1 > 12){
			if(y+1 > 3000){
			%>下一日<br/><%
			}else{
			%><a href="match.jsp?y=<%=y+1%>&amp;m=<%=1%>&amp;d=<%=1%>">下一日</a><br/><%
			}
		}else{
		%><a href="match.jsp?y=<%=y%>&amp;m=<%=m+1%>&amp;d=<%=1%>">下一日</a><br/><%
		}
	}else{
	%><a href="match.jsp?y=<%=y%>&amp;m=<%=m%>&amp;d=<%=d-1%>">上一日</a>|<a href="match.jsp?y=<%=y%>&amp;m=<%=m%>&amp;d=<%=d+1%>">下一日</a><br/><%
	}
}else{
	Calendar cal = Calendar.getInstance();
	cal.setTimeInMillis(System.currentTimeMillis());
	if(cal.get(Calendar.DAY_OF_MONTH) == 1){//每月第一天
		if(cal.get(Calendar.MONTH) == 1){//当月是不是1月
			Calendar cal1 = (Calendar)cal.clone();
			cal1.set(Calendar.YEAR,cal.get(Calendar.YEAR)-1);
			cal1.set(Calendar.MONTH,12);
		%><a href="match.jsp?y=<%=cal1.get(Calendar.YEAR)-1%>&amp;m=12%>&amp;d=<%=cal1.getActualMaximum(Calendar.DAY_OF_MONTH)%>">上一日</a><%
		}else {
			Calendar cal2 = (Calendar)cal.clone();
			cal2.set(Calendar.MONTH,Calendar.MONTH);
		%><a href="match.jsp?y=<%=cal2.get(Calendar.YEAR)%>&amp;m=<%=cal2.get(Calendar.MONTH)+1%>&amp;d=<%=cal2.getActualMaximum(Calendar.DAY_OF_MONTH)%>">上一日</a><%
		}
	}else{
	%><a href="match.jsp?y=<%=cal.get(Calendar.YEAR)%>&amp;m=<%=cal.get(Calendar.MONTH)+1%>&amp;d=<%=cal.get(Calendar.DAY_OF_MONTH)-1%>">上一日</a><%
	}
	%>|<%
	if(cal.get(Calendar.DAY_OF_MONTH) == cal.getActualMaximum(Calendar.DAY_OF_MONTH)){//每月最后一天
		if(cal.get(Calendar.MONTH) == 11){//当月是不是12月
		%><a href="match.jsp?y=<%=cal.get(Calendar.YEAR)+1%>&amp;m=1&amp;d=1">下一日</a><br/><%
		}else{
		%><a href="match.jsp?y=<%=cal.get(Calendar.YEAR)%>&amp;m=<%=cal.get(Calendar.MONTH)+2%>&amp;d=1">下一日</a><br/><%
		}
	}else{
	%><a href="match.jsp?y=<%=cal.get(Calendar.YEAR)%>&amp;m=<%=cal.get(Calendar.MONTH)+1%>&amp;d=<%=cal.get(Calendar.DAY_OF_MONTH)+1%>">下一日</a><br/><%
	}
}%>
年:<input name="y" maxlength="4" format="*N" value="2010"/><br/>月:<input name="m" maxlength="2" format="*N"/><br/>日:<input name="d" maxlength="2" format="*N"/><br/>
<anchor>查询
<go href="match.jsp" method="post">
<postfield name="y" value="$y"/>
<postfield name="m" value="$m"/>
<postfield name="d" value="$d"/>
</go>
</anchor><br/>
<a href="index.jsp">返回NBA首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>