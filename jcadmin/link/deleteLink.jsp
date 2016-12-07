<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*,
                 java.sql.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.cache.*,
                 net.joycool.wap.bean.*,
                 net.joycool.wap.service.factory.*,
                 net.joycool.wap.service.infc.*,
                 net.joycool.wap.util.db.*"%><%
ILinkService linkService = ServiceFactory.createLinkService();

int id = StringUtil.toInt(request.getParameter("id"));
int moduleId = StringUtil.toInt(request.getParameter("moduleId"));
int subModuleId = StringUtil.toInt(request.getParameter("subModuleId"));

int action = StringUtil.toInt(request.getParameter("action"));
if(action==1){ //添加
	int id = StringUtil.toInt(request.getParameter("id"));