<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.PagingBean,net.joycool.wap.util.StringUtil,java.util.List"%><%@ page import="jc.family.*"%><%!
static int index=50;%><%FamilyAction action=new FamilyAction(request,response);
int p=action.getParameterInt("p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族动态"><p align="left"><%=BaseAction.getTop(request, response)%><%
int c=action.service.selectIntResult("select count(id) from fm_movement");
if(c>index)c=index;
PagingBean paging=new PagingBean(action,c,10,"p");
List mlist=action.service.selectFmActivities(paging.getStartIndex(),paging.getCountPerPage());
if(mlist!=null&&mlist.size()>0){
	for(int i=0;i<mlist.size();i++){
		FamilyActivityBean activity=(FamilyActivityBean)mlist.get(i);
		%><%=p*10+i+1%>.<%
		if(activity.getFm_state()==0){
			%><a href="/user/ViewUserInfo.do?userId=<%=activity.getUserid()%>"><%=StringUtil.toWml(activity.getUsername())%></a>建立<a href="myfamily.jsp?id=<%=activity.getFm_id()%>"><%=StringUtil.toWml(activity.getFm_name())%></a>家族<%
		}else if(activity.getFm_state()==1){
			%><a href="/user/ViewUserInfo.do?userId=<%=activity.getUserid()%>"><%=StringUtil.toWml(activity.getUsername())%></a>解散<%=StringUtil.toWml(activity.getFm_name())%>家族<%
		}else if(activity.getFm_state()==2){
			FamilyHomeBean fmhome=action.getFmByID(activity.getFm_id());
			%><a href="myfamily.jsp?id=<%=activity.getFm_id()%>"><%=StringUtil.toWml(fmhome==null?"":fmhome.getFm_name())%></a>家族获得<a href="<%=activity.getFm_url()%>"><%=StringUtil.toWml(activity.getMovement())%></a>的第一名<%
		}else if(activity.getFm_state()==3){
			FamilyHomeBean fmhome=action.getFmByID(activity.getFm_id());
		%><%=StringUtil.toWml(activity.getFm_name())%>家族更名为<a href="myfamily.jsp?id=<%=activity.getFm_id()%>"><%=StringUtil.toWml(fmhome.getFm_name())%>家族</a><%
		}else if(activity.getFm_state()==4){
			FamilyHomeBean fmhome=action.getFmByID(activity.getFm_id());
			%><%=activity.getMovement()%><a href="myfamily.jsp?id=<%=activity.getFm_id()%>"><%=StringUtil.toWml(fmhome==null?"":fmhome.getFm_name())%></a><%
		}%><br/><%
	}
}%><%=paging.shuzifenye("activity.jsp",false, "|", response)%>
<a href="index.jsp">返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>