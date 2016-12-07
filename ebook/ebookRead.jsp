<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.*,net.joycool.wap.bean.*,net.joycool.wap.util.*,net.joycool.wap.bean.friendlink.RandomLinkBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.ebook.EBookBean"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.wxsj.action.flush.*,java.util.Random"%><%
    response.setHeader("Cache-Control","no-cache");
    String contents = (String)request.getAttribute("contents");
    int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
    int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
    String prefixUrl = (String) request.getAttribute("prefixUrl");
    String rootId = (String)request.getAttribute("rootId");
    EBookBean ebook = (EBookBean)request.getAttribute("ebook");
int address = StringUtil.toInt(request.getParameter("address"));
String realAddress = Constants.EBOOK_FILE_URL + ebook.getFileUrl() + "/txt/" + (address+1);
boolean hasNext = (new java.io.File(realAddress)).exists();
	String flushWml = "";//FlushAction.getFlushWml(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
if(!flushWml.equals("") && !flushWml.equals("null")) {
	String [] ss = flushWml.split("\n");
	Random rand = new Random();
	String ddd = ss[rand.nextInt(ss.length)];
	ddd = ddd.replace("\"", "'");
%>
<card title="<%=StringUtil.toWml(ebook.getName())%>" ontimer="<%=response.encodeURL(ddd)%>">
<timer value="30"/>
<%
}
else {
%>
<card title="<%=StringUtil.toWml(ebook.getName())%>">
<%
}
%>
    <p align="left"> 
<%=BaseAction.getTop(request, response)%>
    <%=StringUtil.toWml(contents)%>
    <br/>
    <%=PageUtil.shangxiafenye(totalPageCount, pageIndex, prefixUrl, true, " ", response)%>
    <br/>
第<%=(pageIndex + 1)%>页,共<%=totalPageCount%>页<br/>
跳到第<input type="text" name="pageIndex1" maxlength="3" value="0"/>页 <anchor title ="go">Go
         <go href="<%=(prefixUrl)%>" method="post">
             <postfield name="pageIndex1" value="$pageIndex1"/>
         </go>
         </anchor><br/>
<%if(hasNext){%><a href="<%=("EBookRead.do?ebookId="+ ebook.getId() +"&amp;rootId=" + rootId+"&amp;address="+(address+1))%>">下一章</a><%}else{%>下一章<%}%>
|<%if(address>2){%><a href="EBookRead.do?ebookId=<%=ebook.getId() %>&amp;rootId=<%=rootId%>&amp;address=<%=(address-1)%>">上一章</a><%}else{%>上一章<%}%>
<br/>
    <%if(rootId != null){%>
    <a href="EBookReadList.do?ebookId=<%=ebook.getId() %>&amp;rootId=<%=rootId%>">返回目录</a><br/>
<!--
	<a href="EBookDownloadList.do?ebookId=<%=ebook.getId()%>&amp;rootId=<%=rootId%>">点击下载</a><br/>
-->
	<a href="EBookInfo.do?ebookId=<%=ebook.getId()%>&amp;orderBy=id">返回介绍</a><br/>    
    <%}%>
	<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>