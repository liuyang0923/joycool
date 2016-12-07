<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*"%>
<%!
static net.joycool.wap.service.impl.UserServiceImpl service = new net.joycool.wap.service.impl.UserServiceImpl();

%><%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request);
int id = action.getParameterInt("id");
UserBean user = null;
if(id>0)
	user = UserInfoUtil.getUser(id);
if(user != null) {	// 下发短信
	String mobile=user.getMobile();
	if(mobile.startsWith("1")&&mobile.length()==11) {
		String newPassword = String.valueOf(RandomUtil.nextInt(100000,1000000));
		
		
		UserInfoUtil.updateUser("password='" + StringUtil.toSql(Encoder.encrypt(newPassword)) + "'", "id=" + user.getId(), String.valueOf(user.getId()));
		AdminAction.addUserLog(user.getId(), 4, newPassword);
		
		StringBuilder sb = new StringBuilder(70);
		sb.append("您在乐酷http://m.joycool.net的账号为");
		sb.append(user.getId());
		sb.append(",密码");
		sb.append(newPassword);
		
		// 修改流程，不再下发银行密码，而是直接重置为空
		service.updateUserSetting("bank_pw='',update_datetime=now()", "user_id=" + user.getId());
		/*
		UserSettingBean userSetting = service.getUserSetting("user_id="+user.getId());
		if(userSetting!=null&&userSetting.getBankPw().length()>0){
			sb.append(",银行密码");
			sb.append(userSetting.getBankPw());
		}
		*/
		SmsUtil.send(SmsUtil.CODE, sb.toString(), mobile, SmsUtil.TYPE_SMS);
	}
}
%>