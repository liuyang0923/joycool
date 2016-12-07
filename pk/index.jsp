<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.Set"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.action.pk.PKAction"%><%@ page import="net.joycool.wap.bean.pk.PKActBean"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.bean.pk.PKMonsterBean"%><%@ page import="net.joycool.wap.bean.pk.PKUserBean"%><%@ page import="java.util.Iterator"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.pk.PKNpcBean" %><%@ page import="net.joycool.wap.action.pk.PKWorld" %><%@ page import="net.joycool.wap.bean.pk.PKObjTypeBean" %><%@ page import="net.joycool.wap.bean.pk.PKEquipBean" %><%@ page import="net.joycool.wap.bean.pk.PKMedicineBean" %><%@ page import="net.joycool.wap.bean.pk.PKMObjBean" %><%@ page import="java.util.HashMap" %><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
UserBean loginUser = action.getLoginUser();
if(loginUser==null){
	response.sendRedirect("/user/login.jsp?backTo=/fs/help.jsp");
	return;
}
action.index(request);
String result = (String)request.getAttribute("result");

String url = ("index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=PKAction.cardTitle%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后返回场景)<br/>
<a href="index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("isFull")){
url = ("index.jsp?sceneId=1");
%>
<card title="<%=PKAction.cardTitle%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后返回入口)<br/>
<a href="index.jsp?sceneId=1">返回入口</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{ 
PKActBean pkAct = (PKActBean)request.getAttribute("pkAct");
String orderBy = (String)request.getParameter("orderBy");
if(orderBy==null){
	orderBy="monster";
}
int sceneId = pkAct.getId();
List monsterList=pkAct.getMonsterList();
Set pkUserList=pkAct.getPkUserList();
List npcList =pkAct.getNpcList();
List log=pkAct.getLog();
HashMap dropMap = pkAct.getDropMap();
String name = pkAct.getDescription();
url = ("index.jsp");
%>
<card title="<%=PKAction.cardTitle%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="200"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=pkAct.toImage()%>
<%=pkAct.getName() %><br/>
-------------------<br/>
<%=name %><a href="<%=url%>">刷新</a><br/>
<%
//掉落物品
if(dropMap!=null && dropMap.size()>0){
	%>====掉落物品====<br/><%
	PKObjTypeBean objType=null;
	Iterator iter= dropMap.values().iterator();
	while(iter.hasNext()){
		objType=(PKObjTypeBean)iter.next();
		if(objType==null)continue;
		 switch(objType.getType()){
			 //装备
			 case 0:
		 		PKEquipBean	pkEquip=(PKEquipBean)action.getProto(objType.getType(),objType.getId());
				if(pkEquip==null)continue;
				 %>
				 <a href="takeObj.jsp?type=<%=objType.getType()%>&amp;id=<%=objType.getId()%>"><%=pkEquip.getName()%></a><br/>
				 <%
			 break;
			 //物品
			 case 1:
			 	PKMedicineBean pkMedicine=(PKMedicineBean)action.getProto(objType.getType(),objType.getId());
				if(pkMedicine==null)continue;
				%>
				<a href="takeObj.jsp?type=<%=objType.getType()%>&amp;id=<%=objType.getId()%>"><%=pkMedicine.getName()%></a><br/>
				<%
			break;
			//任务物品
			case 5:
			 	PKMObjBean pkObj=(PKMObjBean)action.getProto(objType.getType(),objType.getId());
				if(pkObj==null)continue;
				%>
				<a href="takeObj.jsp?type=<%=objType.getType()%>&amp;id=<%=objType.getId()%>"><%=pkObj.getName()%></a><br/>
				<%
			break;
		}
	}
}%>
<%if(sceneId!=1){
	//场景内怪兽
	if(monsterList!=null && monsterList.size()>0 && "monster".equals(orderBy)){%>
	==敌人|<%if(pkUserList!=null && pkUserList.size()>1){%><a href="index.jsp?orderBy=role">侠客</a><%}else{%>侠客<%}%><%if(pkUserList!=null){%><%=pkUserList.size()%>人<%}%>==<br/>
		<%
		PKMonsterBean monster = null;
		Iterator iter= monsterList.listIterator();
		int i=1;
		while(iter.hasNext()){
			monster=(PKMonsterBean)iter.next();
			if(monster.isDeath()){continue;}
			%>
		<a href="moster.jsp?index=<%=monster.getIndex()%>"><%=StringUtil.toWml(monster.getName())%></a>
	<%	if(i%2==0){%><br/><%}
		i++;
		}
		if(i%2==0){%><br/><%}
	}else{%>
	==<a href="index.jsp">敌人</a>|侠客<%if(pkUserList!=null){%><%=pkUserList.size()%>人<%}%>==<br/>
		<%if(pkUserList!=null && pkUserList.size()>1){%>
		<%
			PKUserBean pkUser=null;
			UserBean user = null;
			Iterator iter= pkUserList.iterator();
			int i=1;
			while(iter.hasNext()){
				//只显示10个用户
				if(i>10){break;}
				pkUser=(PKUserBean)iter.next();
				user=UserInfoUtil.getUser(pkUser.getUserId());
				if(user==null || user.getId()==loginUser.getId())continue;
				%>
			<%--<%=StringUtil.toWml(user.getNickName())%><br/>--%>
			<a href="player.jsp?playerId=<%=pkUser.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a><br/>
			<%i++;
			}
		}
	}
}%>
<%--
//场景内NPC
if(npcList!=null && npcList.size()>0){%>
	====商业区====<br/><%
	PKNpcBean pkNpc=null;
	Iterator iter= npcList.iterator();
	int i= 1;
	while(iter.hasNext()){
		pkNpc=(PKNpcBean)iter.next();
	if(pkNpc==null)continue;
		if(pkNpc.getMission().equals("")){%>
			<a href="npcInfo.jsp?npcId=<%=pkNpc.getId())%>&amp;type=<%=action.getNpcType(pkNpc%>"><%=pkNpc.getName()%></a>
		<%}
		//商人3个一换行
		if(i%3==0){%><br/><%}
		i++;
	}
}%>--%>
<%if(sceneId==1){%>
====商业区====<br/>
<a href="npcInfo.jsp?npcId=1&amp;type=0">武器店</a>
<a href="npcInfo.jsp?npcId=2&amp;type=0">防具店</a>
<a href="npcInfo.jsp?npcId=3&amp;type=0">佩饰店</a><br/>
<a href="npcInfo.jsp?npcId=4&amp;type=1">药品店</a>
<a href="npcInfo.jsp?npcId=5&amp;type=4">内功馆</a>
<a href="npcInfo.jsp?npcId=6&amp;type=3">外功馆</a><br/>
====任务堂====<br/>
<a href="npcMission.jsp?npcId=7">乐酷老油条</a><br/>
<%}%>
====个人属性====<br/>
<a href="pkUserInfo.jsp">我的状态</a>
<a href="userSkillList.jsp">我的外功</a><br/>
<a href="userBSkillList.jsp">我的内功</a>
<a href="bodyEquip.jsp">身上装备</a><br/>
<a href="pkUserBag.jsp">我的行囊</a>
<a href="userRest.jsp">休息一会</a><br/>
====<%if(sceneId!=1){%>继续冒险<%}else{%>开始冒险<%}%>====<br/>
<%if(sceneId==1){%>
	<a href="sceneList.jsp">选择进入战斗的地点</a><br/>
<%}else if(sceneId>1 && sceneId<PKWorld.pkActMap.size()){%>
	<a href="index.jsp?sceneId=<%=(sceneId+1)%>">下一地点</a>
	<%if(sceneId>1){%>
		<a href="index.jsp?sceneId=<%=(sceneId-1)%>">上一地点</a><br/>
	<%}%>
<%}else if(sceneId>=PKWorld.pkActMap.size()){%>
	<a href="index.jsp?sceneId=<%=(sceneId-1)%>">上一地点</a><br/>
<%}%>
<%if(sceneId!=1){%>
	<a href="index.jsp?sceneId=1">返回入口</a><br/>
<%}%>
<%if(sceneId!=1){%>
	====战斗记录====<br/>
	<%
	//场景内log
	if(log!=null){
		String content=	pkAct.toString(log);%>
		<%=content%>
	<%}
}
//场景内用户
if(pkUserList!=null && pkUserList.size()>1){%>
====其他侠客====<br/>
<%
	PKUserBean pkUser=null;
	UserBean user = null;
	Iterator iter= pkUserList.iterator();
	int i=1;
	while(iter.hasNext()){
		//只显示10个用户
		if(i>10){break;}
		pkUser=(PKUserBean)iter.next();
		if(pkUser.isDeath())continue;
		user=UserInfoUtil.getUser(pkUser.getUserId());
		if(user==null || user.getId()==loginUser.getId())continue;
		if(sceneId==1){%>
		<%=StringUtil.toWml(user.getNickName())%><br/>
		<%}else{%>
		<a href="player.jsp?playerId=<%=pkUser.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a><br/>
		<%}%>
	<%i++;
	}
}%>
<br/>
<a href="readme.jsp?">查看游戏指南</a><br/>
<a href="help.jsp?">返回侠客秘境首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>