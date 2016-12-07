<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %>
当前建筑坚固度提升<%=building.getGrade()*10%>%<br/>
<%if(building.getGrade()<buildingt.getMaxGrade()){%>升级后建筑坚固度提升<%=building.getGrade()*10+10%>%<br/><%}%>