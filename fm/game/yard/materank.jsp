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
String tip=null;
int comfirm = action.getParameterIntS("c");
if(comfirm >= 0 && comfirm == yard.getMateRank()){
	if(!fmUser.isflagYard()){
		action.tip("tip","!只有餐厅大厨才有这个权限!");
	} else if(yard.getMoney()<YardBean.mateCountsMoney[yard.getMateRank()]){
		action.tip("tip","资金不足!库房升级失败!");
	}else if(yard.getMateRank()>9){
		action.tip("tip","等级暂满!库房升级失败!");
	}else {
		yard.addMoney(-YardBean.mateCountsMoney[yard.getMateRank()]);
		yard.setMateRank(yard.getMateRank()+1);
		YardAction.getService().executeUpdate("update fm_yard_info set mate_rank="+yard.getMateRank()+" where fmid="+yard.getFmid());
		action.tip("tip","库房升级成功!");
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="库房升级"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%><%=action.getTip()%><br/><%}%>
库房等级:<%=yard.getMateRank()+1%><br/>
当前容量提升:<%=YardBean.mateCounts[yard.getMateRank()]%>%<br/><%
if(yard.getMateRank()<9){
	%>升级后容量提升:<%=YardBean.mateCounts[yard.getMateRank()+1]%>%<br/>
	升级需花费资金<%=YardAction.moneyFormat(YardBean.mateCountsMoney[yard.getMateRank()])%>元<br/>
	<a href="materank.jsp?c=<%=yard.getMateRank()%>">确认升级</a><br/><%
}%>
<a href="mate.jsp">返回库房</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>