<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.action.wgamepk.big.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.wgame.big.WGamePKBigBean" %><%@ page import="net.joycool.wap.action.wgamepk.big.YlhykAction" %><%!
static int BK_NUMBER_PER_PAGE = 10;
%><%
response.setHeader("Cache-Control","no-cache");
YlhykAction action = new YlhykAction(request);
//登录用户
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(loginUser!=null&&net.joycool.wap.util.ForbidUtil.isForbid("fan",loginUser.getId())) {

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="大富豪俱乐部">
<p align="left">
防沉迷监察已经限制了你的游戏功能<br/>
<a href="/admin/query.jsp">查询详情</a><br/>
<a href="/admin/list.jsp?type=8">+防沉迷监察列表+</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>


</wml><%
return;
}

action.dealIndex(request);
//坐庄列表
List bkList = PKBaseAction.getWGamePKBigList(WGamePKBigBean.YLHYK);
PagingBean paging = new PagingBean(action,bkList.size(),10,"p");
int pageIndex = paging.getCurrentPageIndex();
bkList = DSUtil.sublist(bkList,paging.getStartIndex(),paging.getEndIndex());
//今日前三名
Vector topList = (Vector) request.getAttribute("topList");

int i, count;
WGamePKBigBean bk = null;
UserBean user = null;
String fenye = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="大富豪俱乐部" ontimer="<%=response.encodeURL("index.jsp")%>">
<timer value="200"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(loginUser!=null){%>
<%=loginUser.showImg("img/wgamepkbig.gif")%>
<%}else{%>
<img src="img/wgamepkbig.gif" alt="logo"/><br/>
<%}%>
<%if(loginUser!=null){%>
您的存款:<%=StringUtil.bigNumberFormat(action.getUserStore())%>乐币<br/>
<%}%>
<a href="bkStart.jsp">我要坐庄</a><br/>
+++庄家列表+++<br/>
<%
count = bkList.size();
for(i = 0; i < count; i ++){
	bk = (WGamePKBigBean) bkList.get(i);
	UserStatusBean usTemp=null;
    usTemp=UserInfoUtil.getUserStatus(bk.getLeftUserId());
	if(loginUser==null){%>
		<%=(pageIndex * BK_NUMBER_PER_PAGE + i + 1)%>.<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(bk.getContent())%>:<%=action.bigNumberFormat(bk.getWager())%>乐币<a href="pkout.jsp?bkId=<%=bk.getId()%>">挑战</a><br/>	<%
	}else{
	if(bk.getLeftUserId() != loginUser.getId()){%>
		<%=(pageIndex * BK_NUMBER_PER_PAGE + i + 1)%>.<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(bk.getContent())%>:<%=action.bigNumberFormat(bk.getWager())%>乐币<a href="pkout.jsp?bkId=<%=bk.getId()%>">挑战</a><br/>
	<%}else {%>
		<%=(pageIndex * BK_NUMBER_PER_PAGE + i + 1)%>.<%=StringUtil.toWml(bk.getContent())%>:<%=action.bigNumberFormat(bk.getWager())%>乐币<br/>
	<%}
    }
}//分页
%><%=paging.shuzifenye("index.jsp",false,"|",response)%>
<%if(loginUser!=null){%>
+++今日赢家+++<br/>
<%
count = topList.size();
	for(i = 0; i < count; i ++){
		bk = (WGamePKBigBean) topList.get(i);%>
		<%=i+1%>.
		<%
		if(bk.getWinUserId()==bk.getLeftUserId()){%>
			<a href="/user/ViewUserInfo.do?userId=<%=bk.getLeftUserId()%>"><%=StringUtil.toWml(bk.getLeftNickname())%></a>
			<%=action.bigNumberFormat(bk.getWager())%>乐币<br/>
		<%}else{%>
			<a href="/user/ViewUserInfo.do?userId=<%=bk.getRightUserId()%>"><%=StringUtil.toWml(bk.getRightNickname())%></a>
			<%=action.bigNumberFormat(bk.getWager())%>乐币<br/>
	    <%}
	}
}%>
==========<br/>
富豪们，在这里你们可以过把瘾！让你们的虚拟货币经受过山车的波动！当然在开心之余，每一局游戏都要收取千分之五的乐币捐献给乐酷慈善基金。这个基金专门用于帮助新玩家和家族等社团。感谢大家了！<br/>
==========<br/>
<a href="help.jsp">大富豪规则</a>|<a href="history.jsp">战绩</a>|<a href="/bank/bank.jsp">银行</a><br/>
<a href="/lswjs/wagerHall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>