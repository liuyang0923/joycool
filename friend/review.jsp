<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.bean.friend.FriendReviewBean"%><%@ page import="net.joycool.wap.bean.friend.FriendMarriageBean"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page errorPage=""%><%@ page import="net.joycool.wap.service.infc.IFriendService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.bean.friend.FriendBean" %><%@ page import="net.joycool.wap.bean.friend.FriendGuestBean" %><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
UserBean loginUser= (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
int marriageId = StringUtil.toInt(request.getParameter("marriageId"));
String review=request.getParameter("review");
String reviewUser=request.getParameter("reviewUser");
String delete=request.getParameter("delete");
int reviewId=StringUtil.toInt(request.getParameter("reviewId"));
String money=(String)request.getParameter("money");
FriendAction action=new FriendAction(request);
if(money!=null)
{int redbag=StringUtil.toInt(money);
if(!action.setRedBag(redbag,marriageId)){%>
<card title="送红包">
<p align="left">
<%=BaseAction.getTop(request, response)%>
对不起，您的乐币不够<br/>
<a href="/friend/redbag.jsp?marriageId=<%=marriageId%>">少给红包</a><br/>
<a href="/friend/review.jsp?marriageId=<%=marriageId%>">直接进入</a><br/>
<a href="/bank/bank.jsp">去银行取钱</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}
else{
	out.clearBuffer();
	response.sendRedirect("review.jsp?marriageId="+marriageId);
    return;
    }
}
else if(money==null){%>
<card title="结婚现场">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<br/>
<%
if(delete!=null&&reviewId>0){
action.deleteReview(request);
}
action.getReviewList(request);
String perPage=(String)request.getAttribute("NUM_PER_PAGE");
String totalCount=(String)request.getAttribute("totalCount");
int totalPage=StringUtil.toInt((String)request.getAttribute("totalPage"));
int pageIndex=StringUtil.toInt((String)request.getAttribute("pageIndex"));
String result=(String)request.getAttribute("result");
if(result!=null)
{%>
<%=result%><br/>
<a href="/friend/editReview.jsp?marriageId=<%=marriageId%>">返回发言</a><br/>
<a href="/friend/marriage.jsp">返回结婚礼堂</a><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>

<%}else if(result==null){

Vector reviewList=(Vector)request.getAttribute("reviewList");  
action.addGuest(marriageId);
FriendMarriageBean friendMarriage=action.getOnesMarriage(marriageId);
 if(friendMarriage==null){%>
该婚礼不存在！<br/>
<a href="/friend/marriage.jsp">返回结婚礼堂</a><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%}else if((loginUser.getId()!=friendMarriage.getFromId())&&(loginUser.getId()!=friendMarriage.getToId())&&(friendMarriage.getCandyRemain()>0)&& (action.eatOrNot(marriageId))&& action.isMarriageNow(marriageId)!=0)//非新郎新娘且有喜糖
{%>
新郎新娘请您吃喜糖，添加乐币<%=friendMarriage.getCandyPrice()%><br/>
<%action.eatSuger(friendMarriage.getCandyPrice(),marriageId);}
int toId=friendMarriage.getToId();
		int fromId=friendMarriage.getFromId();
		UserBean toUser=UserInfoUtil.getUser(toId);
		UserBean fromUser=UserInfoUtil.getUser(fromId);
		if(toUser.getGender()==1){%>
新郎:<a href="/user/ViewUserInfo.do?roomId=0&amp;userId=<%=toUser.getId()%>"><%=StringUtil.toWml(toUser.getNickName())%></a><br/>
新娘:<a href="/user/ViewUserInfo.do?roomId=0&amp;userId=<%=fromUser.getId()%>"><%=StringUtil.toWml(fromUser.getNickName())%></a><br/>
<% IFriendService friendService =ServiceFactory.createFriendService();
FriendBean friend=friendService.getFriend(toUser.getId());
if(friend==null||friend.getAttach().equals("")){%>
<img src="/img/friend/attach/2_1.gif" alt="新郎"/>
<%}else{//("/img/friend/attach/"%>
<img src="<%=net.joycool.wap.util.Constants.IMG_ROOT_URL%>/friend/attach/<%=friend.getAttach()%>" alt="新郎"/>
<%}friend=friendService.getFriend(fromUser.getId());
if(friend==null||friend.getAttach().equals("")){%>
<img src="/img/friend/attach/1_1.gif" alt="新娘"/><br/>
<%}else{%>
<img src="<%=net.joycool.wap.util.Constants.IMG_ROOT_URL%>/friend/attach/<%=friend.getAttach()%>" alt="新娘"/><br/>
<%}
}
else{%>
新郎:<a href="/user/ViewUserInfo.do?roomId=0&amp;userId=<%=fromUser.getId()%>"><%=StringUtil.toWml(fromUser.getNickName())%></a><br/>
新娘:<a href="/user/ViewUserInfo.do?roomId=0&amp;userId=<%=toUser.getId()%>"><%=StringUtil.toWml(toUser.getNickName())%></a><br/>
<% IFriendService friendService =ServiceFactory.createFriendService();
FriendBean friend=friendService.getFriend(fromUser.getId());
if(friend==null||friend.getAttach().equals("")){%>
<img src="/img/friend/attach/2_1.gif" alt="新郎"/>
<%}else{%>
<img src="<%=net.joycool.wap.util.Constants.IMG_ROOT_URL%>/friend/attach/<%=friend.getAttach()%>" alt="新郎"/>
<%}friend=friendService.getFriend(toUser.getId());
if(friend==null||friend.getAttach().equals("")){%>
<img src="/img/friend/attach/1_1.gif" alt="新娘"/><br/>
<%}else{%>
<img src="<%=net.joycool.wap.util.Constants.IMG_ROOT_URL%>/friend/attach/<%=friend.getAttach()%>" alt="新娘"/><br/>
<%}
}%>
婚礼庆典 <br/>
欢迎各位好友参加我们的婚礼<br/>
<img src="/img/friend/marriageform/<%=friendMarriage.getMarriageForm()%>.gif" alt="婚礼形式代表图"/><br/>
<%
if(action.isMarriageNow(marriageId)==0){%>
婚礼宣言：<%=StringUtil.toWml(friendMarriage.getPledge())%><br/>
共有<%=friendMarriage.getGuestNum()%>宾客参加我们的婚礼,获红包<%=friendMarriage.getRedbagNum()%>个，总额<%=friendMarriage.getMoney()%>乐币<br/>
<%}
int count = reviewList.size();
for(int i = 0; i < count; i ++){
	FriendReviewBean friendReview = (FriendReviewBean) reviewList.get(i);
	UserBean user=(UserBean)UserInfoUtil.getUser(friendReview.getReviewUserId());
	if(friendReview.getFile()==0){%>
<%=StringUtil.toWml(friendReview.getReview())%>  
(<a href="/chat/post.jsp?toUserId=<%=user.getId()%>" ><%=StringUtil.toWml(user.getNickName())%></a>)
<%}
else{%>
<a href="/chat/post.jsp?toUserId=<%=user.getId()%>" ><%=StringUtil.toWml(user.getNickName())%></a>
<%=StringUtil.toWml(friendReview.getReview())%> <img src="/img/friend/action/<%=friendReview.getFile()%>.gif" alt="闹礼堂图"/>
<%}%>

<%if((loginUser.getId()==toId || loginUser.getId()==fromId )&& (action.isMarriageNow(marriageId)!=0)){%>
<a href="/friend/review.jsp?delete=1&amp;marriageId=<%=marriageId%>&amp;reviewId=<%=friendReview.getId()%>" > 删除</a>
<%}%><br/>
<%}if(marriageId!=0){%>
<%=PageUtil.shuzifenye(totalPage, pageIndex, "review.jsp?marriageId="+marriageId, true, " ", response)%><%if(totalPage>1){%><br/><%}%>
<%}else{%>
<%=PageUtil.shuzifenye(totalPage, pageIndex, "review.jsp", false, " ", response)%><%if(totalPage>1){%><br/><%}}%>
<%if(action.isMarriageNow(marriageId)!=0){%>
<%IFriendService service=ServiceFactory.createFriendService();
if(loginUser.getId()!=friendMarriage.getFromId()&&loginUser.getId()!=friendMarriage.getToId()){
FriendGuestBean guest=service.getFriendGuest("user_id="+loginUser.getId()+" and marriage_id="+marriageId);
if(guest.getAction1()==0){%>
<a href="/friend/review.jsp?action=1&amp;marriageId=<%=marriageId%>">放礼花</a><br/>
<%}if(guest.getAction2()==0){%>
<a href="/friend/review.jsp?action=2&amp;marriageId=<%=marriageId%>">起哄</a><br/>
<%}if(guest.getAction3()==0){%>
<a href="/friend/review.jsp?action=3&amp;marriageId=<%=marriageId%>">灌新人酒</a><br/>
<%}
}%>
<a href="/friend/editReview.jsp?marriageId=<%=marriageId%>">祝福新人</a><br/>
<%}%>
<a href="/friend/marriage.jsp">返回结婚礼堂</a><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>

<%}%><%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>