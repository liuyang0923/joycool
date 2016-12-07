<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*,net.joycool.wap.cache.util.*,net.joycool.wap.bean.item.*,net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");


CustomAction customAction = new CustomAction(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
int id = customAction.getParameterInt("id");
String e = request.getParameter("e");
String action = request.getParameter("a");

List seqList = null;

if(action == null) {
	if(id > 0) {
		seqList = (List)session.getAttribute("extra_info_seq");
		
		if(seqList == null) {
			seqList = loginUser.getEIList(StringUtil.toInts(UserInfoUtil.getUserSettingSeq(loginUser.getId())));
		}
		if(e == null || e.length() == 0 || e.endsWith("top")) {
			seqList = loginUser.bagSeqTop(seqList, id);
		} else if(e.equals("tail")) {
			seqList = loginUser.bagSeqTail(seqList, id);
		} else if(e.equals("up")) {
			seqList = loginUser.bagSeqUp(seqList, id);
		} else {
			seqList = loginUser.bagSeqTop(seqList, id);
		}
		session.setAttribute("extra_info_seq", seqList);
	} else {
		seqList = loginUser.getEIList(StringUtil.toInts(UserInfoUtil.getUserSettingSeq(loginUser.getId())));
		session.setAttribute("extra_info_seq", seqList);
	}
} else {
	seqList = (List)session.getAttribute("extra_info_seq");
	
	if(seqList == null) {
		response.sendRedirect("extraInfo.jsp?userId="+loginUser.getId());
		return;
	}
	
	String seq = loginUser.toSeqString(seqList);
	
	UserSettingBean userSetting = UserBean.uService.getUserSetting("user_id="
			+ loginUser.getId());
	if(userSetting == null) {
		userSetting = new UserSettingBean();
		userSetting.setUserId(loginUser.getId());
		UserBean.uService.addUserSetting(userSetting);
	}	
	UserBean.uService.updateUserSetting(" bag_seq = '" + seq + "'"," user_id = " + loginUser.getId());
	loginUser.getUserSetting().setBagSeq(seq);
	CacheManage.itemShow.srm(loginUser.getId());
	CacheManage.userSettingSeqCache.srm(loginUser.getId());
	response.sendRedirect("extraInfo.jsp?userId="+loginUser.getId());
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="用户特殊荣誉">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("msg") != null? request.getAttribute("msg")+"<br/>":"" %>
<%if(loginUser.getUs2()!=null){%><%=loginUser.getUs2().getHatShow()%><%}%><a href="/user/ViewUserInfo.do?userId=<%=loginUser.getId() %>"><%=StringUtil.toWml(loginUser.getNickName())%></a><br/>
<% // 显示用户信息图片，录入新人卡、旗帜卡等等
List ei = loginUser.getEIList(seqList);
HashMap map = UserBean.uService.getItemShowMap("1");
for(int i = 0; i < ei.size(); i ++){
	ShowBean bean = (ShowBean)map.get((Integer)ei.get(i));
%>
<img src="/rep/lx/e<%=bean.getItemId()%>.gif" alt="<%=bean.getName() %>"/><a href="editExtraInfo.jsp?id=<%=bean.getItemId() %>">最前</a>.<a href="editExtraInfo.jsp?id=<%=bean.getItemId() %>&amp;e=up">上移</a>.<a href="editExtraInfo.jsp?id=<%=bean.getItemId() %>&amp;e=tail">最后</a><br/>
<%}%>
<a href="editExtraInfo.jsp?a=s">保存</a><br/>
<a href="/user/ViewUserInfo.do?userId=<%=loginUser.getId() %>">返回用户信息</a><br/> 
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/> 
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
</p>
</card>
</wml>