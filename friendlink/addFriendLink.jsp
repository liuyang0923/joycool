<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.friendlink.LinkRecordBean"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.bean.friendlink.LinkTypeBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
Vector linktypelist = (Vector)request.getAttribute("list");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<logic:present name="result" scope="request">
    <logic:equal name="result" value="failure">
<card title="申请友情连接">
<p align="left"> 
<%--马长青_2006-6-22_增加顶部信息_start--%>
<%=BaseAction.getTop(request, response)%>
<%--马长青_2006-6-22_增加顶部信息_end--%>
申请友情连接<br/>
------------<br/>
<bean:write name="tip" filter="false"/><br/>
<br/>
<a href="FriendLink.do" title="进入">返回申请页面</a><br/>
<a href="http://wap.joycool.net" title="返回">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    </logic:equal>
</logic:present>

<logic:notPresent name="result" scope="request">
<card title="申请友情连接">
<p align="left">
<%--马长青_2006-6-22_增加顶部信息_start--%>
<%=BaseAction.getTop(request, response)%>
<%--马长青_2006-6-22_增加顶部信息_end--%>
申请友情连接<br/>
------------<br/>
您的wap网站名称（我们给贵站添加的友链中的标题）：<br/><input name="friendlinkName" maxlength="40" value="v"/><br/>
您的wap网站地址（我们给贵站添加的友链中的地址）：<br/><input name="friendlinkaddress" maxlength="40" value="http://"/><br/>
<%
int i, count;
LinkTypeBean linktype = null;
count = linktypelist.size();
if(count > 0){
	linktype = (LinkTypeBean) linktypelist.get(0);
}
%>
请选择贵站所属分类（我们给贵站添加的友链的位置）：<br/><select name="linktype" value="<%if(linktype != null){%><%=linktype.getId()%><%}%>">
<%
for(i = 0; i < count; i ++){
	linktype = (LinkTypeBean) linktypelist.get(i);
%>
    <option value="<%=linktype.getId()%>"><%=linktype.getName()%></option>
<%
}
%>
	</select><br/>
<br/>
    <anchor title="确定" align="center">确定
    <go href="AddFriendLink.do" method="post">
	<postfield name="friendlinkName" value="$friendlinkName"/>
    <postfield name="friendlinkaddress" value="$friendlinkaddress"/>
	<postfield name="linktype" value="$linktype"/>
    </go>
    </anchor>
<br/>
<a href="http://wap.joycool.net" title="返回">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</logic:notPresent>
</wml>