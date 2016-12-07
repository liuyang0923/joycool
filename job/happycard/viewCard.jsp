<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.HappyCardAction"%><%
response.setHeader("Cache-Control","no-cache");
HappyCardAction action=new HappyCardAction();
HappyCardSendBean sendinfo=null;
HappyCardBean card=null;
String sender=null;
String receiver=null;
String sid=null;
if(null!=request.getParameter("id")){
sid=(String)request.getParameter("id");
int sendid=StringUtil.toInt(sid);
sendinfo=action.getJobService().getHappyCardSend("id="+sendid);
if(sendinfo!=null)
{
sender=sendinfo.getSender();
receiver=sendinfo.getReceiver();
int cardid=sendinfo.getCardId();
action.card(cardid,request);
card=(HappyCardBean)request.getAttribute("card");
action.updateCardSend(sendid);
}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="你接收的贺卡">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(null!=sendinfo){%>
<%=StringUtil.toWml(receiver)%>:<br/>
<%=card.getContent()%><br/>                 
----<%=StringUtil.toWml(sender)%><br/>
<img src="<%=card.getImageUrl()%>" alt="loading..." /><br/>
<%}%>
<a href="/job/happycard/index.jsp">返回贺卡首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
