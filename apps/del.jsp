<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="filter.jsp"%><%@ page import="net.joycool.wap.spec.app.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.spec.buyfriends.BeanVisit"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.user.UserAction"%><%! public static AppService service = new AppService();%><%
response.setHeader("Cache-Control","no-cache");
AppAction action = new AppAction(request);
action.view();
boolean result = false;
Map map = action.getAppUserMap();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="删除组件">
<p align="left">
<% 	if(action.isResult("tip")){
		%><%=action.getTip()%><br/><a href="my.jsp">返回我的组件列表</a><br/><%
	} else{
		AppBean app = (AppBean)request.getAttribute("appBean");
		AppAddBean appAdd = (AppAddBean)map.get(new Integer(app.getId()));
		if (appAdd != null){
			appAdd.setUserId(action.getLoginUser().getId());
			result = service.delUserApp(appAdd);
			if (result){
				map.remove(new Integer(app.getId()));
				if(app.getCount()>0)
					app.setCount(app.getCount()-1);
					SqlUtil.executeUpdate("update app set `count`=" + app.getCount() + " where id=" + app.getId(),5);
				%>组件已删除.<br/><a href="my.jsp">返回我的组件列表</a><br/><%
			}
		} else {
			%>你没有安装此插件.<br/><a href="my.jsp">返回我的组件列表</a><br/><%
		}
	}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>