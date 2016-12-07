<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%
response.setHeader("Cache-Control","no-cache");
PetAction.viewpet(request);
PetUserBean petUser = (PetUserBean)request.getAttribute("petBean");
UserBean user = (UserBean) request.getAttribute("user");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="宠物">
<p align="left">
<%=BaseAction.getTop(request, response)%>
主人:
<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>">
<%=StringUtil.toWml(user.getNickName())%>
</a>
<br/>
<br/>
<%
if((petUser != null)&&(user != null))
{

%>
<%if (petUser.getAge() <= 5) {%>
<img src="img/<%=petUser.getType()%>_1.gif" alt=""/><br/>
<%} else if ((petUser.getAge() > 5) && (petUser.getAge() <= 20)) {%>
<img src="img/<%=petUser.getType()%>_2.gif" alt=""/><br/>
<%} else {%>
<img src="img/<%=petUser.getType()%>_3.gif" alt=""/><br/>
<%}%>
			
<%=StringUtil.toWml(petUser.getName())%><br/>
积分：<%=petUser.getIntegral()%><br/>
<%if (petUser.getHealth() > 60) {%>
<img src="img/health1.gif" alt=""/>健康
<%} else if ((petUser.getHealth() > 30) && (petUser.getHealth() <= 60)) {%>
<img src="img/health2.gif" alt=""/>虚弱
<%} else if (petUser.getHealth() > 0) {%>
<img src="img/health3.gif" alt=""/>疾病
<%}else{%>
<img src="img/health4.gif" alt=""/>死亡
<%}%>
<br/>
年龄：<%=petUser.getAge()%><br/>
等级：<%=petUser.getRank()%><br/>
<br/>
<%PetAction petAction =new PetAction(request);
int temp = petAction.propPetResult();
if(temp>0){%>
<a href="<%=("/pet/viewpetall.jsp?id="+petUser.getId()+"&amp;"+"temp="+temp)%>">使用千里眼</a>
<br/>
<%}%>
<br/>
<a href="/pet/viewuserpet.jsp?id=<%=user.getId()%>">宠物列表</a><br/>
<a href="/pet/info.jsp">返回宠物大厅</a><br/>
<%
}else{%>
此用户尚未开通宠物！
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>