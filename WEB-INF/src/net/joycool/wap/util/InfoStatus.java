//更新user_status 数据库将原来的用户进行状态初始化
//zhul-2006-06-08 start
package net.joycool.wap.util;

import java.util.Vector;

import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;

public class InfoStatus {
	
	public static void go()
	{
		
		IUserService userServic=ServiceFactory.createUserService();
		//userServic.updateUserStatus("nickname_change=0,login_count=1,last_login_time=now(),last_logout_time=now(),total_online_time=0","nickname_change is null||last_logout_time is null");
		//System.out.println("change ok!!!");
		Vector allUser=userServic.getUserList("1=1");
		for(int i=0;i<allUser.size();i++)
		{
			UserBean ub=(UserBean)allUser.get(i);
//			if(userServic.getUserStatus("user_id="+ub.getId())==null)
			if(UserInfoUtil.getUserStatus(ub.getId())==null)
			{
				UserStatusBean usb = new UserStatusBean();
            	usb.setUserId(ub.getId());
            	usb.setPoint(0);
            	usb.setRank(0);
            	usb.setGamePoint(10000);
            	usb.setNicknameChange(0);
            	usb.setLoginCount(1);
            	userServic.addUserStatus(usb); 
    			// add by mcq 2006-07-24 for stat user money history start
    			MoneyAction.addMoneyFlowRecord(Constants.USERREGISTER,10000,
    					Constants.PLUS,ub.getId());
    			// add by mcq 2006-07-24  for stat user money history end
				
			}
		System.out.println("ok!!!!!!!!1");
		}
	}

}
//zhul-2006-06-08 start