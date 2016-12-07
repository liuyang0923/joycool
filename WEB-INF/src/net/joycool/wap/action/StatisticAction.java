package net.joycool.wap.action;

import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IStatisticService;
public class StatisticAction {
	public static int getUserCount(String date,String condition)
	{
		IStatisticService statservice=ServiceFactory.createStatisticService();
		int count=statservice.getUserNumber(date,condition);
		return count;
		
	}
	public static int getMobileCount(String date,String condition)
	{
		IStatisticService statservice=ServiceFactory.createStatisticService();
		int count=statservice.getMobileNumber(date,condition);
		return count;
		
	}
	public static int getNewUserNumber(String date,String cond)
	{
		IStatisticService statservice=ServiceFactory.createStatisticService();
		int count=statservice.getNewUserNumber(date,cond);
		return count;
		
	}
	public static int getWapUserCount(String date,String condition)
	{
		IStatisticService statservice=ServiceFactory.createStatisticService();
		int count=statservice.getWapUserNumber(date,condition);
		return count;
		
	}
	public static int getWapMobileCount(String date,String condition)
	{
		IStatisticService statservice=ServiceFactory.createStatisticService();
		int count=statservice.getWapMobileNumber(date,condition);
		return count;
		
	}
	public static int getWapNewUserNumber(String date,String cond)
	{
		IStatisticService statservice=ServiceFactory.createStatisticService();
		int count=statservice.getWapNewUserNumber(date,cond);
		return count;
		
	}
}