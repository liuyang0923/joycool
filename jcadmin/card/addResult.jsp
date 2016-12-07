<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="net.joycool.wap.action.job.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.util.LoadResource" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.util.SqlUtil" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.job.CardBean" %><%!
public void alterCard(HttpServletRequest request) {
		int typeId =StringUtil.toInt(request.getParameter("typeId"));
		int appearRate = StringUtil.toInt(request.getParameter("appearRate"));
		int valueType = StringUtil.toInt(request.getParameter("valueType"));
		int actionValue = StringUtil.toInt(request.getParameter("actionValue"));
		int actionField = StringUtil.toInt(request.getParameter("actionField"));
		int actionDirection = StringUtil.toInt(request.getParameter("actionDirection"));
		String description = request.getParameter("description");

		String tip = null;
		/**
		 * 检查参数，如果信息格式有误直接跳回用户输入页面，并提示用户！
		 */
		 if (typeId<0) {
			tip = "机会卡参数错误！";
			request.setAttribute("tip", tip);
			return;
		}else if (appearRate<0) {
			tip = "机会卡参数错误！";
			request.setAttribute("tip", tip);
			return;
		}else if (valueType<0) {
			tip = "机会卡参数错误！";
			request.setAttribute("tip", tip);
			return;
		}else if (actionValue<0) {
			tip = "机会卡参数错误！";
			request.setAttribute("tip", tip);
			return;
		}else if (actionField<0) {
			tip = "机会卡参数错误！";
			request.setAttribute("tip", tip);
			return;
		}else if (actionDirection<0) {
			tip = "机会卡参数错误！";
			request.setAttribute("tip", tip);
			return;
		}else if (description == null || description.equals("")) {
			tip = "机会卡内容介绍不能为空！";
			request.setAttribute("tip", tip);
			return;
		}
		CardBean card = new CardBean();
		card.setTypeId(typeId);
		card.setAppearRate(appearRate);
		card.setValueType(valueType);
		card.setActionValue(actionValue);
		card.setActionfield(actionField);
		card.setActionDirection(actionDirection);
		card.setDescription(description);
		ServiceFactory.createJobService().addCard(card);
		// 更新内存
		LoadResource resource = new LoadResource();
		resource.clearCardMap();
		request.setAttribute("tip", "更新成功!");
	}
%>
<%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
alterCard(request);
String tip=(String)request.getAttribute("tip");
%>
<html >
<head>
<title>修改猎物</title>
</head>
<p align="center">
<a href="/jcadmin/card/viewQuarry.jsp">显示机会卡</a>&nbsp;&nbsp;
<a href="/jcadmin/card/addQuarry.jsp">增加机会卡</a>&nbsp;&nbsp;
<a href="/jcadmin/manage.jsp">返回管理中心</a><br/><hr>
</p>
<p align="center"><%=tip%><br/>
<a href="viewQuarry.jsp">返回机会卡首页</a></p>
</html>