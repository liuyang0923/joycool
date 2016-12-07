<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmUserBean farmUser = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
地图上分布着一些奇异的石头:七彩石.触摸这些石头会带来不同的效果.<br/>
赤曜石:瞬间补充血<br/>
橙曜石:又称为幸运石,幸运将伴随你,持续一段时间.<br/>
黄曜石:增加一定的血上限和致命一击,持续一段时间.<br/>
绿曜石:瞬间补充体力<br/>
青曜石:又称为飞行石,诺机系列能够用这种石头进行定位导航,有记录并传送的功能.<br/>
蓝曜石:瞬间补充气力<br/>
紫曜石:将灵魂固定在石头里,死亡后将在这块石头边重生.<br/>
注意:石头的位置是不会发生变化的.<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>