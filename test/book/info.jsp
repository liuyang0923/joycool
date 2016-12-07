<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.user.RankAction"%><%@ page import="net.joycool.wap.bean.NoticeBean"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction" %><%@ page import="net.joycool.wap.test.TestAction" %><%@ include file="../testAction.jsp" %><%
response.setHeader("Cache-Control","no-cache");
%>
<%!
/**
 * 更新用户经验值
 * @param userId
 */
public void addUserPoint(UserBean loginUser,int point){
	RankAction.addPoint(loginUser,point);
	NoticeBean notice = new NoticeBean();
	notice.setUserId(loginUser.getId());
	notice.setTitle("感谢支持,"+point+"点经验值已经加上!");
	notice.setContent("");
	notice.setType(NoticeBean.GENERAL_NOTICE);
	notice.setHideUrl("");
	notice.setLink("/chat/hall.jsp");
	NoticeUtil.getNoticeService().addNotice(notice);
}
%>
<%


int point=500;
TestAction action=new TestAction(request);
//测试是否结束
String key = "";
int userId = 0;
if(action.isTested()==false){
	//把回答写回数据库
	action.saveRecord(request);
	UserBean loginUser = action.getLoginUser(request);
	userId = loginUser.getId();
	String sql = "select user_key from jc_test_user where user_id=" + userId;
	key = SqlUtil.getStringResult(sql, Constants.DBShortName);
	if(key==null || key.trim().equals("")){
		key = (System.currentTimeMillis() + "").substring(7);
		sql = "insert into jc_test_user(user_id, user_key) values(" + userId + ",'" + key + "')";  
		SqlUtil.executeUpdate(sql, Constants.DBShortName);
	}
	//addUserPoint(action.getLoginUser(request),point);
}
else{
	//response.sendRedirect(("infoTested.jsp"));
	BaseAction.sendRedirect("/test/book/infoTested.jsp", response);
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="调查问卷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
感谢您参与我们的调查,您已获取乐酷云雨堂购物代金券一张(20元整),具体使用方法如下:<br/>
1.您的id是<%= userId %>，您的答题id是<%= key %>,请您务必记住.凭此两个号码方可使用代金券;<br/>
2.此代金券仅限于在“乐酷云雨堂”购物使用;<br/>
3.此代金券在您购物满100元方可使用;<br/>
4.当我们打电话与您确认订单时，请主动提出您拥有代金券，以便我们为您优惠;<br/>
5.此代金券有效期至2006年12月12日.<br/>
<a href="http://shop.joycool.net/adult/wapIndex.jsp?fr=2"><%= StringUtil.toWml("立即购物>>") %></a><br/>
<a href="http://shop.joycool.net/adult/wapIndex.jsp?fr=2"><%= StringUtil.toWml("去店里看看>>") %></a><br/>
<a href="http://wap.joycool.net"><%= StringUtil.toWml("返回乐酷首页<<") %></a><br/>
<%
String indexUrl = "http://wap.joycool.net";
String mark = (String)session.getAttribute("markQuestion");
if(mark!=null){
	indexUrl = "http://wap.g3me.cn";
}
%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>