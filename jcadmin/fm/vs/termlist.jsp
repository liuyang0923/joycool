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
response.setHeader("Cache-Control","no-cache");
if(!group.isFlag(1)) return;
TermAction action=new TermAction(request,response);
if(action.hasParam("add")){
	String name=action.getParameterNoEnter("name");
	int gametype=action.getParameterInt("type");
	String fmids=action.getParameterString("fmids");
	String info=action.getParameterString("info");
	if(!"".equals(name)){
		TermBean item=new TermBean();
		item.setName(name);
		item.setGameType(gametype);
		item.setFmids(fmids);
		item.setInfo(info);
		insertTermBean(item);
	}else{
		%><script>alert("请填写正确各项参数!");</script><%
	}
}
if(action.hasParam("f")){
	int id=action.getParameterInt("id");
	if(id!=0){
		action.termService.upd("update fm_vs_term set state=1 where id="+id);
	}
}
if(action.hasParam("r")){
	action.reLoad();
}
List itemList=getTermBeanList("1");
int p = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(p,itemList.size(),COUNT_PER_PAGE);
%><html>
<link href="../common.css" rel="stylesheet" type="text/css">
<head>
</head>
<body>
争霸赛<br/>
<a href="termlist.jsp?r=1" onclick="return confirm('是否确定重置?')" >重新加载</a>
<table width="100%">
	<tr>
		<td>id</td>
		<td>名称</td>
		<td>比赛项目</td>
		<td>开始时间</td>
		<td>参加家族</td>
		<td>说明</td>
		<td>状态</td>
		<td>操作</td>
	</tr><%
itemList = itemList.subList(paging.getStartIndex(),paging.getEndIndex());
for(int i =0; i<itemList.size(); i++){
	TermBean item=(TermBean)itemList.get(i);
	%><tr>
		<td><%=item.getId()%></td>
		<td><a href="matchlist.jsp?id=<%=item.getId()%>"><%=item.getName()%></a></td>
		<td><%=VsGameBean.getGameIdName(item.getGameType())%></td>
		<td><%=DateUtil.formatDate2(item.getCreateTime())%></td>
		<td><%
		for(int j=0;j<item.getFmList().size();j++){
			Integer iid = (Integer)item.getFmList().get(j);
			FamilyHomeBean fm = FamilyAction.getFmByID(iid.intValue());
			if(j!=0) out.print(',');
			if(fm!=null){
				%><a href="/jcadmin/fm/familyhome.jsp?id=<%=fm.getId()%>"><%=fm.getFm_nameWml()%></a><%
			}
		}%><br/></td>
		<td><%=item.getInfo()%></td>
		<td><%=item.getState()==0?"正在进行":"完结"%></td>
		<td><a href="editterm.jsp?id=<%=item.getId()%>">修改</a><%if(item.getState()==0){
				%>&nbsp;
				<a href="termlist.jsp?id=<%=item.getId()%>&f=1">完赛</a><%
			}%></td>
	</tr><%
}
%></table>
<%=paging.shuzifenye("termlist.jsp", false, "|", response, COUNT_PER_PAGE)%>
<a href="/jcadmin/fm/index.jsp">返回家族管理页</a><br/>
<form method="post" action="termlist.jsp?add=1">
	名称:<input id="name" name="name"><br/>
	类型:<select name="type"><%
	for(int i=0;i<VsGameBean.vsConfig.length;i++){
		if(VsGameBean.vsConfig[i]!=null){
			%><option value="<%=VsGameBean.vsConfig[i].getId()%>"><%=VsGameBean.vsConfig[i].getName()%></option><%
		}
	}%></select><br/>
	家族ID:<input id="fmids" name="fmids">(英文逗号分隔)<br/>
	说明:<textarea id="info" name="info" ></textarea></br>
	<input type="submit" id="add" name="add" value="增加"><br/>
</form>
</body>
</html>