<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.user.UserBagAction" %><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@ page import="net.joycool.wap.bean.*" %><%
response.setHeader("Cache-Control","no-cache");
UserBagAction action = new UserBagAction(request);
action.userBagUse(request);
UserSettingBean set = action.getLoginUser().getUserSetting();
String result =(String)request.getAttribute("result");
String url=("/lswjs/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="我的行囊" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/home/home.jsp">家园首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
DummyProductBean dummyProduct = (DummyProductBean)request.getAttribute("dummyProduct");
UserBagBean userBag = (UserBagBean)request.getAttribute("userBag");
int cooldown = UserBagAction.getCooldown(userBag, dummyProduct);
url=("/user/userBag.jsp");
long timeleft = userBag.getTimeLeft();
%>
<card title="我的行囊">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=dummyProduct.getName()%><%=userBag.getTimeString2(dummyProduct)%><%if(dummyProduct.isUnique()){%>[唯一]<%}%><br/>
<%if((set==null||!set.isFlagHideStar())&&UserBagCacheUtil.isItemShow(dummyProduct.getId())){%><img src="/rep/lx/e<%=dummyProduct.getId()%>.gif" alt=""/><%}%>
<%if(timeleft>0){%>
<%if(dummyProduct.getMode()!=0&&dummyProduct.getMode()!=6&&dummyProduct.getMode()!=7){%>
<%if(cooldown<=0){%><a href="<%=("userBagResult.jsp?userBagId="+userBag.getId())+"&amp;use=1"%>">使用</a><%}else{%>使用<%}%>/
<%}%>
<%if(dummyProduct.getDummyId()==5){%>
<%if(cooldown<=0){%><a href="<%=("userBagResult.jsp?userBagId="+userBag.getId())+"&amp;compose=1"%>">合成</a><%}else{%>合成<%}%>/
<%}%>
<%if(dummyProduct.isFlag(2)){%>
<a href="<%=("userBagResult.jsp?userBagId="+userBag.getId())+"&amp;quest=1"%>">任务</a>/
<%}%>
<%if(!dummyProduct.isBind()&&!userBag.isMarkBind()){%>
<a href="userBagResult.jsp?present=1&amp;userBagId=<%=userBag.getId()%>">赠送</a>/
<%if(dummyProduct.getDummyId()<=10){%>
<a href="/auction/index.jsp?userBagId=<%=userBag.getId()%>">拍卖</a>/
<%}%>
<%}else{%>
(已绑定)/
<%}%>
<%}else{%>
该物品已经过期！<br/>
<%}%>
<a href="<%=("userBagResult.jsp?userBagId="+userBag.getId())+"&amp;lose=1"%>">丢弃</a><br/>
描述：<%=dummyProduct.getDescription()%><%if(dummyProduct.isFlag(1)){%>(任务物品)<%}%><br/>
<%if(timeleft>0&&timeleft<3600000l*24*100){%>
该物品还有<%=DateUtil.formatTimeInterval3(timeleft)%>到期！<br/>
<%}%>
<%if(cooldown>0){%>还需要<%=DateUtil.formatTimeInterval(cooldown)%>才能再次使用<br/><%}%>
<%if(dummyProduct.getStack()>1||dummyProduct.getTime()==1&&userBag.getTime()>1){%>
<%if(dummyProduct.getStack()>userBag.getTime()){%>
<a href="userBagResult.jsp?stack=-2&amp;userBagId=<%=userBag.getId()%>">合并</a>
<%}else{%>合并<%}%>/拆分
<%if(dummyProduct.getTime()==1&&userBag.getTime()>1){%>
<%for(int i=1;i<=userBag.getTime()/2;i+=i){%>
<a href="userBagResult.jsp?stack=<%=i%>&amp;userBagId=<%=userBag.getId()%>"><%=i%></a>|
<%}%><%}%><br/><%}%>
<%if(userBag.getCreatorId()>300){
UserBean creator = UserInfoUtil.getUser(userBag.getCreatorId());
if(creator != null){
%>制造者:<a href="ViewUserInfo.do?userId=<%=creator.getId()%>"><%=creator.getNickNameWml()%></a><br/>
<%}}%>
<a href="userBag.jsp">返回我的行囊</a><br/>
<a href="/home/home.jsp">返回我的家园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>