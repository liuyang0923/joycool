<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %>
当前资源存储容量<%=ResNeed.getBuildingT(building.getBuildType(),building.getGrade()).getValue()%><br/>
<%if(building.getGrade()<buildingt.getMaxGrade()){%>升级后资源存储容量<%=ResNeed.getBuildingT(building.getBuildType(),building.getGrade()+1).getValue()%><br/><%}%>