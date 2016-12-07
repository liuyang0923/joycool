<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.bean.home.*,net.joycool.wap.service.impl.*,net.joycool.wap.util.UserInfoUtil,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.home.HomeDiaryBean" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.home.HomeReviewBean" %><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.bean.home.HomeDiaryReviewBean" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.diaryEdit(request);
String result=(String) request.getAttribute("result");
String url=("homeDiaryList.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="日记续写" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/jcforum/index.jsp">返回家园列表</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
HomeDiaryBean homeDiary = (HomeDiaryBean)request.getAttribute("homeDiary");
HomeServiceImpl homeServiceImpl = new HomeServiceImpl();
List list = homeServiceImpl.getHomeDiaryCatList(action.getLoginUser().getId(), HomeDiaryCat.PRIVACY_SELF );
%>
<card title="日记续写">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(homeDiary.getContent())%><br/>
续写:<input name="content"  maxlength="500" value=""/><br/>
 <anchor title="确定">确定
    <go href="homeDiaryResult.jsp?diaryId=<%=homeDiary.getId()%>" method="post">
    <postfield name="content" value="$content"/>
    </go>
    </anchor><br/>

修改日记分类:<select name="catId">
<option value="0">默认分类</option>
<%
HomeDiaryCat curCat = null;
for(int i = 0; i < list.size();i++) {
	HomeDiaryCat cat = (HomeDiaryCat)list.get(i);
	if(homeDiary.getCatId() == cat.getId()) {
		curCat = cat;
	}
%>
<option value="<%=cat.getId() %>"><%=StringUtil.toWml(cat.getCatName()) %></option>
<%} %>
</select><br/>
当前日记分类:<%=curCat != null?StringUtil.toWml(curCat.getCatName()):"默认分类"%><br/>
<a href="homeDiaryResult.jsp?diaryId=<%=homeDiary.getId()%>&amp;catId=$catId">修改</a><br/>
<a href="homeDiary.jsp?diaryId=<%=homeDiary.getId()%>">返回日记</a><br/>    
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>