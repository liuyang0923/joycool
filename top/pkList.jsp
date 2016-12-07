<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.cache.*,net.joycool.wap.service.factory.*,net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.top.TopAction" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.bean.top.UserTopBean" %><%
response.setHeader("Cache-Control","no-cache");
TopAction topAction=new TopAction(request);
topAction.updateDuList(request);
List topList=(List)request.getAttribute("pkList");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷PK榜">
<p align="left">
<%=BaseAction.getTop(request, response)%>
乐酷PK榜<br/>
----------<br/>
<%
if(topList.size()>0){
UserBean user=null;
int index = 0;
for(int i=0;i<topList.size();i++){
    Integer userId =(Integer)topList.get(i);
    if(userId==null)continue;

    user=topAction.getUser(userId.intValue());
    if(user==null){
        continue;
    }
    String winPkGroup = "winPkGroup";
    String key = user.getId() + "";
    String query = "SELECT sum(win_count) FROM wgame_history0 where user_id=" + user.getId();
    Integer count = (Integer)OsCacheUtil.get(key, winPkGroup, 60*60);
    if(count==null){
    	count = new Integer(SqlUtil.getIntResult(query, Constants.DBShortName));
    	OsCacheUtil.put(key, count, winPkGroup);
    }
    index++;
%>
<%=index+" "%>
<%if(user.getId()!=loginUser.getId()){%>
<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%>
<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>">
<%
String nickname=StringUtil.toWml(user.getNickName());
if(nickname.equals(""))
nickname="乐客"+user.getId();%>
<%=nickname%></a>
<%}else{%>您自己<%}%>(<%= count %>胜)<br/>
<%}}else{%>
暂时不提供查询乐酷PK榜功能!<br/>
<%}%><br/><br/>
<%@include file="bottom.jsp"%><br/>
</p>
</card>
</wml>