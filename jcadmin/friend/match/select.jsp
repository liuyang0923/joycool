<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="net.joycool.wap.cache.ICacheMap,net.joycool.wap.action.NoticeAction,jc.credit.UserInfo,jc.credit.CreditAction,jc.match.*,java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*" %>
<%
MatchAction action = new MatchAction(request);
String tip = "";
String tmp = "";
int from = action.getParameterInt("f");	// 0:选择省 1:选择省(1) 2:选择城市(2)
int id = action.getParameterInt("id");
int confirm = action.getParameterInt("c");
CreditCity city = null;
CreditProvince province = null;
MatchArea matchArea = MatchAction.getArea(id);
if (confirm == 1 && matchArea != null){
	String[] pro = request.getParameterValues("pro");
	if (pro != null && pro.length > 0){
		StringBuilder sb = new StringBuilder();
		for (int i = 0 ; i < pro.length ; i++){
			sb.append("" + pro[i]);
			sb.append(",");
		}
		tmp = sb.toString();
		if (sb.length() > 0){
			tmp = sb.substring(0,sb.length() - 1);
		}
		if (from == 0){
			// 选择省
			// 将每个省的flag设为这个区的ID
			SqlUtil.executeUpdate("update credit_province set flag=" + id + " where id in (" + tmp + ")",5);
			// 将这些省加到分区中
			if (!"".equals(matchArea.getProvinces())){
				tmp = "," + tmp;
			}
			matchArea.setProvinces(matchArea.getProvinces() + tmp);
			if (!"".equals(tmp)){
				SqlUtil.executeUpdate("update match_area set provinces='" + matchArea.getProvinces() + "' where id=" + id,5);
				SqlUtil.executeUpdate("update credit_city set flag=" + id + " where hypo in(" + matchArea.getProvinces() + ") and flag=0",5);
			}
			response.sendRedirect("area2.jsp?id=" + matchArea.getId());
			return;
		}
		if (from == 1){
			request.getRequestDispatcher("select.jsp?id=" + matchArea.getId() + "&f=2").forward(request,response);
			return;
		}
	}
	String[] citys = request.getParameterValues("citys");
	if (citys != null && citys.length > 0){
		StringBuilder sb = new StringBuilder();
		for (int i = 0 ; i < citys.length ; i++){
			sb.append("" + citys[i]);
			sb.append(",");
		}
		tmp = sb.toString();
		if (sb.length() > 0){
			tmp = sb.substring(0,sb.length() - 1);
		}
		if (from == 2){
			if (!"".equals(tmp)){
				SqlUtil.executeUpdate("update credit_city set flag=" + id + " where id in (" + tmp + ")",5);
				if (!"".equals(matchArea.getCitys())){
					tmp = "," + tmp;
				}
				matchArea.setCitys(matchArea.getCitys() + tmp);
				SqlUtil.executeUpdate("update match_area set citys=concat(citys,'" + matchArea.getCitys() + "') where id=" + id,5);
			}
			
			response.sendRedirect("area2.jsp?id=" + matchArea.getId());
			return;
		}
	}
}
List cityList = new ArrayList();
if (!"".equals(tmp)){
	cityList = CreditAction.service.getCityList(" hypo in (" + tmp + ") and flag=0");
}
List provinceList = CreditAction.service.getProvinceList(" flag=0");
%>
<html>
	<head>
		<title>选择</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
	<%if(!"".equals(tip)){%><font color="red"><%=tip%></font><br/><%}%>
	<a href="index.jsp">回首页</a>|<%if(from != 2){%><a href="area2.jsp?id=<%=id%>">回上页</a><%}else{%><a href="select.jsp?id=<%=id%>&f=1">回上页</a><%}%><br/>
	<%if (from == 0){
		%><form method="post" action="select.jsp?f=<%=from%>&c=1&id=<%=id%>">
		  选择省:<br/>
		  <table width="100%">
				<tr>
					<%if(provinceList != null && provinceList.size() > 0){
						for (int i = 0 ; i < provinceList.size() ; i++){
							province = (CreditProvince)provinceList.get(i);
							if (province != null){
								%><td><input type="checkbox" name="pro" value="<%=province.getId()%>"/><%=province.getProvince()%></td><%if((i+1) % 6 == 0){%><tr/><tr><%}%><%
							}
						}
					}%>
				</tr>
		  </table>
		  <br/>
		  <input type="submit" value="确定" />
		  </form>
		<%
	} else if (from == 1){
		%><form method="post" action="select.jsp?f=<%=from%>&c=1&id=<%=id%>">
		  1、选择省:<br/>
		  <table width="100%">
				<tr>
					<%if(provinceList != null && provinceList.size() > 0){
						for (int i = 0 ; i < provinceList.size() ; i++){
							province = (CreditProvince)provinceList.get(i);
							if (province != null){
								%><td><input type="checkbox" name="pro" value="<%=province.getId()%>"/><%=province.getProvince()%></td><%if((i+1) % 6 == 0){%><tr/><tr><%}%><%
							}
						}
					}%>
				</tr>
		  </table>
		  <br/>
		  <input type="submit" value="确定" />
		  </form><%
	} else if (from == 2){
		%><form method="post" action="select.jsp?f=<%=from%>&c=1&id=<%=id%>">
		  2、选择市:<br/>
		  <table width="100%">
				<tr>
					<%if(cityList != null && cityList.size() > 0){
						for (int i = 0 ; i < cityList.size() ; i++){
							city = (CreditCity)cityList.get(i);
							if (city != null){
								%><td><input type="checkbox" name="citys" value="<%=city.getId()%>"/><%=city.getCity()%></td><%if((i+1) % 6 == 0){%><tr/><tr><%}%><%
							}
						}
					}%>
				</tr>
		  </table>
		  <br/>
		  <input type="submit" value="确定" />
		  </form><%
	}%>
	</body>
</html>