<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.shop.*,net.joycool.wap.framework.BaseAction,jc.show.*,java.util.*,net.joycool.wap.bean.*,net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
CoolShowAction action = new CoolShowAction(request);
UserBean ub = action.getLoginUser();
String[] types = {"购买","赠送","收礼"};
String cond = "";
int uid = ub.getId();
UserInfoBean bean = ShopAction.shopService.getUserInfo(uid);
int type = action.getParameterInt("type");
if(type == 1){
cond = " user_id_1="+uid+" and user_id_2!=0";
}else if(type == 2){
cond = " user_id_2="+uid;
}else{
cond = " user_id_1="+uid+" and user_id_2=0";
}
cond+=" order by id desc";
List list = CoolShowAction.service.getHistory(cond);
PagingBean paging = new PagingBean(action,list.size(),5,"p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="历史记录">
<p><%=BaseAction.getTop(request, response)%>您有<%=bean.getGoldString()
%>【<a href="/pay/pay.jsp">充值</a>.<a href="/pay/myOrder.jsp">查询</a>】<br/><%
	for(int i=0;i<types.length;i++){
		if(i==type){
		%><%=types[type]%><%
		}else{
		%><a href="history.jsp?type=<%=i%>"><%=types[i]%></a><%
		}
		if(i==types.length-1){
		%><br/><%
		}else{
		%>|<%
		}
	}
	if(list != null && list.size() > 0){
		for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
			History his = (History)list.get(i);
			if(his != null){
			Commodity com = CoolShowAction.getCommodity(his.getIid());
				if(com != null){
					if(type == 1 || type==2){
					UserBean temp = null;
						if(type == 1){
						temp = UserInfoUtil.getUser(his.getTouid());
						%>赠送给<%
						}else{
						%>收到<%
						temp = UserInfoUtil.getUser(his.getUid());
						}
						if(ub != null){
%><a href="/home/home.jsp?userid=<%=temp.getId()%>"><%=temp.getNickNameWml()%></a><%
						if(type == 2){
						%>赠送的<%
						}
%>【<a href="consult.jsp?from=1&amp;Iid=<%=com.getIid()%>"><%=com.getName()%></a>】<%
						}else{
						%>记录被删除!<br/><%
						}
					}else{
%>购买【<a href="consult.jsp?from=1&amp;Iid=<%=com.getIid()%>"><%=com.getName()%></a>】<%
					}
					%><%=com.getPrice()==0?"免费":com.getPrice()+"酷币"%><br/><%=DateUtil.sformatTime(com.getCreateTime())%><br/><%
				}else{
				%>记录被删除!<br/><%
				}
			}
		}
	%><%=paging.shuzifenye("history.jsp?type="+type,true,"|",response)%><%
	}else{
	%>暂无信息<br/><%
	}
 %><a href="index.jsp">&gt;我的酷秀</a><br/><a href="downtown.jsp?gend=2">&gt;&gt;商城男装区</a><br/><a href="downtown.jsp?gend=1">&gt;&gt;商城女装区</a><br/><%=BaseAction.getBottom(request, response)%></p>
</card>
</wml>