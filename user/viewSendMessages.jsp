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
String condition = "from_user_id = " + loginUser.getId() +" and flag != 1 ORDER BY id DESC LIMIT " + pageIndex
                * MESSAGE_PER_PAGE + ", " + (MESSAGE_PER_PAGE+1);

Vector messageList = messageService.getMessageList(condition);

int i, count;
MessageBean message;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="我的信箱">
<p align="left">
<%=BaseAction.getTop(request, response)%>
--我发送的信件--<br/>
<%
String id = "";
count = messageList.size();
for(i = 0; i < count&&i<MESSAGE_PER_PAGE; i ++){
	message = (MessageBean) messageList.get(i);
	id=id+message.getId()+",";
%>
<%=(i + 1)%>.->[<%if(message.getToUser().getUs2()!=null){%><%=message.getToUser().getUs2().getHatShow()%><%}%><a href="ViewUserInfo.do?userId=<%=message.getToUser().getId()%>"><%=StringUtil.toWml(message.getToUser().getNickName())%></a>]：<%=StringUtil.toWml(message.getContent())%>(<%=message.getSendDatetime()%>)
<br/>
<%
}
%>
<br/>
当前第<%=(pageIndex + 1)%>页<br/>
<%if(count>MESSAGE_PER_PAGE){%><a href="ViewSendMessages.do?p=<%=(pageIndex + 1)%>">下一页</a><%}else{%>下一页<%}%>
|<%if(pageIndex > 0){%><a href="ViewSendMessages.do?p=<%=(pageIndex - 1)%>">上一页</a><%}else{%>上一页<%}%>
<%if(pageIndex > 0 || count>MESSAGE_PER_PAGE){%><br/><%}%>
跳到第<input name="p1"  maxlength="5" value="0"/>页 <anchor title="确定">跳转
    <go href="ViewSendMessages.do" method="post">
    <postfield name="p1" value="$p1"/>
    </go>
    </anchor><br/>
<%if(!"".equals(id)){%>
<a href="delSendMessage.jsp?id=<%=id%>" title="进入">删除本页全部信件</a><br/><br/>
<%}%>
<a href="messageIndex.jsp" title="进入">信件管理</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>