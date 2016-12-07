<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.service.impl.*"%><%@ page import="net.joycool.wap.framework.*"%><%!
static byte[] lock = new byte[0];
static DummyServiceImpl dummyService = new DummyServiceImpl();
%><%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request);
int userId = action.getParameterInt("userId");
String code = action.getParameterNoEnter("code");
int presentId = 107;	// 礼品id
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷休闲网游社区">
<%=BaseAction.getTop(request, response)%>
<%if(userId > 0&&code!=null){  /*兑换*/
int result = 0;
if(UserInfoUtil.getUser(userId)==null){
	result = 1;
}else{
	Class.forName("com.mysql.jdbc.Driver");
	synchronized(lock){
		Connection conn = DriverManager.getConnection("jdbc:mysql://211.157.107.135/shop", "remoteroot", "rt55betterdb");
		try{
			int noteId = 0;
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select id from property_history where code='" + StringUtil.toSql(code) + "' and status=0");
			if(rs.next()) {
				noteId = rs.getInt(1);
			}
			
			if(noteId > 0) {
				
				if(st.executeUpdate("update property_history set status=1,used_datetime=now(),joycool_user_id="+userId+" where id="+noteId)>0){
					UserBagCacheUtil.addUserBagCache(userId, presentId, 1);
					result = 2;
					
					net.joycool.wap.action.NoticeAction.sendNotice(userId, "礼品兑换成功", "", NoticeBean.GENERAL_NOTICE, "", "/user/userBag.jsp");
				}
			}
			
			st.close();
		}catch(Exception e) {e.printStackTrace();}
		
		conn.close();
	}
}
%>

<%if(result==2){%>
兑换成功!<br/>
您使用乐酷礼品券得到了<%=dummyService.getDummyProduct("id="+presentId).getName()%>，已放入行囊情前往查收。祝您在乐酷玩得开心！<br/>
<a href="/wapIndex.jsp">返回乐酷首页</a>|
<a href="http://wap.yytun.com">继续逛逛云雨堂</a>
<br/>

<%}else if (result==1){%>
兑换失败,帐号<%=userId%>不存在<br/>
<a href="yyt2.jsp?code=<%=StringUtil.toWml(code)%>">重新兑换</a><br/>
<%}else{%>
兑换失败<br/>
您输入的序列号 有误/已使用，请检查后重新再试。
<a href="yyt2.jsp">继续兑换</a><br/>
<%}%>


<%}else{ /*显示兑换页面*/ %>
使用乐酷礼品券<br/>
----------<br/>
请输入乐酷帐号ID:<br/>
<input type="text" name="userId" format="*N" maxlength="10" value=""/><br/>

<%if(code==null){%>
请在下面输入框内填入您的礼品券序列号:<br/>
<input type="text" name="code" value=""/><br/>
<%}else{%>
乐酷礼品券号码:<br/>
<%=code%>
<a href="yyt2.jsp">修改乐酷礼品券号码</a><br/>
<%}%>
<br/>
<anchor title="ok">确认兑换
  <go href="yyt2.jsp" method="post">
    <postfield name="userId" value="$userId"/>
<%if(code==null){%>
    <postfield name="code" value="$code"/>
<%}else{%>
    <postfield name="code" value="<%=code%>"/>
<%}%>
  </go>
</anchor><br/>
=如何获得乐酷礼品券=<br/>
在云雨堂每个商品的介绍页面最底部,都有可能散落着乐酷的新年小礼物,友友们看见后点击捡起来,输入乐酷ID即可使用.<br/>
<a href="http://wap.yytun.com/adult/Column.do?columnId=5965&amp;fr=17">马上去找礼品&gt;&gt;</a><br/>
<br/>
<%}%>

<%=BaseAction.getBottom(request, response)%>
</card>
</wml>