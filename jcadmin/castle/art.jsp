<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	response.setHeader("Cache-Control", "no-cache");
	
	CustomAction action = new CustomAction(request);
	int cid = action.getParameterInt("cid");
	int id=action.getParameterInt("id");
	
	ArtBean art = null;
	if(id!=0) {
		art = CastleUtil.getArt(id);
		if(art!=null)
			cid=art.getCid();
	}
	CastleBean castle = CastleUtil.getCastleById(cid);
	if(!action.isMethodGet()){
		int type=action.getParameterInt("type");
		int flag=action.getParameterFlag("flag");
		int effect=action.getParameterInt("effect");
		if(id==0){
			CastleUtil.addArt(cid, type,StringUtil.toSql(action.getParameterNoEnter("name")),flag,effect);
		} else {
			int status=action.getParameterInt("status");
			int cid2=action.getParameterInt("cid2");
			CastleUtil.modifyArt(id, cid2, type,StringUtil.toSql(action.getParameterNoEnter("name")),flag,effect, status);
		}
		if(cid==0)
			response.sendRedirect("arts.jsp");
		else
			response.sendRedirect("castle.jsp?id="+cid);
		return;
	}
	if(request.getParameter("del")!=null){
		CastleUtil.deleteArt(cid);
		response.sendRedirect("castle.jsp?id="+cid);
		return;
	}
%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
	添加宝物<br/>
<%if(castle!=null){%>【<a href="castle.jsp?id=<%=cid%>"><%=castle.getCastleNameWml()%></a>】(<%=castle.getX()%>|<%=castle.getY()%>)<br/><%}%>
<%if(art==null){%>
<form action="art.jsp?cid=<%=cid%>" method=post>
<select name="type">
<%for(int i=0;i<ArtBean.typeNames.length;i++){%><option value="<%=i%>"><%=ArtBean.typeNames[i]%></option><%}%>
</select>
名称:<input type=text name="name" value=""><br/>
效果:<input type=text name="effect" value=""><br/>
<input type=checkbox name="flag0">整个帐户生效<br/>
<input type=checkbox name="flag1">需要20级宝库<br/>
<input type=checkbox name="flag2">随机变换属性<br/>
<input type=submit value="确认添加">
<%}else{%>

<form action="art.jsp?cid=<%=cid%>&id=<%=id%>" method=post>
<select name="type">
<%for(int i=0;i<ArtBean.typeNames.length;i++){%><option value="<%=i%>"<%if(art.getType()==i){%> selected<%}%>><%=ArtBean.typeNames[i]%></option><%}%>
</select>
名称:<input type=text name="name" value="<%=art.getName()%>"><br/>
效果:<input type=text name="effect" value="<%=art.getEffect()%>"><br/>
城堡:<input type=text name="cid2" value="<%=art.getCid()%>"><%if(castle!=null){%> <a href="castle.jsp?id=<%=art.getCid()%>"><%=castle.getCastleNameWml()%>(<%=castle.getX()%>|<%=castle.getY()%>)</a><%}%><br/>
状态:<select name="status">
<option value="0" <%if(art.getStatus()==0){%>selected<%}%> >未激活</option>
<option value="1" <%if(art.getStatus()==1){%>selected<%}%> >已激活</option>
</select><br/>
<input type=checkbox name="flag" value="0"<%if(art.isFlagAccount()){%> checked<%}%>>整个帐户生效<br/>
<input type=checkbox name="flag" value="1"<%if(art.isFlagBig()){%> checked<%}%>>需要20级宝库<br/>
<input type=checkbox name="flag" value="2"<%if(art.isFlagChange()){%> checked<%}%>>随机变换属性<br/>
<input type=submit value="确认修改">

<%}%>
</form>
	</body>
</html>