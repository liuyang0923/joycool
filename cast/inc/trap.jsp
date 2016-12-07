<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%
	int cid = action.getCastle().getId();
	List cacheList = cacheService.getCacheSoldierByCid(cid,building.getBuildType());

	float rate = ResNeed.getBuildingT(ResNeed.CASERN_BUILD,building.getGrade()).getValue() / 100f;
if(cacheList.size()>0){
	for(int i = 0; i < cacheList.size(); i++) {
	SoldierThreadBean sol = (SoldierThreadBean)cacheList.get(i);
%><%if(i==0){%>下一个陷阱将在<%=DateUtil.formatTimeInterval2(sol.getEndTime())%>内完成<br/><%}%>
有<%=sol.getCount()%>个陷阱正在建造,剩余<%=DateUtil.formatTimeInterval2(sol.getAllEndTime())%>,结束于<%=DateUtil.formatTime(sol.getAllEndTime())%><br/>
<%}}%>
[陷阱](现有:<%=action.getUserResBean().getTrap()%>)<br/>
木20|石30|铁10|粮20<br/>
建造时间<%=DateUtil.formatTimeInterval2((int)(300*rate))%>
<input name="count" format="*N"/>
<anchor title="训练">建造<go href="train2.jsp?pos=<%=pos%>">
<postfield name="count" value="$count" />
</go></anchor><br/>

当前最多可以造<%=building.getGrade()*10%>个陷阱<br/>
<%if(building.getGrade()<buildingt.getMaxGrade()){%>升级后最多可以造<%=building.getGrade()*10+10%>个陷阱<br/><%}%>