<%@include file="../checkMobile.jsp"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.Tong2Action"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%!
static String[] effectString = {"能力值增加1","效果增加1倍","能力值增加2","效果增加1","效果变为3","效果增加2倍"};
static String[] choiceString={"破坏城墙500","加固城墙500","破坏增加50%","加固增加50%"};
%><%
response.setHeader("Cache-Control","no-cache");
Tong2Action action = new Tong2Action(request);
action.shop(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
Tong2Action.Tong2User t2u = action.getUser2();
int rand = net.joycool.wap.util.RandomUtil.seqInt(4);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml> 
<%if(result.equals("failure")){
String url=("tongList.jsp");
%>
<card title="帮会商店" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong=(TongBean)request.getAttribute("tong");
%>
<card title="帮会商店">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(rand%2==0){%>.请选择物品:<%}else{%>选择物品<%}%><%if(t2u.same){%>与上一次相同<%}else{%><%=choiceString[t2u.choice]%><%}%><%if(rand/2==0){%><br/><%}%>[获得:<%=effectString[t2u.effect]%>]<br/>
<a href="shopEmpolder.jsp?tongId=<%=tong.getId()%>&amp;c=0<%if(t2u.same){%>&amp;s=1<%}%>">轰天炮</a>|
<a href="shopEmpolder.jsp?tongId=<%=tong.getId()%>&amp;c=1<%if(t2u.same){%>&amp;s=1<%}%>">防护膜</a><br/>
<a href="shopEmpolder.jsp?tongId=<%=tong.getId()%>&amp;c=2<%if(t2u.same){%>&amp;s=1<%}%>">攻城战鼓</a>|
<a href="shopEmpolder.jsp?tongId=<%=tong.getId()%>&amp;c=3<%if(t2u.same){%>&amp;s=1<%}%>">守城战鼓</a><br/>
商店开发度<%=tong.getShop()%><br/>
当前能力值:<%=t2u.level%>(能力值越高,商店开发的效果越好)<br/>
请根据提示选择对应的道具,如果选择正确,将获得相应的效果奖励<br/>
<a href="tongCityShopRecord.jsp?tongId=<%=tong.getId()%>">商店开发记录</a><br/>
<a href="tong.jsp?tongId=<%=tong.getId()%>">返回帮会</a><br/>
</p>
</card>
<%}%>
</wml>