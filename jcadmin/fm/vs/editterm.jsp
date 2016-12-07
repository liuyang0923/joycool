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
TermBean term=getTermBean("id="+termid);
if(action.hasParam("edit")){
	String name=action.getParameterNoEnter("name");
	int gametype=action.getParameterInt("type");
	String fmids=action.getParameterString("fmids");
	String info=action.getParameterString("info");
	if(!"".equals(name)){
		term.setName(name);
		term.setGameType(gametype);
		term.setFmids(fmids);
		term.setInfo(info);
		String time=request.getParameter("time");
		term.setCreateTime(DateUtil.parseTime(time).getTime());
		action.termService.upd("update fm_vs_term set name='"+StringUtil.toSql(term.getName())+"',game_type="+term.getGameType()+",fmids='"+term.getFmids()+"',info='"+StringUtil.toSql(term.getInfo())+"',create_time='"+StringUtil.toSql(time)+"' where id="+term.getId());
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
<form method="post" action="editterm.jsp?id=<%=termid%>&edit=1">
	名称:<input id="name" name="name" value="<%=term.getName()%>"><br/>
	类型:<select name="type"><%
	for(int i=0;i<VsGameBean.vsConfig.length;i++){
		if(VsGameBean.vsConfig[i]!=null){
			%><option value="<%=VsGameBean.vsConfig[i].getId()%>" <%=term.getGameType()==i?"selected":""%> ><%=VsGameBean.vsConfig[i].getName()%></option><%
		}
	}%></select><br/>
	家族ID:<input id="fmids" name="fmids" value="<%=term.getFmids()%>">(英文逗号分隔)<br/>
	说明:<textarea id="info" name="info" cols="80" rows="10"><%=term.getInfo()%></textarea></br>
	开始时间:<input id="time" name="time" value="<%=DateUtil.formatSqlDatetime(term.getCreateTime())%>"><br/>
	<input type="submit" id="add" name="add" value="修改"><br/>
<a href="termlist.jsp">返回争霸赛</a><br/>
<a href="/jcadmin/fm/index.jsp">返回家族管理页</a><br/>
</body>
</html>