<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*,java.io.*,
                 java.sql.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.bean.*,
                 net.joycool.wap.service.factory.*,
                 net.joycool.wap.service.infc.*,
                 net.joycool.wap.util.db.*"%><%!
public String getContent(String file){
	String ret = "";
	
	try{
		BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
		String line = reader.readLine();
		while(line!=null){
			ret = ret + line + "\r\n";
			line = reader.readLine();
		}
	}catch(Exception e){		
	}
	
	return ret;
}
%>                 
<%
String[] ret = new String[3];

String file = "/usr/local/joycool-rep/joycoolLog/joycool_access_count.log";
ret[0] = getContent(file);

file = "/usr/local/joycool-rep/joycoolLog/joycool_rep_access_count.log";
ret[1] = getContent(file);

file = "/usr/local/joycool-rep/joycoolLog/joycool_pv_count.log";
ret[2] = getContent(file);

int jcHttpCount = 0;
int jcRepHttpCount = 0;
int jcPvCount = 0;
try{
	int pos = ret[0].indexOf(" ");
	jcHttpCount = Integer.parseInt(ret[0].substring(0, pos));
	
	pos = ret[1].indexOf(" ");
	jcRepHttpCount = Integer.parseInt(ret[1].substring(0, pos));
	
	pos = ret[2].indexOf(" ");
	jcPvCount = Integer.parseInt(ret[2].substring(0, pos));
}catch(Exception e){	
}
%>  
(<%= jcHttpCount %> + <%= jcRepHttpCount %>) / <%= jcPvCount %> <br>
<%= ((float)(jcHttpCount + jcRepHttpCount))/jcPvCount %> <br>