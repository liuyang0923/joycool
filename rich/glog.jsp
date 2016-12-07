<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);
int id = action.getParameterInt("id");

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="大富翁记录">
<p align="left">
<%if(id==0){
PagingBean paging = new PagingBean(action, 500, 10, "p");
int start = paging.getStartIndex();
List list = RichAction.service.getRichLogList(" 1 order by id desc limit "+start+",10");
for(int i=0;i<list.size();i++){
RichLogBean bean = (RichLogBean)list.get(i);
%>
<%=start+i+1%>.<a href="glog.jsp?id=<%=bean.getId()%>"><%=DateUtil.formatDate2(bean.getCreateTime())%></a>(<%=bean.getUserCount()%>人)<br/>
<%}%>
<%=paging.shuzifenye("glog.jsp",false,"|",response)%>
<%}else{

RichLogBean bean = RichAction.service.getRichLog("id="+id);
if(bean==null){
%>没有找到该记录<br/><%}else{%>
该局结束于<%=DateUtil.formatDate2(bean.getCreateTime())%>,用时<%=bean.getInterval()/60%>分钟<br/>
人数:<%=bean.getUserCount()%><br/>
<%=bean.getDetail()%>
<%}%>
<a href="glog.jsp">返回记录列表</a><br/>
<%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>