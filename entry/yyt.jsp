<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.framework.*"%><%!
static byte[] lock = new byte[0];
static int[] moneys={0,0,100,200,500};%><%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request);
int userId = action.getParameterInt("userId");
String code = action.getParameterNoEnter("code");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷休闲网游社区">
<%=BaseAction.getTop(request, response)%>
<%if(userId > 0&&code!=null){  /*兑换*/
int result = 0;
long money = 0;
if(UserInfoUtil.getUser(userId)==null){
	result = 1;
}else{
	Class.forName("com.mysql.jdbc.Driver");
	synchronized(lock){
		Connection conn = DriverManager.getConnection("jdbc:mysql://211.157.107.135/shop", "remoteroot", "rt55betterdb");
		try{
			int noteId = 0;
			int type=0;
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select id,type_id from user_card where code='" + StringUtil.toSql(code) + "' and status=0");
			if(rs.next()) {
				noteId = rs.getInt(1);
				type = rs.getInt(2);
			}
			
			if(noteId > 0 && type>1 && type<5) {
				money = (long)moneys[type] * 100000000;
				
				if(st.executeUpdate("update user_card set status=1,use_time=now() where id="+noteId)>0){
				
					BankCacheUtil.updateBankStoreCacheById(money, userId,100,Constants.BANK_TRADE_TYPE);
					result = 2;
					
					net.joycool.wap.action.NoticeAction.sendNotice(userId, "云雨堂兑换券兑换成功", "", NoticeBean.GENERAL_NOTICE, "", "/bank/bank.jsp");
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
您使用乐币兑换券，得到了<%=StringUtil.bigNumberFormat(money)%>乐币，已存进银行请前往查收。祝您在乐酷玩得开心！<br/>
<a href="/wapIndex.jsp">返回乐酷首页</a>|
<a href="yyt.jsp">继续兑换</a>
<br/>

<%}else if (result==1){%>
兑换失败,帐号<%=userId%>不存在<br/>
<a href="yyt.jsp?code=<%=StringUtil.toWml(code)%>">重新兑换</a><br/>
<%}else{%>
兑换失败<br/>
您输入的序列号 有误/已使用，请检查后重新再试。
<a href="yyt.jsp">继续兑换</a><br/>
<%}%>


<%}else{ /*显示兑换页面*/ %>
使用乐币券换乐币<br/>
----------<br/>
请输入乐酷帐号ID:<br/>
<input type="text" name="userId" format="*N" maxlength="10" value=""/><br/>

<%if(code==null){%>
请在下面输入框内填入您的兑换券序列号:<br/>
<input type="text" name="code" value=""/><br/>
<%}else{%>
兑换券号码:<br/>
<%=code%>
<a href="yyt.jsp">修改兑换券号码</a><br/>
<%}%>
<br/>
<anchor title="ok">确认兑换
  <go href="yyt.jsp" method="post">
    <postfield name="userId" value="$userId"/>
<%if(code==null){%>
    <postfield name="code" value="$code"/>
<%}else{%>
    <postfield name="code" value="<%=code%>"/>
<%}%>
  </go>
</anchor><br/>
=如何获得乐币券=<br/>
云雨堂站内活动或购物均能获得积分.120积分可兑换100亿乐币券,200积分兑换200亿乐币券.300积分兑换500亿券. 
<a href="http://wap.yytun.com/adult/Column.do?columnId=6648">查看详情&gt;&gt;</a><br/>

<%}%>

<%=BaseAction.getBottom(request, response)%>
</card>
</wml>