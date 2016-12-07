<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.spec.admin.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="java.util.*"%><%!
	static String[] reason = {"未选择","不文明","有黄色/非法言论","违反论坛规定","其他"};
%><%
response.setHeader("Cache-Control","no-cache");
ReportAction action = new ReportAction(request);
UserBean loginUser = action.getLoginUser();
int id = action.getParameterInt("id");
ForumContentBean con = ForumCacheUtil.getForumContent(id);
if(con == null || loginUser == null){
	response.sendRedirect("/bottom.jsp");
	return;
}
if(!action.isMethodGet()){
	String info = action.getParameterNoEnter("info");
	if(info!=null&&info.length()>200)
		info = info.substring(0, 200);
	int rea = action.getParameterInt("rea");
	if(rea >= reason.length || rea<0)
		rea = 0;
	ReportAction.addReport(loginUser.getId(),2,id,con.getForumId(),info,reason[rea],con.getContent());
	action.tip("tip", "提交举报信息成功");
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="x举报x">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){

%><%=action.getTip()%><br/><%

}else{%>

你选择了举报帖子:<%=StringUtil.toWml(con.getTitle())%><br/>
选择理由:<select name="rea">
<%for(int i=0;i<reason.length;i++){%><option value="<%=i%>"><%=reason[i]%></option><%}%>
</select><br/>
其他说明(200字以内):<br/>
<input name="info" maxlength="200"/><br/>
<anchor title="ok">提交举报信息
  <go href="forum.jsp?id=<%=id%>" method="post">
    <postfield name="rea" value="$rea"/>
    <postfield name="info" value="$info"/>
  </go>
</anchor><br/>

<%}%>

<a href="/jcforum/viewContent.jsp?contentId=<%=id%>">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>