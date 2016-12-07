<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control", "no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="银行帮助">
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行经理：乐酷银行目前开放的主要功能，是存款和贷款。您可以把乐币存在这里，以防各种不测;贷款额度，跟您的等级有关。等级越高的人，越容易贷款。预期不还贷款，要降级的哦。有什么需要服务的吗？<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>
