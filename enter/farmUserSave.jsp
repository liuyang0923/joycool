<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.lang.reflect.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.spec.farm.*"%><%
response.setHeader("Cache-Control","no-cache");
    	Class cc = CacheManage.farmUser.getClass();
    	Field cf = cc.getDeclaredField("map");
    	cf.setAccessible(true);
    	Map map = (Map)cf.get(CacheManage.farmUser);
List keyList = Arrays.asList(map.keySet().toArray());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷门户">
<p align="left">
<%



DbOperation dbOp = new DbOperation(4);
try{

for(int i = 0;i < keyList.size();i++){
Integer key = (Integer)keyList.get(i);
FarmUserBean user = (FarmUserBean)CacheManage.farmUser.get(key);
dbOp.executeUpdate("update farm_user set pos=" + user.getPos() + " where user_id=" + user.getUserId());
List questCreatureFinish = user.getQuestCreatureFinish();
if(questCreatureFinish.size()==0) continue;
String str = "";
%>
<%for(int j = 0;j<questCreatureFinish.size();j++){
int[] f = (int[])questCreatureFinish.get(j);
if(f[2]==0) continue;
str += f[0] + "-" + f[1] + "-" + f[2] + ",";
%>
<%}%><%if(str.length()>0){
dbOp.executeUpdate("update farm_user set quest_creature='" + str + "' where user_id=" + user.getUserId());
%>
<%=user.getNameWml()%>(<%=user.getUserId()%>)<br/><%=str%><br/><%}%>
<%}






}catch(Exception e){}
dbOp.release();
%>
</p>
</card>
</wml>