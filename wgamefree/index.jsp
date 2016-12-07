<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%
response.setHeader("Cache-Control","no-cache");
if(SecurityUtil.oxcxMobile(request)){
	LogUtil.totalRedirect ++;
	String url = "http://wap.joycool.net/wgamefree/index.jsp";	
	SecurityUtil.redirectGetMobile(response, url);
} else {
	String enterUrl = PageUtil.getCurrentPageURL(request);
	String mobile = SecurityUtil.getPhone(request);	
	if(mobile != null){
		LogUtil.totalBack ++;
	}
	if(mobile != null && !mobile.equals("")){
		if(mobile.startsWith("86")){
			mobile = mobile.substring(2);
		}
		int linkId = 0;
		/*
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "select id from jc_log_mobile_ua where mobile='" + mobile + "' and enter_datetime > subdate(now(), interval 30 MINUTE)";
		ResultSet rs = dbOp.executeQuery(query);
		if(!rs.next()){
		    query = "insert into jc_log_mobile_ua(mobile, ua, enter_datetime, ip_address, link_id, enter_url) values('" + mobile + "', '" + SecurityUtil.getUA(request) + "', now(), '" + request.getRemoteAddr() + "', " + linkId + ", '" + enterUrl + "')";
			System.out.println(query);
		    dbOp.executeUpdate(query);
		}
		dbOp.release();
		*/
		LogUtil.logMobile(mobile + ":" + SecurityUtil.getUA(request) + ":" + request.getRemoteAddr() + ":" + linkId);
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="美女赌场-脱衣试玩版">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="http://wap.joycool.net/Column.do?columnId=1139">热！玩游戏赢相机</a><br/>
<a href="http://wap.joycool.net/wgamepk/index.jsp">热！新推出PK大厅！快来互相PK吧！</a><br/>
美女赌场-脱衣试玩版<br/>
<img src="img/ducheng.gif" alt="loading..."/><br/>
美女陪玩！连赢次数越多，美女越开放！<br/>
<a href="girls.jsp">赌场美女列表</a><br/>
<a href="http://wap.joycool.net/wapbbs/Paging.do?messageId=181&amp;key=5000" title="进入">[强]我把丝丝脱光啦！好大好漂亮哦！</a><br/>
交流经验<a href="http://wap.joycool.net/wapbbs/Paging.do?forumId=8&amp;key=2002" title="进入">进入</a><br/>
<a href="dice/index.jsp">掷骰子</a><br/>
<a href="tiger/index.jsp">老虎机</a><br/>
<a href="g21/index.jsp">二十一点</a><br/>
<a href="showhand/index.jsp">乐酷梭哈</a><br/>
<a href="jsb/index.jsp">剪刀石头布</a><br/>
还看什么看，快开始玩吧，赢了就有惊喜！<br/>
注册之后更精彩！财富美女等着您！注册即送10000个乐币！看更多美女！<br/>
<a href="http://wap.joycool.net/user/register.jsp?backTo=<%=PageUtil.getBackTo(request)%>">我要注册！！</a><br/>
进入<a href="http://wap.joycool.net/wgame/index.jsp">美女赌场财富版</a><br/>
说明：这里是未注册版赌场，游戏中下注的乐币可以随便填。
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>