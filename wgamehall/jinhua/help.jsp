<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%
response.setHeader("Cache-Control","no-cache");
JinhuaAction action = new JinhuaAction(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="砸金花帮助">
<p align="center">砸金花帮助</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
游戏帮助：<br/>
１、游戏开始后双方各发３张扑克，双方轮流下注次数不限，直到一方开牌或者逃跑。开牌后牌大获胜或者对方中途认输逃跑，则赢光双方已下的所有乐币。开牌牌小或认输逃跑则输光之前已下的所有乐币。<br/>
２、“底”就是开始赌局时自动扣除的基本赌注，“赌注”金额的意思是每次下注的金额。开牌需要2倍赌注乐币（玩1千赌注局开牌需要2千乐币）。赌注大小在邀请别人游戏前选取，接受邀请时必须看清赌注大小，游戏过程中要注意自己的乐币数量，不要到最后没法开牌只好认输。砸金花牌大小是一方面，“诈”对方才是取胜之道。<br/>
３、牌面大小规则依次为：豹子（AAA最大，222最小）>金花顺（黑桃AKQ最大，方块234最小）>金花（黑桃AKJ最大，方块235最小）>顺子（连牌）>对子（AAK最大，223最小）>单张（AKJ最大，235最小），喜儿牌>豹子。喜儿牌就是：2、3、5，大于豹子，但是比其他任何牌点数都小。如果对子一样，则比第３张牌大小；都是单牌且第１张一样，则比第２张牌大小，还一样则比第３张。牌型完全一样，则黑桃〉红桃〉草花〉方片。例：三张8比三张5大；黑桃A、10、9比红桃A、8、3大，杂色4、5、6肯定比杂色2、3、5大，依此类推。<br/>
<a href="/wgamehall/jinhua/start.jsp">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>