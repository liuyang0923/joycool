<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.user.RankAction"%><%@ page import="net.joycool.wap.bean.NoticeBean"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ include file="../../testAction.jsp" %><%
response.setHeader("Cache-Control","no-cache");
%>
<%!
/**
 * 更新用户经验值
 * @param userId
 */
public void addUserPoint(UserBean loginUser,int point){
	RankAction.addPoint(loginUser,point);
	NoticeBean notice = new NoticeBean();
	notice.setUserId(loginUser.getId());
	notice.setTitle("感谢支持,"+point+"点经验值已经加上!");
	notice.setContent("");
	notice.setType(NoticeBean.GENERAL_NOTICE);
	notice.setHideUrl("");
	notice.setLink("/chat/hall.jsp");
	NoticeUtil.getNoticeService().addNotice(notice);
}




%>
<%
String questionName="question";
//验证第一页是否填写了
if(null==session.getAttribute(questionName+1)){
 response.sendRedirect(("page1.jsp"));
 return;
 }
int answer1=StringUtil.toInt((String)session.getAttribute(questionName+1));
 if(answer1!=1)
 {
//	验证第二页是否填写了
if(null==session.getAttribute(questionName+2)){
 response.sendRedirect(("page2.jsp"));
 return;
 }
 int answer2=StringUtil.toInt((String)session.getAttribute(questionName+2));
	 if(answer2==5||answer2==7){
	 	for(int i=18;i<=31;i++){
			session.removeAttribute(questionName+i);
		}
//		验证第3页是否填写了
		 if(null==session.getAttribute(questionName+3)){
		 response.sendRedirect(("page3.jsp"));
		 return;
		 }//		验证第4页是否填写了
		  if(null==session.getAttribute(questionName+11)){
		 response.sendRedirect(("page4.jsp"));
		 return;
		 }
		
	 }else{
	 	for(int i=3;i<=17;i++){
			session.removeAttribute(questionName+i);
		}
//		验证第3页是否填写了
	 	 if(null==session.getAttribute(questionName+18)){
		 response.sendRedirect(("page3.jsp"));
		 return;
		 }
//			验证第4页是否填写了
		  if(null==session.getAttribute(questionName+25)){
		 response.sendRedirect(("page4.jsp"));
		 return;
		 }
		 
	 }
//		验证第5页是否填写了
   if(null==session.getAttribute(questionName+32)){
 response.sendRedirect(("page5.jsp"));
 return;
 }
//	验证第6页是否填写了
 if(null==session.getAttribute(questionName+40)){
 response.sendRedirect(("page6.jsp"));
 return;
 }
//	验证第7页是否填写了
  if(null==session.getAttribute(questionName+46)){
 response.sendRedirect(("page7.jsp"));
 return;
 }
 }

int point=500;
TestAction action=new TestAction(request);
//是否为北京用户
if(action.getLoginUser().getCityno()!=55){
	response.sendRedirect(("infoNonBj.jsp"));
	return;
}
if(answer1==1){
	point=50;
}else{
	point=500;
}
int answer2=StringUtil.toInt((String)session.getAttribute(questionName+2));

//测试是否结束
if(action.isTested()==false){
	//把回答写回数据库
	action.saveRecord(request);
	//addUserPoint(action.getLoginUser(request),point);
}
else{
	response.sendRedirect(("infoTested.jsp"));
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="成人用品调查问卷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<% 
String info="";
int top=action.isAwarded();
   if(top>0 && top<=20){
		info="恭喜您获得性爱光碟一套，您的Id号是"+action.getLoginUser().getId()+"；答题号是"+action.getUserMaxRecordId()+",请在9月30日前凭以上2个号码到以下地址领取奖品。逾期不领视为自动放弃；号码有误者不能领奖。地址：海淀区双清路学研大厦B709 电话：010-62790306-806 严小姐";
   }else if(top>21&&top<=40){
	   info="恭喜您获得情趣内衣一件，您的Id号是"+action.getLoginUser().getId()+"；答题号是"+action.getUserMaxRecordId()+",请在9月30日前凭以上2个号码到以下地址领取奖品。逾期不领视为自动放弃；号码有误者不能领奖。地址：海淀区双清路学研大厦B709 电话：010-62790306-806 严小姐";
   }else if(top>41&&top<=60){
	   info="恭喜您获得印度神油一盒，您的Id号是"+action.getLoginUser().getId()+"；答题号是"+action.getUserMaxRecordId()+",请在9月30日前凭以上2个号码到以下地址领取奖品。逾期不领视为自动放弃；号码有误者不能领奖。地址：海淀区双清路学研大厦B709 电话：010-62790306-806 严小姐";
   }else if(top>61&&top<=100){
	   info="恭喜您获得品牌安全套一盒，您的Id号是"+action.getLoginUser().getId()+"；答题号是"+action.getUserMaxRecordId()+",请在9月30日前凭以上2个号码到以下地址领取奖品。逾期不领视为自动放弃；号码有误者不能领奖。地址：海淀区双清路学研大厦B709 电话：010-62790306-806 严小姐";
   }else{
	   info="很遗憾，您没有中奖，感谢参与！";
   }
%><%=info%><br/>
<a href="http://wap.g3me.cn">返回首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>