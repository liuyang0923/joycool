<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<% response.setHeader("Cache-Control","no-cache");
   GuestHallAction action = new GuestHallAction(request,response);
   String tip = "";
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="保存书签"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>保存书签的方法<br/>
------------------<br/>
诺基亚:依次选"操作"-"增加书签"<br/>
摩托罗拉:依次选"菜单键"-"书签"-"标记站点"-"保存"<br/>
索爱:依次选"移动梦网"-"更多"-"书签"-"添加书签"-"确定"<br/>
三星:依次选"功能表"-"娱乐功能"-"WAP浏览器" -"收藏夹"-选择一个空的收藏夹地址-确认url地址-输入"乐酷门户"<br/>
松下:选择页面左上角的"菜单"-"书签"-"标记站点"-"保存"<br/>
西门子:依次选"上网键"-"收藏夹"-"储存"<br/>
CECT:依次选"菜单"-"保存书签"-"保存"<br/>
LG:依次选"菜单"-"书签"-"标记站点"-"保存"<br/>
三菱:按左功能键-"书签"-"添加新书签"-"保存"<br/>
海尔:网页浏览状态下长按"*"键-"书签"-"新建"-"编辑"-输入"乐酷门户"-"保存"<br/>
夏新:访问网站时选中页面左上角-"书签"-"保存"- "新建" -输入""乐酷门户"-"保存"<br/>
联想:依次选"浏览器"-"空书签"-输入"乐酷门户"-"保存"<br/>
东信:依次选"选项"-"保存书签"<br/>
NEC:依次选"菜单"-"访问WEB浏览器"-"选择网站"-"书签"-"标记站点"-"保存"<br/>
<a href="bm.jsp">返回保存书签</a><br/>
<a href="index.jsp">返回游乐园</a><br/>
<%
} else {
%><%=tip%><br/><a href="index.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>