<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.fs.*" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
FSAction action = new FSAction(request);
UserBean loginUser = action.getLoginUser();
if(loginUser==null){
	response.sendRedirect("/user/login.jsp?backTo=/fs/help.jsp");
	return;
}
action.index();
String result =(String)request.getAttribute("result");
String url=("/lswjs/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=FSAction.title%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
FSUserBean fsUser = action.getFsUser();
//判断游戏是否已经结束
if (fsUser.isGameOver()) {
//session.setAttribute("sceneList", "sceneList");
out.clearBuffer();
BaseAction.sendRedirect("/fs/result.jsp",response);
return;
}
int sceneId= fsUser.getSceneId();
%>
<card title="<%=FSAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=loginUser.showImg("/img/fs/bjfs.gif")%>
<%=FSAction.FS_CITY_NAME[sceneId]%>
第<%=fsUser.getDay()%>天
(<%=fsUser.getTotalDay()%>)<br/>
<%
String[] eventMsg = {
"欠款太多，你被村长带人打了一顿，健康度下降！",
"你健康状况太差晕倒在街头，醒来时发现被“好心人”送进了医院，但是好像钱包瘪了不少……",
"刚下火车，你发现钱包丢了，还好你的钱放在隐密地方，损失不大。",
"两个人叽哩咕噜朝你说你听不懂的话，你表示莫名其妙。他们骂你“我顶你个肺”,还打了你一顿。",
"城管来了！跑！",
"过天桥时陷入乞丐包围，现金被掏走好多。",
"三个老太太拦住你：“没有暂住证？罚款！”",
"你买了电视上侯总推荐的“八心八箭”钻石名表，发现原来是玻璃做的。",
"你被路边的推销员拉住，高价买了一口锅王糊师傅，结果发现就是口破铝锅，亏大了。",
"暴雨成灾淹没了街道，你差点被冲到下水道去，还好捡回了条命。",
"坐汽车刚刚过了一座桥，桥就塌了，你吓出一身冷汗。",
"“兄弟，毛片要吗？主角武腾兰、D9、无码，带拍摄花絮。”买回去一放，里面是《畜牧养殖助您致富》。",
"猪肉涨价中，豆腐涨价中，方便面涨价中…你钱越花越多饭越吃越烂，惨。",
"你被闷棍敲昏，醒来发现自己成了黑砖窑的包身工，钱也不见了。",
"早饭吃的包子，肚子一直不爽，难道是纸箱馅的？",
};
int eventId = fsUser.getScene().getSpecialEvent();
if(eventId > 0) {
	out.println("@@" + eventMsg[eventId - 1] + "<br/>");
}

%>
------我的状态------<br/>
现金：<%=fsUser.getMoney()%>元
存款：<%=fsUser.getSaving()%>元<br/>
负债：<%=fsUser.getDebt()%>元
健康：<%=fsUser.getHealth()%>点<br/>
名声：<%=fsUser.getHonor()%>点
房屋容量：<%=fsUser.getUserBag()%>个<br/>
<%@include file="map.jsp"%>
----买卖动态信息----<br/>
<%=action.toString(FSAction.log)%>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>