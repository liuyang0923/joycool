<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%
String ua = request.getParameter("ua");
if(ua==null){
    ua = "";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="书签">
<p align="left">
您访问乐酷游戏社区(http://wap.joycool.net)时,请按以下步骤收藏本站：<br/>
<br/>
<% if(ua.equals("nokia")){ %>
诺基亚：依次选“操作”-“增加书签”
<% }else if(ua.equals("mot")){ %>
摩托罗拉：依次选“菜单键”-“书签”-“标记站点”-“保存”
<% }else if(ua.equals("se")){ %>
索爱：依次选“移动梦网”-“更多”-“书签”-“添加书签”-“确定”
<% }else if(ua.equals("sum")){ %>
三星：依次选“功能表”-“娱乐功能”-“WAP浏览器” -“收藏夹”-选择一个空的收藏夹地址-确认url地址-输入“乐酷门户”
<% }else if(ua.equals("pan")){ %>
松下：选择页面左上角的“菜单”-“书签”-“标记站点”-“保存”
<% }else if(ua.equals("sie")){ %>
西门子：依次选“上网键”-“收藏夹”-“储存”
<% }else if(ua.equals("cec")){ %>
CECT：依次选“菜单”-“保存书签”-“保存”
<% }else if(ua.equals("lg")){ %>
LG：依次选“菜单”-“书签”-“标记站点”-“保存”
<% }else if(ua.equals("mit")){ %>
三菱：按左功能键-“书签”-“添加新书签”-“保存”
<% }else if(ua.equals("hai")){ %>
海尔：网页浏览状态下长按“*”键-“书签”-“新建”-“编辑”-输入“乐酷门户”-“保存”
<% }else if(ua.equals("xia")){ %>
夏新：访问网站时选中页面左上角-“书签”-“保存”- “新建” -输入““乐酷门户”-“保存”
<% }else if(ua.equals("len")){ %>
联想：依次选“浏览器”-“空书签”-输入“乐酷门户”-“保存”
<% }else if(ua.equals("eas")){ %>
东信：依次选“选项”-“保存书签”
<% }else if(ua.equals("nec")){ %>
NEC：依次选“菜单”-“访问WEB浏览器”-“选择网站”-“书签”-“标记站点”-“保存”
<% } %>
<br/>
<br/>
<a href="/favorite/mobileType.jsp">返回上一级</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
</p>
</card>
</wml>