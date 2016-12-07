<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.ebook.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.IEBookService"%><%@ page import="java.util.Vector" %><%
response.setHeader("Cache-Control","no-cache");
String type=request.getParameter("type");
String sql1=null;
if(type==null){
    type="jebook";
    sql1=" catalog_id in(260,261,262,263,264,265,266,267,268,269) order by download_sum desc";
}else{
   if(type.equals("ebook")){
   sql1=" catalog_id in(20,21,22,23,17,18,24,25,26,27,28,29,30) order by download_sum desc";
   }else if(type.equals("id")){
   sql1=" catalog_id in(20,21,22,23,17,18,24,25,26,27,28,29,30,740,741,761) order by id desc";
   }else{
   sql1=" catalog_id in(260,261,262,263,264,265,266,267,268,269) order by download_sum desc";
   }
}
IEBookService ebookSer = ServiceFactory.createEBookService();
int numberPage = 10;
//int numberPage = 20;
int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
if (pageIndex == -1) {
	pageIndex = 0;
}
String prefixUrl = "ebookTop.jsp?type="+type;
int totalCount = 100;

int totalPageCount = totalCount / numberPage;
if (totalCount % numberPage != 0) {
	totalPageCount++;
}
if (pageIndex > totalPageCount - 1) {
	pageIndex = totalPageCount - 1;
}
if (pageIndex < 0) {
	pageIndex = 0;
}
// 取得要显示的消息列表
int start = pageIndex * numberPage;
int end = numberPage;
Vector eBookList =ebookSer.getEBooksList(sql1+" limit "+ start + ", " + end);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%if(type.equals("jebook")){%>禁书人气排名<%}else if(type.equals("id")){%>最新精品上架<%}else{%>乐酷书城排行榜<%}%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(type.equals("jebook")){%>禁书人气排名<%}else if(type.equals("id")){%>最新精品上架<%}else{%>乐酷书城排行榜<%}%><br/>
------------<br/>
<%
EBookBean ebook=null;
if(eBookList!=null){
for(int i=0;i<eBookList.size();i++){
    ebook=(EBookBean)eBookList.get(i);
%>
<%=(i + 1)%>.<a href="EBookInfo.do?ebookId=<%=ebook.getId()%>&amp;orderBy=id"><%=StringUtil.toWml(ebook.getName())%></a><br/>
人气:<%=ebook.getDownloadSum()%><br/>
<%}}else{%>暂时无法提供排行查询服务，请稍后在试！<br/><%}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, " ", response);
if(!"".equals(fenye)){
%>
<%--<%=fenye%><br/>--%>
<%
}
%>
<a href="/Column.do?columnId=8774">返回书城首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>