<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.show.*,java.util.*,net.joycool.wap.bean.PagingBean,net.joycool.wap.bean.*"%><%!
static String[] chggend = {"男","女"};
static String[] genderText = {"女","男"};
%><%
response.setHeader("Cache-Control","no-cache");
CoolShowAction action = new CoolShowAction(request);
TryBean tryShow = (TryBean)session.getAttribute("tryShow");
if(tryShow==null){
	response.sendRedirect("room.jsp");
	return;
}
List tryList = tryShow.getTryList();
PagingBean paging = new PagingBean(action,tryList.size(),10,"p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="试穿历史">
<p><%=BaseAction.getTop(request, response)%>
【试穿历史】<br/><%
for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
Commodity comm = (Commodity)tryList.get(i);
%><%=i+1%>.<a href="consult.jsp?from=1&amp;Iid=<%=comm.getIid()%>"><%=comm.getName()%></a><br/><%}%>
<%=paging.shuzifenye("trylist.jsp",false,"|",response)%>
&lt;&lt;<a href="room.jsp">返回试衣间</a><br/>
<a href="index.jsp">&gt;我的酷秀</a>|<a href="myGoods.jsp">我的物品</a><br/>
&gt;商城<a href="downtown.jsp?gend=1">女装区</a>|<a href="downtown.jsp?gend=2">男装区</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>