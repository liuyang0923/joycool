<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.CacheManage"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%!
	static class FrBean {
		public int id;
		public String name;
		public String frs;
		public String code;
		public FrBean(int id, String name, String frs, String code){
			this.id=id;
			this.name=name;
			this.frs=frs;
			this.code=code;
		}
	};

	static FrBean[] frArray = {
		new FrBean(1,"买卖宝","7,60,61,62,63,64","atevbg5"),
		new FrBean(2,"看书网","56,57,58,59","h33ef678"),
		new FrBean(3,"有米代理","88,89,90,91,92","j;3rsfs"),
		new FrBean(4,"杭州导逊柠檬浏览器","86","fg346;9"),
		new FrBean(5,"广州指点代理乐趣领域","87,94,95","jj763rf"),
		new FrBean(6,"航海桌面","81","jh9034d"),
		new FrBean(7,"美人鱼图库","82","gkp;345"),
		new FrBean(8,"100度魔比门户","83","yudejic"),
		new FrBean(9,"酷搜沃","84","mksftx"),
		new FrBean(10,"幻祭文学","85","xiao9346u"),
		new FrBean(11,"柠檬浏览器","86","dumd;23"),
		new FrBean(12,"深讯和","97","wdigt789d"),
		new FrBean(13,"北京风网","99","humrxfu2"),
		new FrBean(14,"100tv","100","oo789etnd"),
		new FrBean(15,"好的wap网","103","sofi2;hts"),
		new FrBean(16,"拇指浏览器","104","eifmo34d"),
		new FrBean(17,"181860.com","105","dfy672hkdc"),
		new FrBean(18,"中润","106","sdyrf78"),
		new FrBean(19,"中润","107","dgyei34p"),
		new FrBean(20,"中润","108","efty820m"),
	}; 
%><%
CustomAction action = new CustomAction(request, response);

int start = action.getParameterInt("start");
int id = action.getParameterInt("id");
FrBean fr = null;
for(int i = 0;i<frArray.length;i++){
	if(frArray[i].id==id){
		fr=frArray[i];
		break;
	}
}
if(fr==null||!fr.code.equals(request.getParameter("code"))) return;
int end = action.getParameterInt("end");

%>
<html>
	<head>
	</head>
<link href="../../jcadmin/farm/common.css" rel="stylesheet" type="text/css">
	<body>
【<%=fr.name%>】<br/>
<%
	DbOperation dbOp = new DbOperation(5);

	String query = "select date(create_time) 日期, link_id 友链id,count(id) 注册数  from jump_register where create_time>curdate() and link_id in("+fr.frs+") group by link_id";

	ResultSet rs = dbOp.executeQuery(query);
	
	%><%@ include file="../../jcadmin/showQuery.jsp"%><%
	
	rs.close();
	
	%><br/><%
	
	query = "select date(create_time) 日期, link_id 友链id,count(id) 注册数  from jump_register where create_time between date_add(curdate(),interval -1 day) and curdate() and link_id in("+fr.frs+") group by link_id";

	rs = dbOp.executeQuery(query);
	
	%><%@ include file="../../jcadmin/showQuery.jsp"%><%
	
	rs.close();
	
	%><br/><%
	
	query = "select date(create_time) 日期, link_id 友链id,count(id) 注册数  from jump_register where create_time between date_add(curdate(),interval -2 day) and date_add(curdate(),interval -1 day) and link_id in("+fr.frs+") group by link_id";

	rs = dbOp.executeQuery(query);
	
	%><%@ include file="../../jcadmin/showQuery.jsp"%><%

	dbOp.release();	%><br/>
	</body>
</html>
