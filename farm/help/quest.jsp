<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmUserBean farmUser = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
某些任务是必须要完成的,否则可能无法升级或者学到新的技能,或者修行职业技能等等.<br/>
任务的奖励除了钱币和经验,还有职业经验,等级上限,职业转变,五行逆转等等.<br/>
有些任务可以重复完成,一般这种任务将不会给予经验奖励.还有一些任务可以定期完成.<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>