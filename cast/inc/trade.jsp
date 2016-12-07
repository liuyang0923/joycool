<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %>
当前商人运载量提升<%=building.getGrade()*10%>%<br/>
<%if(building.getGrade()<buildingt.getMaxGrade()){%>升级后商人运载量提升<%=building.getGrade()*10+10%>%<br/><%}%>