<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request, response);
FarmUserBean farmUser = action.getUser();
int id = action.getParameterInt("id");
if(id>0&&farmUser.getCurTrigger().size() > 0) {
TriggerBean trigger = (TriggerBean)farmUser.getCurTrigger().getFirst();
if(trigger.getId()==id)		// 匹配表示正常阅读
	farmUser.getCurTrigger().removeFirst();
}
action.redirect("/farm/map.jsp");
%>