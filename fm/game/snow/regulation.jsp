<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="打雪仗规则"><p align="left"><%=BaseAction.getTop(request, response)%>
两个家族之间的较量,报名后由系统自动配对.<br/>
只有PK的两个家族的人能参与,参与人数不限.<br/>
打雪仗主要是扔雪球等增加对方积雪量,同时己方打扫减少积雪量,看谁的积雪量达到系统规定的值,谁就输掉了比赛.<br/>
进入游戏后玩家可以选择多项操作,投掷雪球或者做雪球,这些都是增加对方的积雪的,还可以选择清扫己方的积雪,减少己方的积雪.<br/>
做雪球有多种选择.根据雪球功效的不同,花费的乐币也不相同.制造的雪球,投雪车全帮会的人都能用.<br/>
小雪球:1雪币一个.制作时间2秒.对方增加积雪2<br/>
中雪球:10雪币一个.制作时间3秒.对方增加积雪5<br/>
大雪球:40雪币一个.制作时间5秒.对方增加积雪10<br/>
投雪车:500雪币一个.制作时间10秒,使用一次对方增加积雪10,可重复使用10次<br/>
点击扔雪球后,回答数学题.答错则雪球打飞;答对则攻击中.每3秒能扔一次.<br/>
清理积雪<br/>
扫把:1雪币一次.清理时间2秒.自己方积雪减少1<br/>
铁锹:5雪币一次.清理时间3秒,自己方减少积雪3<br/>
推雪车:10雪币1次.清理时间4秒,自己方减少积雪10.<br/>
积雪上限定值为<%=jc.family.game.snow.Constants.MAX_SNOW %>.<br/>
若游戏途中服务器事故导致游戏不能正常进行,则退回报名费用.<br/>
<a href="snowBallFight.jsp">返回打雪仗</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>