<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.spec.garden.*,java.util.*,net.joycool.wap.spec.castle.*, net.joycool.wap.spec.buyfriends.*,net.joycool.wap.framework.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="java.util.List"%><%
	response.setHeader("Cache-Control", "no-cache");
	
	GardenAction gardenAction = new GardenAction(request);
	int uid = gardenAction.getLoginUser().getId();
	GardenUserBean gardenUser = GardenUtil.getUserBean(uid);
	int cur = gardenAction.getParameterIntS("p");
	int userCur = 0;
	if(cur == -1 && gardenUser!=null){
		List list = GardenAction.gardenService.getHistoryStoreList(" uid = " + uid);
		int count = 0;
		int price = 0;
		for(int i = 0; i < list.size(); i ++) {
			GardenStoreBean bean = (GardenStoreBean)list.get(i);
			GardenSeedBean seedBean = GardenAction.gardenService.getSeedBean(bean.getSeedId());
			count += bean.getCount();
			price += seedBean.getValue()*bean.getCount();
		}
		
		if(GardenAction.gardenService.isContainStat(uid)) {
			GardenAction.gardenService.updateStat(uid,count,price);
			userCur = GardenAction.gardenService.getCurStat(uid);
		}
		else {
			GardenAction.gardenService.addStat(uid,UserInfoUtil.getUser(uid).getNickNameWml(),count,price);
		}
		userCur = GardenAction.gardenService.getCurStat(uid)+1;
	}
	if(cur<0) cur=0;
	int start = cur * 10;
	int limit = 11;
	
	//PagingBean paging = new PagingBean(action, total, 10, "p");
	List list = GardenAction.gardenService.getStat("id > 0 order by price desc limit "+start+","+limit);
	int count = list.size() > 10 ? 10 : list.size();
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="农场达人"><p>
<%if(userCur > 0) {%>
您当前的排名为:第<%=userCur %>名<br/>
<%} %>==农场主/作物总数/总价值==<br/>
<%for(int i=0;i<count;i++){
	Object[] obj = (Object[])list.get(i);
%>
<%=i+(cur*10)+1%>.<a href="garden.jsp?uid=<%=obj[1]%>"><%=StringUtil.toWml((String)obj[2])%></a>/<%=obj[3]%>/<%=obj[4] %>
<br/><%}%>
<%if(list.size() > 10) {%><a href="top.jsp?p=<%=cur+1%>">下一页</a><%}else{%>下一页<%}%>
<%if(cur > 0) {%><a href="top.jsp?p=<%=cur-1%>">上一页</a><%}else{%>上一页<%}%><br/>
跳转到:<input name="p" maxlength="3" format="*N"/><anchor>GO<go href="top.jsp"><postfield name="p" value="$p"/></go></anchor><br/>
<a href="s.jsp">返回农场首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>