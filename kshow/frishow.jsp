<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.framework.BaseAction,net.joycool.wap.util.*,net.joycool.wap.bean.*,jc.show.*"%><%
response.setHeader("Cache-Control","no-cache");
CoolShowAction action = new CoolShowAction(request);
int uid = action.getParameterInt("uid");
UserBean ub = UserInfoUtil.getUser(uid);
CoolUser cu = CoolShowAction.getCoolUser(ub);
List list = StringUtil.toInts(cu.getCurItem());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="好友酷秀">
<p><%=BaseAction.getTop(request, response)%><%
if(cu != null && !"".equals(cu.getImgurl())){
	%><a href="/user/ViewUserInfo.do?userId=<%=ub.getId()%>"><%=ub.getNickNameWml()%></a>的酷秀<br/><img src="/rep<%=CoolShowAction.IMAGE_URL[3]%><%=cu.getImgurl()%>" alt="x"/><br/><%
	if(list != null && list.size() > 0){
		%>==当前穿戴==<br/><%
		for(int i=0;i<list.size();i++){
			Commodity com = CoolShowAction.getCommodity(((Integer)list.get(i)).intValue());
			if(com != null){
				%>[<%=com.getCatalogName()%>]<a href="consult.jsp?from=1&amp;Iid=<%=com.getId() %>"><%=StringUtil.toWml(com.getName())%></a><br/><%
			}
		}
		%><a href="room.jsp?strId=<%=cu.getCurItem()%>">+试穿以上全部酷秀+</a><br/><%
	}else{
		%>(暂无搭配)<br/><%
	}
}else{
%>(该好友暂无酷秀)<br/><%
}
%><a href="frdshowlist.jsp">&lt;好友的酷秀列表</a><br/><a href="index.jsp">&gt;我的酷秀</a><br/><a href="downtown.jsp?gend=2">&gt;&gt;商城男装区</a><br/><a href="downtown.jsp?gend=1">&gt;&gt;商城女装区</a><br/>
<%=BaseAction.getBottom(request, response)%></p>
</card>
</wml>