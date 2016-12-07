<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.framework.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.util.db.*,java.sql.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean,net.joycool.wap.bean.stock.*"%><%
String sql = "select max_send_count from sms_log where type=" + SmsUtil.TYPE_PUSH;
int maxNum = SqlUtil.getIntResult(sql, Constants.DBShortName);

sql = "select send_count from sms_log where type=" + SmsUtil.TYPE_PUSH;
int sendNum = SqlUtil.getIntResult(sql, Constants.DBShortName);

if(maxNum-sendNum<300){
	String[] mobiles = {"13681062751", "13811061464"};
	String message = "短信猫还可以发" + Math.max(maxNum-sendNum, 0) + "条push,请及时充值";
	for(int i=0;i<mobiles.length;i++){
		String mobile = mobiles[i];
		SmsUtil.send(SmsUtil.CODE, message, mobile, SmsUtil.TYPE_SMS);
	}
}
%>