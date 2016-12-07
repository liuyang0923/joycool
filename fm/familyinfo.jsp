<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,net.joycool.wap.util.StringUtil,net.joycool.wap.framework.BaseAction,jc.family.*,jc.family.game.vs.*"%><%!
	static java.text.DecimalFormat numFormat = new java.text.DecimalFormat("000");
	static String familyDomain(int id) {
		if(id<20000)
			return "";
		String setDomain = FamilyAction.getDomain(id);	// 家族自定义域名
		if(setDomain != null)
			return setDomain;
		id -= 20000;
		String ids = numFormat.format(id);
		if(ids.startsWith("0"))
			return ids;
		else
			return "0" + ids;
	}
%><%
VsAction action=new VsAction(request,response);
int id=action.getParameterInt("id");
if(id==0){
response.sendRedirect("index.jsp");return;
}
FamilyHomeBean fmhome=FamilyAction.getFmByID(id);
if(fmhome==null){
response.sendRedirect("index.jsp");return;
}
FamilyUserBean fmUser = action.getFmUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族资料"><p align="left"><%=BaseAction.getTop(request, response)%>
【<%=fmhome.getFm_nameWml()%>】资料<br/>
今日活跃<%=fmhome.getUvSelf()%>,人气<%=fmhome.getUv()%>.<br/>
&gt;&gt;家族域名(<a href="/enter/index.jsp?fm=<%=fmhome.getId()%>">保存书签</a>)<br/>
<%=familyDomain(fmhome.getId())%>.joycool.net<%if(fmUser!=null&&fmUser.isflagAppoint()&&fmUser.getFmId()==fmhome.getId()){%>(<a href="manage/domain.jsp">修改</a>)<%}%><br/>
&gt;&gt;基本信息<br/>
家族等级:<a href="manage/level.jsp?id=<%=fmhome.getId()%>&#38;cmd=see"><%=jc.family.Constants.FM_LEVEL_NAME[fmhome.getFm_level()]%></a><br/>
家族人数:<a href="memberlist.jsp?id=<%=fmhome.getId()%>"><%=fmhome.getFm_member_num()%>人</a><br/>
家族声望:<%=fmhome.getAllyCount2()%><br/>
游戏经验值:<a href="point.jsp?id=<%=fmhome.getId()%>"><%=fmhome.getGame_num()%></a><br/>
家族基金:<%=fmhome.getMoney()%><br/><%
FmScore fmScore=action.service.selectFmScore("id="+id);
%>
&gt;&gt;活动积分<br/>
问答:<%=fmScore.getAskScore()%><br/>
赛龙舟:<%=fmScore.getBoatScore()%><br/>
打雪仗:<%=fmScore.getSnowScore()%><br/>
&gt;&gt;挑战积分<br/><%
int[] socre=action.getFmSocre(id);
for(int i=0;i<VsGameBean.getMaxGameId();i++){
VsConfig vsConfig=VsGameBean.getVsConfig(i);
if(vsConfig!=null&&vsConfig.getHide()==0){
%><%=VsGameBean.getGameIdName(i)%>:<%=socre[i]!=0?socre[i]+"":"未参赛"%><br/><%
}
}%>
&lt;<a href="myfamily.jsp?id=<%=id%>"><%=fmhome.getFm_nameWml()%></a>&lt;<a href="/fm/index.jsp">家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>