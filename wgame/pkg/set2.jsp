<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.friend.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="net.joycool.wap.bean.UserBagBean" %><%@ page import="net.joycool.wap.bean.dummy.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");%><%@include file="../../bank/checkpw.jsp"%><%
PkgAction action = new PkgAction(request);
action.set2();
PkgBean pkg = (PkgBean)request.getAttribute("pkg");;
if(pkg==null){
	response.sendRedirect("my1.jsp");
	return;
}
PkgTypeBean type = (PkgTypeBean)action.getPkgType(pkg.getType());
UserBean loginUser = action.getLoginUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="礼品店">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=type.getName()%><br/>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else if(pkg.getPreMoney()>0||pkg.getItemList()!=null&&pkg.getItemList().size()>0){%>
红包金额:<%=StringUtil.bigNumberFormat2(pkg.getPreMoney())%>乐币<br/>
礼品列表:<%
List items = pkg.getItemList();
if(items != null){
for(int i=0;i<items.size();i++){
	int id = ((Integer)items.get(i)).intValue();
    UserBagBean userBag=UserBagCacheUtil.getUserBagCache(id);if(userBag==null||userBag.getUserId()!=loginUser.getId())continue;
    DummyProductBean dummyProduct=action.getItem(userBag.getProductId());if(dummyProduct==null)continue;
    long timeleft = userBag.getTimeLeft();%>
<%if(i>0){%>,<%}%><%=dummyProduct.getName()%><%if(dummyProduct.getTime()==1){%>x<%=userBag.getTime()%><%}%>
<%if(timeleft>0&&timeleft<3600000l*24*100){%>(<%=DateUtil.formatTimeInterval(timeleft)%>)<%}%><%}%>
<%}else{%>(空)
<%}%><br/>
注意:红包和礼品在确定后不能进行修改!!请仔细确认后,再点击
<a href="set2.jsp?o=1&amp;id=<%=pkg.getId()%>">确定为以上设置</a><br/>
<br/>
<a href="set2.jsp?o=2&amp;id=<%=pkg.getId()%>">取消以上设置,重新填写</a><br/>
<%}else{%>

现金:<%=StringUtil.bigNumberFormat(UserInfoUtil.getUserStatus(loginUser.getId()).getGamePoint())%><br/>
请输入红包金额(乐币):<br/>
<input type="text" name="money" format="*N" value=""/><br/>
<%
List list = new ArrayList();
if(type.getCount()>0){

if(!action.hasParam("noitem")){
// 取出所有可以交易的物品
List userBagList = UserBagCacheUtil.getUserBagListCacheById(loginUser.getId());;
for(int i=0;i<userBagList.size();i++){
    Integer userBagId=(Integer)userBagList.get(i);
    UserBagBean userBag=UserBagCacheUtil.getUserBagCache(userBagId.intValue());
    DummyProductBean dummyProduct=action.getItem(userBag.getProductId());
    if(dummyProduct != null&&!dummyProduct.isBind())list.add(userBag);}
%>
<%if(list.size()>0){%>
请填写礼品清单(最多<%=type.getCount()%>件):<br/>
<select multiple="true"  name="items">
<%
for(int i=0;i<list.size();i++){
    UserBagBean userBag=(UserBagBean)list.get(i);
    DummyProductBean dummyProduct=action.getItem(userBag.getProductId());
%><option value="<%=userBag.getId()%>"><%=dummyProduct.getName()%><%=userBag.getTimeString(dummyProduct)%></option>
<%}%></select><br/>
<%}%>
<%}%>
<%}%>
<anchor>确认提交
<go href="set2.jsp?id=<%=pkg.getId()%>" method="post">
<%if(list.size()>0){%><postfield name="items" value="$items" /><%}%>
<postfield name="money" value="$money" />
</go>
</anchor><br/>
<%}%><br/>
<%if(pkg!=null){%><a href="send.jsp?id=<%=pkg.getId()%>">返回</a><br/><%}%>
<a href="my1.jsp">返回我的礼包列表</a><br/>
<a href="index.jsp">返回礼包店首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>