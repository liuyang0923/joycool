<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.service.infc.IJobService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="java.util.Vector" %><%@ page import="java.util.TreeMap" %><%@ page import="net.joycool.wap.bean.job.HandbookingerBean" %><%@ page import="net.joycool.wap.action.job.HandbookingerAction" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="java.util.HashMap" %><%
response.setHeader("Cache-Control","no-cache");
HandbookingerAction action=new HandbookingerAction(request);
Vector handbookingerList=action.getHandbookingerList();
action.getHorseList(request);
HashMap map=(HashMap)session.getAttribute("horseList");
int array[]=new int[8];
array=(int[])session.getAttribute("array");
TreeMap handbookingerMap = action.getHandbookingerMap();
int base=Integer.parseInt((String)handbookingerMap.get(new Integer(Constants.RANDOM_BASE)));%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="赌马游戏">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="<%=("/img/job/handbookinger/logo.gif" )%>" alt="跑马中心图片"/><br/>
骠叔：欢迎来到乐酷跑马场，请仔细研究赛马赔率，慎重下注。<br/>
点击马名下注！<br/>
编号&nbsp;
马名&nbsp;
赔率<br/>
<%for(int i=0;i<8;i++)
{HandbookingerBean handbookinger=(HandbookingerBean)handbookingerList.get(i);
	String name = (String) map.get(i+"");
		int j=i+1;	
%>
<%=i+1%>号&nbsp;&nbsp;&nbsp;
<anchor title="确定"><%=name%>
    <go href="/job/handbookinger/chipIn.jsp?id=<%=j%>" method="post">
    <postfield name="horseName" value="<%=array[i]%>"/>
    <postfield name="compensateName" value="<%=handbookinger.getId()%>"/>
    </go>
</anchor>&nbsp;
<%=handbookinger.getCompensate()%><br/>
<%}
session.setAttribute("compensateList",handbookingerList);%>
<a href="/lswjs/wagerHall.jsp">游戏首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>