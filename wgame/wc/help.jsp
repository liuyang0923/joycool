<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="jc.game.worldcup.*,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*"%>
<% response.setHeader("Cache-Control","no-cache");
WorldCupAction action = new WorldCupAction(request);
WcInfo wcInfo = WorldCupAction.getWcInfo();
String tip = "";
int type = action.getParameterInt("t");
if (type < 0 || type > 1){
	type = 0;
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="竞猜"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
if (type == 0){
%>游戏规则<br/>
1,每个用户初始账户有10000球币,用来给每一场比赛,按照胜、平、负下注.<br/>
2,如果猜对,则按照赔率获得球币.如果猜错,则下注的球币输掉.例如,南非对墨西哥比赛,胜平负赔率是3:2:1.2,你压平100球币,获胜后连本金总共得到200球币.<br/>
3,每场比赛,用户可以下注不止一次,但每场比赛的总投注上限是2000球币;一旦投注不得撤销.<br/>
4,比赛开场后20分钟,不能再下注.<br/>
5,世界杯结束时,球币账户最多的前10名,获得丰厚大奖.此外球币账户大于1万的用户中,抽取20名幸运奖,免费获取阿根廷、巴西等球队球衣一件！<br/>
6,为了保障公平、透明,用户点击排行榜里的任何人,都可查询他们的投注记录.<br/>
<%	
} else {
%>笔记本大礼,世界杯球衣向你召唤！<br/><img src="img/nb.jpg" alt="o" /><br/>
本次竞猜设优胜奖和幸运奖,力争让优胜者和参与者都有机会获奖！<br/>
比赛前10名获得优胜奖,奖品如下:<br/>
第一名.价值2500元的台硕mini笔记本电脑一台！<br/>
第二名.价值900元的英耐特8000E数码相机一部！<br/>
第三名.价值700元的天时达T687娱乐手机一部！<br/>
第四名.价值500元的Q8双摄像头手机！<br/>
第五名.价值400元的松下EW3106电子血压计！<br/>
第六名.价值320元的DV136数码摄像机！<br/>
第七名.价值260元的口香糖造型摄像机！<br/>
第八名.价值200元的经典米奇造型MP3！<br/>
第九名.价值150元的雅芳男士去油润肤护肤套装！<br/>
第十名.价值120元的贵度全能魔环套装！<br/>
<img src="img/c.jpg" alt="o" /><br/>
赛事结束后从球币账户大于1万球币的用户中,抽取幸运奖20名,奖励世界杯队服一件！德国、阿根廷、巴西、意大利、英格兰、法国,随你挑！<br/>
<%	
}
%>
<a href="bet.jsp">我要投注!!</a><br/>
<%
} else {
%><%=tip%><br/><%	
}%>
[<a href="index.jsp">返回投注首页</a>]<br/>
<%if (wcInfo != null && wcInfo.getSubjectId() > 0){
%>[<a href="/Column.do?columnId=<%=wcInfo.getSubjectId()%>">返回竞猜首页</a>]<br/><%	
}%>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>