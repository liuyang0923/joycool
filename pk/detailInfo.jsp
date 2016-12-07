<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.pk.PKAction" %><%@ page import="net.joycool.wap.bean.pk.PKEquipBean" %><%@ page import="net.joycool.wap.bean.pk.PKMedicineBean" %><%@ page import="net.joycool.wap.bean.pk.PKUserBagBean" %><%@ page import="net.joycool.wap.bean.pk.PKMObjBean" %><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
action.detailInfo(request);
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
<%=request.getAttribute("tip") %>(3秒后跳转其它场景)<br/>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
PKUserBagBean userBag =(PKUserBagBean)request.getAttribute("userBag");
%>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(userBag.getType()==PKAction.PK_EQUIP){
	PKEquipBean equip = (PKEquipBean)userBag.getPorto();;
	if(equip!=null){%>
		<%=equip.toDetail()%>
	<%}
}else if(userBag.getType()==PKAction.PK_MEDICINE){
	PKMedicineBean medicine = (PKMedicineBean)userBag.getPorto();;
	if(medicine!=null){%>
		<%=medicine.toDetail()%>
	<%}
}else if(userBag.getType()==PKAction.PK_MOBJ){
	PKMObjBean mObj = (PKMObjBean)userBag.getPorto();
	if(mObj!=null){%>
		<%=mObj.toDetail()%>
	<%}
}
%>
<%--<%=userBag.toDetail()%>--%>
<%if(userBag.getType()!=PKAction.PK_MOBJ){%>
<anchor title="确定">使用
  <go href="detailResult.jsp" method="post">
    <postfield name="id" value="<%=userBag.getId()%>"/>
    <postfield name="flag" value="<%=0%>"/>
  </go>
</anchor>|
<%}%>
<anchor title="确定">丢弃
  <go href="detailResult.jsp" method="post">
    <postfield name="id" value="<%=userBag.getId()%>"/>
    <postfield name="flag" value="<%=1%>"/>
  </go>
</anchor>
<br/>
<a href="/pk/pkUserBag.jsp">返回行囊</a><br/>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>