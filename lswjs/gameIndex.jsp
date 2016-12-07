<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.action.chat.RoomRateAction,net.joycool.wap.framework.JoycoolSpecialUtil"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.wgame.WGameAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
//zhul 2006-09-07 定义点好友或邮箱进入登陆后返回页 start
String reURL=request.getRequestURL().toString();
String queryStr=request.getQueryString();
session.setAttribute("loginReturnURL",queryStr==null?reURL:reURL+"?"+queryStr);
//zhul 2006-09-07 定义点好友或邮箱进入登陆后返回页 end
WGameAction action = new WGameAction();
//action.hall(request);
Vector chatList = (Vector)request.getAttribute("chatList");
//liuyi 2006-12-27 赌场首页修改 start
int friendCount=0;
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(loginUser!=null){
friendCount=UserInfoUtil.getUserOnlineFriendsCount(loginUser.getId());
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="休闲娱乐城">
<p align="left"><img src="../img/gameCenter.gif" alt="休闲娱乐城"/><br/>
<%=BaseAction.getTop(request, response)%>
<a href="/user/ViewFriends.do">好友<%=friendCount>0?String.valueOf(friendCount):""%></a>|<a href="/chat/hall.jsp">聊天</a>|<a href="/user/messageIndex.jsp">信箱</a><br/>
<%--=BaseAction.getAdver(5,response)--%>
==最热游戏==<br/>
<a href="/cast/index.jsp">城堡战争</a>:攻城略地,运筹帷幄,决胜千里<br/>
<a href="/farm/index.jsp">桃花源</a>:桃源寻梦,扮演侠义人生<br/>
<a href="/garden/island.jsp">采集岛</a>:蔬菜满园,乐在黄金农场<br/>
<a href="/beacon/bFri/myInfo.jsp">朋友买卖</a>|<a href="/dhh/index.jsp">海商王</a>|<a href="/rich/index.jsp">大富翁</a><br/>
<a href="/pet/info.jsp">宠物大厅</a>|<a href="/pk/help.jsp">侠客秘境</a><br/>
<a href="/job/fish/index.jsp">欢乐渔场</a>|<a href="/fs/help.jsp">乐酷浮生记</a><br/>
<a href="/job/hunt/hunt.jsp">狩猎公园</a>|<a href="/wgame/guess/index.jsp">猜数字</a>|<a href="/wgamehall/chess/index.jsp">象棋</a><br/>
<a href="/question/index.jsp">问答接龙</a>|<a href="/job/question.jsp">开心辞典</a><br/>
<a href="/job/card/buyCard.jsp">命运巫婆</a>|<a href="/floor/index.jsp">踩踩乐</a><br/>
<a href="/job/anger/index.jsp">出气筒</a>|<a href="/lhc/index.jsp">六合彩</a>|<a href="/job/punch/index.jsp">打小强</a><br/>
==经典趣味==<br/>
<a href="/job/mindex.jsp">听歌猜名</a>|
<a href="/job/lottery.jsp">乐透彩票</a><br/>
<a href="/job/psychology/index.jsp">心理测试</a>|
<a href="/job/luck/index.jsp">每日运势</a><br/>
<a href="/job/character/index.jsp">诸葛神推</a>|
<a href="/job/spirit/spirit.jsp">静国神社</a><br/>
==服务中心==<br/>
<a href="/Column.do?columnId=9438">新手指南</a>|<a href="/bank/bank.jsp">银行</a><br/>
<%--=BaseAction.getAdver(6,response)--%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
<%//liuyi 2006-12-27 赌场首页修改 end %>