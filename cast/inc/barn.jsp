<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %>
当前粮食存储容量<%=ResNeed.getBuildingT(building.getBuildType(),building.getGrade()).getValue()%><br/>
<%if(building.getGrade()<buildingt.getMaxGrade()){%>升级后粮食存储容量<%=ResNeed.getBuildingT(building.getBuildType(),building.getGrade()+1).getValue()%><br/><%}%>