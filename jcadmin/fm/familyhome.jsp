<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.PagingBean,net.joycool.wap.util.StringUtil,net.joycool.wap.action.tong.TongAction,java.util.List,jc.family.*"%><%@include file="../filter.jsp"%><%
response.setHeader("Cache-Control","no-cache");
if(!group.isFlag(1)) return;
FamilyAction action=new FamilyAction(request,response);
int id=action.getParameterInt("id");
FamilyHomeBean fmHome=action.getFmByID(id);
if(fmHome==null){
	%><script type="text/javascript">alert('家族不存在')</script>
	<a href="/jcadmin/fm/index.jsp">返回家族首页</a>
	<%
	return;
}
if(action.hasParam("del")&&fmHome.getFm_member_num()<5){	// 解散家族
	List list = FamilyAction.service.selectAllFmList(id);
	for (int i = 0; i < list.size(); i++) {
		FamilyUserBean fmUserBean = (FamilyUserBean) list.get(i);
		UserInfoUtil.updateUserTong(fmUserBean.getId(), 0);
		TongAction.getUserService().updateOnlineUser("tong_id=0", "user_id=" + fmUserBean.getId());// 修改在线用户消息
		FamilyAction.familyUserCache.srm(new Integer(fmUserBean.getId()));
	}
	fmHome.setFm_member_num(0);
	FamilyAction.service.updateFmHome("fm_member_num=0",id);
}
String name=action.getParameterNoEnter("name");
if(name!=null){
	if(!action.checkFamilyName(name)){
	%><script type="text/javascript">alert('<%=action.getTip()%>')</script><%
	}else{
		if(action.service.updateFmHome("fm_name='"+StringUtil.toSql(name)+"'",id)){
		fmHome.setFm_name(StringUtil.toSql(name));
		%><script type="text/javascript">alert('成功,改名为<%=name%>')</script><%
		}
	}
}
List levelList=action.getParameterIntList("level");
if(levelList.size()!=0){
	int level=((Integer)levelList.get(0)).intValue();
	if(level>0&&level<jc.family.Constants.FM_LEVEL_NAME.length){
		if(action.service.updateFmHome("fm_level="+level,id)){
			fmHome.setFm_level(level);
			%><script type="text/javascript">alert('成功')</script><%
		}
	}
}
int game=action.getParameterInt("game");
if(game!=0){
	if(action.service.updateFmHome("game_num="+game,id)){
		fmHome.setGame_num(game);
		%><script type="text/javascript">alert('成功')</script><%
	}
}
long money=action.getParameterLong("money");
if(money!=0){
	if(action.service.updateFmHome("money=(money+"+money+")",id)){
		fmHome.setMoney(fmHome.getMoney()+money);
		LogUtil.logAdmin(adminUser.getName() + "给家族["+id+"]加基金"+money);
		action.service.insertFmFundDetail(id, money, FundDetail.ADMIN_TYPE, fmHome.getMoney());
		%><script type="text/javascript">alert('成功')</script><%
	}
}
int maxcount=action.getParameterInt("maxcount");
if(maxcount!=0){
	maxcount=maxcount+fmHome.getMaxMember();
	if(maxcount<0){
		%><script type="text/javascript">alert('失败,检查输入值')</script><%
	}else{
		if(action.service.updateFmHome("max_member="+maxcount,id)){
			fmHome.setMaxMember(maxcount);
			%><script type="text/javascript">alert('成功,现在人数上限是:<%=fmHome.getMaxMember()%>')</script><%
		}
	}
}
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>家族后台</title>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  <a href="/jcadmin/fm/index.jsp">返回家族首页</a><br/>
  	<table border="1">
  		<tr>
  			<td>家族ID</td>
  			<td>名称</td>
  			<td>族长</td>
  			<td>等级</td>
  			<td>游戏经验</td>
  			<td>人数</td>
  			<td>家族基金</td>
  			<td>家族公告</td>
  			<td>论坛</td>
  			<td>人数上限</td>
  		</tr>
  		<tr>
  			<td><%=fmHome.getId()%></td>
  			<td><a href="familyhome.jsp?id=<%=fmHome.getId()%>"><%=fmHome.getFm_nameWml()%></a></td>
  			<td><a href="../user/queryUserInfo.jsp?id=<%=fmHome.getLeader_id()%>"><%=fmHome.getLeaderNickNameWml()%></a></td>
  			<td><%=jc.family.Constants.FM_LEVEL_NAME[fmHome.getFm_level()]%>(<%=fmHome.getFm_level()%>)</td>
  			<td><%=fmHome.getGame_num()%></td>
  			<td><a href="familymember.jsp?id=<%=id%>"><%=fmHome.getFm_member_num()%></a></td>
  			<td><a href="funddetail.jsp?id=<%=id%>"><%=fmHome.getMoney()%></a></td>
  			<td><%=fmHome.getBulletin()%></td>
  			<td><%if(fmHome.getForumId()!=0){%><a href="/jcadmin/jcforum/index.jsp?id=<%=fmHome.getForumId()%>">论坛</a><%}else{%>(无)<%}%>|<a href="chat.jsp?fid=<%=fmHome.getId()%>">聊天</a></td>
  			<td><%=fmHome.getMaxMember()%></td>
  		</tr>
  	</table>
  	<form method="get" action="familyhome.jsp">家族ID:<input name="id"/>&nbsp;<input type="submit" value="查询"/></form>
  	<form method="post" action="familyhome.jsp"><input type="hidden" name="id" value="<%=id%>"/><input name="name"/>&nbsp;<input type="submit" value="修改名称"/></form>
  	<form method="post" action="familyhome.jsp"><input type="hidden" name="id" value="<%=id%>"/>
  	<select name="level"><%
  		for(int i=1;i<jc.family.Constants.FM_LEVEL_NAME.length;i++){
  			%><option value="<%=i%>"><%=jc.family.Constants.FM_LEVEL_NAME[i]%></option><%
  		}%>
  	</select>&nbsp;<input type="submit" value="修改等级"/></form>
  	<form method="post" action="familyhome.jsp"><input type="hidden" name="id" value="<%=id%>"/><input name="game"/>&nbsp;<input type="submit" value="修改游戏经验"/></form>
  	<form method="post" action="familyhome.jsp"><input type="hidden" name="id" value="<%=id%>"/><input name="money"/>&nbsp;<input type="submit" value="增加基金"/></form>
  	<form method="post" action="familyhome.jsp"><input type="hidden" name="id" value="<%=id%>"/><input name="maxcount"/>&nbsp;<input type="submit" value="增加人数上限"/></form><%
  	if(fmHome.isEatery()){
  	%><a href="eatery.jsp?id=<%=fmHome.getId()%>">家族餐厅</a><%
  	}%>
  	<br/>
  	<%if(fmHome.getFm_member_num()<5){%><a href="familyhome.jsp?id=<%=fmHome.getId()%>&del=1" onclick="return confirm('确认解散该家族？')"><font color=red>解散家族</font></a><br/><%}%>
  </body>
</html>