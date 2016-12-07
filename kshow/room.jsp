<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.show.*,java.util.*,net.joycool.wap.bean.*"%><%!
static String[] chggend = {"男","女"};
static String[] genderText = {"女","男"};
%><%
response.setHeader("Cache-Control","no-cache");
CoolShowAction action = new CoolShowAction(request);
UserBean ub = action.getLoginUser();
CoolUser cu = CoolShowAction.getCoolUser(ub);
String notice = null;
int Iid = action.getParameterInt("Iid");
if (Iid < 0) {
	notice = "没有此物品!";
}
action.prepareImage(ub,3);
Integer changeGender = (Integer)request.getAttribute("c");
if(changeGender != null){
	int from = action.getParameterInt("from");
	if(from!=3)	from = 2;
	

// ------询问是否切换性别的页面----------

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="切换性别">
<p>您选择了穿戴<%=genderText[changeGender.intValue()]%>性物品,是否要切换酷秀性别?<br/><a href="room.jsp?g=<%=changeGender%>&amp;Iid=<%=Iid%>">&gt;确定切换</a><br/>
<a href="room.jsp">&gt;返回试衣间</a><br/>
返回商城<a href="downtown.jsp?gend=1">女装区</a>|<a href="downtown.jsp?gend=2">男装区</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml><%
// -------------------------------------
	return;
}
TryBean tryShow = (TryBean)session.getAttribute("tryShow");
List tryList = tryShow.getTryList(3);
cu = CoolShowAction.getCoolUser(ub);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="我的试衣间">
<p><%=BaseAction.getTop(request, response)%><%
if(notice != null){
%><%=notice%><br/><%
}%>==当前试穿形象==<br/><%
if(tryShow.getGender() == 0){
%><img alt="text" src="/rep/show/tried/<%=cu.getImgurlF()%>"/><%
}else{
%><img alt="text" src="/rep/show/tried/<%=cu.getImgurlM()%>"/><%
}
%><br/>
+<a href="save.jsp">保存酷秀形象</a>+<a href="room.jsp?g=<%=cu.getGenderUseing()%>&amp;strId=<%=cu.getCurItem()%>">还原</a><br/>
【当前酷秀搭配】<br/><%
List list = tryShow.getCommodityList();
if(list.size()==0){
	%>(暂无)<a href="downtown.jsp?gend=<%=tryShow.getGender()+1%>">去商城挑选</a><br/><%
}else{
	for(int i=0;i<list.size();i++){
		Commodity com = (Commodity)list.get(i);
		Pocket po = CoolShowAction.service.getDate1("del=0 and user_id="+cu.getUid()+" and item_id="+com.getId());
		%><%=i+1%>.<a href="consult.jsp?from=1&amp;Iid=<%=com.getIid()%>"><%=com.getName()%></a>|<a href="room.jsp?Iid=<%=com.getId()%>">脱</a><%
		if(po==null){
		%>|<a href="sureBuy.jsp?Iid=<%=com.getIid()%>">购</a><br/><%
		}else{
		%><br/><%
		}
	}
%><a href="room.jsp?alld=1">+全部脱下+</a><br/><%
}%>
<a href="room.jsp?g=<%=1-tryShow.getGender()%>">+切换为<%=chggend[tryShow.getGender()]%>性形象+</a><br/>
<a href="index.jsp">&gt;我的酷秀</a>|<a href="myGoods.jsp">我的物品</a><br/>
&gt;商城<a href="downtown.jsp?gend=1">女装区</a>|<a href="downtown.jsp?gend=2">男装区</a><br/>
<%if(tryList.size()!=0){
	%>【试穿历史】<%if(tryShow.getTryCount()>3){%><a href="trylist.jsp">更多</a><%}%><br/><%
Iterator iter = tryList.iterator();
int i=0;
while(iter.hasNext()){
Commodity comm = (Commodity)iter.next();
%><%=++i%>.<a href="consult.jsp?from=1&amp;Iid=<%=comm.getIid()%>"><%=comm.getName()%></a><br/><%}}%>
<%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>