<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
采摘:发现并采集草药和水果等食物的的技能<br/>
采矿:在矿脉上采集矿石的技能,熟练后可以熔炼金属<br/>
战斗:战斗等级是战斗中最为重要的元素,决定了一个角色的战斗能力.<br/>
攻击技:战斗中可以使用的通用攻击技能,所有职业都能学习<br/>
职业技能:转为某个职业后才能学习这个专业,学习后就能到相应的老师那里学习相应的职业技能<br/>
裁缝:制作/升级服装等<br/>
锻造:制作/升级兵器和重盔甲等<br/>
珠宝:制作/升级项链等各种首饰<br/>
返回<a href="../user/pros.jsp">专业</a>功能<br/>

<%@include file="bottom.jsp"%>
</p>
</card>
</wml>