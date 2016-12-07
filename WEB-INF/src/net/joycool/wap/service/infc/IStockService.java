package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.stock.StockBean;
import net.joycool.wap.bean.stock.StockDealBean;
import net.joycool.wap.bean.stock.StockGrailBean;
import net.joycool.wap.bean.stock.StockHolderBean;
import net.joycool.wap.bean.stock.StockInfoBean;
import net.joycool.wap.bean.stock.StockNoticeBean;
import net.joycool.wap.bean.stock.StockPvBean;

public interface IStockService {
	public StockPvBean getStockPv(String condition);
	public Vector getStockPvList(String condition);
	public boolean addStockPv(StockPvBean bean);
	public boolean delStockPv(String condition);
	public boolean updateStockPv(String set, String condition);
	public int getStockPvCount(String condition);
	
	public StockBean getStock(String condition);
	public Vector getStockList(String condition);
	public boolean addStock(StockBean bean);
	public boolean delStockv(String condition);
	public boolean updateStock(String set, String condition);
	public int getStockCount(String condition);
	
	public StockDealBean getStockDeal(String condition);
	public Vector getStockDealList(String condition);
	public boolean addStockDeal(StockDealBean bean);
	public boolean delStockDeal(String condition);
	public boolean updateStockDealk(String set, String condition);
	public int getStockDealCount(String condition);
	
	public StockHolderBean getStockHolder(String condition);
	public Vector getStockHolderList(String condition);
	public boolean addStockHolder(StockHolderBean bean);
	public boolean delStockHolder(String condition);
	public boolean updateStockHolder(String set, String condition);
	public int getStockHolderCount(String condition);
	
	public StockInfoBean getStockInfo(String condition);
	public Vector getStockInfoList(String condition);
	public boolean addStockInfo(StockInfoBean bean);
	public boolean delStockInfo(String condition);
	public boolean updateStockInfo(String set, String condition);
	public int getStockInfoCount(String condition);
	
	public StockGrailBean getStockGrail(String condition);
	public Vector getStockGrailList(String condition);
	public boolean addStockGrail(StockGrailBean bean);
	public boolean delStockGrail(String condition);
	public boolean updateStockGrail(String set, String condition);
	public int getStockGrailCount(String condition);
	
	public StockNoticeBean getStockNotice(String condition);
	public Vector getStockNoticeList(String condition);
	public boolean addStockNotice(StockNoticeBean bean);
	public boolean delStockNotice(String condition);
	public boolean updateStockNotice(String set, String condition);
	public int getStockNoticeCount(String condition);
	
	//wucx 查找公司10大股东 
	public Vector getStockUser(String condition);
	//wucx 查找公司10大股东 
	
	//wucx 查找交易记录大于10的用户列表 
	public Vector getStockUserList();
	//wucx 查找交易记录大于10的用户列表
	
	//wucx 查找交易记录大于10的用户列表 
	public Vector getStockIdList(String condition);
	//wucx 查找交易记录大于10的用户列表
}
