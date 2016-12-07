<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.action.*,net.joycool.wap.util.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	
	int uid = gardenAction.getParameterInt("uid");
	
	if(uid>0){
		NoticeAction.sendNotice(uid, gardenAction.getLoginUser().getNickName() + "邀请你加入农场"
				, "", NoticeBean.GENERAL_NOTICE, "", "/garden/s.jsp");
		//NoticeAction.sendNotice(uid, gardenAction.getLoginUser().getNickNameWml() + "邀请你加入农场", NoticeBean.GENERAL_NOTICE, "/garden/s.jsp");
	}
	String actions = request.getParameter("action");
	String indexs = request.getParameter("index");
	String pres = request.getParameter("pre");
	String urls = "";
	if(actions!=null){
		urls += "&amp;action="+actions;
	}
	if(indexs!=null){
		urls += "&amp;index="+indexs;
	}
	if(pres != null) {
		urls += "&amp;pre="+pres;
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
邀请发送成功<br/>
<a href="invite.jsp?a=a<%=urls %>">返回继续邀请</a><br/>
<a href="myGarden.jsp">返回我的农场</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>