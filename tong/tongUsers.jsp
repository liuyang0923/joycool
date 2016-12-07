<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.UserBean,net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%response.setHeader("Cache-Control","no-cache");
TongAction action=new TongAction(request);
action.tongUsers(request);
TongBean tong=(TongBean)request.getAttribute("tong");
UserBean loginUser= (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="帮主转让">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(tong!=null){
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List tongList=(List)request.getAttribute("tongList");
%>
请任命你的继任帮主：<br/>
<%if(tongList!=null){
for(int i=0;i<tongList.size();i++){
Integer tongUser=(Integer)tongList.get(i);
if(tongUser.intValue()==loginUser.getId())
continue;
TongUserBean tongUserBean=TongCacheUtil.getTongUser(tong.getId(),tongUser.intValue());
if(tongUserBean!=null){
    UserBean user= UserInfoUtil.getUser(tongUserBean.getUserId());
    //UserStatusBean userStatus = UserInfoUtil.getUserStatus(tongUserBean.getUserId());
    if(user!=null ){
    	//if(userStatus.getRank()<=TongAction.MIN_RANK_FOR_TONG || userStatus.getGamePoint()<TongAction.MIN_MONEY_FOR_TONG || userStatus.getSocial()<=TongAction.MIN_SOCIAL_FOR_TONG){
    	//	continue;
    	//}
    	//else
    	{
%>
<a href="/tong/transferToNotice.jsp?tongId=<%=tong.getId()%>&amp;user=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%>  </a>
<%=tongUserBean.getDonation()%>贡献<%=StringUtil.toWml(action.getTongTitle(tongUserBean))%><br/>
<%     
    	}
    } 
}
}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回公告 </a>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a><br/>
<% 
 }
}else{%>
您无权转让！ <br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>