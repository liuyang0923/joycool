<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.job.*" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.service.infc.IJobService,net.joycool.wap.service.factory.ServiceFactory" %><%!
static IJobService jobService = ServiceFactory.createJobService();%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);

String backTo=request.getParameter("backTo");
int quarryId=StringUtil.toInt(request.getParameter("quarry"));
//获得用户拥有该猎物数量
int quarryCount=0;
HuntUserQuarryBean userQuarry=jobService.getHuntUserQuarry("quarry_id="+quarryId+" and user_id="+loginUser.getId());
if(userQuarry!=null )
{
	quarryCount=userQuarry.getQuarryCount();
}else
{
	//response.sendRedirect(("quarryMarket.jsp"));
	BaseAction.sendRedirect("/job/hunt/quarryMarket.jsp", response);
	return;
}


//当一次交易开始，做一次标记
session.setAttribute("deal","begin");

HashMap quarryMap=LoadResource.getQuarryMap();
HuntQuarryBean quarry=(HuntQuarryBean)quarryMap.get(new Integer(quarryId));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="买猎物" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
走私贩子：<%=quarry.getName()%>的价格是<%=quarry.getPrice()%>每个.<br/>
<img src="<%=Constants.JOB_HUNT_IMG_PATH%><%=quarry.getImage()%>" alt="loading..."/><br/>
您有<%=quarryCount%>个，要卖几个？<br/>
<input type="text" name="salenum" size="4" value="<%=quarryCount%>"/><br/>
<anchor title="确定">确定
<go href="quarryMarket.jsp" method="post">
<postfield name="sales" value="$salenum"/>
<postfield name="quarryId" value="<%=quarryId%>"/>

</go>
</anchor>
<a href="<%=(backTo.replace("&","&amp;"))%>">返回</a><br/><br/>
<a href="hunt.jsp">返回狩猎首页</a><br/>
<a href="<%=("/lswjs/index.jsp")%>">返回导航中心</a><br/>
</p>
</card>
</wml>