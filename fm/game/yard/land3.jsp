<%@ page language="java" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
String tip = "";
int result = 0;
int id = action.getFmId();
FamilyUserBean fmUser = action.getFmUser();
int fmId = fmUser.getFmId();
if(id == 0){
	if(fmId > 0){
		id = fmId;
	}else{
		response.sendRedirect("/fm/index.jsp");
		return;
	}
}
YardBean yard = action.getYardByID(id);
if (yard.getLandRank() > YardAction.LAND_MAX_RANK){
	response.sendRedirect("land2.jsp");
	return;
}
int comfirm = action.getParameterIntS("c");
if(comfirm >= 0 && comfirm == yard.getLandRank()){
	// 买地
	if(!fmUser.isflagYard()){
		action.tip("tip","!只有餐厅大厨才有这个权限!");
	} else if (!yard.buyLand()){
		action.tip("tip","!您的资金不足!");
	} else {
		action.tip("tip","!土地购买成功!");
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="培育中心"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%><%=action.getTip()%><br/><%}%>
购买第<%=yard.getLandCount() + 1%>块地,需要资金<%=YardAction.moneyFormat(YardBean.landPrice[yard.getLandRank()])%>元.购买吗?<br/>
<a href="land3.jsp?c=<%=yard.getLandRank()%>">确认购买</a><br/>
<a href="land2.jsp">返回培育中心</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>