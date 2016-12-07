<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.video.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
String videoN=(String)request.getAttribute("videoName");
String perPage=(String)request.getAttribute("NUM_PER_PAGE");
String totalCount=(String)request.getAttribute("totalCount");
String totalPage=(String)request.getAttribute("totalPage");
String pageIndex=(String)request.getAttribute("pageIndex");
Vector video = (Vector)request.getAttribute("video");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="视频搜索">
<p align="left">
<%=BaseAction.getTop(request, response)%>
    <%if((video == null)||(video.size()<1)){%>
    搜索结果为空<br/>
    <%}else
    {  %>
<%
     VideoBean currentVideo;
    for(int i = 0;i <video.size();i++)
        { currentVideo = (VideoBean)video.get(i);
          String videoName = currentVideo.getName();
        %>
    <%=(Integer.parseInt(perPage)*Integer.parseInt(pageIndex) + i+1)%>:
    <a href="VideoInfo.do?videoId=<%=currentVideo.getId()%>"><%=videoName%></a><br/>
	人气：<%=currentVideo.getDownloadSum()%><br/>
    <%}%>
    <%}%>
<%=PageUtil.shuzifenye(Integer.parseInt(totalPage),Integer.parseInt(pageIndex),"SearchVideo.do?videoName="+videoN,true," ",response)%><br/>
<a href="VideoCataList.do" title="返回">返回视频首页</a><br/>

	<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>