package net.joycool.wap.servlet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.charitarian.CharitarianBean;
import net.joycool.wap.bean.charitarian.CharitarianHistoryBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ICharitarianService;
import net.joycool.wap.util.CharitarianCacheUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * 
 * @author bombzj
 * 
 * 每分钟执行一次，处理所有的缓冲数据库更新
 */
public class SqlThread extends Thread {
	
	static LinkedList sqls = new LinkedList();
	
	public void run() {
		while (true) {
			try {
				synchronized(sqls) {
					sqls.wait(1000 * 60);
				}
			} catch (InterruptedException e) {
				execute();
				return;
			}
			try {
				execute();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void execute() {
		if(sqls.size() == 0)
			return;
		String sql;
		DbOperation db = new DbOperation(2);	// 暂时只能用于其中一个数据库forum
		if(db.isClosed())		// 连接失败
			return;

		try {
			while(true) {
				synchronized(sqls) {
					if(sqls.size() > 0)
						sql = (String)sqls.removeFirst();
					else {
						db.release();
						return;
					}
				}
				
				if(!db.executeUpdate(sql)) {		// 执行失败？
					if(db.isClosed()) {	// 连接出现问题导致执行失败，等待下次重试
						synchronized(sqls) {
							sqls.addFirst(sql);
						}
						return;
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		db.release();
	}
	// 加入到缓冲池
	public static void addSql(String sql) {
		synchronized(sqls) {
			sqls.addLast(sql);
			if(sqls.size() >= 100)		// 队列超过100，立刻执行
				sqls.notify();
		}
	}
	// 立刻执行
	public static void executeSql() {
		synchronized(sqls) {
			sqls.notify();
		}
	}
	
	public static LinkedList getSqls() {
		return sqls;
	}
}
