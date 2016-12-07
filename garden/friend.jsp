<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.cache.*,net.joycool.wap.util.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	
	List list = UserInfoUtil.getUserFriends(gardenAction.getLoginUser().getId());
	
	int index = gardenAction.getParameterInt("index");
	String action = gardenAction.getParameterString("action");
	int count = 0;
	int pre = gardenAction.getParameterInt("pre");
	List data = new ArrayList();
	if(action != null) {
		if(action.equals("next")) {
			pre = index;
			for(int i = index; i<list.size();i++){
				if(count <= 10) {
					int ii = Integer.parseInt((String)list.get(i));
					
					GardenUserBean bean = GardenUtil.getUserBean(ii);
					if(bean != null) {
						data.add(new Integer(ii));
						count++;
					} else {
						CacheManage.noGardenCache.spt(new Integer(ii),GardenUtil.NULL_Garden);
					}
					
					if(count <=10)
						index++;
				} else {
					break;
				}
			}
		}else if(action.equals("pre")){
			index = pre;
			for(int i = pre; i>=0 && i <= list.size();i--){
				
				if(count <= 10) {
					int ii = Integer.parseInt((String)list.get(i));
					GardenUserBean bean = GardenUtil.getUserBean(ii);
					if(bean != null) {
						data.add(0,new Integer(ii));
						count++;
					} else {
						CacheManage.noGardenCache.spt(new Integer(ii),GardenUtil.NULL_Garden);
					}
					if(count <=10)
						pre--;
				} else {
					break;
				}
			}
		}
	}else{
		for(int i = index; i<list.size();i++){
			pre = index;
			if(count <= 10) {
				int ii = Integer.parseInt((String)list.get(i));
				GardenUserBean bean = GardenUtil.getUserBean(ii);
				if(bean != null) {
					data.add(new Integer(ii));
					count++;
				}else {
					CacheManage.noGardenCache.spt(new Integer(ii),GardenUtil.NULL_Garden);
				}
				if(count <=10)
					index++;
			} else {
				break;
			}
		}
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
<%
int cc = data.size() > 10 ? 10:data.size();
if(cc>0){
%>
好友的农场<br/>
<%
}
if(cc==0){
%>您还没有开通农场的好友<br/>
<a href="invite.jsp">邀请你的乐酷好友加入农场</a><br/>
添加其他农场玩家为好友<br/>
<%
List list2 = GardenAction.gardenService.getGardenUsers("select uid from garden_user order by rand() limit 5",5);
%>
<%for(int i = 0;i<list2.size();i++) {
	Integer ii = (Integer)list2.get(i);
%>
<a href="/user/ViewUserInfo.do?userId=<%=ii.intValue()%>"><%=UserInfoUtil.getUser(ii.intValue()).getNickNameWml() %></a><br/>
<%} %>
<a href="random.jsp">更多农场玩家</a><br/>
<%
}
for(int i = 0;i < cc;i++) {
	Integer ii = (Integer)data.get(i);
%>
<a href="garden.jsp?uid=<%=ii.intValue()%>"><%=UserInfoUtil.getUser(ii.intValue()).getNickNameWml() %></a><br/>
<%}
if(action == null) {
if(count > 10) {%>
<a href="friend.jsp?action=next&amp;index=<%=index %>">下一页</a><br/><%}%>
<%} else {%>
<%if(count > 10) {%><a href="friend.jsp?action=next&amp;index=<%=index %>">下页</a><%if(count > 10 && pre > 0){%>.<%}%><%} if(pre > 0) {%><a href="friend.jsp?action=pre&amp;pre=<%=pre %>">上页</a><%} %><br/>
<%} %>
<a href="myGarden.jsp">返回我的农场</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>