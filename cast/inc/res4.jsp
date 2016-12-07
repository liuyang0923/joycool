<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %>
当前产量<%=ResNeed.getBuildingT(building.getBuildType(),building.getGrade()).getValue()%>每小时<br/>
<%if(building.getGrade()<buildingt.getMaxGrade()){%>升级后产量<%=ResNeed.getBuildingT(building.getBuildType(),building.getGrade()+1).getValue()%>每小时<br/><%}%>