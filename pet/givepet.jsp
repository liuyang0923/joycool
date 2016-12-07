<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%@ page import="java.util.List;"%><%
response.setHeader("Cache-Control","no-cache");
PetAction action = new PetAction(request);
action.givePetIndex();
int pageIndex=((Integer)request.getAttribute("pageIndex")).intValue();
int	totalHallPageCount=	((Integer)request.getAttribute("totalHallPageCount")).intValue();
String	prefixUrl=(String)request.getAttribute("prefixUrl");
List list=(List)request.getAttribute("friendList");
String result= (String)request.getAttribute("result");
String url = ("/pet/index.jsp");
int petId = StringUtil.toInt((String)request.getAttribute("petId"));
PetUserBean petUserBean = action.getPetUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="乐酷" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/pet/index.jsp">我的宠物</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="赠送宠物">
<p align="left">
<%=BaseAction.getTop(request, response)%>
你想把<%=StringUtil.toWml(petUserBean.getName())%>赠送给哪位好友？<br/>
好友名单:<br/>
<%for(int i=0;i<list.size();i++){
int friendId = StringUtil.toInt((String)list.get(i));
UserBean user = (UserBean)UserInfoUtil.getUser(friendId);if(user != null){%>
<a href="<%=("/pet/givepetchoice.jsp?petId="+petId+"&amp;"+"userId="+user.getId())%>"><%=StringUtil.toWml(user.getNickName())%></a><br/>
<%}}

String fenye = PageUtil.shuzifenye(totalHallPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<br/>
<a href="/pet/info.jsp">返回宠物大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>
