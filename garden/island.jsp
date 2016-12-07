<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.bean.*"%><%!
 static FlowerService service = new FlowerService();%><%
	response.setHeader("Cache-Control","no-cache");
	//GardenService gardenService = GardenService.getInstance();
	//GardenAction gardenAction = new GardenAction(request);
	//int uid = gardenAction.getLoginUser().getId();
	//GardenUserBean bean = gardenService.getGardenUser(uid);
	//if(bean == null) {
	//	bean = gardenService.addGardenUser(uid);
		
	//	for(int i = 0;i<GardenUtil.defaultCount;i++) {
	//		gardenService.addGardenField(uid);
	//	}
			
	//}

	GardenAction action = new GardenAction(request);
	UserBean loginUser = action.getLoginUser();
	int type = action.getParameterInt("t");
	
	List list = GardenUtil.getRandomUserList(5);//GardenAction.gardenService.getGardenUsers("select uid from garden_user order by rand() limit 5",5);

	List list2 = GardenAction.gardenService.getMessages(" from_uid > 0 and id>=(select max(id)-100 from garden_message) group by uid order by id desc limit 5");

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="采集岛"><p>
<%=BaseAction.getTop(request, response)%>
<%if(loginUser==null||loginUser.getUserSetting()==null||!loginUser.getUserSetting().isFlagHideLogo()){%><img src="img/logo.gif" alt="logo"/><br/><%}%>
<a href="s.jsp">黄金农场</a>：种植喜爱的作物,收获勤劳的果实.<br/>
<a href="f/index.jsp">花之境</a>:不仅可以种植自己喜欢的鲜花,还可以配置出珍奇的花种哦!<br/>
欢乐牧场:(即将推出).<br/>
=采集岛居民+<a href="/jcforum/forum.jsp?forumId=9066">交流区</a>=<br/>
<%for(int i = 0;i<list.size();i++) {
	Integer ii = (Integer)list.get(i);
	GardenUserBean bean = GardenUtil.getUserBean(ii.intValue());
%>
<a href="/user/ViewUserInfo.do?userId=<%=ii.intValue()%>"><%=UserInfoUtil.getUser(ii.intValue()).getNickNameWml() %></a>  <%=GardenUtil.getLevel(bean.getExp()) %>级<br/>
<%} %>
==最新动态==<br/>
<% if ( type > 1 ) { type = 0; }
 %>
<%for(int i = 0; i < list2.size(); i ++) {
	GardenMessage msg = (GardenMessage)list2.get(i);%>
<%=i+1%>.<%="<a href=\"garden.jsp?uid="+msg.getFromUid()+"\">"+UserInfoUtil.getUser(msg.getFromUid()).getNickNameWml()+"</a>" %><%=StringUtil.toWml(msg.getMessage()).replace("你","<a href=\"garden.jsp?uid="+msg.getUid()+"\">"+UserInfoUtil.getUser(msg.getUid()).getNickNameWml()+"</a>" )%><br/>
<%}%>
=采集岛达人=<br/>
<%  
	if ( type > 1 ) { type = 0; }
	if (type == 0){
		// 农场动态
		List list3 = GardenAction.gardenService.getStat("id > 0 order by price desc limit 5");
		%>农场|<a href="island.jsp?t=1">花之境</a><br/>
		农场主/作物总数/总价值<br/>
		<%for(int i=0;i<list3.size();i++){
			Object[] obj = (Object[])list3.get(i);
		%>
		<%=i+1%>.<a href="garden.jsp?uid=<%=obj[1]%>"><%=StringUtil.toWml((String)obj[2])%></a>/<%=obj[3]%>/<%=obj[4] %>
		<br/><%}%><%
	} else if (type == 1){
		// 花之境动态
		List rankList = service.getRank("1 limit 0,5");
	//	List logList = service.getMessageList("1 order by id desc limit 0,5");
		%><a href="island.jsp">农场</a>|花之境<br/>
		用户名/成就值<br/>
		<%  UserBean user = null;
			FlowerRankBean frb = null;
			for (int i=0;i<rankList.size();i++){
				frb = (FlowerRankBean)rankList.get(i);
				user = UserInfoUtil.getUser(frb.getUserId());
				%><%=frb.getId() %>.<a href="/user/ViewUserInfo.do?userId=<%=user.getId() %>"><%=user.getNickNameWml()%></a>/<%=frb.getExp() %><br/><%
			}
		%>
		<%
	}
%>
<a href="/home/home2.jsp">返回我的家园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>