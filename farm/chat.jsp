<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%! static int NUMBER_OF_PAGE = 10;%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmUserBean farmUser = action.getUser();
String url = "chat.jsp";

MapNodeBean node = action.getUserNode();
int mapId = node.getMapId();
MapBean map = FarmAction.world.getMap(mapId);
if(map.getParent()!=0)
	map = FarmAction.world.getMap(map.getParent());
boolean noName = action.getUser().getName().length()==0;
SimpleChatLog sc = SimpleChatLog.getChatLog("fm"+map.getId());	// farm map chat 桃花源区域公聊
PagingBean paging = new PagingBean(action, sc.size(),NUMBER_OF_PAGE,"p");
	String content = action.getParameterNoEnter("content");
	if(content != null) {		// 发言
		if(noName){
			response.sendRedirect("user/set.jsp");
			return;
		}
		sc.add(farmUser.getNameWml() + ":" + StringUtil.toWml(content) + "(" + DateUtil.formatTime(new Date()) + ")");
	}
	action.setAttribute2("mapchat" + map.getId(),new Integer(sc.getChatTotal()));

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="区域公聊">
<p align="left">
<%=BaseAction.getTop(request, response)%>
[<%=map.getName()%>]公聊<br/>
<%if(noName){%>发言[<a href="user/set.jsp">先取个名字</a>]
<%}else{%><input name="fmchat"  maxlength="100"/>
<anchor title="确定">发言
<go href="<%=url%>" method="post">
    <postfield name="content" value="$fmchat"/>
</go></anchor><%}%>|<a href="<%=url%>">刷新</a>|<a href="map.jsp">返回场景</a><br/>
<%=sc.getChatString(paging.getStartIndex(), NUMBER_OF_PAGE)%>
<%=paging.shuzifenye("chat.jsp", false, "|", response)%>

<a href="map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>