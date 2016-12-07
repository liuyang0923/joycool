<%@include file="../checkMobile.jsp"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.Tong2Action"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.job.HuntUserQuarryBean"%><%@ page import="java.util.HashMap"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%!
static String[] effectString = {"能力值增加1","效果增加1倍","能力值增加2","效果增加1","效果变为3","效果增加2倍"};
%><% response.setHeader("Cache-Control","no-cache");
int hour = net.joycool.wap.util.DateUtil.dayHour(System.currentTimeMillis());
if(hour < 7 && hour > 0){
	response.sendRedirect("deopt.jsp");
	return;
}
Tong2Action action = new Tong2Action(request);
action.shop(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("tongList.jsp");
%>
<card title="城帮列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong=(TongBean)request.getAttribute("tong");
TinyAction tiny = new TinyAction(request, response).checkGame(TongAction.tinyGamesDepot[1]);
if(tiny == null) {
	action.tip("tip", "开发失败");
} else {
	if(tiny.getGameResult() != -1) {
		
		int[] count = TongAction.countMap2.getCount(loginUser.getId());
		if(count[0] < 100)
			TongCacheUtil.updateTongDepot(tong.getId(),1);
		count[0]++;
		action.tip("tip", "开发成功");
	} else {
		action.tip("tip", "开发失败");
	}
}
%>
<card title="帮会仓库">
<p align="left">
<%=action.getTip()%><br/>
<a href="depot.jsp?tongId=<%=tong.getId()%>">返回仓库</a><br/>
<a href="tong.jsp?tongId=<%=tong.getId()%>">返回<%=StringUtil.toWml(tong.getTitle())%></a><br/>
</p>
</card>
<%}%>
</wml>