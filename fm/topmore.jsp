<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.util.SqlUtil,net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.*,net.joycool.wap.util.StringUtil,java.util.List,net.joycool.wap.bean.PagingBean,jc.family.*,jc.family.game.yard.*"%><%
FamilyAction action=new FamilyAction(request,response);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族"><p align="left"><%=BaseAction.getTop(request, response)%><%
int a=action.getParameterInt("a");
int c;
if(a==5)
	c=action.service.selectIntResult("select count(id) from fm_home where fm_member_num>30 and uv_self_yes>0");
else if(a==0)
	c=action.service.selectIntResult("select count(id) from fm_home where fm_member_num>10 and uv_self_yes>0");
else if(a==1)
	c=action.service.selectIntResult("select count(id) from fm_home where fm_member_num>10 and uv_yes>0");
else if(a==3)
	c=action.service.selectIntResult("select count(id) from fm_home where fm_member_num>10 and money>100000000");
else if(a!=4)
	c=action.service.selectIntResult("select count(id) from fm_home where fm_member_num>10");
else
	c=action.service.selectIntResult("select count(1) from fm_yard_info where exp>10");
PagingBean paging=new PagingBean(action, c, 10, "p");
int p=action.getParameterInt("p");
List list=null;
%>家族排行榜<br/><%
if(a==0){//活跃排行榜
list=action.service.selectFamilyIdList("fm_member_num>10 and uv_self_yes>0 order by uv_self_yes desc limit "+paging.getStartIndex()+","+paging.getCountPerPage());
%>活跃|<a href="topmore.jsp?a=5">活跃度</a>|<a href="topmore.jsp?a=1">人气</a>|<a href="topmore.jsp?a=2">规模</a>|<a href="topmore.jsp?a=3">财富</a>|<a href="topmore.jsp?a=4">餐厅</a><br/><%
}
if(a==1){//人气排行榜
list=action.service.selectFamilyIdList("fm_member_num>10 and uv_yes>0 order by uv_yes desc limit "+paging.getStartIndex()+","+paging.getCountPerPage());
%><a href="topmore.jsp?a=0">活跃</a>|<a href="topmore.jsp?a=5">活跃度</a>|人气|<a href="topmore.jsp?a=2">规模</a>|<a href="topmore.jsp?a=3">财富</a>|<a href="topmore.jsp?a=4">餐厅</a><br/><%
}
if(a==2){//规模排行榜
list=action.service.selectFamilyIdList("ft=0 and fm_member_num>10 order by fm_member_num desc limit "+paging.getStartIndex()+","+paging.getCountPerPage());
%><a href="topmore.jsp?a=0">活跃</a>|<a href="topmore.jsp?a=5">活跃度</a>|<a href="topmore.jsp?a=1">人气</a>|规模|<a href="topmore.jsp?a=3">财富</a>|<a href="topmore.jsp?a=4">餐厅</a><br/><%
}
if(a==3){//财富排行榜
list=action.service.selectFamilyIdList("fm_member_num>10 and money>100000000 order by money desc limit "+paging.getStartIndex()+","+paging.getCountPerPage());
%><a href="topmore.jsp?a=0">活跃</a>|<a href="topmore.jsp?a=5">活跃度</a>|<a href="topmore.jsp?a=1">人气</a>|<a href="topmore.jsp?a=2">规模</a>|财富|<a href="topmore.jsp?a=4">餐厅</a><br/><%
}
if(a==4){//餐厅排行榜
list=SqlUtil.getIntList("select fmid from fm_yard_info where exp>10 order by exp desc limit "+paging.getStartIndex()+","+paging.getCountPerPage(),5);
%><a href="topmore.jsp?a=0">活跃</a>|<a href="topmore.jsp?a=5">活跃度</a>|<a href="topmore.jsp?a=1">人气</a>|<a href="topmore.jsp?a=2">规模</a>|<a href="topmore.jsp?a=3">财富</a>|餐厅<br/><%
}
if(a==5){//活跃度排行榜
list=action.service.selectFamilyIdList("fm_member_num>30 and uv_self_yes>0 order by uv_self_yes/fm_member_num desc limit "+paging.getStartIndex()+","+paging.getCountPerPage());
%><a href="topmore.jsp?a=0">活跃</a>|活跃度|<a href="topmore.jsp?a=1">人气</a>|<a href="topmore.jsp?a=2">规模</a>|<a href="topmore.jsp?a=3">财富</a>|<a href="topmore.jsp?a=4">餐厅</a><br/><%
}
if(list!=null&&list.size()>0){
int count=0;
	for(int i=0;i<list.size();i++){
		Integer fmId=(Integer)list.get(i);
		FamilyHomeBean home=FamilyAction.getFmByID(fmId.intValue());
		if(home!=null){
			count++;
			%><%=count+p*10%>.<a href="myfamily.jsp?id=<%=home.getId()%>"><%=StringUtil.toWml(home.getFm_name())%></a>|<%
			if(a==0){
				%><%=home.getUvSelfYes()%><%
			}else if(a==1){
				%><%=home.getUvYes()%><%
			}else if(a==5){
				%><%=(float)(home.getUvSelfYes()*1000/home.getFm_member_num())/10%>%<%
			}else if(a==2){
				%><%=home.getFm_member_num()+"人"%><%
			}else if(a==4){
				YardBean yard = YardAction.getYardByID(fmId.intValue());
				%><%=yard.getExp()%>|<%=yard.getRank()+1%>级<%
			}else{
				%><%=StringUtil.bigNumberFormat(home.getMoney())%><%
			}%><br/><%
		}
	}
}%><%=paging.shuzifenye("topmore.jsp?a="+a, true, "|", response)%>
<a href="topgame.jsp?a=2">家族游戏排行榜</a><br/>
<a href="index.jsp">返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>