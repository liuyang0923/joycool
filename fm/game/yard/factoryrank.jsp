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
if(comfirm >= 0 && comfirm == yard.getFactoryRank()){
	if(!fmUser.isflagYard()){
		action.tip("tip","!只有餐厅大厨才有这个权限!");
	}else if(yard.getMoney()<yard.getFactoryRankMoney(yard.getFactoryRank())){
		action.tip("tip","!资金不足!加工厂升级失败!");
	}else if(yard.getFactoryRank()>9){
		action.tip("tip","!等级暂满!加工厂升级失败!");
	}else {
		yard.addMoney(-yard.getFactoryRankMoney(yard.getFactoryRank()));
		yard.setFactoryRank(yard.getFactoryRank()+1);
		YardAction.getService().executeUpdate("update fm_yard_info set factory_rank="+yard.getFactoryRank()+" where fmid="+yard.getFmid());
		action.tip("tip","!加工厂升级成功!");
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="工厂升级"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%><%=action.getTip()%><br/><%}%>
加工厂等级:<%=yard.getFactoryRank()+1%><br/>
当前可拥有的车间数:<%=yard.getFactoryRankCounts(yard.getFactoryRank())%><br/><%
if(yard.getFactoryRank()<9){
	%>升级后可拥有的车间数:<%=yard.getFactoryRankCounts(yard.getFactoryRank()+1)%><br/>
	升级需花费资金<%=YardAction.moneyFormat(yard.getFactoryRankMoney(yard.getFactoryRank()))%>元<br/>
	<a href="factoryrank.jsp?c=<%=yard.getFactoryRank()%>">确认升级</a><br/><%
}%>
<a href="factory4.jsp">返回加工厂</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>