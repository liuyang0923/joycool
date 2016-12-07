<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.cache.*,net.joycool.wap.service.factory.*,net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.top.TopAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.top.UserTopBean" %><%
response.setHeader("Cache-Control","no-cache");
TopAction topAction=new TopAction(request);
topAction.credit(request);
Vector creditList=(Vector)request.getAttribute("creditList");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷荣誉公民">
<p align="left">
<%=BaseAction.getTop(request, response)%>
乐酷荣誉公民<br/>
----------<br/>
<%
if(creditList.size()>0){
UserTopBean top=null;
UserBean user=null;
for(int i=0;i<creditList.size();i++){
top=(UserTopBean)creditList.get(i);
user=topAction.getUser(top.getUserId());
if(user==null){
continue;
}
%>
<%=i+1+" "%>
<%if(top.getUserId()!=loginUser.getId()){%>
<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%>
<a href="/user/ViewUserInfo.do?userId=<%=top.getUserId()%>">
<%
String nickname=StringUtil.toWml(user.getNickName());
if(nickname.equals(""))
nickname="乐客"+user.getId();%>
<%=nickname%>
</a><%}else{%>您自己<%}%><br/>
<%}}else{%>
暂时不提供查询荣誉市民功能!<br/>
<%}%><br/><br/>
<%@include file="bottom.jsp"%><br/>
</p>
</card>
</wml>