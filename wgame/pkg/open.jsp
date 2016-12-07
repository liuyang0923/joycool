<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.friend.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
PkgAction action = new PkgAction(request);
action.open();
if(action.isResult("tip")){
	response.sendRedirect("my2.jsp");
	return;
}
UserBean loginUser = action.getLoginUser();
PkgBean bean = (PkgBean)request.getAttribute("pkg");
if(bean==null||bean.getToId()!=loginUser.getId()){
	response.sendRedirect("my2.jsp");
	return;
}
UserBean user = UserInfoUtil.getUser(bean.getUserId());
PkgTypeBean type = (PkgTypeBean)action.getPkgType(bean.getType());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="礼品店">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=type.getName()%>(<%=user.getNickNameWml()%>)<br/>
<%=StringUtil.toWml(bean.getContent())%><br/>
<%if(bean.getMoney()>0){%>获得红包<%=StringUtil.bigNumberFormat(bean.getMoney())%>乐币<br/><%}%>
<%if(bean.getItem().length()>0){%>获得了礼品<%=bean.getItemName()%><br/><%}%>

<a href="view.jsp?id=<%=bean.getId()%>">确定</a><br/>
<br/>
<a href="my2.jsp">返回我的礼包列表</a><br/>
<a href="index.jsp">返回礼包店首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>