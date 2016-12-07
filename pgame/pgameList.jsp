<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.pgame.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
String orderBy = (String) request.getAttribute("orderBy");
PGameProviderBean provider = (PGameProviderBean) request.getAttribute("provider");
Vector pgameList = (Vector) request.getAttribute("pgameList");
int i, count;
PGameBean pgame = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=provider.getName()%>游戏专区">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=provider.getName()%>游戏专区<br/>
<a href="PGameList.do?providerId=<%=provider.getId() %>&amp;orderBy=id">按时间</a>|<a href="PGameList.do?providerId=<%=provider.getId() %>&amp;orderBy=download_sum">按人气</a><br/>
------------------------------<br/>
<%
count = pgameList.size();
for(i = 0; i < count; i ++){
	pgame = (PGameBean) pgameList.get(i);
%>
<%=(pageIndex * Constants.PGAME_PER_PAGE + i + 1)%>:<a href="PGameInfo.do?gameId=<%=pgame.getId() %>&amp;orderBy=<%=orderBy%>"><%=pgame.getName()%></a>(<%=pgame.getDownloadSum()%>)<br/>
<%
}

String fenye = PageUtil.shangxiafenye(totalPageCount, pageIndex, prefixUrl.replace("&", "&amp;"), true, "|", response);
%>
<%if(!fenye.equals("")){%><%=fenye%><br/><%}%><br/>
第<%=(totalPageCount == 0? pageIndex : (pageIndex + 1))%>页|共<%=totalPageCount%>页<br/>
<a href="http://wap.joycool.net" title="进入">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>