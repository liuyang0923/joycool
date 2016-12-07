<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);
int o = action.getParameterInt("o");
RichUserBean richUser = RichAction.worlds[0].getRichUser(o, false);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=richUser.getName()%><br/>
现金:<%=richUser.getMoney()%><br/>
存款:<%=richUser.getSaving()%><br/>
点券:<%=richUser.getMoney2()%><br/>
位置:<%=richUser.getPosition()%><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>