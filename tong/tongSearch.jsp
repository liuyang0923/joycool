<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.SqlUtil" %><%@ page import="net.joycool.wap.cache.util.*"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
String tongTitle = request.getParameter("tongTitle");
List tongList=null;
String result = "failure";
String tip = null;
if (tongTitle == null || "".equals(tongTitle.replace(" ", ""))) {
tip="请输入帮会名称!";
} else {

tongList = (List) SqlUtil.getIntList("select id from jc_tong where title like'%" + StringUtil.toSql(tongTitle) + "%' order by id limit 20");
if(tongList.size()==0)
	tip="没有搜索到符合条件的帮会!";
else
	result = "success";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("tongCenter.jsp");
%>
<card title="帮会列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=tip%>(3秒后跳转帮会首页)<br/>
<a href="tongCenter.jsp">帮会首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
%>
<card title="帮会">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
for(int i=0;i<tongList.size();i++){
	Integer iid = (Integer)tongList.get(i);
	TongBean tong = TongCacheUtil.getTong(iid.intValue());
	if(tong==null) continue;%>
<%=i+1%>.<a href="tong.jsp?tongId=<%=tong.getId()%>"><%=StringUtil.toWml(tong.getTitle()) %></a><br/>
<%}%><br/>
<a href="tongList.jsp">帮会列表</a><br/>
<a href="tongCenter.jsp">帮会首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>