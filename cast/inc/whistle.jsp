<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %>
当前防御力附加<%=ResNeed.getWhistleAttact(building.getGrade())%><br/>
<%if(building.getGrade()<buildingt.getMaxGrade()){%>升级后防御力附加<%=ResNeed.getWhistleAttact(building.getGrade()+1)%><br/><%}%>