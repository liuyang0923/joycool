<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%!
static CastleService service = CastleService.getInstance();
%><%
	response.setHeader("Cache-Control", "no-cache");
	
	CustomAction action = new CustomAction(request);
	int cid = action.getParameterInt("cid");
	int pos = action.getParameterInt("i");
	CastleBean castle = CastleUtil.getCastleById(cid);
	BuildingBean build = service.getBuildingBeanByPos(cid,pos);
	int[] baseBuild = ResNeed.baseBuildRes[castle.getType2()];
	if(!action.isMethodGet()){
		int id = action.getParameterInt("id");
		int type = action.getParameterInt("type");
		int grade = action.getParameterInt("grade");
		UserResBean userRes = CastleUtil.getUserResBeanById(cid);
		if(id==0&&request.getParameter("del")==null){		// 如果是0表示新增
			if(type!=0&&grade>0&&grade<=ResNeed.getBuildingTs()[type].getMaxGrade()){
				BuildingBean b = new BuildingBean();
				b.setCid(cid);
				b.setBuildType(type);
				b.setGrade(grade);
				b.setBuildPos(pos);
				service.addBuilding(b);
				userRes.getBuildings()[type]=(byte)grade;
			}
		} else if(type==0||request.getParameter("del")!=null){	//0表示删除
			if(build!=null){
				service.deleteBuilding(build);
				userRes.getBuildings()[type]=0;
			}
		}else if(build!=null){	// 修改
			if(grade>=0&&grade<=ResNeed.getBuildingTs()[type].getMaxGrade()) {
				SqlUtil.executeUpdate("update castle_building set build_type="+type+",grade="+grade+" where id="+id,5);
				userRes.getBuildings()[type]=(byte)grade;
			}
		}

		response.sendRedirect("castle.jsp?id="+cid);
		return;
	}
	BuildingTBean[] bts = ResNeed.getBuildingTs();
	int grade = 0;
	int id = 0;
	if(build!=null){
		grade=build.getGrade();
		id=build.getId();
	}
	int cur = -1;

	if(build!=null){
		cur=build.getBuildType();
	} else 	if(pos <= 18)
		cur = baseBuild[pos];;
%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body onload="f1.grade.focus();f1.grade.select();">
	修改城堡建筑<br/>
【<a href="castle.jsp?id=<%=cid%>"><%=castle.getCastleNameWml()%></a>】(<%=castle.getX()%>|<%=castle.getY()%>)<br/>

<form action="mbuild.jsp?cid=<%=cid%>&id=<%=id%>&i=<%=pos%>" method=post id="f1">
<select name="type">
<%for(int i=1;i<bts.length;i++)if(bts[i]!=null){
BuildingTBean bt = bts[i];

%><option value="<%=i%>"<%if(cur==i){%> selected<%}%>><%=bt.getName()%></option><%

}%></select>
&nbsp;<input size=5 type="text" name="grade" value="<%=grade%>">

<input type=submit value="确认修改">
<input type=submit name="del" value="删除">
</form><br/>
	</body>
</html>