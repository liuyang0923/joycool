<%@ page language="java" import="jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
FamilyUserBean fmUser = action.getFmUser();
if(fmUser==null||!fmUser.isflagYard()){
	response.sendRedirect("/fm/index.jsp");
	return;
}
YardBean yard=action.getYardByID(fmUser.getFm_id());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="素材库房"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(action.hasParam("cmd")){
	int money=action.getParameterInt("money");
	if(money<0){
		%>输入错误,请重新输入!<br/><%
	}else if(money>yard.getMoney()||money==0){
		%>家族没有足够的资金!<br/><%
	}else{
		action.exchangeFmFund(yard,money,fmUser);
		%>兑换资金<%=money%>元=<%=money%>0万乐币!已放入家族基金!<br/><%
	}
}%>
餐厅现有资金<%=YardAction.moneyFormat(yard.getMoney())%>元<br/>
当前兑换比例:1元=10万乐币<br/>
<input name="money" maxlength="30" />
<anchor title="兑换">兑换
  <go href="mate2.jsp?cmd=a" method="post">
    <postfield name="money" value="$(money)"/>
  </go>
</anchor><br/>
<anchor title="全部兑换">全部兑换
  <go href="mate2.jsp?cmd=a" method="post">
    <postfield name="money" value="<%=yard.getMoney()%>"/>
  </go>
</anchor><br/>
<a href="mate.jsp?id=<%=fmUser.getFm_id()%>">返回素材库房</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>