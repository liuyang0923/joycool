<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.action.tong.TongAction,net.joycool.wap.bean.UserBean,net.joycool.wap.bean.UserStatusBean,net.joycool.wap.framework.BaseAction,net.joycool.wap.cache.util.*,net.joycool.wap.bean.tong.*,net.joycool.wap.util.StringUtil,net.joycool.wap.bean.PagingBean,java.util.List,jc.family.*"%><%@include file="../filter.jsp"%><%!
	static boolean tongforfm(TongBean tong,FamilyHomeBean fmHome) {

		Vector users = (Vector) TongAction.getTongService().getTongUserList("tong_id=" + tong.getId());
		if (users != null) {
			for (int i = 0; i < users.size(); i++) {
				TongUserBean user = (TongUserBean) users.get(i);
				if (user != null) {
					UserBean userBean = (UserBean) UserInfoUtil.getUser(user.getUserId());
					if (userBean != null) {
						UserInfoUtil.updateUserTong(user.getUserId(), fmHome.getId());
						TongAction.getUserService().updateOnlineUser("tong_id=" + fmHome.getId(), "user_id=" + user.getUserId());// 修改在线用户消息
						jc.family.FamilyAction.service.insertuserchange(user.getUserId(), fmHome.getId());// 创建用户
					}
				}
			}
		}
		TongCacheUtil.updateTong(
				"title='荒城',user_count=0,cadre_count=0,user_id=-1,user_id_a=-1,user_id_b=-1,rate=0", "id="
						+ tong.getId(), tong.getId());// 城变荒城
		TongCacheUtil.deleteTongUserAll("tong_id=" + tong.getId());

		/**
		 * 删除加入请求
		 */
		Vector tongApplyList = TongAction.getTongService().getTongApplyList("tong_id=" + tong.getId() + " and mark=0 ");
		if (tongApplyList != null) {
			TongApplyBean tongapply = null;
			UserStatusBean status = null;
			for (int i = 0; i < tongApplyList.size(); i++) {
				tongapply = (TongApplyBean) tongApplyList.get(i);
				if (tongapply != null) {
					status = (UserStatusBean) UserInfoUtil.getUserStatus(tongapply.getUserId());
					if (status != null) {
						UserInfoUtil.updateUserTong(tongapply.getUserId(), 0);
					}
				}
			}
			TongAction.getTongService().delTongApply("tong_id=" + tong.getId() + " and mark=0 ");
		}
		int c=jc.family.FamilyAction.service.selectIntResult("select count(1) from fm_user where fm_id="+fmHome.getId());
		jc.family.FamilyAction.service.updateFmHome("ft=0,fm_member_num="+c,fmHome.getId());
		fmHome.setFm_member_num(c);

		jc.family.FamilyAction.service.insertFamilyMovement(0, "", fmHome.getId(), fmHome.getFm_name(),
				"帮会" + StringUtil.toWml(tong.getTitle()) + "合并为家族", "", 4);
		return true;
	}
%><%
response.setHeader("Cache-Control","no-cache");
FamilyAction action=new FamilyAction(request,response);
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>家族后台</title>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body><%
  if(true){//if(group.isFlag(1)){
	int tongid=action.getParameterInt("tongid");
	int fmid=action.getParameterInt("fmid");
	if(fmid==0){
		%><script type="text/javascript">alert('家族ID错误')</script>
		<a href="javascript:history.go(-1);">返回</a><br/>
		<a href="/jcadmin/fm/index.jsp">返回家族首页</a><br/><%
	}else if(tongid==0){
		%><script type="text/javascript">alert('帮会ID错误')</script>
		<a href="javascript:history.go(-1);">返回</a><br/>
		<a href="/jcadmin/fm/index.jsp">返回家族首页</a><br/><%
	}else{
		int c=action.getParameterInt("c");
		FamilyHomeBean fmHome=action.getFmByID(fmid);
		TongBean tong = TongCacheUtil.getTong(tongid);// 获取帮户记录
		if(fmHome==null){
			%><script type="text/javascript">alert('家族ID错误')</script>
			<a href="javascript:history.go(-1);">返回</a><br/>
			<a href="/jcadmin/fm/index.jsp">返回家族首页</a><br/><%
		}else if(tong==null){
			%><script type="text/javascript">alert('帮会ID错误')</script>
			<a href="javascript:history.go(-1);">返回</a><br/>
			<a href="/jcadmin/fm/index.jsp">返回家族首页</a><br/><%
		}else if(c==0){
			%>您决定把帮派的人员合并到家族里吗?<span style="color:red;">(注意:如果家族等级不够则人数会出错!)</span><br/>
			帮派:<%=tong.getTitle()%>,人数:<%=tong.getUserCount()%><br/>
			家族:<%=fmHome.getFm_nameWml()%>,人数:<%=fmHome.getFm_member_num()%>,当前等级为<%=jc.family.Constants.FM_LEVEL_NAME[fmHome.getFm_level()]
			%>,还可以容纳<%=jc.family.Constants.FM_LEVEL[fmHome.getFm_level()]-fmHome.getFm_member_num()%><br/>
			<a href="tongforfm.jsp?c=1&tongid=<%=tongid%>&fmid=<%=fmid%>">确定合并</a><br/><%
		}else if(c==1){
			if(tongforfm(tong,fmHome)){
				%><script type="text/javascript">alert('成功')</script>
				<a href="javascript:history.go(-1);">返回</a><br/>
				<a href="/jcadmin/fm/index.jsp">返回家族首页</a><br/><%
			}else{
				%><%=request.getAttribute("tip")%><br/>
				<a href="javascript:history.go(-1);">返回</a><br/>
				<a href="/jcadmin/fm/index.jsp">返回家族首页</a><br/><%
			}
		}
	}
}%>
  </body>
</html>