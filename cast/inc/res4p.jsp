<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %>
当前产量提升的百分比<%=ResNeed.getBuildingT(building.getBuildType(),building.getGrade()).getValue()%>%<br/>
<%if(building.getGrade()<buildingt.getMaxGrade()){%>升级后产量提升的百分比<%=ResNeed.getBuildingT(building.getBuildType(),building.getGrade()+1).getValue()%>%<br/><%}%>