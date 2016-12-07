<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.cache.*,net.joycool.wap.service.factory.*,net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.top.TopAction" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.bean.top.UserTopBean" %><%!
public void pkMoney(HttpServletRequest request) {
	List ret = null;
	String key = "pkMoneyList";
	String group = "top";
	
	ret = (List) OsCacheUtil.get(key, group, 24 * 60 * 60);
	if (ret == null) {
		String sql = "select user_id from (SELECT sum(money) money, user_id FROM wgame_history0 group by user_id) as temp "
				+ " where money>1000000 order by money desc limit 0,10";
		ret = SqlUtil.getIntList(sql, Constants.DBShortName);
		if (ret == null) {
			ret = new ArrayList();
		}
		OsCacheUtil.put(key, ret, group);
	}
	request.setAttribute("pkList", ret);
	return;
}
response.setHeader("Cache-Control","no-cache");
TopAction topAction=new TopAction(request);
pkMoney(request);
List topList=(List)request.getAttribute("pkList");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="赌王排行榜">
<p align="left">
<%=BaseAction.getTop(request, response)%>
赌王排行榜<br/>
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
    String winPkMoneyGroup = "winPkMoneyGroup";
    String key = user.getId() + "";
    String query = "SELECT sum(money) FROM wgame_history0 where to_days(now())-to_days(log_date)=1 and user_id=" + user.getId();
    Long count = (Long)OsCacheUtil.get(key, winPkMoneyGroup, 60*60);
    if(count==null){
    	count = new Long(SqlUtil.getLongResult(query, Constants.DBShortName));
    	OsCacheUtil.put(key, count, winPkMoneyGroup);
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
<%}else{%>您自己<%}%>(赢<%= count %>乐币)<br/>
<%}}else{%>
暂时不提供查询赌王排行榜功能!<br/>
<%}%><br/><br/>
<%@include file="bottom.jsp"%><br/>
</p>
</card>
</wml>