<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.*,java.sql.*,net.joycool.wap.util.db.DbOperation,net.joycool.wap.bean.PagingBean,java.util.List,jc.family.game.vs.*,jc.family.game.vs.term.*,jc.family.*"%><%@include file="../../filter.jsp"%><%!
static int COUNT_PER_PAGE = 10;
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
int matchid=action.getParameterInt("mid");
if(matchid==0) return;
TermBean term=getTermBean("id="+termid);
TermMatchBean match=getTermMatchBean("id="+matchid);
if(action.hasParam("edit")){
int fmida=action.getParameterInt("fmida");
	int fmidb=action.getParameterInt("fmidb");
	int wager=action.getParameterInt("wager");
	String time=action.getParameterString("time");
	if(fmida!=0&&fmidb!=0){
		match.setFmidA(fmida);
		match.setFmidB(fmidb);
		match.setWager(wager);
		match.setStartTime(DateUtil.parseTime(time).getTime());
		action.termService.upd("update fm_vs_term_match set start_time='"+DateUtil.formatSqlDatetime(match.getStartTime())+"',fma="+match.getFmidA()+",fmb="+match.getFmidB()+",wager="+match.getWager()+" where id="+match.getId());
	}else{
		%><script>alert("请填写正确各项参数!");</script><%
	}
}
%><html>
<link href="../common.css" rel="stylesheet" type="text/css">
<head>
</head>
<body>
争霸赛<br/>
<form method="post" action="editmatch.jsp?id=<%=termid%>&mid=<%=matchid%>&edit=1">
	家族ID:<select name="fmida"><%
		for(int j=0;j<term.getFmList().size();j++){
			Integer iid = (Integer)term.getFmList().get(j);
			FamilyHomeBean fm = FamilyAction.getFmByID(iid.intValue());
			if(fm!=null){
				%><option value="<%=fm.getId()%>" <%=match.getFmidA()==fm.getId()?"selected":""%> ><%=fm.getFm_nameWml()%></option><%
		}
	}%></select>&nbsp;VS&nbsp;家族ID:<select name="fmidb"><%
		for(int j=0;j<term.getFmList().size();j++){
			Integer iid = (Integer)term.getFmList().get(j);
			FamilyHomeBean fm = FamilyAction.getFmByID(iid.intValue());
			if(fm!=null){
				%><option value="<%=fm.getId()%>" <%=match.getFmidB()==fm.getId()?"selected":""%> ><%=fm.getFm_nameWml()%></option><%
		}
	}%></select><br/>
	时间(yyyy-MM-dd HH:mm:ss):<input type="text" id="time" name="time" value="<%=DateUtil.formatSqlDatetime(match.getStartTime())%>"><br/>
	奖金(亿):<input type="text" id="wager" name="wager" value="<%=match.getWager()%>"><br/>
	<input type="submit" id="add" name="add" value="修改"><br/>
</form>
<a href="matchlist.jsp?id=<%=termid%>">返回争霸赛</a><br/>
</body>
</html>