<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.SquareWheelAction"%><%
response.setHeader("Cache-Control","no-cache");
SquareWheelAction action= new SquareWheelAction(request);
action.setSession();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="---俄罗斯轮盘---"> 
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="<%="/img/job/wheel/11.gif"%>" alt="图片"/><br/>
图例:<br/>
<img src="<%="/img/job/wheel/eg/0.gif"%>" alt="图片"/>0<img src="<%="/img/job/wheel/eg/1.gif"%>" alt="图片"/>1<br/>
<img src="<%="/img/job/wheel/eg/2.gif"%>" alt="图片"/>2<img src="<%="/img/job/wheel/eg/3.gif"%>" alt="图片"/>3<br/>
<img src="<%="/img/job/wheel/eg/4.gif"%>" alt="图片"/>4<img src="<%="/img/job/wheel/eg/5.gif"%>" alt="图片"/>5<br/>
<img src="<%="/img/job/wheel/eg/6.gif"%>" alt="图片"/>6<img src="<%="/img/job/wheel/eg/7.gif"%>" alt="图片"/>7<br/>
<img src="<%="/img/job/wheel/eg/8.gif"%>" alt="图片"/>8<img src="<%="/img/job/wheel/eg/9.gif"%>" alt="图片"/>9<br/>
请下注:<br/>
<input type="text" name="money" value=""/>乐币（最高1千万）<br/>
请选择数字:<br/>
<select name="num" value="1">
  	<option value="0">0</option>
    <option value="1">1</option>
    <option value="2">2</option>
    <option value="3">3</option>
    <option value="4">4</option>
    <option value="5">5</option>
    <option value="6">6</option>
    <option value="7">7</option>
    <option value="8">8</option>
    <option value="9">9</option>
	</select><br/> 
<anchor title="确定">确定
    <go href="/job/wheel/jump.jsp" method="post">
    <postfield name="money" value="$money"/>
    <postfield name="num" value="$num"/>
    <postfield name="wheelrun" value="true"/>
    </go>
</anchor><br/>
<a href="/job/wheel/RWheel.jsp" >游戏规则</a><br/>
<a href="/lswjs/gameIndex.jsp" >返回游戏首页</a><br/>
<a href="/lswjs/index.jsp" title="返回上一级">返回导航中心</a>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>