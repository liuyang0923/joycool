<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.imglib.*"%>
<% response.setHeader("Cache-Control","no-cache");%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="图库说明"><p>
<%=BaseAction.getTop(request, response)%>
图库功能说明<br/>
为遵守国家法规,抵制不良网络信息,乐酷对所有用户发的图片先审核,再发送.<br/>
您保存在个人图库中的图片,则可以正常使用,包括直接发贴或发给好友.<br/>
若您上传的图片违反国家法规,低俗,将会被管理员删除.敬请谅解!<br/><br/>
<a href="lib.jsp">返回图库</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>