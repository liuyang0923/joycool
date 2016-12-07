<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%>
<%@ page import="net.joycool.wap.framework.BaseAction,jc.news.nba.*,java.util.*,net.joycool.wap.bean.*"%><%
	response.setHeader("Cache-Control","no-cache");
	NbaAction action = new NbaAction(request);
	List list = action.service.getNews(" flag=4 order by create_time desc");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="=诸强动态="><p>
<%=BaseAction.getTop(request, response)%>=球队新闻=<br/><%
if(list != null && list.size() > 0){
	PagingBean paging = new PagingBean(action, list.size(), 10, "p");
	for(int i=paging.getStartIndex();i<=paging.getEndIndex()-1;i++){
		BeanNews bn = (BeanNews)list.get(i);
		if(bn.getFlag() == 4){
			if(bn.getView().equals("")){
			%><a href="news.jsp?nid=<%=bn.getId()%>&amp;from=4"><%=bn.getName()%></a><br/><%
			}else{
			%><a href="news.jsp?nid=<%=bn.getId()%>&amp;from=4">[<%=bn.getView()%>]&#160;<%=bn.getName()%></a><br/><%
			}
		}
	}
%><%=paging.shuzifenye("news4.jsp?jcfr=1", true, "|", response)%><%
}
 %><a href="index.jsp">返回NBA专题</a><br/><%=BaseAction.getBottom(request, response)%></p>
</card>
</wml>