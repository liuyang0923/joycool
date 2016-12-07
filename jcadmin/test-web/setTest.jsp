<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.wxsj.action.test.*"%><%@ page import="net.wxsj.bean.test.*"%><%@ page import="net.wxsj.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.wxsj.framework.*"%><%@ page import="net.wxsj.bean.*"%><%@ page import="net.wxsj.service.infc.*"%><%@ page import="net.wxsj.service.factory.*,java.util.*"%><%
		// 创建service
		INewTestService testService = ServiceFactory.createNewTestService();
		int testId = StringUtil.toInt(request.getParameter("testId"));
		if(testId != -1){
			testService.updateTest("is_closed = (1 - is_closed)", "id = " + testId);
		}

		//response.sendRedirect("testList.jsp");
		BaseAction.sendRedirect("/jcadmin/test-web/testList.jsp", response);
%>