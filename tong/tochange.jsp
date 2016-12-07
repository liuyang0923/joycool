<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.action.tong.TongAction,net.joycool.wap.bean.tong.TongBean,java.util.List,java.util.Vector,net.joycool.wap.bean.UserBean,jc.family.FundDetail,net.joycool.wap.cache.util.TongCacheUtil,net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean"%><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%@ page import="net.joycool.wap.bean.tong.TongApplyBean" %><%!
	/**
	 * 转化帮派为家族
	 * 
	 * @param tongId
	 * @param loginUser
	 * @return
	 */
	static boolean changeTong(TongBean tong, UserBean loginUser, String name, HttpServletRequest request) {
		String tip=null;
		if (name == null || name.trim().equals("")) {
			request.setAttribute("tip", "家族名称最少一个字!");
			return false;
		}
		if (name.equals("荒城")) {
			tip = "您的家族名称已经存在!";
			request.setAttribute("tip", tip);
			return false;
		}
		if (tong.getUserCount() > 500) {
			tip = "转换失败,您的帮会人数超过500人不能转化成家族!";
			request.setAttribute("tip", tip);
			return false;
		}
		if (tong.getUserCount() < 5) {
			tip = "操作失败,帮会人数至少为5人才可转为家族!";
			request.setAttribute("tip", tip);
			return false;
		}
		if (name.length() > 6) {
			tip = "您输入的名称过长,请重新输入";
			request.setAttribute("tip", tip);
			return false;
		}
		int count = jc.family.FamilyAction.service.selectIntResult("select id from fm_home where fm_name='" + StringUtil.toSql(name)
				+ "' limit 1");
		if (count > 0) {
			tip = "您的家族名称已经存在!";
			request.setAttribute("tip", tip);
			return false;
		}
		ForumBean forum = null;
		try {
			forum = ForumCacheUtil.getForumCacheBean(tong.getId());
		} catch (Exception e) {
		}
		int level = 0;
		if (tong.getUserCount() > 80) {
			level = 4;
		}else if (tong.getUserCount() < 81 && tong.getUserCount() > 50) {
			level = 3;
		}else if (tong.getUserCount() < 51 && tong.getUserCount() > 30) {
			level = 2;
		}else if (tong.getUserCount() < 31 && tong.getUserCount() > 4) {
			level = 1;
		}
		int fmid = jc.family.FamilyAction.service.insertchangTong(name, tong.getUserCount()>100?100:tong.getUserCount(), level, tong.getUserId(), loginUser.getNickName(),
				tong.getFund(), forum == null ? 0 : forum.getId());// 创建家族

		if (forum != null) {
			ForumCacheUtil.updateForum("title='" + name + "家族的论坛'", "id=" + forum.getId(), forum.getId());
		}

		if (fmid == 0) {
			tip = "创建家族错误";
			request.setAttribute("tip", tip);
			return false;
		}

		jc.family.FamilyAction.service.insertFmFundDetail(fmid, tong.getFund(), FundDetail.TONG_TYPE, tong.getFund());

		Vector users = (Vector) TongAction.getTongService().getTongUserList("tong_id=" + tong.getId());
		List includeUser = null;
		int maxuser=100;
		if(users.size()>maxuser){
			includeUser = SqlUtil.getIntList("select a.user_id from jc_tong_user a,user_status b where a.tong_id=" + tong.getId()+" and a.user_id=b.user_id order by b.last_login_time desc limit "+maxuser, 0);
		}
		if (users != null) {
			for (int i = 0; i < users.size(); i++) {
				TongUserBean user = (TongUserBean) users.get(i);
				if (user != null) {
					UserBean userBean = (UserBean) UserInfoUtil.getUser(user.getUserId());
					if (userBean != null) {
						if(includeUser == null || includeUser.contains(new Integer(userBean.getId()))){
							UserInfoUtil.updateUserTong(user.getUserId(), fmid);
							TongAction.getUserService().updateOnlineUser("tong_id=" + fmid, "user_id=" + user.getUserId());// 修改在线用户消息
							if (user.getUserId() != loginUser.getId()) {
								jc.family.FamilyAction.service.insertuserchange(user.getUserId(), fmid);// 创建用户
							}
						} else {	// 退出帮会不加入家族
							UserInfoUtil.updateUserTong(user.getUserId(), 0);
						}
					}
				}
			}
		}
		TongCacheUtil.updateTong(
				"title='荒城',user_count=0,cadre_count=0,user_id=-1,user_id_a=-1,user_id_b=-1,rate=0", "id="
						+ tong.getId(), tong.getId());// 城变荒城
		TongCacheUtil.deleteTongUserAll("tong_id=" + tong.getId());
		
		ForumCacheUtil.updateForum("tong_id="+fmid,"tong_id="+tong.getId(),tong.getId());

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
		int c=jc.family.FamilyAction.service.selectIntResult("select count(1) from fm_user where fm_id="+fmid);
		jc.family.FamilyAction.service.updateFmHome("ft=1,fm_member_num="+c,fmid);

//		jc.family.FamilyAction.service.insertFamilyMovement(loginUser.getId(), loginUser.getNickName(), fmid, name,
//				"帮会" + StringUtil.toWml(tong.getTitle()) + "转为家族", "", 4);

		request.setAttribute("fmid", Integer.valueOf(fmid));
		return true;
	}
%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tong(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
String url=null;
if(result.equals("failure")){
response.sendRedirect(("/tong/tongCenter.jsp"));
return;}
TongBean tong =(TongBean)request.getAttribute("tong");
if(tong.getUserId()!= loginUser.getId()){
response.sendRedirect(("/tong/tongCenter.jsp"));
return;}
int cmd=action.getParameterInt("cmd");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="转为家族"><p align="left"><%
if(cmd==0&&tong.getTitle().equals("荒城")){%>
您的家族名为荒城,不能转成家族,可以改名后继续转!<br/>
<a href="tochange.jsp?tongId=<%=tong.getId()%>&#38;cmd=1">改名</a><br/>
<a href="tong.jsp?tongId=<%=tong.getId()%>">返回我的帮会</a><br/><%
}else if(tong.getUserCount()>500){
%>转换失败,您的帮会人数超过500人不能转换成家族!<br/>
<a href="tong.jsp?tongId=<%=tong.getId()%>">返回我的帮会</a><br/><%
}else if(tong.getUserCount()<5){
%>操作失败,帮会人数至少为5人才可转为家族!<br/>
<a href="tong.jsp?tongId=<%=tong.getId()%>">返回我的帮会</a><br/><%
}else{
String name=action.getParameterNoEnter("fname");
if(name==null){name=tong.getTitle();}
if(cmd==1){%>
输入您想要的家族名字<br/>
<input name="fname" maxlength="6"/><br/>
<anchor title="改名">确定<go href="tochange.jsp?tongId=<%=tong.getId()%>&#38;cmd=2" method="post">
<postfield name="fname" value="$(fname)" />
</go></anchor><br/><%
}else if(cmd==2){
if(changeTong(tong,loginUser,name,request)){
int fmid=((Integer) request.getAttribute("fmid")).intValue();
%>恭喜您转换成功,您的帮会成功转为<%=StringUtil.toWml(name)%>家族.<br/>
<a href="/fm/myfamily.jsp?id=<%=fmid%>">去我的家族</a><br/><%
}else{
%><%=request.getAttribute("tip")%><br/>
请重新输入您想要的家族名字<br/>
<input name="fname" maxlength="6"/><br/>
<anchor title="改名">确定<go href="tochange.jsp?tongId=<%=tong.getId()%>&#38;cmd=2" method="post">
<postfield name="fname" value="$(fname)" />
</go></anchor><br/>
<a href="tong.jsp?tongId=<%=tong.getId()%>">返回我的帮会</a><br/><%
}
}else{
%>您的<%=StringUtil.toWml(name)%>帮会即将转为<%=StringUtil.toWml(name)%>家族!<br/>
<a href="tochange.jsp?tongId=<%=tong.getId()%>&#38;cmd=2">确定</a><br/>
或者您可以<a href="tochange.jsp?tongId=<%=tong.getId()%>&#38;cmd=1">改名</a>后在转成家族!<br/><%
}
}%><%=BaseAction.getBottom(request,response)%></p></card></wml>