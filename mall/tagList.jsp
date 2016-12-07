<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.wxsj.bean.mall.*,net.wxsj.action.mall.*,java.util.*,net.wxsj.util.*"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%
response.setHeader("Cache-Control","no-cache");

MallAction action = new MallAction();
action.tagList(request, response);

ArrayList hotTagList = (ArrayList) request.getAttribute("hotTagList");
ArrayList hotAreaTagList = (ArrayList) request.getAttribute("hotAreaTagList");

int i, count;
TagBean tag = null;
AreaTagBean aTag = null;
%>
<wml>
<card title="乐乐卖场">
<p align="left">
<%=BaseAction.getTop(request, response)%>
贸易信息分类：<%
count = hotTagList.size();
for(i = 0; i < count; i ++){
	tag = (TagBean) hotTagList.get(i);
	if(i > 0){
		out.println(" ");
	}
%><a href="infoList.jsp?tagId=<%=tag.getId()%>"><%=tag.getName()%></a><%
}
%> <a href="infoList.jsp?tagId=999999">其他</a><br/>
地区：<%
count = hotAreaTagList.size();
for(i = 0; i < count; i ++){
	aTag = (AreaTagBean) hotAreaTagList.get(i);
	if(i > 0){
		out.println(" ");
	}
%><a href="infoList.jsp?areaTagId=<%=aTag.getId()%>"><%=aTag.getName()%></a><%
}
%> <a href="infoList.jsp?areaTagId=999999">其他</a><br/>
<a href="index.jsp">返回卖场首页</a><br/>
</p>
</card>
</wml>