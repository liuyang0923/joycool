<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.wxsj.bean.mall.*,net.wxsj.action.mall.*,java.util.*,net.wxsj.util.*"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%
response.setHeader("Cache-Control","no-cache");

MallAction action = new MallAction();
action.index(request, response);

ArrayList hotTagList = (ArrayList) request.getAttribute("hotTagList");
ArrayList hotAreaTagList = (ArrayList) request.getAttribute("hotAreaTagList");
ArrayList newSellList = (ArrayList) request.getAttribute("newSellList");
ArrayList newBuyList = (ArrayList) request.getAttribute("newBuyList");

int i, count;
TagBean tag = null;
AreaTagBean aTag = null;
InfoBean info = null;
%>
<wml>
<card title="乐乐卖场">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="logo.gif" alt="乐乐卖场"/><br/>
<a href="post.jsp">发布新交易</a>|<a href="myPost.jsp">我的交易</a><br/>
输入感兴趣的商品名称：<br/>
<input type="text" name="name" maxlength="10"/><br/><anchor>找到商家买下来
  <go href="infoList.jsp" method="post">
    <postfield name="name" value="$name"/>
	<postfield name="infoType" value="1"/>	
  </go>
</anchor><br/><anchor>找到顾客卖出去
  <go href="infoList.jsp" method="post">
    <postfield name="name" value="$name"/>
	<postfield name="infoType" value="0"/>
  </go>
</anchor><br/>
最新发布的交易：<br/>
<%
count = newSellList.size();
for(i = 0; i < count; i ++){
	info = (InfoBean) newSellList.get(i);	
%><%=(i + 1)%>.<a href="info.jsp?id=<%=info.getId()%>">[卖]<%=StringUtil.toWml(info.getName())%></a><br/><%
}
%>
--------<br/>
<%
count = newBuyList.size();
for(i = 0; i < count; i ++){
	info = (InfoBean) newBuyList.get(i);	
%><%=(i + 1)%>.<a href="info.jsp?id=<%=info.getId()%>">[买]<%=StringUtil.toWml(info.getName())%></a><br/><%
}
%>
<a href="infoList.jsp">查看全部信息>></a><br/>
通过信息编号搜索：<br/>
<input type="text" name="id" maxlength="10"/><anchor>搜
  <go href="infoList.jsp" method="post">
    <postfield name="id" value="$id"/>
  </go>
</anchor><br/>
通过发布者ID搜索：<br/>
<input type="text" name="userId" maxlength="10"/><anchor>搜
  <go href="infoList.jsp" method="post">
    <postfield name="userId" value="$userId"/>
  </go>
</anchor><br/>
分类热点：<%
count = hotTagList.size();
for(i = 0; i < count; i ++){
	tag = (TagBean) hotTagList.get(i);
	if(i > 0){
		out.println(" ");
	}
%><a href="infoList.jsp?tagId=<%=tag.getId()%>"><%=tag.getName()%></a><%
}
%> <a href="tagList.jsp">更多</a><br/>
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
必读:<a href="/Column.do?columnId=9631">乐乐卖场规则</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>