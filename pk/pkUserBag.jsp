<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.pk.PKAction" %><%@ page import="net.joycool.wap.bean.pk.PKUserBean" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.pk.PKUserBagBean" %><%@ page import="net.joycool.wap.bean.pk.PKEquipBean" %><%@ page import="net.joycool.wap.bean.pk.PKMedicineBean" %><%@ page import="net.joycool.wap.bean.pk.PKMObjBean" %><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
action.pkUserBag(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String url=("/pk/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=PKAction.cardTitle%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后返回场景)<br/>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("isDeath")){
out.clearBuffer();
BaseAction.sendRedirect("/pk/isDeath.jsp",response);
return;
}else{
PKUserBean pkloginUser =action.getPkUser();
Vector userBagList = pkloginUser.getUserBagList();
%>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="../img/pk/userBag.gif" alt="行囊"/><br/>
<%
PKUserBagBean pkUserBag=null;
int j = 0;
for(int i=0;i<userBagList.size();i++){
		pkUserBag=(PKUserBagBean)userBagList.get(i);
		//除去装备在身上物品
		if(pkUserBag==null || pkUserBag.getSiteId()!=0)continue;
		%><%=j+1%>.<%
		if(pkUserBag.getType()==PKAction.PK_EQUIP){
			PKEquipBean equip=(PKEquipBean)action.getProto(pkUserBag.getType(),pkUserBag.getEquipId());
			if(equip==null)continue;%>
			<a href="/pk/detailInfo.jsp?id=<%=pkUserBag.getId()%>"><%=equip.getName()%></a><br/>
		<%}//物品
		else if(pkUserBag.getType()==PKAction.PK_MEDICINE){
			PKMedicineBean medicine=(PKMedicineBean)action.getProto(pkUserBag.getType(),pkUserBag.getEquipId());
			if(medicine==null)continue;%>
			<a href="/pk/detailInfo.jsp?id=<%=pkUserBag.getId()%>"><%=medicine.getName()%></a><br/>
		<%}else if(pkUserBag.getType()==PKAction.PK_MOBJ){
			PKMObjBean mObj=(PKMObjBean)action.getProto(pkUserBag.getType(),pkUserBag.getEquipId());
			if(mObj==null)continue;%>
			<a href="/pk/detailInfo.jsp?id=<%=pkUserBag.getId()%>"><%=mObj.getName()%></a><br/>
		<%}		
		j++;
}	
if(j==0){
%>
您的行囊中没有物品！<br/>
<%}%>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>