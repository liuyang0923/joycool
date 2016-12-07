<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.job.JobMusicBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.MusicAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
int winCountM=0;
//读取连赢次数
if(session.getAttribute("winCountM") != null){
     String count=(String)session.getAttribute("winCountM");
     winCountM=Integer.parseInt(count);
} 
String types=(String)request.getParameter("types");
if(types==null){
types="mid";
}
MusicAction action = new MusicAction(request);
action.question(request);
//取得登陆用户信息
UserBean loginUser = (UserBean) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean status=action.getUserStatus(loginUser.getId());
UserStatusBean status=UserInfoUtil.getUserStatus(loginUser.getId());
//获取一条随机生成的题目
JobMusicBean music=(JobMusicBean)request.getAttribute("jobMusic");
String url=("/job/musicq.jsp?types="+types);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="听歌猜名" onenterbackward="<%=response.encodeURL(url)%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//判断如果连赢次数大于1,显示连赢次数
if(winCountM>1){%>
您已经连赢<%=winCountM%>次,继续加油!<br/>
<%}%>
<%//显示随机题目和答案选项%>
<a href="<%=(music.ATTACH_URL_ROOT + music.getName())%>">下载试听</a><br/>
选择歌曲名称:<br/>
A:<a href="<%=("/job/musicr.jsp?id="+music.getId()+"&amp;types="+types+"&amp;rs=1")%>"><%=music.getKey1()%></a><br/>
B:<a href="<%=("/job/musicr.jsp?id="+music.getId()+"&amp;types="+types+"&amp;rs=2")%>"><%=music.getKey2()%></a><br/>
<%if(!music.getKey3().equals("")){%>
C:<a href="<%=("/job/musicr.jsp?id="+music.getId()+"&amp;types="+types+"&amp;rs=3")%>"><%=music.getKey3()%></a><br/>
<%}%>
<%if(!music.getKey4().equals("")){%>
D:<a href="<%=("/job/musicr.jsp?id="+music.getId()+"&amp;types="+types+"&amp;rs=4")%>"><%=music.getKey4()%></a><br/>
<%}%>
<%if(!music.getKey5().equals("")){%>
E:<a href="<%=("/job/musicr.jsp?id="+music.getId()+"&amp;types="+types+"&amp;rs=5")%>"><%=music.getKey5()%></a><br/>
<%}%>
提示:试听后按手机上的返回键即可返回本页面.<br/>
你现有乐币<%=status.getGamePoint()%><br/>
<br/>
<a href="/job/mindex.jsp">返回上一级</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%--马长青_2006-6-22_增加底部信息_start--%>
<%=BaseAction.getBottom(request, response)%>
<%--马长青_2006-6-22_增加底部信息_end--%>
</p>
</card>
</wml>
