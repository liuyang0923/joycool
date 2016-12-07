<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.framework.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.util.db.*,java.sql.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%
String password = Encoder.encrypt("111111");
String sql = "select a.id from user_info a,user_status b where a.id=b.user_id and a.password='" + password + "' and b.last_login_time<'2006-11-01' limit 0,500000";
List idList = SqlUtil.getIntList(sql, Constants.DBShortName); 

for(int i=0;i<idList.size();i++){
    try{
        Integer userId = (Integer)idList.get(i);
        if(userId==null)continue;
	
        int key = RandomUtil.nextIntNoZero(1000000);
        String newPassword = Encoder.encrypt("" + key);
        UserInfoUtil.updateUser("password='" + newPassword + "'", "id=" + userId.intValue(), "" + userId.intValue());
	
        if(i%50==0){
        	if(i%10000==0){
        		System.out.println("modify:" + i + "  " + userId.intValue());
            }
            Thread.sleep(200);
        }
    }catch(Exception e){		
    }
}
%>
done.
<%= sql %><br/>
<%= password %><br/>
cc2739c64fefb199