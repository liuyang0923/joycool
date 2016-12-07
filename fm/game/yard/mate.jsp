<%@ page language="java" import="jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction,java.util.List" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
FamilyUserBean fmUser = action.getFmUser();
int id=action.getParameterInt("id");
if(id==0){
	if(fmUser!=null){
		id=fmUser.getFm_id();
	}else{
		response.sendRedirect("/fm/index.jsp");
		return;
	}
}
YardBean yard=action.getYardByID(id);
int a=action.getParameterInt("a");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="餐厅库房"><p align="left"><%=BaseAction.getTop(request, response)%>
餐厅现有资金<%=YardAction.moneyFormat(yard.getMoney())%>元<br/><%
if(false && fmUser.isflagYard()){
	%><a href="mate2.jsp">兑换乐币</a><br/><%
}
if(a==0){
	%>种子|<%
}else{
	%><a href="mate.jsp?id=<%=id%>&amp;a=0">种子</a>|<%	
}
if(a==1){
	%>食材|<%
}else{
	%><a href="mate.jsp?id=<%=id%>&amp;a=1">食材</a>|<%
}
if(a==2){
	%>配料<br/><%
}else{
	%><a href="mate.jsp?id=<%=id%>&amp;a=2">配料</a><br/><%
}
List list=yard.getItemList();
if(list!=null){
	int cap = yard.getMateCount();// 库房升级
	for(int i=0;i<list.size();i++){
		YardItemBean item=(YardItemBean)list.get(i);
		YardItemProtoBean itemProto = YardAction.getItmeProto(item.getItemId());
		if(item.getItemType()==a){
			%><%=itemProto.getName()%>:<%=item.getNumber()%>/<%=itemProto.getCapacity()*cap/100%><br/><%
		}
	}
}
%><a href="materank.jsp">升级库房</a><br/><a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>