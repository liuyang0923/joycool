<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="java.util.Date"%><%@ page import="net.joycool.wap.util.DateUtil"%><%@ page import="java.text.SimpleDateFormat"%><%@ page import="java.util.Calendar"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.bean.friend.FriendMarriageBean"%><%@ page import="net.joycool.wap.util.Constants"%><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷管理员">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
UserBean loginUser= (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
int marriageId=StringUtil.toInt(request.getParameter("marriageId"));
FriendAction action=new FriendAction(request);	
FriendMarriageBean marriage=action.getOnesMarriage(marriageId);
if(marriage==null){
%>
该婚礼不存在<br/>
<%
}
else if(action.isOrderTime()>0 && action.isOrderTime()==marriageId && action.changeTime()>0){	
	session.setAttribute("engage", "true");
	String createTime=marriage.getCreateDatetime();
	String startTime=null;
	Date date=DateUtil.parseDate(createTime, "yyyy-MM-dd");
	String tip=(String)request.getAttribute("tip"); 
	if(tip!=null){
%>
	 <%=tip%>
<%
    }
%>
甜蜜的新婚燕尔，定下婚礼的佳期吧！千万别忘了和另一半商量好再决定哦！<br/>
日：请选择   <select name="marriagedate" >
<%
      Date dates= DateUtil.rollDate(date,0);
       startTime=DateUtil.formatDate(dates,"yyyy-MM-dd");
       %>
      <option value=" <%=startTime%>"><%=startTime%></option>
    <%    for(int i=0;i<14;i++){	
             dates= DateUtil.rollDate(date,1);
	    startTime=DateUtil.formatDate(dates,"yyyy-MM-dd");
	%><option value=" <%=startTime%>"><%=startTime%></option>
<%  }%>
	</select><br/>
时：请选择  ：<select name="marriagetime" >
<%
    for(int i=0;i<24;i++){
    %><option value=" <%=i%>"><%=i%></option>
<%  }%>
	</select><br/>
喜糖的价格：<input type="text" name="price" value=""/> 乐币/个<br/>
喜糖的数量：<input type="text" name="number" value=""/> 个<br/>
选择婚礼形式 <select name="marriageform" >
<option value="1">中式</option>
<option value="2">西式</option>
<option value="3">旅行</option>
	</select><br/>
婚礼宣言
<input type="text" name="content" value=""/><br/>
 <anchor title="决定">决定
    <go href="/friend/FriendMarriage.do" method="post">
    <postfield name="content" value="$content"/>
    <postfield name="marriageId" value="<%=marriageId%>"/>
    <postfield name="price" value="$price"/>
     <postfield name="number" value="$number"/>
     <postfield name="marriagedate" value="$marriagedate"/>
     <postfield name="marriagetime" value="$marriagetime"/>
     <postfield name="marriageform" value="$marriageform"/>
    </go>
    </anchor><br/>
<a href="/friend/marriage.jsp">返回结婚礼堂</a><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%
}
else if(action.isOrderTime()>0 && action.isOrderTime()==marriageId && action.changeTimeBefore()!=0){
    String tip=(String)request.getAttribute("tip"); 
	if(tip!=null){%>
	<font color="red">
	<%=tip%>
	</font><br/>
	<%}
    if(request.getParameter("time")!=null){
    	session.setAttribute("engage", "true");
        String createTime=marriage.getCreateDatetime();
	    String startTime=null;
	    Date date=DateUtil.parseDate(createTime, "yyyy-MM-dd");%>
	     日：请选择   <select name="marriagedate" >
<%          Date dates= DateUtil.rollDate(date,0);
       startTime=DateUtil.formatDate(dates,"yyyy-MM-dd");
       %>
      <option value=" <%=startTime%>"><%=startTime%></option>
    <%    for(int i=0;i<14;i++){	
             dates= DateUtil.rollDate(date,1);
            startTime=DateUtil.formatDate(dates,"yyyy-MM-dd");
	%>
    <option value=" <%=startTime%>"><%=startTime%></option>
<%      }%>
	</select><br/>
时：请选择  ：<select name="marriagetime" >
<%
		for(int i=0;i<24;i++){%>
    	<option value=" <%=i%>"><%=i%></option>
    	<%}%>
	</select><br/>
选择婚礼形式 <select name="marriageform" >
<option value="1">中式</option>
<option value="2">西式</option>
<option value="3">旅行</option>
	</select><br/>
	<anchor title="决定">决定
    <go href="/friend/FriendMarriage.do" method="post">
    <postfield name="marriageId" value="<%=marriageId%>"/>
     <postfield name="marriagedate" value="$marriagedate"/>
     <postfield name="marriagetime" value="$marriagetime"/>
     <postfield name="marriageform" value="$marriageform"/>
    </go>
    </anchor><br/>
<%
	}
	if(request.getParameter("candy")!=null){ 
		session.setAttribute("engage", "true");
	%>
甜蜜的新婚燕儿，增加糖后上次剩余的喜糖被系统回收哈哈！<br/>
喜糖的价格：<input type="text" name="price" value=""/> 乐币/个<br/>
喜糖的数量：<input type="text" name="number" value=""/> 个<br/>
	 <anchor title="决定">决定
    <go href="/friend/FriendMarriage.do" method="post">
    <postfield name="marriageId" value="<%=marriageId%>"/>
    <postfield name="price" value="$price"/>
     <postfield name="number" value="$number"/>
    </go>
    </anchor><br/>

<%
	}
	FriendMarriageBean marriages=action.getOnesMarriage(marriageId);
	String time=marriages.getMarriageDatetime();
	String showTime=time.substring(5,7)+"月"+time.substring(8,10)+"日"+time.substring(11,13)+"点:";
%>
甜蜜的新婚燕尔,您已经定下了婚期并定好了喜糖<br/>
婚期<%=showTime%><br/>
喜糖价格<%=marriages.getCandyPrice()%><br/>
喜糖总数<%=marriages.getCandyNum()%><br/>
剩余喜糖数量<%=marriages.getCandyRemain()%><br/>
<a href="/friend/engage.jsp?time=1&amp;marriageId=<%=marriageId%>">更改婚期及婚礼方式</a><br/>
<a href="/friend/engage.jsp?candy=1&amp;marriageId=<%=marriageId%>">增加喜糖</a><br/>
<a href="/friend/marriage.jsp">返回结婚礼堂</a><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%
}
else if(action.isOrderTime()>0 && action.isOrderTime()==marriageId &&action.changeSuger()>0){
	 String tip=(String)request.getAttribute("tip"); 
	 if(tip!=null){%>
	 <font color="red">
	 <%=tip%>
	 </font><br/>
	 <%}
	 if(request.getParameter("candy")!=null){
		 session.setAttribute("engage", "true");
	 %>
甜蜜的新婚燕儿，增加糖后上次剩余的喜糖被系统回收哈哈！<br/>
喜糖的价格：<input type="text" name="price" value=""/> 乐币/个<br/>
喜糖的数量：<input type="text" name="number" value=""/> 个<br/>
	 <anchor title="决定">决定
    <go href="/friend/FriendMarriage.do" method="post">
    <postfield name="marriageId" value="<%=marriageId%>"/>
    <postfield name="price" value="$price"/>
     <postfield name="number" value="$number"/>
    </go>
    </anchor><br/>
<%
    }	
	FriendMarriageBean marriages=action.getOnesMarriage(marriageId);%>
甜蜜的新婚燕尔,现在正在举行您的婚礼<br/>
喜糖价格<%=marriages.getCandyPrice()%><br/>
喜糖总数<%=marriages.getCandyNum()%><br/>
剩余喜糖数量<%=marriages.getCandyRemain()%><br/>
<a href="/friend/engage.jsp?candy=1&amp;marriageId=<%=marriageId%>">增加喜糖</a><br/>
<a href="/friend/marriage.jsp">返回结婚礼堂</a><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%
}
else{%>
哎，可能婚礼可以已经开始了，现在已经不能定婚期了。<br/>
<%
}
%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>