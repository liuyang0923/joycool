<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.framework.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.util.db.*,java.sql.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%!
public static Hashtable getObjectHash(String sql, List params, String dbName) {
	Hashtable ret = new Hashtable();

	DbOperation dbOp = new DbOperation();
	dbOp.init(dbName);

	PreparedStatement st = null;
	ResultSet rs = null;
	try {
		dbOp.prepareStatement(sql);
		st = dbOp.getPStmt();

		if (params != null && params.size() > 0) {
			try {
				for (int i = 0; i < params.size(); i++) {
					st.setObject(i + 1, params.get(i));
				}
			} catch (Exception e) {
			}
		}

		rs = st.executeQuery();
		while (rs.next()) {
			ret.put(rs.getObject(1), "");
		}
	} catch (Exception e) {
		e.printStackTrace(System.out);
	} finally {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
		}
		dbOp.release();
	}

	return ret;
}
%>
<%
String sql = "select mobile from jc_invite_push_log where mark=0";
Hashtable mobiles = getObjectHash(sql, null, Constants.DBShortName);

if(mobiles!=null){  
	String message = "点击进入乐酷免费社区";
	String url = "wap.joycool.net";
	Enumeration enu = mobiles.keys();
	while(enu.hasMoreElements()){
		String mobile = (String)enu.nextElement();
		if(mobile==null || mobile.equals("")){
			continue;
		}
		
		if(mobile.startsWith("13") || mobile.startsWith("15")){
		    SmsUtil.sendPush(message, mobile, url);
		    sql = "update jc_invite_push_log set mark=1 where mobile='" + mobile + "'";
		    SqlUtil.executeUpdate(sql, Constants.DBShortName);
		}
	}
}
%>
done.