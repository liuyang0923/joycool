<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.wxsj.bean.stage.*,net.wxsj.action.stage.*,java.util.*,net.wxsj.util.*,net.joycool.wap.bean.PagingBean,net.joycool.wap.util.PageUtil"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%
response.setHeader("Cache-Control","no-cache");

StageAction action = new StageAction();
action.index(request, response);

ArrayList list = (ArrayList) request.getAttribute("list");
PagingBean paging = (PagingBean) request.getAttribute("paging");

int i, count;
QuestionBean question = null;
%>
<wml>
<card title="定向越野任务">
<p align="left">
<%=BaseAction.getTop(request, response)%>
完成任务能拿奖<br/>
--------<br/>
下面每个任务为一道选择题，请仔细阅读提示到相应的网址寻找正确答案，答对的话就能获得乐币、道具、经验值任一个奖励，每题只能选一次，每人每天限答三题，好好珍惜机会哦：）<br/>
今天的任务是：<br/>
<%
count = list.size();
for(i = 0; i < count; i ++){
	question = (QuestionBean) list.get(i);
%>
<%=(i + 1)%>.<a href="question.jsp?id=<%=question.getId()%>"><%=StringUtil.toWml(question.getTitle())%></a><br/>
<%
}
if(count == 0){
%>
任务已全部结束，下次请早！<br/>
<%
}

String fenye = PageUtil.shuzifenye(paging, paging.getPrefixUrl(), false, "|", response);
if(fenye != null && !"".equals(fenye)){
%>
<%=fenye%><br/>
<%
}
%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>