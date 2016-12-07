<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.PositionUtil"%><%@ page import="net.joycool.wap.bean.wgamehall.HallBean" %><%
response.setHeader("Cache-Control","no-cache");
JinhuaAction action = new JinhuaAction(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean usTemp = new UserStatusBean();
if (loginUser != null){
	usTemp = UserInfoUtil.getUserStatus(loginUser.getId());
}
int count = SqlUtil.getIntResult("select count(id) from wgame_hall where mark != "+ HallBean.GS_END + " and game_id = " + HallBean.JINHUA, Constants.DBShortName);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="砸金花">
<p align="center">砸金花</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="../img/jinhua.gif" alt="砸金花"/><br/>
<a href="/wgamehall/jinhua/ksGameIndex.jsp">快速开始游戏</a>(<%=count*2%>人)<br/>
系统自动快速找人与你开始游戏<br/>
-------------<br/>
<a href="index.jsp?type=0">5百底1千赌注局</a><br/>

<a href="index.jsp?type=1">1千底1万赌注局</a><br/>

<a href="index.jsp?type=2">2千底2万赌注局</a><br/>

<a href="index.jsp?type=3">5千底5万赌注局</a><br/>

<a href="index.jsp?type=4">1万底10万赌注局</a><br/>

<a href="index.jsp?type=5">10万底100万赌注局</a><br/>

游戏帮助：<br/>
１、游戏开始后双方各发３张扑克，双方轮流下注次数不限，直到一方开牌或者逃跑。开牌后牌大获胜或者对方中途认输逃跑，则赢光双方已下的所有乐币。开牌牌小或认输逃跑则输光之前已下的所有乐币。<br/>
２、“底”就是开始赌局时自动扣除的基本赌注，“赌注”金额的意思是每次下注的金额。开牌需要2倍赌注乐币（玩1千赌注局开牌需要2千乐币）。赌注大小在邀请别人游戏前选取，接受邀请时必须看清赌注大小，游戏过程中要注意自己的乐币数量，不要到最后没法开牌只好认输。砸金花牌大小是一方面，“诈”对方才是取胜之道。<br/>
３、牌面大小规则依次为：豹子（AAA最大，222最小）>金花顺（黑桃AKQ最大，方块234最小）>金花（黑桃AQJ最大，方块235最小）>顺子（连牌）>对子（AAK最大，223最小）>单张（AKJ最大，235最小），喜儿牌>豹子。喜儿牌就是：2、3、5，大于豹子，但是比其他任何牌点数都小。如果对子一样，则比第３张牌大小；都是单牌且第１张一样，则比第２张牌大小，还一样则比第３张。牌型完全一样，则黑桃〉红桃〉草花〉方片。例：三张8比三张5大；黑桃A、10、9比红桃A、8、3大，杂色4、5、6肯定比杂色2、3、5大，依此类推。<br/>

<a href="history.jsp">今日战绩</a>|<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>