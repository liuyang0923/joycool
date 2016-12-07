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
if(comfirm >= 0 && comfirm == yard.getKitchenRank3()){
	if(!fmUser.isflagYard()){
		action.tip("tip","!只有餐厅大厨才有这个权限!");
	} else if(yard.getMoney()<yard.getkitchenCountsMoney3(yard.getKitchenRank3())){
		action.tip("tip","!资金不足!厨房升级失败!");
	}else if(yard.getKitchenRank3() > 9){
		action.tip("tip","!等级暂满!厨房升级失败!");
	}else {
		yard.addMoney(-yard.getkitchenCountsMoney(yard.getKitchenRank3()));
		yard.setKitchenRank3(yard.getKitchenRank3()+1);
		YardAction.getService().executeUpdate("update fm_yard_info set kitchen_rank3="+yard.getKitchenRank3()+" where fmid="+yard.getFmid());
		action.tip("tip","!厨房升级成功!");
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="厨房升级"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%><%=action.getTip()%><br/><%}%>
中级厨房等级:<%=yard.getKitchenRank3()+1%><br/>
当前可同时烹饪数:<%=yard.getKitchen3Count()%><br/><%
if(yard.getKitchenRank3()<9){
	%>升级后可同时烹饪数:<%=yard.gekitchenCounts(yard.getKitchenRank3()+1)%><br/>
	升级需花费资金<%=YardAction.moneyFormat(yard.getkitchenCountsMoney(yard.getKitchenRank3()))%>元<br/>
	<a href="kitchenrank3.jsp?c=<%=yard.getKitchenRank3()%>">确认升级</a><br/><%
}%>
<a href="kitchen4.jsp?id=<%=fmId%>">返回中级厨房</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>