<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.PagingBean,net.joycool.wap.util.StringUtil,java.util.List,jc.family.*"%><%!
static int COUNT_PER_PAGE = 20;	// 一页10个好友
static String[] type={"捐款","取款","改名","通知","升级","帮派转家族","家族对战","家族活动","管理员","餐厅兑换","未知"};
%><%response.setHeader("Cache-Control","no-cache");
FamilyAction action=new FamilyAction(request,response);
int id=action.getParameterInt("id");
FamilyHomeBean fmHome=action.getFmByID(id);
if(fmHome==null){
	%><script type="text/javascript">alert('家族不存在')</script>
	<a href="/jcadmin/fm/index.jsp">返回家族首页</a>
	<%
	return;
}
int count=action.service.selectIntResult("select count(id) from fm_fund_detail where fm_id="+id);
PagingBean paging=new PagingBean(action, count, COUNT_PER_PAGE, "p");
List list=action.service.selectFmFundDetail("fm_id="+id+" order by id desc limit "+paging.getStartIndex()+","+ paging.getCountPerPage());
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>家族后台</title>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  	<a href="/jcadmin/fm/index.jsp">返回家族首页</a><br/>
	  <div>
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
	  			<td><%=Constants.FM_LEVEL_NAME[fmHome.getFm_level()]%></td>
	  			<td><%=fmHome.getGame_num()%></td>
	  			<td><a href="familymember.jsp?id=<%=id%>"><%=fmHome.getFm_member_num()%></a></td>
	  			<td><a href="funddetail.jsp?id=<%=id%>"><%=fmHome.getMoney()%></a></td>
	  			<td><%=fmHome.getBulletin()%></td>
	  		</tr>
	  		</table>
	  </div>
	  <table border="1">
  		<tr>
  			<td>ID</td>
  			<td>时间</td>
  			<td>花费(-)/收入(+)</td>
  			<td>余额</td>
  			<td>类型</td>
  			<td>花费(-)/收入(+)(精确)</td>
  			<td>余额(精确)</td>
  		</tr><%
		for(int i=0;i<list.size();i++){
			FundDetail fundDetail=(FundDetail)list.get(i);
  			%><tr><td><%=fundDetail.getId()%></td>
  			<td><%=action.sd2.format(new java.util.Date(fundDetail.getcTime()))%></td>
  			<td><%=StringUtil.bigNumberFormat(fundDetail.getSum())%></td>
  			<td><%=StringUtil.bigNumberFormat(fundDetail.getBalance())%></td>
  			<td><%=type[type.length<fundDetail.getType()?10:fundDetail.getType()]%></td>
  			<td><%=fundDetail.getSum()%></td>
  			<td><%=fundDetail.getBalance()%></td>
  			</tr><%
  		}%>	
  	</table>
   <%=paging.shuzifenye("funddetail.jsp?id=" + id, true, "|", response)%>
  </body>
</html>