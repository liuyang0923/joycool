<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%!
static int BK_NUMBER_PER_PAGE = 8;%><%
response.setHeader("Cache-Control","no-cache");
DiceAction action = new DiceAction(request);
action.dealIndex(request, response);
//登录用户
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//坐庄列表
List bkList = PKBaseAction.getWGamePKList(WGameBean.PK_DICE);
PagingBean paging = new PagingBean(action,bkList.size(),BK_NUMBER_PER_PAGE,"p");
bkList = DSUtil.sublist(bkList,paging.getStartIndex(),paging.getEndIndex());

//玩家列表
int totalOnlinePageCount = ((Integer) request.getAttribute("totalOnlinePageCount")).intValue();
int pageIndex1 = ((Integer) request.getAttribute("pageIndex1")).intValue();
Vector userList = (Vector) request.getAttribute("userList");
String prefixUrl1 = (String) request.getAttribute("prefixUrl1");

//刷新
String refreshUrl = PageUtil.getCurrentPageURL(request);
refreshUrl = refreshUrl.replace("&", "&amp;");

int i, count;
WGamePKBean bk = null;
UserBean user = null;
String fenye = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="掷骰子" ontimer="<%=response.encodeURL(refreshUrl)%>">
<timer value="200"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(loginUser==null||loginUser.isShowImg()){%><img src="../img/dice.gif" alt="掷骰子"/><%}else{%>【掷骰子】<%}%><br/>
+庄家列表+<a href="/wgame/dicedx/index.jsp">挑战美女</a><br/>
<%
count = bkList.size();
for(i = 0; i < count; i ++){
	bk = (WGamePKBean) bkList.get(i);
	UserStatusBean usTemp=null;
    usTemp=UserInfoUtil.getUserStatus(bk.getLeftUserId());
	//zhul_2006-09-07 如果用户没登陆也可以进入查看 start
	if(loginUser==null)
	{
%><%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(bk.getLeftNickname())%>坐庄<%=StringUtil.bigNumberFormat2(bk.getWager())%>乐币<a href="chlStart.jsp?bkId=<%=bk.getId()%>">挑战</a><br/>	<%
	}
	else{
	//zhul_2006-09-07 如果用户没登陆也可以进入查看 end
	if(bk.getLeftUserId() != loginUser.getId()){
%>
<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(bk.getLeftNickname())%>坐庄<%=StringUtil.bigNumberFormat2(bk.getWager())%>乐币<a href="chlStart.jsp?bkId=<%=bk.getId()%>">挑战</a><br/>
<%
    } else {
%>
我坐庄<%=StringUtil.bigNumberFormat2(bk.getWager())%>乐币<br/>
<%
    }
    }
}
%><%=paging.shuzifenye("index.jsp",false,"|",response)%>
<a href="bkStart.jsp">我要坐庄&gt;&gt;</a><br/>
+玩家列表+<br/>
<%
count = userList.size();
for(i = 0; i < count; i ++){
	user = (UserBean) userList.get(i);
%>
<%=(pageIndex1 * action.ONLINE_NUMBER_PER_PAGE + i + 1)%>.<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(user.getNickName())%>(<%=StringUtil.bigNumberFormat2(user.getUs().getGamePoint())%>乐币)<a href="pkStart.jsp?userId=<%=user.getId()%>">挑战</a><br/>
<%
}

//分页
fenye = PageUtil.shuzifenye1(totalOnlinePageCount, pageIndex1, prefixUrl1, false, " ", response);
if(fenye != null && !fenye.equals("")){
%>
<%=fenye%><br/>
<%
}
%>
+功能选项+<br/>
<a href="help.jsp">游戏帮助</a>|<a href="history.jsp">今日战绩</a><br/>
<a href="/lswjs/wagerHall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>