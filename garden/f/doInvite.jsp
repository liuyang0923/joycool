<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,net.joycool.wap.action.*"%><%! static FlowerService service=new FlowerService();%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	List list = UserInfoUtil.getUserFriends(action.getLoginUserId());
	int uid = action.getParameterInt("uid");
	UserBean user = UserInfoUtil.getUser(uid);
	String tip = "";
	if (user == null){
		tip = "该用户不存在.";
	} else {
		NoticeAction.sendNotice(uid, action.getLoginUser().getNickNameWml() + "邀请你加入花之境."
				, "", NoticeBean.GENERAL_NOTICE, "", "/garden/f/index.jsp");
		tip = "发送成功.";
	}
%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
<%=tip%><br/>
<a href="invite.jsp">返回继续邀请</a><br/>
<a href="fgarden.jsp">返回我的花园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>