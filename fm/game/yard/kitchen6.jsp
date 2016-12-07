<%@ page language="java" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%>
<%! static int COUNT_PRE_PAGE = 10;%>
<%YardAction action=new YardAction(request);
int tmp = 0;
String tip = "";
boolean isThisFamily = false;
int id = action.getFmId();
int fmId = action.getFmId();
if(fmId > 0){
	if (id == fmId)
		isThisFamily = true;
}else{
	response.sendRedirect("/fm/index.jsp");
	return;
}
List deployList = null;	// 配菜列表
YardDeployBean deploy = null;
YardItemProtoBean itemProto  = null;
YardBean yard = action.getYardByID(id);
YardRecipeProtoBean recipe = null;
int[] mat = null;
if (!action.getFmUser().isflagYard()){
	tip = "只有大厨才能进入中级厨房哦.";
} else {
	int deployId = action.getParameterInt("did");
	if (deployId <= 0){
		tip = "参数错误.";
	} else {
		deploy = YardAction.yardService.getYardDeployBean(" id=" + deployId);
		if (deploy == null || deploy.getFmid() != yard.getFmid())
			tip = "配菜不存在,或不是您家族的配菜.";
		else {
			deployList = deploy.getMaterialList();
		}
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="中级厨房"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (deployList != null && deployList.size() > 0){
	for (int i = 0 ; i < deployList.size() ; i++ ){
		mat = (int[])deployList.get(i);	
		if (mat.length == 2){
			itemProto = YardAction.getItmeProto(mat[0]);
			if (itemProto != null){
				%><%=itemProto.getNameWml()%>(<%=mat[1]%>个)<a href="kitchen5.jsp?id=<%=id%>&amp;rid=<%=deploy.getProtoId()%>&amp;in=<%=mat[0]%>&amp;did=<%=deploy.getId()%>">放入</a><br/><%			
			}
		}
	}
} else {
%>无材料.<br/><%
}
} else {
%><%=tip%><br/><%
}%>
<a href="kitchen4.jsp?id=<%=id%>">返回中级厨房</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>