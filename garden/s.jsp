<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*,net.joycool.wap.bean.UserBean"%><%
	response.setHeader("Cache-Control","no-cache");
	//GardenService gardenService = GardenService.getInstance();
	GardenAction gardenAction = new GardenAction(request);
	UserBean loginUser = gardenAction.getLoginUser();
	int uid = loginUser.getId();
	GardenUserBean bean = GardenAction.gardenService.getGardenUser(uid);
	if(bean != null) {
		boolean flag = false;
		List keyList = GardenUtil.getLatestUserList();
		for(int i = 0; i < keyList.size(); i ++){
			int ii = ((Integer)keyList.get(i)).intValue();
			if(ii == uid) {
				flag = true;
			}
		}
		if(!flag) {
			GardenUtil.addLatestUser(uid);
		}
	}
	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
<%if(loginUser==null||loginUser.getUserSetting()==null||!loginUser.getUserSetting().isFlagHideLogo()){%><img src="img/logo_no.gif" alt="logo"/><br/><%}%>
【<a href="intro.jsp">黄金农场</a>】开张了，赶快开辟自己的一块农田来种出丰厚的果实吧!<br/>
<a href="myGarden.jsp">进入我的农场</a><br/>
+<a href="/jcforum/forum.jsp?forumId=9066">采集岛居民交流区</a>+<br/>
<%-- <a href="friend.jsp">参观好友农场</a><br/> --%>
<a href="top.jsp">农场达人榜</a><br/>
<a href="help.jsp">游戏帮助说明</a><br/>
<a href="faq.jsp">常见问题</a><br/>
<a href="island.jsp">返回采集岛</a><br/>
<a href="/home/home2.jsp">返回我的家园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>