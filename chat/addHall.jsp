<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.chat.JCRoomBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.Constants"%><%
response.setHeader("Pragma","No-cache"); 
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<head>
<meta http-equiv="Cache-Control" content="max-age=0" forua="true"/>
</head>

<%
// 添加房间步骤标志
String step = (String)request.getAttribute("step");
if (step == null){
	// 从jsp页面进时(主要是其它页面返回该页面第1和第5步时)
	step = request.getParameter("step");
}
if(step == null){
   step = "1";
}
// 上一级页面url
String backTo = (String)request.getAttribute("backTo");
if(backTo == null){
	// 从jsp页面进第1步时
	backTo = request.getParameter("backTo");
}
if(backTo == null){
	backTo = BaseAction.INDEX_URL;
}

// 当前页url
String currentUrl = request.getRequestURL().toString();
currentUrl = URLEncoder.encode(currentUrl+"?step="+step,"UTF-8");

if ("5".equals(step)){
	// 添加成功页，从其它页返回该页是不必调用action
	currentUrl = "addHall.jsp?step=5";
}

// 后台参数验证提示信息
String message = "";
if (request.getAttribute("message") != null){
	message = "<font color='red'>"+(String)request.getAttribute("message")+"</font>";
}
// 开始添加操作
if (("1").equals(step)){
%>
<!--第1步-->
<card title="自建聊天室">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<br/>
<%=message%>
自建聊天室（房间）<br/>

聊天室名称：（不超过10个字）<br/>
<input type="text" name="name" maxlength="10"/><br/>
    <anchor title="确定提交">确定
    <go href="AddHall.do?step=2&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="name" value="$name"/>
    </go>
    </anchor>
<br/><br/>
    <a href="<%=(backTo.replace("&", "&amp;"))%>" title="返回上一级">返回上一级</a><br/>
    <a href="hall.jsp?roomId=<%=Constants.CHAT_BIGROOM_ID%>" title="进入聊天大厅">进入聊天大厅</a><br/>   


<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if (("2").equals(step)){
%>
<!--第2步-->
<card title="自建聊天室">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<br/>
<%
if (!"".equals(message)){
	if (message.indexOf("冲值") != -1){
	%>
	<%=message%><br/>
	<%--
	<a href="../sysPayment.jsp" title="我要冲值">我要冲值</a><br/>
	--%>
	<%
	}else{
	%>
	<%=message%><br/>
	<%
	}
}
%>

自建聊天室（房间）<br/>
您要建立多大的房间？<br/>
<input type="text" name="maxOnlineCount" maxlength="10" size="14"/> 平米<br/>
(最多这么多人同时在)<br/>
付费方式：<br/>
购买(每平米10万) 
    <anchor title="选择">选择
    <go href="AddHall.do?step=3&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="maxOnlineCount" value="$maxOnlineCount"/>
    <postfield name="payDays" value=""/>
    <postfield name="payWay" value="0"/>
    </go>
    </anchor>
<br/>
租用(每天每平米1千，至少5天)
<br/>
租用时间<input type="text" name="payDays" maxlength="8" size="14"/> 天 
    <anchor title="选择"> 选择
    <go href="AddHall.do?step=3&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="maxOnlineCount" value="$maxOnlineCount"/>
    <postfield name="payDays" value="$payDays"/>
    <postfield name="payWay" value="1"/>
    </go>
    </anchor>
<br/><br/>
<a href="AddHall.do?step=1" title="返回上一级">返回上一级</a><br/>
<a href="hall.jsp?roomId=<%=Constants.CHAT_BIGROOM_ID%>" title="进入聊天大厅">进入聊天大厅</a><br/> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if (("3").equals(step)){
%>
<!--第3步-->
<card title="自建聊天室">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<br/>
<%=message%>
您要给您的房间选择一个漂亮的图片吗？ 
    <anchor title="放弃">放弃
    <go href="AddHall.do?step=4&amp;backTo=<%=currentUrl%>" method="post">
	<postfield name="thumbnail" value=""/>
    </go>
    </anchor>
<br/>
    <anchor title="选择"><img src="<%=Constants.CHAT_IMG_PATH%>1.gif" alt="loading..."/>
    <go href="AddHall.do?step=4&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="thumbnail" value="1.gif"/>
    </go>
    </anchor>
    
    <anchor title="选择"><img src="<%=Constants.CHAT_IMG_PATH%>2.gif" alt="loading..."/>
    <go href="AddHall.do?step=4&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="thumbnail" value="2.gif"/>
    </go>
    </anchor>
<br/>
    <anchor title="选择"><img src="<%=Constants.CHAT_IMG_PATH%>3.gif" alt="loading..."/>
    <go href="AddHall.do?step=4&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="thumbnail" value="3.gif"/>
    </go>
    </anchor>
    
    <anchor title="选择"><img src="<%=Constants.CHAT_IMG_PATH%>4.gif" alt="loading..."/>
    <go href="AddHall.do?step=4&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="thumbnail" value="4.gif"/>
    </go>
    </anchor>
<br/>
    <anchor title="选择"><img src="<%=Constants.CHAT_IMG_PATH%>5.gif" alt="loading..."/>
    <go href="AddHall.do?step=4&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="thumbnail" value="5.gif"/>
    </go>
    </anchor>
    
    <anchor title="选择"><img src="<%=Constants.CHAT_IMG_PATH%>6.gif" alt="loading..."/>
    <go href="AddHall.do?step=4&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="thumbnail" value="6.gif"/>
    </go>
    </anchor>
<br/>
    <anchor title="选择"><img src="<%=Constants.CHAT_IMG_PATH%>7.gif" alt="loading..."/>
    <go href="AddHall.do?step=4&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="thumbnail" value="7.gif"/>
    </go>
    </anchor>
    
    <anchor title="选择"><img src="<%=Constants.CHAT_IMG_PATH%>8.gif" alt="loading..."/>
    <go href="AddHall.do?step=4&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="thumbnail" value="8.gif"/>
    </go>
    </anchor>
<br/>
<a href="AddHall.do?step=2" title="返回上一级">返回上一级</a><br/>
<a href="hall.jsp?roomId=<%=Constants.CHAT_BIGROOM_ID%>" title="进入聊天大厅">进入聊天大厅</a><br/> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if (("4").equals(step)){
%>
<!--第4步-->
<card title="自建聊天室">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<br/>
<%=message%>
您的房间允许其他用户自由进入，还是需要您的批准？<br/>
    <anchor title="自由进入">1、自由进入
    <go href="http://wap.joycool.net/chat/AddHall.do?step=5&amp;&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="grantMode" value="0"/>
    </go>
    </anchor>
<br/>
    <anchor title="需管理员批准">2、需管理员批准
    <go href="AddHall.do?step=5&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="grantMode" value="1"/>
    </go>
    </anchor>
    
<br/><br/>
<a href="AddHall.do?step=3" title="返回上一级">返回上一级</a><br/>
<a href="hall.jsp?roomId=<%=Constants.CHAT_BIGROOM_ID%>" title="进入聊天大厅">进入聊天大厅</a><br/> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if (("5").equals(step)){
JCRoomBean foundRoom = (JCRoomBean)session.getAttribute("foundRoom");
%>
<!--第5步-->
<card title="自建聊天室">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<br/>
<%=message%>
您的房间已经建立成功！您以后可以在个人资料里面管理自己的房间。<br/>
您现在可以邀请朋友来您的聊天室啦！<br/>
房间编号：<%=foundRoom.getId()%><br/>
房间名称：<%=StringUtil.toWml(foundRoom.getName())%><br/>
房间大小：<%=foundRoom.getMaxOnlineCount()%><br/>
<%
if ( foundRoom.getThumbnail() != null && !"".equals(foundRoom.getThumbnail()) ){
%>
房间图片：<img src="<%=Constants.CHAT_IMG_PATH%><%=foundRoom.getThumbnail()%>" alt="loading..."/><br/>
<%
}
%>
<a href="editHallLink.jsp?roomId=<%=foundRoom.getId()%>&amp;backTo=<%=currentUrl%>" title="点击可修改">点击可修改</a><br/>
<a href="http://wap.joycool.net/chat/hall.jsp?roomId=<%=foundRoom.getId()%>&amp;backTo=<%=currentUrl%>" title="进入聊天室">进入聊天室</a><br/>
<a href="http://wap.joycool.net/chat/inviteFriends.jsp?roomId=<%=foundRoom.getId()%>&amp;backTo=<%=currentUrl%>" title="邀请别人来">邀请别人来</a><br/>
<a href="roomManager.jsp?roomId=<%=foundRoom.getId()%>&amp;backTo=<%=currentUrl%>" title="设置管理员">设置管理员</a><br/>
<a href="hall.jsp?roomId=<%=Constants.CHAT_BIGROOM_ID%>" title="进入聊天大厅">进入聊天大厅</a><br/> 
<br/><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
%>

</wml>