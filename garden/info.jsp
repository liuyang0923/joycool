<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	int uid = gardenAction.getLoginUser().getId();
	
	//GardenAction.gardenService.updateMessages("readed=1","uid="+uid);
		
	int p = gardenAction.getParameterInt("p");
	int limit = 5;
	int start = limit *p;
	
	GardenUserBean bean = GardenUtil.getUserBean(uid);
	bean.setMsgCount(0);
	
	List list = GardenAction.gardenService.getMessages("uid="+uid+" order by id desc limit "+start+","+limit);
	int count = GardenAction.gardenService.getMessageCount("uid="+uid);
	//int p = gardenAction.getParameterInt("p");
	PagingBean paging = new PagingBean(gardenAction, count, limit, "p");
	if(count > 20){
		GardenAction.gardenService.deleteMessages("uid="+uid+" and create_time < DATE_SUB(create_time,INTERVAL 2 day)");
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
<%if(count == 0) {%>
没有任何消息<br/>
<%} else {%>
<%for(int i = 0; i < list.size(); i ++) {
	GardenMessage msg = (GardenMessage)list.get(i);%>
<%=i+1%>.<%=msg.getFromUid()>0?"<a href=\"garden.jsp?uid="+msg.getFromUid()+"\">"+UserInfoUtil.getUser(msg.getFromUid()).getNickNameWml()+"</a>":"你" %><%=StringUtil.toWml(msg.getMessage()) %><br/>
<%} }%>
<%=paging.shuzifenye("info.jsp", false, "|", response)%>
<a href="myGarden.jsp">返回我的农场</a><br/>
<a href="store.jsp">仓库</a>|<a href="bag.jsp">包裹</a>|<a href="shop.jsp">商店</a><br/><a href="friend.jsp">好友的农场</a><br/><a href="invite.jsp">邀请好友开通</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>