<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.job.SpiritBean"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.action.job.SpiritAction"%><%
response.setHeader("Cache-Control","no-cache");
SpiritAction action=new SpiritAction(request);
String sid=null;
SpiritBean spirit=null;
int point=0;
int effect=0;
String tit=null;
if(null!=request.getParameter("id")){
sid=(String)request.getParameter("id");
int spiritid=StringUtil.toInt(sid);
spirit=action.getSpirit(spiritid);
if(spirit!=null)
{
point=spirit.getPrice();
effect=spirit.getEffect();
tit=spirit.getTitle();
}
}%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="静国神社" ontimer="<%=response.encodeURL("/job/spirit/spirit.jsp")%>">
<timer value="30"/>
<p align="left" >
<%=BaseAction.getTop(request, response)%>
<%if(spirit==null){%>
该按键不存在！<br/>
<%}
else if((null!=spirit)&&(action.haveEnoughpoint(point))&&(!tit.equals("参拜")))
{%>
<%=spirit.getDescription()%><br/>
<%=spirit.getEffectdes()%><br/>
您消费了<%=spirit.getPrice()%>个乐币<br/>
<%action.deductUserpoint(point,effect);}
else if((null!=spirit)&&(action.haveEnoughpoint(point))&&(tit.equals("参拜")))
{%>
<%=spirit.getDescription()%><br/>
<%=spirit.getEffectdes()%><br/>
您消费了<%=spirit.getPrice()%>个乐币<br/>
<%action.deductUserpoint(point);}
else{%>
你的乐币不够支付！<br/>
<%}%>
(3秒钟跳转回主页面)<br/>
<a href="<%=("/job/spirit/spirit.jsp") %>">直接进入</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>