package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.auction.AuctionBean;
import net.joycool.wap.bean.auction.AuctionHistoryBean;

/**
 * @author macq
 * @datetime 2006-12-12 下午04:52:55
 * @explain  拍卖系统接口
 */
public interface  IAuctionService {
    //拍卖记录
	public boolean addAuction(AuctionBean dummyType);
	public AuctionBean getAuction(String condition);
	public Vector getAuctionList(String condition);
	public boolean deleteAuction(String condition);
	public boolean updateAuction(String set, String condition);
	public int getAuctionCount(String condition);
	
    //拍卖历史记录
	public boolean addAuctionHistory(AuctionHistoryBean dummyType);
	public AuctionHistoryBean getAuctionHistory(String condition);
	public Vector getAuctionHistoryList(String condition);
	public boolean deleteAuctionHistory(String condition);
	public boolean updateAuctionHistory(String set, String condition);
	public int getAuctionHistoryCount(String condition);
}
