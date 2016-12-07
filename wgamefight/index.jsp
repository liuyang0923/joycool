<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.wgamefight.WGameFightBean" %><%@ page import="net.joycool.wap.action.wgamefight.FightAction" %><%
response.setHeader("Cache-Control","no-cache");
FightAction action = new FightAction(request);
action.dealIndex(request);
//登录用户
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//坐庄列表
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
Vector fightList = (Vector) request.getAttribute("fightList");
String prefixUrl = (String) request.getAttribute("prefixUrl");
int i, count;
WGameFightBean bk = null;
UserBean user = null;
String fenye = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷街霸" ontimer="<%=response.encodeURL("index.jsp")%>">
<timer value="200"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(loginUser!=null){%>
<%=loginUser.showImg("/wgamefight/img/wgamefight.gif")%>
<%}else{%>
<img src="img/wgamefight.gif" alt=""/><br/>
<%}%>
<%if(loginUser!=null){%>
您随身携带:<%=action.getUserGamePoint()%>乐币<br/>
<%}%>

+++庄家列表+++<br/>
<%
count = fightList.size();
for(i = 0; i < count; i ++){
	bk = (WGameFightBean) fightList.get(i);
	UserStatusBean usTemp=null;
    usTemp=UserInfoUtil.getUserStatus(bk.getLeftUserId());
	if(loginUser==null){%>
		<%=(pageIndex * action.BK_NUMBER_PER_PAGE + i + 1)%>.<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(bk.getContent())%>
		<%=bk.getWager()%>乐币:<a href="/wgamefight/pkout.jsp?bkId=<%=bk.getId()%>"><%if(loginUser!=null){%>挑战<%}%></a><br/>	<%
	}else{
	if(bk.getLeftUserId() != loginUser.getId()){%>
		<%=(pageIndex * action.BK_NUMBER_PER_PAGE + i + 1)%>.<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(bk.getContent())%>
		<%=bk.getWager()%>乐币:<a href="/wgamefight/pkout.jsp?bkId=<%=bk.getId()%>">挑战</a><br/>
	<%}else {%>
		<%=(pageIndex * action.BK_NUMBER_PER_PAGE + i + 1)%>.<%=StringUtil.toWml(bk.getContent())%>
		<%=bk.getWager()%>乐币<br/>
	<%}
    }
}//分页
fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, false, " ", response);
if(fenye != null && !fenye.equals("")){%><%=fenye%><br/><%}%>
<%if(loginUser!=null){%>
<a href="/wgamefight/bkStart.jsp">我要坐庄</a><br/>
<a href="/wgamefight/actGroup.jsp">动作组设置</a><br/>
<%}%>
<a href="/wgamefight/help.jsp">游戏帮助</a><br/>
<a href="/lswjs/wagerHall.jsp">返回通吃岛赌坊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>