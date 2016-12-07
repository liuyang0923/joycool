<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.HuntAction"%><%@ page import="net.joycool.wap.bean.job.HuntUserWeaponBean"%><%@ page import="net.joycool.wap.bean.job.HuntWeaponBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.factory.*,net.joycool.wap.bean.job.*"%><%@ page import="java.text.*,net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page errorPage=""%><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil" %><%!
static IJobService jobService = ServiceFactory.createJobService();%><%
response.setHeader("Cache-Control","no-cache");
List huntTaskList=new ArrayList(0);//jobService.getHuntTaskList(null);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
UserBean loginUser=(UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
if(loginUser==null){
%>
<card title="选择武器">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="/img/hunt.gif" alt="打猎"/><br/>
欢迎进入通吃岛狩猎区。打到的猎物，当场就可以兑换成乐币；但也要小心躲开猛兽，别受伤哦：）<br/>
请选择您要租用的武器：（租用期1小时）<br/>
<%
HashMap huntWeapon = LoadResource.getWeaponMap();
Set key=huntWeapon.keySet();
Iterator it=key.iterator();
while(it.hasNext()){
		HuntWeaponBean weaponBean = (HuntWeaponBean)huntWeapon.get(it.next());
		String unit = "发";
		String unitValue = "子弹";
		if (weaponBean.getName().equals("弓箭")){
			unit = "支";
			unitValue = "箭";
		}
	if(weaponBean.getId()!=5){%>
	<a href="huntArea.jsp?weaponId=<%=weaponBean.getId()%>&amp;buy=1"><%=weaponBean.getName()%>：<%=weaponBean.getPrice()%>（<%=unitValue%><%=weaponBean.getShotPrice()%>/<%=unit%>）</a><br/>
	<%}else{%>
	<%=weaponBean.getName()%>:(需要特殊道具才可以购买)<br/>
	<%}
	}
%><br/>
<a href="quarryMarket.jsp">猎物收购公司</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else{
//4代表猎物类型道具
int userBagId = UserBagCacheUtil.getUserBagById(12,loginUser.getId());
// 后台参数验证提示信息
String message = "";
if ((request.getParameter("msg")!=null)&&("1".equals(request.getParameter("msg")))){
	message = "您的乐币不足以购买该武器！<br/>";
}
if ((request.getParameter("msg")!=null)&&("4".equals(request.getParameter("msg")))){
	message = "太晚了明早再来买吧！<br/>";
}
if ((request.getParameter("msg")!=null)&&("2".equals(request.getParameter("msg")))){
	// 只有从在开枪时武器进期或从其它页面进入提示
	message = "您现在没有武器或武器已经过期，请重新选择。<br/>";
}
if ((request.getParameter("msg")!=null)&&("3".equals(request.getParameter("msg")))){
	// 只有从在开枪时武器进期或从其它页面进入提示
	message = "您现在没有特殊猎物道具，不能购买狙击步枪，请重新选择。<br/>";
}
HuntAction action = new HuntAction(request);
// 查找用户已买的可用武器
HuntUserWeaponBean userWeapon = action.getUsableHuntUserWeapon();
if (userWeapon == null){
// 没有武器
%>
<card title="选择武器">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="/img/hunt.gif" alt="打猎"/><br/>
<%=message%>
<%
SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
for(int i=0;i<huntTaskList.size();i++)
{
	HuntTaskBean task=(HuntTaskBean)huntTaskList.get(i);
	Date ds=sdf.parse(task.getStartTime().substring(0,19));
	Date de=sdf.parse(task.getEndTime().substring(0,19));
	Calendar cal=Calendar.getInstance();
	if(cal.getTime().after(ds) && cal.getTime().before(de)){
%>
<%=task.getNotice()%><br/>
<%}}%>
欢迎进入通吃岛狩猎区。打到的猎物，当场就可以兑换成乐币；但也要小心躲开猛兽，别受伤哦：）<br/>
请选择您要租用的武器：（租用期1小时）<br/>
<%
Vector huntWeaponList = action.doGetHuntWeaponList();
if (huntWeaponList != null){
	for(int i=0;i<huntWeaponList.size();i++){
		HuntWeaponBean weaponBean = (HuntWeaponBean)huntWeaponList.get(i);
		String unit = "发";
		String unitValue = "子弹";
		if (weaponBean.getName().equals("弓箭")){
			unit = "支";
			unitValue = "箭";
		}if(weaponBean.getId()!=5){%>
<a href="huntArea.jsp?weaponId=<%=weaponBean.getId()%>&amp;buy=1"><%=weaponBean.getName()%>：<%=weaponBean.getPrice()%>（<%=unitValue%><%=weaponBean.getShotPrice()%>/<%=unit%>）</a><br/>
		<%}else{
			if(userBagId==-1){%>
<%=weaponBean.getName()%>:(需要特殊道具才可以购买)<br/>
			<%}else{%>
<a href="<%=("huntArea.jsp?userBagId="+userBagId+"&amp;weaponId="+weaponBean.getId()+"&amp;buy=1")%>"><%=weaponBean.getName()%>：<%=weaponBean.getPrice()%>（<%=unitValue%><%=weaponBean.getShotPrice()%>/<%=unit%>）</a><br/>
			<%}
		}
	}
}
%>
<a href="quarryMarket.jsp">猎物收购公司</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
// 有武器
}else{
Integer sid = (Integer)session.getAttribute("huntcheck");
%>
<card title="继续狩猎">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="/img/hunt.gif" alt="打猎"/><br/>
<%=message%>
欢迎进继续狩猎：）。<br/>
您现在的武器是<%=userWeapon.getName()%><br/>
<a href="huntArea.jsp<%if(sid!=null){%>?s=<%=sid%><%}%>">继续使用，直接开始</a><br/>
换武器（租用期1小时）<br/>
<%
Vector huntWeaponList = action.doGetHuntWeaponList();
if (huntWeaponList != null){
	for(int i=0;i<huntWeaponList.size();i++){
		HuntWeaponBean weaponBean = (HuntWeaponBean)huntWeaponList.get(i);
		String unit = "发";
		String unitValue = "子弹";
		if (weaponBean.getName().equals("弓箭")){
			unit = "支";
			unitValue = "箭";
		}if(weaponBean.getId()!=5){%>
<a href="huntArea.jsp?weaponId=<%=weaponBean.getId()%>&amp;buy=1" title="select"><%=weaponBean.getName()%>：<%=weaponBean.getPrice()%>（<%=unitValue%><%=weaponBean.getShotPrice()%>/<%=unit%>）</a><br/>
		<%}else{
			if(userBagId==-1){%>
<%=weaponBean.getName()%>:(需要特殊道具才可以购买)<br/>
			<%}else{%>
<a href="<%=("huntArea.jsp?userBagId="+userBagId+"&amp;weaponId="+weaponBean.getId()+"&amp;buy=1")%>" title="select"><%=weaponBean.getName()%>：<%=weaponBean.getPrice()%>（<%=unitValue%><%=weaponBean.getShotPrice()%>/<%=unit%>）</a><br/>
			<%}
	   }
	}
}
%>
<br/>
<a href="quarryMarket.jsp">猎物收购公司</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card><%
}
}
%></wml>