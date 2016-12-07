/*
 * 2006-10-8
 * WUCX
 * 乐酷现金流追踪
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.jcadmin.UserCashBean;

public interface IUserCashService{
	
	public boolean addUserCash(UserCashBean usercash);

	public UserCashBean getUserCash(String condition);

	public Vector getUserCashList(String condition);

	public int getUserCashCount(String condition);
	
	public boolean updateUserCash(String set,String condition);
	
	public boolean deleteUserCash(String condition);
}