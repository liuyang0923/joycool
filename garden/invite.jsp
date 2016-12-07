<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.cache.*,net.joycool.wap.util.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	
	List list = UserInfoUtil.getUserFriends(gardenAction.getLoginUser().getId());
	
	int index = gardenAction.getParameterInt("index");
	String action = gardenAction.getParameterString("action");
	int count = 0;
	int pre = gardenAction.getParameterInt("pre");
	List data = new ArrayList();
	synchronized(CacheManage.noGardenCache){
	if(action != null) {
		if(action.equals("next")) {
			pre = index;
			for(int i = index; i<list.size();i++){
				if(count <= 5) {
					int ii = Integer.parseInt((String)list.get(i));
					GardenUserBean bean = (GardenUserBean)CacheManage.noGardenCache.get(new Integer(ii));
					if(bean == null) {
						if(bean == null)
							bean = GardenUtil.getUserBean(ii);
						
						if(bean == null) {
							data.add(new Integer(ii));
							count++;
							CacheManage.noGardenCache.put(new Integer(ii),GardenUtil.NULL_Garden);
						}
					} else if(bean.getUid() == GardenUtil.NULL_Garden.getUid()){
						data.add(new Integer(ii));
						count++;
						CacheManage.noGardenCache.put(new Integer(ii),GardenUtil.NULL_Garden);
					}
					
					if(count <=5)
						index++;
				} else {
					break;
				}
			}
		}else if(action.equals("pre")){
			index = pre;
			for(int i = pre; i>=0 && i <= list.size();i--){
				
				if(count <= 5) {
					int ii = Integer.parseInt((String)list.get(i));
					
					GardenUserBean bean = (GardenUserBean)CacheManage.noGardenCache.get(new Integer(ii));
					if(bean == null) {
						if(bean == null)
							bean = GardenUtil.getUserBean(ii);
						
						if(bean == null) {
							data.add(new Integer(ii));
							count++;
							CacheManage.noGardenCache.put(new Integer(ii),GardenUtil.NULL_Garden);
						}
					} else if(bean.getUid() == GardenUtil.NULL_Garden.getUid()){
						data.add(new Integer(ii));
						count++;
						CacheManage.noGardenCache.put(new Integer(ii),GardenUtil.NULL_Garden);
					}
					
					if(count <=5)
						pre--;
				} else {
					break;
				}
			}
		}
	}else{
		for(int i = index; i<list.size();i++){
			pre = index;
			if(count <= 5) {
				int ii = Integer.parseInt((String)list.get(i));

				GardenUserBean bean = (GardenUserBean)CacheManage.noGardenCache.get(new Integer(ii));
				if(bean == null) {
					if(bean == null)
						bean = GardenUtil.getUserBean(ii);
					
					if(bean == null) {
						data.add(new Integer(ii));
						count++;
						CacheManage.noGardenCache.put(new Integer(ii),GardenUtil.NULL_Garden);
					}
				} else if(bean.getUid() == GardenUtil.NULL_Garden.getUid()){
					data.add(new Integer(ii));
					count++;
					CacheManage.noGardenCache.put(new Integer(ii),GardenUtil.NULL_Garden);
				}
				
				if(count <=5)
					index++;
			} else {
				break;
			}
		}
	}
	}
	String actions = request.getParameter("action");
	String indexs = request.getParameter("index");
	String pres = request.getParameter("pre");
	String urls = "";
	if(actions!=null){
		urls += "&amp;action="+actions;
	}
	if(indexs!=null){
		urls += "&amp;index="+indexs;
	}
	if(pres != null) {
		urls += "&amp;pre="+pres;
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
好东西要与好朋友一起分享哦，马上邀请你的好友来加入农场吧！<br/>
<%
int cc = data.size() > 5 ? 5:data.size();
if(cc > 0) {%>
<%
for(int i = 0;i < cc;i++) {
	Integer ii = (Integer)data.get(i);
%>
<a href="doInvite.jsp?uid=<%=ii.intValue()%><%=urls%>">邀请开通</a>.<a href="/user/ViewUserInfo.do?userId=<%=ii.intValue()%>"><%=UserInfoUtil.getUser(ii.intValue())==null?"乐客":UserInfoUtil.getUser(ii.intValue()).getNickNameWml() %></a><br/>
<%}
if(action == null) {
if(count > 5) {%>
<a href="invite.jsp?action=next&amp;index=<%=index %>">下一页</a><br/><%}%>
<%} else {%>
<%if(count > 5) {%><a href="invite.jsp?action=next&amp;index=<%=index %>">下页</a><%if(count > 5 && pre > 0){%>.<%}%><%} if(pre > 0) {%><a href="invite.jsp?action=pre&amp;pre=<%=pre %>">上页</a><%} %><br/>
<%} %>
<%} else { 
	List list2 = GardenAction.gardenService.getGardenUsers("select uid from garden_user order by rand() limit 5",5);
%>
您没有未开通农场的好友<br/>
<a href="/user/ViewFriends.do">添加其他乐酷好友</a><br/>
添加其他农场玩家为好友<br/>
<%for(int i = 0;i<list2.size();i++) {
	Integer ii = (Integer)list2.get(i);
%>
<a href="/user/ViewUserInfo.do?userId=<%=ii.intValue()%>"><%=UserInfoUtil.getUser(ii.intValue()).getNickNameWml() %></a><br/>
<%} %>
<a href="random.jsp">更多农场玩家</a><br/>
<%} %>
<a href="myGarden.jsp">返回我的农场</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>