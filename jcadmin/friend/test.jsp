<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.text.DecimalFormat" %><%@ page import="net.joycool.wap.util.SqlUtil" %><%@ page import="net.joycool.wap.util.db.DbOperation" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.framework.*"%><%response.setHeader("Cache-Control", "no-cache");%>
<%
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
DecimalFormat df = new DecimalFormat("0.##");
//社交指数大于0的所有总和
String query="select sum(social) from user_status where social>0"; 
int socialCount=SqlUtil.getIntResult(query,Constants.DBShortName);
//社交指数大于0的总人数
query="select count(*) from user_status where social>0";
int userSocialCount=SqlUtil.getIntResult(query,Constants.DBShortName);
//社交指数比率
float  social = 0.0f;
if(userSocialCount==0){
}else{
social= (float)socialCount/(float)userSocialCount;
}
float  socialRate =Float.parseFloat(df.format(social));
//		//获取前天的社交指数比率
//		float  yesterdaySocial = 0.0f;
//		dbOp = new DbOperation();
//		dbOp.init();
//		ResultSet rs = null;
//		try {
//			rs = dbOp.executeQuery("select social_rate from jc_admin_friend where to_days(now())-to_days(create_datetime)=2");
//			if (rs != null && rs.next()) {
//				yesterdaySocial = rs.getFloat(1);
//			}
//		} catch (Exception e) {
//			e.printStackTrace(System.out);
//		} finally {
//			dbOp.release();
//		}
//		//社交指数增长比率
//		float increaseSocialRate = socialRate-yesterdaySocial;
//当天求婚次数
query="select count(*) from jc_friend_proposal where to_days(now())-to_days(create_datetime)=1";
int proposalCount=SqlUtil.getIntResult(query,Constants.DBShortName);
//当天结婚次数
query="select count(*) from jc_friend_marriage where to_days(now())-to_days(create_datetime)=1";
int marriageCount=SqlUtil.getIntResult(query,Constants.DBShortName);
//当天离婚次数
query="select count(*) from jc_friend_marriage where mark=2 and to_days(now())-to_days(create_datetime)=1";
int divorceCount=SqlUtil.getIntResult(query,Constants.DBShortName);
//当天离婚率]
float  divorce = 0.0f;
if(divorceCount==0 || marriageCount==0){
}else{
divorce= (float)divorceCount/(float)marriageCount;
}
String	divorceRate =df.format(divorce);
//当天结义次数
query="select count(*) from jc_friend_user where mark=2 and to_days(now())-to_days(create_datetime)=1";
int jyCount=SqlUtil.getIntResult(query,Constants.DBShortName);
//当天割袍断义次数
query="select count(*) from jc_friend_bad_user where to_days(now())-to_days(create_datetime)=1";
int preDeletejyCount=SqlUtil.getIntResult(query,Constants.DBShortName);
//插入交友数据到数据库中
//数据库操作类
DbOperation dbOp = new DbOperation();
dbOp.init();
// 构建查询语句
query = "insert into jc_admin_friend (social_rate,proposal_count,marriage_count," +
	"divorce_count,divorce_rate,jy_count,break_jy_count,create_datetime) values(" +
	""+socialRate+","+ proposalCount + "" +
	","+marriageCount+","+divorceCount+","+divorceRate+","+jyCount+","
	+preDeletejyCount+",now())";
// 执行操作
dbOp.executeUpdate(query);
// 释放资源
dbOp.release();
// macq_2006-11-06_统计更新交友系统后台数据_start
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>       
    <title>test</title>
  </head>
  <body>
    done<br>
  </body>
</html>
