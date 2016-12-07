<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
int userid=action.getParameterInt("userid");
FamilyUserBean fmUser = null;
if(userid>0){
	fmUser=action.getFmUserByID(userid);
}
FamilyHomeBean fmhome=null;
if(fmUser!=null)
	fmhome = action.getFmByID(fmUser.getFmId());
boolean same = fmUser!=null&&fmLoginUser!=null&&fmUser.getFmId()==fmLoginUser.getFmId();// 是同一个家族
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族成员信息"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if(fmUser!=null){%>

<a href="/user/ViewUserInfo.do?userId=<%=fmUser.getId()%>"><%=fmUser.getNickNameWml()%></a><br/>
所属家族:<a href="myfamily.jsp?id=<%=fmhome.getId()%>"><%=fmhome.getFm_nameWml()%></a><br/>
称号:<%="".equals(fmUser.getFm_name())?"无":StringUtil.toWml(fmUser.getFm_name())%><br/>
<a href="/chat/post.jsp?toUserId=<%=fmUser.getId()%>">&gt;&gt;与ta聊天</a><br/>
&gt;&gt;<%if(same&&fmLoginUser.isflagAppoint()){%><a href="manage/powermgt.jsp?uid=<%=fmUser.getId()%>">任命</a><%}else{%>任命<%}%><%if(same&&fmLoginUser.isflagRemoveMemberint()&&!fmUser.isflagAppoint()){%>-<a href="manage/tofire.jsp?uid=<%=fmUser.getId()%>">开除</a><%}%><br/>
个人功勋:<a href="exploitmyuser.jsp?userid=<%=fmUser.getId()%>"><%=fmUser.getCon_fm()%></a><br/>
活跃点:<%=fmUser.getAlive()%><br/>
管理权限:<%=fmUser.getFmFlags()%><br/>
挑战赛:<%if (FamilyUserBean.isVsGame(fmUser.getFm_state())){%>精英<%}else{%>无<%}%><br/>
基金贡献:<%=fmUser.getGift_fm()%><br/>
加入于:<%=DateUtil.formatDate1(fmUser.getCreate_time())%><br/>
<%}else{%>

没有找到该家族成员!<br/>
<%if(fmLoginUser!=null){%>
<input name="userid" format="*N" maxlength="9"/>
<anchor title="搜索">根据ID搜索
  <go href="fmuserinfo.jsp" method="post">
    <postfield name="userid" value="$(userid)"/>
  </go>
</anchor><br/>
<%}%>

<%}%>
<%if(same || fmUser==null&&fmLoginUser!=null){%><a href="exploituser.jsp">返回家人排名</a><br/><%}%>
&lt;<%if(fmhome==null){%><%if(fmLoginUser!=null){%><a href="myfamily.jsp">我的家族</a><%}%><%}else{%><a href="myfamily.jsp?id=<%=fmhome.getId()%>"><%=fmhome.getFm_nameWml()%></a><%}%>&lt;<a href="index.jsp">家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>