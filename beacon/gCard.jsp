<%@ page language="java" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.*"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
response.setHeader("Cache-Control","no-cache");
	int type = StringUtil.toId(request.getParameter("type"));
	
	StringBuilder result = new StringBuilder();
	switch(type) {
		case 1:
			result.append("我在乐酷上传了我的新照片，还有我的最新动态，你现在就来登录wap.joycool.net就可以了解我的动态");
		break;
		case 2:
			result.append("哥们儿！快点来救我！我在乐酷上被别人欺负了，你现在就来登录wap.joycool.net就可以了解我的动态 ");
		break;
		case 3:
			result.append("我现在是乐酷上的游戏达人，你敢来和我较量吗，你现在就来登录wap.joycool.net就可以了解我的动态 ");
		break;
		case 4:
			result.append("老同学，你没忘了我吧，我可是很想念你的，咱们的同学都在乐酷上呢，就差你了，你现在就来登录wap.joycool.net就可以了解我的动态 ");
		break;
		case 5:
			result.append("亲爱的，我在这里给你存了一份神秘的礼物，赶紧过来取吧。要快哦，你现在就来登录wap.joycool.net就可以了解我的动态 ");
		break;
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="邀请"><p><%=BaseAction.getTop(request, response)%>
<%=result.toString()%><br/>
<a href="#">获取邀请函</a><br/>该邀请函将直接发送到您的手机
<br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>

