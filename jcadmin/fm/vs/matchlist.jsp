<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.*,java.sql.*,net.joycool.wap.util.db.DbOperation,net.joycool.wap.bean.PagingBean,java.util.List,jc.family.game.vs.*,jc.family.game.vs.term.*,jc.family.*"%><%@include file="../../filter.jsp"%><%!
static int COUNT_PER_PAGE = 20;
	/**
	 * 
	 * @param bean
	 * @return
	 */
	static  boolean insertTermBean(TermBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_vs_term(name,game_type,fmids,create_time,info)values(?,?,?,now(),?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setInt(2, bean.getGameType());
			pstmt.setString(3, bean.getFmids());
			pstmt.setString(4, bean.getInfo());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		bean.setId(dbOp.getLastInsertId());
		// 释放资源
		dbOp.release();
		return true;
	}

	/**
	 * 
	 * @param bean
	 * @return
	 */
	static boolean insertTermMatchBean(TermMatchBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_vs_term_match(challenge_id,start_time,term_id,fma,fmb,wager)values(?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getChallengeId());
			pstmt.setTimestamp(2, new java.sql.Timestamp(bean.getStartTime()));
			pstmt.setInt(3, bean.getTermId());
			pstmt.setInt(4, bean.getFmidA());
			pstmt.setInt(5, bean.getFmidB());
			pstmt.setInt(6, bean.getWager());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		bean.setId(dbOp.getLastInsertId());
		// 释放资源
		dbOp.release();
		return true;
	}

	/**
	 * 
	 * @param cond
	 * @return
	 */
	static List getTermMatchBeanList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_term_match where " + cond);
		try {
			while (rs.next()) {
				list.add(getTermMatchBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	static TermMatchBean getTermMatchBean(String cond) {
		TermMatchBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_term_match where " + cond);
		try {
			if (rs.next()) {
				bean = getTermMatchBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	static TermMatchBean getTermMatchBean(ResultSet rs) throws SQLException {
		TermMatchBean bean = new TermMatchBean();
		bean.setId(rs.getInt("id"));
		bean.setTermId(rs.getInt("term_id"));
		bean.setChallengeId(rs.getInt("challenge_id"));
		bean.setStartTime(rs.getTimestamp("start_time").getTime());
		bean.setFmidA(rs.getInt("fma"));
		bean.setFmidB(rs.getInt("fmb"));
		bean.setWager(rs.getInt("wager"));
		bean.setState(rs.getInt("state"));
		return bean;
	}

	/**
	 * 
	 * @param cond
	 * @return
	 */
	static List getTermBeanList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_term where " + cond);
		try {
			while (rs.next()) {
				list.add(getTermBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	static TermBean getTermBean(String cond) {
		TermBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_term where " + cond);
		try {
			if (rs.next()) {
				bean = getTermBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	static TermBean getTermBean(ResultSet rs) throws SQLException {
		TermBean bean = new TermBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setGameType(rs.getInt("game_type"));
		bean.setFmids(rs.getString("fmids"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setInfo(rs.getString("info"));
		bean.setState(rs.getInt("state"));
		return bean;
	}
%><%
%><%
response.setHeader("Cache-Control","no-cache");
if(!group.isFlag(1)) return;
TermAction action=new TermAction(request,response);
int termid=action.getParameterInt("id");
if(termid==0) return;
if(action.hasParam("add")){
	int fmida=action.getParameterInt("fmida");
	int fmidb=action.getParameterInt("fmidb");
	int wager=action.getParameterInt("wager");
	String time=action.getParameterString("time");
	if(fmida!=0&&fmidb!=0){
		TermMatchBean item=new TermMatchBean();
		item.setTermId(termid);
		item.setFmidA(fmida);
		item.setFmidB(fmidb);
		item.setWager(wager);
		item.setStartTime(DateUtil.parseTime(time).getTime());
		insertTermMatchBean(item);
	}else{
		%><script>alert("请填写正确各项参数!");</script><%
	}
}
TermBean term=getTermBean("id="+termid);
List itemList=getTermMatchBeanList("term_id="+termid);
int p = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(p,itemList.size(),COUNT_PER_PAGE);
%><html>
<link href="../common.css" rel="stylesheet" type="text/css">
<head>
</head>
<body>
争霸赛<br/>
<table width="100%">
	<tr>
		<td>id</td>
		<td>第几届</td>
		<td>是否完赛</td>
		<td>对战双方</td>
		<td>比赛时间</td>
		<td>奖金</td>
		<td>操作</td>
	</tr><%
itemList = itemList.subList(paging.getStartIndex(),paging.getEndIndex());
for(int i =0; i<itemList.size(); i++){
	TermMatchBean item=(TermMatchBean)itemList.get(i);
	long taskTime = TermAction.getTaskTime(item.getId());
	%><tr>
		<td><%=item.getId()%></td>
		<td><%=term.getName()%></td>
		<td><%=item.getChallengeId()==0?"未开始":"已完赛"%></td>
		<td><%
		FamilyHomeBean fm = FamilyAction.getFmByID(item.getFmidA());
		if(fm!=null){
			%><a href="/jcadmin/fm/familyhome.jsp?id=<%=fm.getId()%>"><%=fm.getFm_nameWml()%></a>,<%
		}
		fm = FamilyAction.getFmByID(item.getFmidB());
		if(fm!=null){
			%><a href="/jcadmin/fm/familyhome.jsp?id=<%=fm.getId()%>"><%=fm.getFm_nameWml()%></a><%
		}
		%></td>
		<td><%=DateUtil.formatDate2(item.getStartTime())%><%if(taskTime!=0){%>(<font color=red><%=DateUtil.formatDate2(taskTime)%></font>)<%}%></td>
		<td><%=item.getWager()%>亿</td>
		<td><a href="editmatch.jsp?id=<%=termid%>&mid=<%=item.getId()%>">修改</a></td>
	</tr><%
}
%></table>
<%=paging.shuzifenye("termlist.jsp?id="+termid, true, "|", response, COUNT_PER_PAGE)%>
<a href="termlist.jsp">返回争霸赛</a><br/>
<a href="/jcadmin/fm/index.jsp">返回家族管理页</a><br/>
<form method="post" action="matchlist.jsp?add=1&id=<%=termid%>">
	家族ID:<select name="fmida"><%
		for(int j=0;j<term.getFmList().size();j++){
			Integer iid = (Integer)term.getFmList().get(j);
			FamilyHomeBean fm = FamilyAction.getFmByID(iid.intValue());
			if(fm!=null){
				%><option value="<%=fm.getId()%>"><%=fm.getFm_nameWml()%></option><%
		}
	}%></select>&nbsp;VS&nbsp;家族ID:<select name="fmidb"><%
		for(int j=0;j<term.getFmList().size();j++){
			Integer iid = (Integer)term.getFmList().get(j);
			FamilyHomeBean fm = FamilyAction.getFmByID(iid.intValue());
			if(fm!=null){
				%><option value="<%=fm.getId()%>"><%=fm.getFm_nameWml()%></option><%
		}
	}%></select><br/>
	时间(yyyy-MM-dd HH:mm:ss):<input type="text" id="time" name="time"><br/>
	奖金(亿):<input type="text" id="wager" name="wager"><br/>
	<input type="submit" id="add" name="add" value="增加"><br/>
</form>
</body>
</html>