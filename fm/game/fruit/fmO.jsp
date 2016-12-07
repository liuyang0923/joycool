<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.fruit.*,jc.family.*,net.joycool.wap.util.*"%><%
FruitAction action=new FruitAction(request);
%><%@include file="inc.jsp"%><%
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族水果战"><p align="left"><%=BaseAction.getTop(request, response)%>
<%
int fmSide= 0;
if(vsUser!=null){
	fmSide=vsUser.getSide();
}
if(action.hasParam("s")){fmSide=action.getParameterInt("s");}
if(action.hasParam("cmd")){%>
开赛于<%=DateUtil.formatTime(vsGame.getStartTime()) %>,已用时<%=vsGame.getGameUsedTime() %><br/>
挑战方■:<a href="/fm/myfamily.jsp?id=<%=vsGame.getFmIdA() %>"><%=vsGame.getFmANameWml()%>家族</a><br/>
参赛人数:<%=vsGame.getFmCountA() %><br/>
果园数量:<%=vsGame.getOrchardACount() %><br/>
接受方▲:<a href="/fm/myfamily.jsp?id=<%=vsGame.getFmIdB() %>"><%=vsGame.getFmBNameWml()%>家族</a><br/>
参赛人数:<%=vsGame.getFmCountB() %><br/>
果园数量:<%=vsGame.getOrchardBCount() %><br/>
<%}else{
FruitFamilyBean bean = action.getFruitFamilyBean(fmSide);
int fmID=0;
if(fmSide==0){
	fmID=vsGame.getFmIdA();
}else if(fmSide==1){
	fmID=vsGame.getFmIdB();
}
FamilyHomeBean fhb = FamilyAction.getFmByID(fmID);

int orchardCount = 0;
if(fmSide==0){orchardCount = vsGame.getOrchardACount();}else if(fmSide==1){orchardCount = vsGame.getOrchardBCount();}
if(bean==null){;}else{%>
<%if(vsUser!=null&&vsUser.getFmId()==fmID){%>本<%}else{%><%=fhb.getFm_nameWml()%><%}%>家族果园(<%=orchardCount%>)<br/>
阳光总量:?|采集率:<%=bean.getSunTotalCaptureRate()%><br/>
家族水果:<%=bean.getFruitTotalcount()%><br/>
水果科技:<br/>
<a href="tecIntro.jsp?t=1&amp;s=<%=fmSide %>">尖刺果皮</a>:<%=bean.getFruitATKGrade()%>级<br/>
<a href="tecIntro.jsp?t=2&amp;s=<%=fmSide %>">加厚果皮</a>:<%=bean.getFruitHP()%>级<br/>
<a href="tecIntro.jsp?t=3&amp;s=<%=fmSide %>">果影分身</a>:<%=bean.getFruitYield()%>级<br/>
<a href="tecIntro.jsp?t=4&amp;s=<%=fmSide %>">喷气水果</a>:<%=bean.getFruitSpeed()%>级<br/>
已消灭水果:<%=bean.getFruitBeatted()%><br/>
家族水果烈士:<%=bean.getFruitSacrificed()%><br/><%}%>
<%}%>
<a href="game.jsp">返回水果战</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>