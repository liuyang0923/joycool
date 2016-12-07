package net.joycool.wap.service.infc;

import java.util.List;
import java.util.Vector;

import net.joycool.wap.bean.tong.TongApplyBean;
import net.joycool.wap.bean.tong.TongBean;
import net.joycool.wap.bean.tong.TongCityRecordBean;
import net.joycool.wap.bean.tong.TongDestroyHistoryBean;
import net.joycool.wap.bean.tong.TongFriendBean;
import net.joycool.wap.bean.tong.TongFundBean;
import net.joycool.wap.bean.tong.TongHockshopBean;
import net.joycool.wap.bean.tong.TongLocationBean;
import net.joycool.wap.bean.tong.TongManageLogBean;
import net.joycool.wap.bean.tong.TongTitleRateLogBean;
import net.joycool.wap.bean.tong.TongUserBean;

/**
 * @author macq
 * @datetime 2006-12-24 下午02:08:55
 * @explain 城帮系统
 */
public interface ITongService {
	public TongBean getTong(String condition);

	public Vector getTongList(String condition);

	public boolean addTong(TongBean bean);

	public boolean delTong(String condition);

	public boolean updateTong(String set, String condition);

	public int getTongCount(String condition);

	public TongLocationBean getTongLocation(String condition);

	public Vector getTongLocationList(String condition);

	public boolean addTongLocation(TongLocationBean bean);

	public boolean delTongLocation(String condition);

	public boolean updateTongLocation(String set, String condition);

	public int getTongLocationCount(String condition);

	public TongApplyBean getTongApply(String condition);

	public Vector getTongApplyList(String condition);

	public boolean addTongApply(TongApplyBean bean);

	public boolean delTongApply(String condition);

	public boolean updateTongApply(String set, String condition);

	public int getTongApplyCount(String condition);

	public TongUserBean getTongUser(String condition);

	public Vector getTongUserList(String condition);

	public boolean addTongUser(TongUserBean bean);

	public boolean delTongUser(String condition);

	public boolean updateTongUser(String set, String condition);

	public int getTongUserCount(String condition);

	public TongFundBean getTongFund(String condition);

	public Vector getTongFundList(String condition);

	public boolean addTongFund(TongFundBean bean);

	public boolean delTongFund(String condition);

	public boolean updateTongFund(String set, String condition);

	public int getTongFundCount(String condition);

	public TongHockshopBean getTongHockshop(String condition);

	public Vector getTongHockshopList(String condition);

	public boolean addTongHockshop(TongHockshopBean bean);

	public boolean delTongHockshop(String condition);

	public boolean updateTongHockshop(String set, String condition);

	public int getTongHockshopCount(String condition);

	public TongManageLogBean getTongManageLog(String condition);

	public Vector getTongManageLogList(String condition);

	public boolean addTongManageLog(TongManageLogBean bean);

	public boolean delTongManageLog(String condition);

	public boolean updateTongManageLog(String set, String condition);

	public int getTongManageLogCount(String condition);

	public TongTitleRateLogBean getTongTitleRateLog(String condition);

	public Vector getTongTitleRateLogList(String condition);

	public boolean addTongTitleRateLog(TongTitleRateLogBean bean);

	public boolean delTongTitleRateLog(String condition);

	public boolean updateTongTitleRateLog(String set, String condition);

	public int getTongTitleRateLogCount(String condition);

	public TongCityRecordBean getTongCityRecord(String condition);

	public Vector getTongCityRecordList(String condition);

	public boolean addTongCityRecord(TongCityRecordBean bean);

	public boolean delTongCityRecord(String condition);

	public boolean updateTongCityRecord(String set, String condition);

	public int getTongCityRecordCount(String condition);

	public Vector getTongAssistantList(int tongId, int start, int end);

	public int getTongAssistantCount(int tongId);

	public long getTongDonationCount(int tongId);

	public long getTongFundCount(int tongId, int userId);

	// liuyi 2007-01-25 城墙破坏记录 start
	public TongDestroyHistoryBean getTongDestroyHistoryBean(int id);

	public boolean addTongDestroyHistory(TongDestroyHistoryBean bean);
	// liuyi 2007-01-25 城墙破坏记录 end
	
	//macq_2007-2-7_帮会结盟_start
	public TongFriendBean getTongFriend(String condition);

	public Vector getTongFriendList(String condition);

	public boolean addTongFriend(TongFriendBean bean);

	public boolean delTongFriend(String condition);

	public boolean updateTongFriend(String set, String condition);

	public int getTongFriendCount(String condition);
	//macq_2007-2-7_帮会结盟_end
	// 荣誉系统，旗帜
	public List getHonorList(String condition);
	public void changeHonor(int userId, int tongId, int flag);
	public int getHonorCount(String condition) ;
}
