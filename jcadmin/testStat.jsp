<%@ page contentType="text/html;charset=utf-8" %><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.test.ITestService"%><%@ page import="net.joycool.wap.test.TestServiceImpl"%><%!
public class Stat {
	private int pageSize = 100;// 每页记录条数

	/**
	 * 获取用户调查问卷回答记录
	 * 
	 * @param userId
	 * @return
	 */
	public Hashtable getRecord(String userId) {
		Hashtable hashTbl = new Hashtable();
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String strsql = "select a.answer_datetime,b.question_id,b.answer_id,a.mark from jc_test_record   as  a join jc_test_answer  as b on a.answer_id=b.id where a.user_id= "
				+ userId + " order by b.question_id,b.answer_id ";
		ResultSet rs = dbOp.executeQuery(strsql);
		int i = 1;
		int questionId = 0;
		String answer = "";
		int mark=0;
		try {
			while (rs.next()) {
				if (i == 1) {
					hashTbl.put("answerTime", rs.getString(1));
					questionId = rs.getInt(2);
					answer = rs.getInt(3) + "";
				} else {
					if (questionId != rs.getInt(2)) {
						hashTbl.put("question" + questionId, answer);
						questionId = rs.getInt(2);
						answer = rs.getInt(3) + "";
					} else {
						answer = answer + "," + rs.getInt(3);
					}
				}
				mark=rs.getInt("mark");
				i++;
			}
			hashTbl.put("question" + questionId, answer);
			hashTbl.put("mark",mark+"");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return hashTbl;
	}

	/**
	 * 用户总数
	 * 
	 * @return
	 */
	public int getUserCount() {
		int count = 0;
		String strsql = "select count(distinct user_id) from jc_test_record order by user_id  ";
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		ResultSet rs = dbOp.executeQuery(strsql);
		try {
			while (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return count;
	}

	/**
	 * 总页数
	 * 
	 * @return
	 */
	public int getPageCount() {
		int userCount = getUserCount();
		int pageCount = userCount / pageSize;
		if (userCount % pageSize != 0)
			pageCount++;
		return pageCount;
	}

	/**
	 * 用户ID
	 * 
	 * @param startIndex
	 * @return
	 */
	public int[] getUserIds(int startIndex) {
		int[] userIds = null;
		String sqlLimit = " limit " + startIndex + "," + pageSize;
		int userCount = getUserCount();
		if (startIndex + pageSize < userCount)
			userIds = new int[pageSize];
		else
			userIds = new int[userCount - startIndex];
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String strsql = " select  user_id from jc_test_record group by user_id order by user_id "
				+ sqlLimit;
		ResultSet rs = dbOp.executeQuery(strsql);
		int i = 0;
		try {
			while (rs.next()) {
				userIds[i] = rs.getInt(1);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return userIds;
	}

	/**
	 * 根据用户ID获取用户列表
	 * 
	 * @param userIds
	 * @return
	 */
	public Vector[] getUserList(int[] userIds) {
		Vector[] userList = new Vector[userIds.length];
		ResultSet rs = null;
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String strsql = " select a.nickname,a.cityname,a.gender,a.age,a.mobile,"
				+ "a.create_datetime,b.game_point,b.point,b.rank,"
				+ "b.last_login_time,b.total_online_time,b.login_count from "
				+ " user_info  as a join user_status as b on a.id=b.user_id "
				+ " where a.id=";
		for (int i = 0; i < userIds.length; i++) {
			rs = dbOp.executeQuery(strsql + userIds[i] + " order by a.id ");
			userList[i] = new Vector();
			try {
				while (rs.next()) {
					userList[i].add(userIds[i] + "");

					userList[i].add(rs.getString(1));
					if (rs.getString(2) != null)
						userList[i].add(rs.getString(2));
					else
						userList[i].add("无");
					if (rs.getInt(3) == 0)
						userList[i].add("女");
					else
						userList[i].add("男");
					userList[i].add(rs.getInt(4) + "");
					userList[i].add(rs.getString(5));
					userList[i].add(rs.getString(6));
					userList[i].add(rs.getInt(7) + "");
					userList[i].add(rs.getInt(8) + "");
					userList[i].add(rs.getInt(9) + "");
					userList[i].add(rs.getString(10));
					userList[i].add(rs.getString(11));
					userList[i].add(rs.getInt(12) + "");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dbOp.release();
		return userList;
	}

	/***************************************************************************
	 * 根据当前页号获取用户列表
	 * 
	 * @param pageIndex
	 * @return
	 */
	public Vector[] getUserList(int pageIndex) {
		int startIndex = 0;
		pageIndex = getCurIndex(getPageCount(), pageIndex);
		startIndex = pageIndex * pageSize;
		int[] userIds = getUserIds(startIndex);
		return getUserList(userIds);
	}

	/**
	 * 得到当前页号
	 * 
	 * @param totalPages
	 * @param curIndex
	 * @return
	 */
	public int getCurIndex(int totalPages, int curIndex) {
		curIndex = Math.min(totalPages - 1, curIndex);
		curIndex = Math.max(0, curIndex);
		return curIndex;
	}

}
%>
<%
	response.setHeader("Cache-Control","no-cache");
    Stat testStat=new Stat();
	
	Hashtable tbl=null;
	int totalPageCount=0;
	
	int pageIndex=0;
	String prefixUrl="testStat.jsp";
	totalPageCount=testStat.getPageCount();
	if(request.getParameter("pageIndex")!=null)
		pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
	Vector[] userList=testStat.getUserList(pageIndex);
	ITestService testService=new TestServiceImpl();
	int questionCount=testService.getQuestionCount(" 1=1 ");
%>
<html>
<head>
</head>
<body>
<h2 align="cneter">问卷调查结果</h2>
<table width="3000" border="1">
<th align="center">编号</th>
<th align="center">昵称</th>
<th align="center">城市</th>
<th align="center">性别</th>
<th align="center">年龄</th>
<th align="center">用户手机号</th>
<th align="center">注册时间</th>
<th align="center">乐币数</th>
<th align="center">经验值</th>
<th align="center">等级</th>
<th align="center">上次登录时间</th>
<th align="center">总的登录次数(次)</th>
<th align="center">总的在线时间(分)</th>
<th align="center">答题时间</th>
<th align="center">答题用户来自网站</th>
<%for(int i=1;i<=questionCount;i++){ %>
<th align="center">问题<%=i%></th>
<%} %>
<%for(int i=0;i<userList.length;i++){
tbl=testStat.getRecord((String)userList[i].get(0));
%>
<tr>
<td><%=(i+1)%></td>
<%for(int j=0;j<userList[i].size()-1;j++){%>
<td><%=userList[i].get(j+1)%></td><%}%>
<td><%=tbl.get("answerTime") %></td>
<%int mark=0;
mark=StringUtil.toInt((String)tbl.get("mark"));
%>
<td><%if(mark==0){%>乐酷<%}else{%>wap通<%}%></td>
<%
String val="";
for(int j=1;j<=questionCount;j++){
	val=(String)tbl.get("question"+j);
	if(val!=null){
%><td><%=val %></td><%}else{ %><td>&nbsp;</td><%}}} %></tr>
</table>
<%
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, false, "|", response);
if(!"".equals(fenye)){
%>
<%=fenye%><br/>
<%} %>
<br/>
<!--  <a href="<%=("testStatAward.jsp") %>">奖品发放</a><br/> -->
<a href="<%=("manage.jsp") %>">返回</a><br/>



</body>
</html>