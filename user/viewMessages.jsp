<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.MessageBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%!
static int MESSAGE_PER_PAGE = 5;
static IMessageService messageService = ServiceFactory.createMessageService();
%><%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request);
UserBean loginUser = action.getLoginUser();
int pageIndex = action.getParameterInt("p");
if (pageIndex == 0) {
    pageIndex = action.getParameterInt("p1");
    if(pageIndex > 0){
		pageIndex = pageIndex - 1;
    }
}
String condition = "to_user_id = " + loginUser.getId() +" and flag != 1 ORDER BY id DESC LIMIT " + pageIndex
                * MESSAGE_PER_PAGE + ", " + (MESSAGE_PER_PAGE+1);

Vector messageList = messageService.getMessageList(condition);


int i, count;
MessageBean message;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="我的信箱">
<p align="left">
<%UserInfoUtil.getUser(loginUser.getId()).notice[1]=0;	// 清空新信件数量
%><%=BaseAction.getTop(request, response)%>
--收到的信件--<%=PositionUtil.getCurrentModuleUrl(request)%><br/>
<%
String id = "";
count = messageList.size();
for(i = 0; i < count&&i<MESSAGE_PER_PAGE; i ++){
	message = (MessageBean) messageList.get(i);
	id=id+message.getId()+",";
%><%=(i + 1)%>.<%if(message.getFromUserId()!=100){%>[<a href="ViewUserInfo.do?userId=<%=message.getFromUserId()%>"><%=StringUtil.toWml(message.getFromUser().getNickName())%></a>]<%}else{%>系统信息<%}%>:<%if(message.getContent().indexOf("</a>") == -1){%><%=StringUtil.toWml(message.getContent())%><%}else{%><%=message.getContent()%><%}%>(<%=message.getSendDatetime()%>)<%if(message.getFromUserId()!=100){%><a href="sendMessage.jsp?toUserId=<%=message.getFromUserId()%>">回复</a>|<%}%><a href="ViewMessages.do?p=<%=pageIndex %>&amp;delete=1&amp;messageId=<%=message.getId()%>">删除</a><br/>
<%}%><br/>
当前第<%=(pageIndex + 1)%>页<br/>
<%if(count>MESSAGE_PER_PAGE){%><a href="ViewMessages.do?p=<%=(pageIndex + 1)%>">下一页</a><%}else{%>下一页<%}%>
|<%if(pageIndex > 0){%><a href="ViewMessages.do?p=<%=(pageIndex - 1)%>">上一页</a><%}else{%>上一页<%}%>
<%if(pageIndex > 0 || count>MESSAGE_PER_PAGE){%><br/><%}%>
跳到第<input name="p1"  maxlength="4" value=""/>页
<anchor title="确定">跳转
<go href="ViewMessages.do" method="post">
    <postfield name="p1" value="$p1"/>
</go>
</anchor><br/>
<%if(!"".equals(id)){%><a href="delMessage.jsp?id=<%=id%>">删除本页全部信件</a><br/><br/><%}%>
<a href="messageIndex.jsp">信箱管理</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>