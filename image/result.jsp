<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.image.ImageBean"%><%@ page import="net.joycool.wap.bean.image.ImageFileBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
String imageN=(String)request.getAttribute("imageName");
String perPage=(String)request.getAttribute("NUM_PER_PAGE");
String totalCount=(String)request.getAttribute("totalCount");
String totalPage=(String)request.getAttribute("totalPage");
String pageIndex=(String)request.getAttribute("pageIndex");
Vector images = (Vector)request.getAttribute("images");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="图片搜索">
<p align="left"> 
<%=BaseAction.getTop(request, response)%>
    <%if((images == null)||(images.size()<1)){%>
    搜索结果为空<br/>
    <%}else
    {  %>
<%  ImageBean currentImage;
    for(int i = 0;i <images.size();i++)
        { currentImage = (ImageBean)images.get(i);
          String imageName = currentImage.getName();
          String code = currentImage.getCode();
          ImageFileBean file7070 = currentImage.getFile7070();
          if(file7070==null)
          	continue;
          String imageUrl = file7070.getRealFileUrl();
        %>
    <%=(Integer.parseInt(perPage)*Integer.parseInt(pageIndex) + i+1)%>:
    <a href="ImageInfo.do?imageId=<%=currentImage.getId()%>"><%=imageName%></a><br/>
	人气：<%=currentImage.getHits()%><br/>
    <img src="<%=imageUrl%>" alt="loading"/><br/>
    <%}%>
    <%}%>
<%if(images!=null&&images.size()>10){%>
<%=PageUtil.shuzifenye(Integer.parseInt(totalPage),Integer.parseInt(pageIndex),"SearchImage.do?imageName="+imageN,true," ",response)%><br/>
<%}%>
<a href="/Column.do?columnId=4381">返回图片首页</a><br/>
	<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>