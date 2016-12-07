<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.imglib.*"%>
<% response.setHeader("Cache-Control","no-cache");
   ImgLibAction action = new ImgLibAction(request);
   int mid = action.getParameterInt("mid");
   int userId = action.getParameterInt("uid");
   int id2 = action.getParameterInt("id2");
   String tip2 = "";
   String[] backJsp = {"lib.jsp",
		   			   "lib.jsp",
		   			   "show.jsp?uid=" + userId + "&amp;id=" + id2,
		   			   "lib.jsp"};
   String[] tip = {"跳转错误.<br/>",
		           "上传成功!您是从手机中选取的图片,需要审核后才能发表.感谢理解!<br/>(3秒钟自动跳转)<br/>",
		           "您已经添加成功!<br/>(3秒钟自动跳转)<br/>",
		           ""};
   if (mid < 0 || mid > tip.length - 1){
	   mid = 0;
   }
   if (mid == 3){
	   tip[mid] = (String)request.getAttribute("tip") + "<br/>(3秒钟自动跳转)<br/>";
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="我的图库" ontimer="<%=response.encodeURL(backJsp[mid])%>"><timer value="30"/><p>
<%=BaseAction.getTop(request, response)%>
<%=tip[mid]%>
<a href="<%=backJsp[mid]%>">直接返回</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>