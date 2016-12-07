<%@ page language="java" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
int fmId = action.getFmId();
if(fmId < 0){
	response.sendRedirect("/fm/index.jsp");
	return;
}
YardBean yard = action.getYardByID(fmId);
YardDeployBean deploy=(YardDeployBean)action.getAttribute2("rid");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="配菜间"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(deploy==null || "".equals(deploy.getMaterial()) || deploy.getMaterial() == null){
	%>没有配菜,偷懒?该打!<br/><%
}else{
	int count=action.yardService.getIntResult("select count(id) from fm_yard_deploy where fmid="+deploy.getFmid()+" and proto_id="+deploy.getProtoId());
	if(!yard.checkMaterial(deploy.getMaterialList())){
		%>原料已经被别人用了,动作慢?该打!<br/><%
		action.removeAttribute2("rid");
	}else if(count>=10){
		%>配菜失败!配好的菜已经堆满了厨房,先去烹饪吧!<br/><%
		action.removeAttribute2("rid");
	}else if(yard.compose(deploy.getMaterialList(),0,null,0)==0){
		action.removeAttribute2("rid");
		action.yardService.upd("insert into fm_yard_deploy(fmid,proto_id,material) values (" + deploy.getFmid() + "," + deploy.getProtoId()+",'"+deploy.getMaterial()+"')");
		%>孺子可教也~加油,将来的大厨就是你啊~<br/><%
	}else{
		%>什么?你做什么东西了?<br/><%
	}
}
%><a href="deploy.jsp">返回配菜间</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>