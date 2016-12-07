<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.app.*"%><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.action.user.UserAction"%><%
response.setHeader("Cache-Control","no-cache");
UserAction action = new UserAction(request);
action.shortcut();

action.shortcutList();
UserBean loginUser = action.getLoginUser();
String s = loginUser.getUserSetting().getShortcut2();

PagingBean pagingBean = (PagingBean)request.getAttribute("page");
List sList=(List)request.getAttribute("sList");


String[] ss = loginUser.getUserSetting().getShortcut2().split(",");
HashMap sMap=(HashMap)request.getAttribute("sMap");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="ME设置">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
LinkBuffer lb = new LinkBuffer(response);
for(int j = 0;j < ss.length;j++){
	int ssid = StringUtil.toInt(ss[j]);
	if(ssid>500){
		lb.appendLink("shortcut2Res.jsp?id=" + ssid+"&amp;p="+pagingBean.getCurrentPageIndex(), AppAction.getApp(ssid-500).getShortName());
	}else{
	    ShortcutBean bean = (ShortcutBean)sMap.get(Integer.valueOf(ssid));
	    if(bean != null) {
	    	lb.appendLink("shortcut2Res.jsp?id=" + ssid+"&amp;p="+pagingBean.getCurrentPageIndex(), bean.getShortName());
    }
}}
String shortcut2 = lb.toString();%>
<%if(shortcut2.length() > 0){%>
已添加链接:(点击删除)<br/><%}%>
<%=shortcut2%><br/>
点击以下链接，把它加入到ME：<br/>
<%
int i = 0;
LinkBuffer b = new LinkBuffer(response);
Iterator iter = sList.iterator();
while(iter.hasNext()){
    ShortcutBean bean = (ShortcutBean)iter.next();
    if(bean==null||bean.getHide()==1) continue;
    if(s.indexOf(','+String.valueOf(bean.getId())+',') == -1) {
    	b.appendLink("shortcut2Res.jsp?id=" + bean.getId()+"&amp;p="+pagingBean.getCurrentPageIndex(), bean.getShortName());
}else{b.appendWml(bean.getShortName());}%><%}

if(pagingBean.getCurrentPageIndex() == pagingBean.getTotalPageCount() - 1){	// 最后一页的话把添加了的组件的链接加上
	List list = new ArrayList(AppAction.getAppUserMap(loginUser.getId()).keySet());
	
	for(int j = 0;j < list.size();j++){
	Integer iid = (Integer)list.get(j);
	AppBean app = AppAction.getApp(iid.intValue());
	if(app.isFlagDirect() || app.isFlagLocal())
		continue;
		if(s.indexOf(','+String.valueOf(app.getId()+500)+',') == -1)
			b.appendLink("shortcut2Res.jsp?id=" + (app.getId()+500)+"&amp;p="+pagingBean.getCurrentPageIndex(), app.getShortName());
		else
			b.appendWml(app.getShortName());
	}
	
}

%>
<%=b%><br/>
<%=PageUtil.shuzifenye(pagingBean, "shortcut2.jsp", false, "|", response)%>


<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>