<%@ page language="java" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%!
static int COUNT_PRE_PAGE = 10;%><%
YardAction action=new YardAction(request);
FamilyUserBean fmUser = action.getFmUser();
int fmId = fmUser.getFmId();
if(fmId < 0){
	response.sendRedirect("/fm/index.jsp");
	return;
}
YardBean yard = action.getYardByID(fmId);

int comfirm = action.getParameterIntS("c");
if(comfirm >= 0 && comfirm == yard.getKitchenRank2()){
	if(!fmUser.isflagYard()){
		action.tip("tip","!只有餐厅大厨才有这个权限!");
	} else if(yard.getMoney()<YardBean.kitchenCounts2Money[yard.getKitchenRank2()]){
		action.tip("tip","!资金不足!冰箱升级失败!");
	}else if(yard.getKitchenRank()>9){
		action.tip("tip","!等级暂满!冰箱升级失败!");
	}else {
		yard.addMoney(-YardBean.kitchenCounts2Money[yard.getKitchenRank2()]);
		yard.setKitchenRank2(yard.getKitchenRank2()+1);
		YardAction.getService().executeUpdate("update fm_yard_info set kitchen_rank2="+yard.getKitchenRank2()+" where fmid="+yard.getFmid());
		action.tip("tip","!冰箱升级成功!");
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="冰箱升级"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%><%=action.getTip()%><br/><%}%>
冰箱等级:<%=yard.getKitchenRank2()+1%><br/>
当前冰箱容量:<%=yard.getKitchen2Count()%><br/><%
if(yard.getKitchenRank2()<9){
	%>升级后冰箱容量:<%=yard.gekitchenCounts2(yard.getKitchenRank2()+1)%><br/>
	升级需花费资金<%=YardAction.moneyFormat(YardBean.kitchenCounts2Money[yard.getKitchenRank2()])%>元<br/>
	<a href="kitchenrank2.jsp?c=<%=yard.getKitchenRank2()%>">确认升级</a><br/><%
}%>
<a href="hall.jsp">返回餐厅大堂</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>