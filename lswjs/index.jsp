<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.WGameAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%//@ page errorPage="../failure.jsp"%><%@ page import="net.joycool.wap.framework.JoycoolSpecialUtil" %><%@ page import="java.util.Vector" %><%@ page import="java.util.HashMap" %><%
response.setHeader("Cache-Control","no-cache");
WGameAction action = new WGameAction();
//action.hall(request);
//用户乐币数
UserBean loginUser = action.getLoginUser(request);
UserStatusBean us=null;
if(loginUser!=null){
us=UserInfoUtil.getUserStatus(loginUser.getId());
}
//macq_2006-6-20_随机显示社区名称_start
//String title=(String)request.getAttribute("title");
//macq_2006-6-20_随机显示社区名称_end
//mcq_2006-6-20_取得2条公聊大厅中人对人的聊天记录_start
//Vector chatList = (Vector)request.getAttribute("chatList");
//mcq_2006-6-20_取得2条公聊大厅中人对人的聊天记录_end
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷导航中心">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(loginUser!=null){%>
<%=loginUser.showImg("/img/urlcenter.gif")%><%--a href="/wgame/pkg/index.jsp">圣诞礼包送好友&gt;&gt;</a><br/--%>
<a href="<%=("/bottom.jsp")%>">ME</a>|<%if(loginUser.getHome()==1){%><a href="<%=("/home/home.jsp")%>">家园</a><%}else{%><a href="<%=("/home/viewAllHome.jsp")%>">家园</a><%}%>|<a href="<%=("/user/ViewUserInfo.do?userId="+loginUser.getId())%>">资料</a>|<a href="<%=("/user/userInfo.jsp")%>">设置</a><br/>
<%}else{%>
<img src="../img/urlcenter.gif" alt="logo"/><br/>
<a href="/Column.do?columnId=9438">新手指南</a>|<a href="/user/register.jsp">注册</a>|<a href="/user/login.jsp?dir=1">登录</a><br/>
<%}%><%if(SqlUtil.isTest){%>[测试]<a href="/guest/index.jsp">游乐园</a>|<a href="/fm/index.jsp">家族</a>|<a href="/guest/wall/index.jsp">用户墙</a>|<a href="/fm/index.jsp">家族</a>|<a href="/wgame/wc/index.jsp">世界杯竞猜</a>|<a href="/kshow/index.jsp">酷秀</a>|<a href="/friend/match/index.jsp">选美</a>|<a href="/friend/help/hpindex.jsp">求助</a>|<a href="/friend/stranger/index.jsp">陌生人</a>|<a href="/mental/index.jsp">心理测试</a>|<a href="/mentalpic/index.jsp">图片心理测试</a><br/><%}%>
<a href="/wgame/wc/index.jsp">&gt;&gt;世界杯竞猜火热进行中</a><br/>
[推荐]<a href="/friend/match/index.jsp">选秀</a>.<a href="/stock2/index.jsp">股市</a>.<a href="/jcforum/index.jsp">论坛</a><br/>
[新]<a href="/kshow/index.jsp">酷秀</a>.<a href="/wgame/texas/index.jsp">德州</a>.<a href="/beacon/bo/index.jsp">漂流瓶</a><br/>
[测]<a href="/friend/help/hpindex.jsp">求助</a>|<a href="/friend/search/searchCenter.jsp">搜友</a>|<a href="/friend/credit/credit.jsp">可信度</a>|<a href="/exam/bag/index.jsp">书包</a>|<a href="/image/lib/lib.jsp">图库</a><br/>
<%--=BaseAction.getAdver(1,response)--%>
<%if(us!=null&&us.getRank()>3){%><a href="../shop/index.jsp?id=1"><img src="img/1d.gif" alt="new"/>乐秀商城新品不断!!</a><br/><%}%>
【<a href="/news/index.jsp">新闻</a>】<a href="/news/index2.jsp">女性</a>.<a href="/news/nba/index.jsp">NBA专题</a><br/>
【社区生活】<br/>
<a href="/jcforum/index.jsp">论坛</a>|<a href="/chat/hall.jsp">聊天</a>|<a href="/tong/tongList.jsp">帮会</a>|<a href="/team/index.jsp">圈子</a><br/>
<a href="/wgame/pkg/index.jsp">礼品</a>|<a href="/job/happycard/index.jsp">贺卡</a>|<a href="/friend/friendCenter.jsp">交友</a>|<a href="/friend/marriage.jsp">婚礼</a><br/>
<a href="/stock2/index.jsp">股市</a>|<a href="/auction/auctionHall.jsp">拍卖</a>|<a href="/auction/propShop.jsp">商店</a>|<a href="/user/onlineManager.jsp?forumId=355">警局</a><br/>
<a href="/Column.do?columnId=9438">新手</a>|<a href="/friend/seeYoungLing.jsp">迎新</a>|<a href="/top/index.jsp">龙虎</a>|<a href="/charitarian/index.jsp">慈善</a><br/>
【娱乐城】<a href="gameIndex.jsp">更多</a><br/>
<a href="/cast/index.jsp">城堡</a>|<a href="/garden/island.jsp">采集岛</a>|<a href="/rich/index.jsp">大富翁</a><br/>
<a href="/pet/info.jsp">宠物</a>|<a href="/fs/help.jsp">浮生</a>|<a href="/farm/index.jsp">桃花源</a><br/>
<a href="/pk/help.jsp?">侠客</a>|<a href="/job/fish/index.jsp">渔场</a>|<a href="/question/index.jsp">问答接龙</a><br/>
<a href="/wgamehall/chess/index.jsp">象棋</a>|<a href="/job/card/buyCard.jsp">机会卡</a>|<a href="/dhh/index.jsp">海商王</a><br/>
【通吃岛】<a href="wagerHall.jsp">更多</a><br/>
<a href="/wgamepk/3gong/index.jsp">三公</a>|<a href="/wgamepk/football/index.jsp">射门</a>|<a href="/wgamepk/dice/index.jsp">骰子</a>|<a href="/wgame/showhand/index.jsp">梭哈</a><br/>
<a href="/wgamehall/football/index.jsp">点球</a>|<a href="/wgamepk/basketball/index.jsp">篮球</a>|<a href="/wgame/g21/index.jsp">21点</a>|<a href="/wgamepk/jsb/index.jsp">猜拳</a><br/>
<a href="/wgame/wore/index.jsp">单双</a>|<a href="/wgame/tiger/index.jsp">老虎机</a>|<a href="/wgame/dice/index.jsp">猜大小</a><br/>
【资源库】<br/>
<a href="/Column.do?columnId=8774">书城</a>|<a href="/Column.do?columnId=4381">图库</a>|<a href="/Column.do?columnId=8602">极品游铃</a><br/>
<%--<a href="/Column.do?columnId=3840">是非</a>|<a href="/Column.do?columnId=8751">两性</a>|<a href="/Column.do?columnId=8713">女性专区</a><br/>--%>
<%--=BaseAction.getAdver(2,response)--%><%--在页面中加广告--%>
<%=BaseAction.separator%>
<a href="/bottom.jsp">ME</a>|导航|<a href="/wapIndex.jsp">乐酷首页</a><%if(SqlUtil.isTest){if(!Integer.valueOf(2).equals(session.getAttribute("wapEd"))){%>|<a href="/enter/set.jsp?ed=2">HTML</a><%}else{%>|<a href="/enter/set.jsp?ed=1">WAP</a><%}}%><br/>
<%=DateUtil.getCurrentDatetimeAsStr()%><br/>
</p>
</card>
</wml>