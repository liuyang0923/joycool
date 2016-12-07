<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.plant();
int fieldId = action.getParameterInt("id");
List crops = action.world.cropList;
FarmUserBean user = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源农场">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%for(int i=0;i<crops.size();i++){
FarmCropBean crop = (FarmCropBean)crops.get(i);
if(crop.getProId()!=1) continue;
%>
<a href="act.jsp?a=1&amp;id=<%=fieldId%>&amp;c=<%=crop.getId()%>"><%=crop.getName()%></a><br/>
<%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>