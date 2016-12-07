<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%!
static Tiny2Game[] games = {
new Tiny2Game2(50, 1800),
};
%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
UserBean loginUser = action.getLoginUser();
if(ForbidUtil.isForbid("game",loginUser.getId())){
	response.sendRedirect("/bottom.jsp");
	return;
}

Tiny2Action action2 = new Tiny2Action(request, response);
if(action2.checkGame(6)) return;
if(action2.getGame() == null){
	if(RandomUtil.nextInt(50)==1){
		action2.startGame(games[0], 6);
		return;
	}
}

	Integer sid = (Integer)session.getAttribute("hockshopCheck");
	if (sid == null || sid.intValue() != action.getParameterIntS("s")) {// 防止刷新
		request.setAttribute("result", "refrush");
		int tongId = action.getParameterInt("tongId");
		request.setAttribute("tong", TongCacheUtil.getTong(tongId));
	} else {
		action.empolder(request);
	}
int rand = net.joycool.wap.util.RandomUtil.seqInt(100);
session.setAttribute("hockshopCheck2", new Integer(rand));
String result =(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("/tong/tongList.jsp");
%>
<card title="城帮列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=request.getAttribute("tip") %><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("refrush")){
TongBean tong =(TongBean)request.getAttribute("tong");
%><card title="当铺">
<p align="left">
<a href="/tong/hockshop.jsp?tongId=<%=tong.getId()%>&amp;s=<%=rand%>">继续开发当铺</a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回帮会</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card><%
}else if(result.equals("priceError")){
TongBean tong =(TongBean)request.getAttribute("tong");
String url=("/tong/tong.jsp?tongId="+tong.getId());
%>
<card title="当铺" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong=(TongBean)request.getAttribute("tong");
String url=("/tong/hockshop.jsp?tongId="+tong.getId()+"&amp;s="+rand);
%>
<card title="当铺" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>
<a href="<%=url%>">直接跳转</a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回帮会</a><br/>
</p>
</card>
<%}%>
</wml>