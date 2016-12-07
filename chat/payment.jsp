<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.chat.JCRoomBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%@ page import="net.joycool.wap.util.Constants"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<head>
<meta http-equiv="Cache-Control" content="max-age=0" forua="true"/>
</head>

<%
JCRoomChatAction action = new JCRoomChatAction(request);

int roomId=action.getParameterInt("roomId");

JCRoomBean room = action.getRoomName("id="+Integer.parseInt(roomId));

if (room == null){
	BaseAction.sendRedirect("/failure.jsp", response);
	return;
}

String backTo = request.getParameter("backTo");
if(backTo == null){
	backTo = BaseAction.INDEX_URL;
}

String currentUrl = PageUtil.getBackTo(request);

String message = "";
String messageFlag = action.checkAddPayment(request);

if ((request.getParameter("action") == null) || (request.getParameter("action") != null && !"".equals(messageFlag))){


	
if (request.getParameter("action") != null){
	message = action.checkAddPayment(request);
	if (!"".equals(message)){
		message = "<font color='red'>"+message+"</font>";
	}
}
%>


<card title="聊天室冲值">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<br/>

聊天室冲值<br/>

您的聊天室(房间)的信息。<br/><br/>
房间编号：<%=room.getId()%><br/>
房间名称：<%=StringUtil.toWml(room.getName())%><br/>
房间大小：<%=room.getMaxOnlineCount()%><br/>
付款方式：<%if (room.getPayWay()==0) out.print("一次买断");else out.print("按日租");%><br/>
到期时间：<%=(room.getExpireDatetime()).substring(0,10)%><br/>
授权模式：<%if (room.getGrantMode()==0) out.print("任何用户可以进入");else out.print("要批准才能进入");%><br/>
当前状态：<%if (room.getStatus()==0) out.print("拍卖中");else if(room.getStatus()==1) out.print("活动中");else out.print("待激活");%><br/>
<%
if ( room.getThumbnail() != null && !"".equals(room.getThumbnail()) ){
%>
房间图片：<img src="<%=Constants.CHAT_IMG_PATH%><%=room.getThumbnail()%>" alt="loading..."/><br/>
<%
}else{

%>
房间图片：无图片<br/>
<%
}
%>
<br/>
<a href="editHallLink.jsp?roomId=<%=roomId%>&amp;backTo=<%=currentUrl%>" title="修改聊天室">修改聊天室</a>
<br/><br/>
<%=message%>
付款类型：
<select title="付款类型" multiple="false" name="payType" >
<option value="0">购买</option>
<option value="1">租赁</option> 
<option value="2">购买扩容</option>
<option value="3">租赁扩容</option>
<option value="4">租赁冲值</option>
</select>
<br/>
付款金额：
<input type="text" name="money"  maxlength="15"/><br/>
冲值描述：
<input type="text" name="remark"/><br/>

    <anchor title="确定提交"><br/>确定提交
    <go href="payment.jsp?action=add&amp;roomId=<%=roomId%>&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="payType" value="$payType"/>
    <postfield name="money" value="$money"/>
    <postfield name="remark" value="$remark"/>
    </go>
    </anchor>
    
<br/><br/>
    <a href="<%=(backTo.replace("&","&amp;"))%>">返回上一级</a><br/>
    <a href="hall.jsp?roomId=<%=Constants.CHAT_BIGROOM_ID%>" title="进入聊天大厅">进入聊天大厅</a><br/>   


<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else{


if(action.updateUserPoint(request)){

	int money = Integer.parseInt(request.getParameter("money"));
	// 更改session中用户的乐币数				
	//action.getLoginUser().getUs().setGamePoint(action.getLoginUser().getUs().getGamePoint()+money);
	
	if(action.addRoomPayment(request)){
		//response.sendRedirect(response
		//		.encodeURL("paymentResult.jsp?rs=1"));
		BaseAction.sendRedirect("/chat/paymentResult.jsp?rs=1", response);
	}else{
		//response.sendRedirect(response
		//		.encodeURL("paymentResult.jsp?rs=0"));
		BaseAction.sendRedirect("/chat/paymentResult.jsp?rs=0", response);
	}
}



}
%>


</wml>