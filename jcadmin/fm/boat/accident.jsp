<%@ page language="java" import="com.jspsmart.upload.*,net.joycool.wap.util.*,jc.family.game.boat.*,java.util.*" pageEncoding="utf-8"%>
<%
	BoatAction action = new BoatAction(request,response);
	int del = action.getParameterInt("del");
	String[] distance = {"无","前进","后退"}; 
	String[] speed = {"无","提高","降低"}; 
//	String[] angle = {"无","有"}; 
	if(1==action.getParameterInt("clear")){
		TreeMap treeMap = BoatAction.accidentMap;
		if(treeMap != null){
			treeMap.clear();
		}
	}
	if(del > 0){
		BoatAction.service.upd("delete from fm_game_accident where id="+del);
		request.setAttribute("tip","删除成功!");
	}
	if(1==action.getParameterInt("a")){
		SmartUpload smUpload = new SmartUpload();
		smUpload.initialize(pageContext);
		smUpload.setAllowedFilesList("GIF,gif,JPG,jpg,PNG,png");
		smUpload.setMaxFileSize(50*1024);
		smUpload.upload();
		File upfile = smUpload.getFiles().getFile(0);
		String name = smUpload.getRequest().getParameter("name").trim();
		String bak = smUpload.getRequest().getParameter("bak").trim();
		String strId = smUpload.getRequest().getParameter("id");
		String angle1 = smUpload.getRequest().getParameter("angle1");
		String angle2 = smUpload.getRequest().getParameter("angle2");
		String speed1 = smUpload.getRequest().getParameter("speed1");
		String speed2 = smUpload.getRequest().getParameter("speed2");
		String strAngleType = smUpload.getRequest().getParameter("angleType");
		String strSpeedType = smUpload.getRequest().getParameter("speedType");
		String strDistanceType = smUpload.getRequest().getParameter("distanceType");
		String distance1 = smUpload.getRequest().getParameter("distance1");
		String distance2 = smUpload.getRequest().getParameter("distance2");
		String strPercent = smUpload.getRequest().getParameter("percent");
		if(name != null && name.length() > 0){
			AccidentBean ab = new AccidentBean();
			int angleType = Integer.parseInt(strAngleType);
			int speedType = Integer.parseInt(strSpeedType);
			int distanceType = Integer.parseInt(strDistanceType);
			int id = 0;
			int percent = 0;
			if(strPercent != null && strPercent.length() > 0){
				percent = Integer.parseInt(strPercent);
			}
			if(strId != null) {
				id = Integer.parseInt(strId);
				ab = BoatAction.service.getAccidentBean("id="+strId);
				if (ab==null) {
					ab=new AccidentBean();
					ab.setId(id);
				}
			}
			ab.setBak(bak);
			ab.setName(name);
			ab.setPercent(percent);
			ab.setAngleType(angleType);
			ab.setSpeedType(speedType);
			ab.setDistanceType(distanceType);
			if(angleType > 0){
				ab.setAngle1(Integer.parseInt(angle1));
				ab.setAngle2(Integer.parseInt(angle2));
			}else{
				ab.setAngle1(0);
				ab.setAngle2(0);
			}
			if(speedType > 0){
				ab.setSpeed1(Integer.parseInt(speed1));
				ab.setSpeed2(Integer.parseInt(speed2));
			}else{
				ab.setSpeed1(0);
				ab.setSpeed2(0);
			}
			if(distanceType > 0){
				ab.setDistance1(Integer.parseInt(distance1));
				ab.setDistance2(Integer.parseInt(distance2));
			}else{
				ab.setDistance1(0);
				ab.setDistance2(0);
			}
			AccidentBean ab2 = null;
			if(ab.getId() > 0){
				ab2 = BoatAction.service.getAccidentBean(" id="+ab.getId());
			}
			if(upfile != null && upfile.getFileExt() != null && upfile.getFileExt().length() > 0){
				String bigimg = System.currentTimeMillis()+"."+upfile.getFileExt();
				ab.setBigImg(bigimg);
				upfile.saveAs(BoatAction.IMAGE_URL + bigimg,SmartUpload.SAVE_PHYSICAL);
				if(ab2 != null) {
					BoatAction.delFile(ab2.getBigImg());
				}
			}else{
				//ab.setBigImg("");
			}
			if(id > 0){
				BoatAction.service.alterAccident(ab);
				request.setAttribute("tip","修改成功!");
			} else {
				BoatAction.service.insertAccident(ab);
				request.setAttribute("tip","添加成功!");
			}
		}else{
			response.sendRedirect("addaccident.jsp?a=1");
			return;
		}
	}
	List list = BoatAction.service.getAccidentList("1 order by id desc");
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>随机事件</title>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  <p style="color:red;"><%=request.getAttribute("tip") != null?request.getAttribute("tip"):""%></p>
	<a href="addaccident.jsp">添加事件</a>|<a href="accident.jsp?clear=1">清除随机事件缓存</a><br>
	<table border="1">
		<tr>
			<td align="center">ID</td>
			<td align="center">名称</td>
			<td align="center">描述</td>
			<td align="center">位置变化(米)</td>
			<td align="center">速度变化(百分数)</td>
			<td align="center">角度变化(度)</td>
			<td align="center">出现概率</td>
			<td align="center">图片</td>
			<td align="center">操作</td>
		</tr>
		<%
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					AccidentBean bean = (AccidentBean)list.get(i);
					if(bean != null){
						%>
						<tr>
							<td align="center"><%=bean.getId()%></td>
							<td align="center"><%=StringUtil.toWml(bean.getName())%></td>
							<td align="center"><%=bean.getBak()%></td>
							<td align="center"><%if(bean.getDistanceType()>0){%><%=distance[bean.getDistanceType()]%><%=bean.getDistance1()%>m-<%=bean.getDistance2()%>m<%}else{%>无<%}%></td>
							<td align="center"><%if(bean.getSpeedType()>0){%><%=speed[bean.getSpeedType()]%><%=bean.getSpeed1()%>%-<%=bean.getSpeed2()%>%<%}else{%>无<%}%></td>
							<td align="center"><%if(bean.getAngleType()>0){%>有<%=bean.getAngle1()%>°-<%=bean.getAngle2()%>°<%}else{%>无<%}%></td>
							<td align="center"><%=bean.getPercent()%></td>
							<td align="center"><img src="/rep/family/boat/<%=bean.getBigImg()%>" alt="x"/></td>
							<td align="center"><a href="accident.jsp?del=<%=bean.getId()%>">删除</a>|<a href="addaccident.jsp?id=<%=bean.getId()%>">修改</a></td>
						</tr>
						<%
					}
				}
			}
		 %>
	</table>
	<a href="boat.jsp">返回龙舟管理</a><br>
	<a href="/jcadmin/fm/index.jsp">返回游戏管理主页面</a>
  </body>
</html>