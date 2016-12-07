<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%
response.setHeader("Cache-Control","no-cache");
PetAction.viewotherpet(request);
Vector petList = (Vector)request.getAttribute("petList");
UserBean user = (UserBean) request.getAttribute("user");
PetUserBean petUser;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="宠物列表">
<p align="left">
<%=BaseAction.getTop(request, response)%>
主人:<%=StringUtil.toWml(user.getNickName())%><br/>
拥有<%=petList.size()%>个宠物<br/>
<br/>
<%
if((petList != null)&&(user != null))
{
Iterator iter = petList.iterator();
while(iter.hasNext()) {
petUser = (PetUserBean)iter.next();
PetAction petAction =new PetAction(request);
int temp = 0;//petAction.propPetResult();
if(temp>0){
%>
<a href="<%=("/pet/viewpetall.jsp?id="+petUser.getId()+"&amp;"+"temp="+temp)%>">
<%=StringUtil.toWml(petUser.getName())%><br/>
</a>
<%if(petUser.getHealth() > 60){%>
<img src="img/health1.gif" alt=""/>健康
<%}else if ((petUser.getHealth() > 30) && (petUser.getHealth() <= 60)){%>
<img src="img/health2.gif" alt=""/>虚弱
<%}else if(petUser.getHealth() > 0){%>
<img src="img/health3.gif" alt=""/>疾病
<%}else{%>
<img src="img/health4.gif" alt=""/>死亡
<%}%>
<br/>
年龄：<%=petUser.getAge()%><br/>
等级：<%=petUser.getRank()%><br/>
-----------------------------
<br/>
<%}else{%>
<a href="/pet/viewpet.jsp?id=<%=petUser.getId()%>">
<%=StringUtil.toWml(petUser.getName())%><br/>
</a>
<%if(petUser.getHealth() > 60){%>
<img src="img/health1.gif" alt=""/>健康
<%}else if ((petUser.getHealth() > 30) && (petUser.getHealth() <= 60)){%>
<img src="img/health2.gif" alt=""/>虚弱
<%}else if(petUser.getHealth() > 0){%>
<img src="img/health3.gif" alt=""/>疾病
<%}else{%>
<img src="img/health4.gif" alt=""/>死亡
<%}%>
<br/>
年龄：<%=petUser.getAge()%><br/>
等级：<%=petUser.getRank()%><br/>
-----------------------------
<br/>
<%}}
}else{%>
此用户尚未开通宠物！
<%}%>
<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>">查看主人信息</a><br/>
<a href="/pet/info.jsp">返回宠物大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>