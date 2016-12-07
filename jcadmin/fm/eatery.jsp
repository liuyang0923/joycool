<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.PagingBean,net.joycool.wap.util.StringUtil,java.util.List,jc.family.*,jc.family.game.yard.*"%><%@include file="../filter.jsp"%><%
response.setHeader("Cache-Control","no-cache");
if(!group.isFlag(1)) return;
YardAction action=new YardAction(request,response);
int id=action.getParameterInt("id");
FamilyHomeBean fmHome=action.getFmByID(id);
YardBean yard=action.getYardByID(id);
if(fmHome==null){
	%><script type="text/javascript">alert('家族不存在')</script>
	<a href="/jcadmin/fm/index.jsp">返回家族首页</a>
	<%
	return;
}
int game=action.getParameterInt("game");
if(game!=0){
	yard.addExp(game);
	%><script type="text/javascript">alert('成功')</script><%
}
int money=action.getParameterInt("money") * 10;
if(money!=0){
	yard.setMoney(yard.getMoney()+money);
	SqlUtil.executeUpdate("update fm_yard_info set money=money+("+money+") where fmid="+yard.getFmid(),5);
	%><script type="text/javascript">alert('成功')</script><%
}
int count=action.getParameterInt("count");
if(count!=0){
	int itemid=action.getParameterInt("itemid");
	yard.addMaterial(itemid,count);
	%><script type="text/javascript">alert('成功')</script><%
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
  		</tr>
  		<tr>
  			<td><%=fmHome.getId()%></td>
  			<td><a href="familyhome.jsp?id=<%=fmHome.getId()%>"><%=fmHome.getFm_nameWml()%></a></td>
  			<td><a href="../user/queryUserInfo.jsp?id=<%=fmHome.getLeader_id()%>"><%=fmHome.getLeaderNickNameWml()%></a></td>
  			<td><%=jc.family.Constants.FM_LEVEL_NAME[fmHome.getFm_level()]%></td>
  			<td><%=fmHome.getGame_num()%></td>
  			<td><a href="familymember.jsp?id=<%=id%>"><%=fmHome.getFm_member_num()%></a></td>
  			<td><a href="funddetail.jsp?id=<%=id%>"><%=fmHome.getMoney()%></a></td>
  			<td><%=fmHome.getBulletin()%></td>
  		</tr>
  	</table>
  	<table>
  		<tr>
  			<td>餐厅名称</td>
  			<td>等级</td>
  			<td>经验值</td>
  			<td>餐厅钱</td>
  		</tr>
  		<tr>
  			<td><%=yard.getNameWml()%></td>
  			<td><%=yard.getRank()%></td>
  			<td><%=yard.getExp()%></a></td>
  			<td><%=YardAction.moneyFormat(yard.getMoney())%></td>
  		</tr>
  	</table><%
  	  List list=yard.getItemList();
  	%><table>
  		<tr><td>id</td>
  			<td>拥有物品</td>
  			<td>数量</td>
  		</tr><%
  		for(int i=0;i<list.size();i++){
  			YardItemBean item=(YardItemBean)list.get(i);
			YardItemProtoBean itemProto = YardAction.getItmeProto(item.getItemId());
  			%><tr><td><%=itemProto.getId()%></td>
  			<td><%=itemProto.getNameWml()%></td>
  			<td><%=item.getNumber()%></a></td></tr><%
  		}%>
  	</table>
  	
  	<form method="post" action="eatery.jsp"><input type="hidden" name="id" value="<%=id%>"/><input name="game"/>&nbsp;<input type="submit" value="增加经验"/></form>
  	<form method="post" action="eatery.jsp"><input type="hidden" name="id" value="<%=id%>"/><input name="money"/>&nbsp;<input type="submit" value="增加基金"/></form>
  	<form method="post" action="eatery.jsp"><input type="hidden" name="id" value="<%=id%>"/><select name="itemid"><%
  		for(int i=0;i<list.size();i++){
  			YardItemBean item=(YardItemBean)list.get(i);
			YardItemProtoBean itemProto = YardAction.getItmeProto(item.getItemId());
  			%><option value="<%=itemProto.getId()%>"><%=itemProto.getNameWml()%></option><%
  		}%>
  	</select>&nbsp;<input name="count"/>&nbsp;<input type="submit" value="增加数量"/></form>
  </body>
</html>