package net.joycool.wap.service.infc;

import java.util.*;

import net.joycool.wap.bean.friend.FriendActionBean;
import net.joycool.wap.bean.friend.FriendBadUserBean;
import net.joycool.wap.bean.friend.FriendBagBean;
import net.joycool.wap.bean.friend.FriendBean;
import net.joycool.wap.bean.friend.FriendCartoonBean;
import net.joycool.wap.bean.friend.FriendDrinkBean;
import net.joycool.wap.bean.friend.FriendGuestBean;
import net.joycool.wap.bean.friend.FriendMarriageBean;
import net.joycool.wap.bean.friend.FriendProposalBean;
import net.joycool.wap.bean.friend.FriendReviewBean;
import net.joycool.wap.bean.friend.FriendRingBean;
import net.joycool.wap.bean.friend.FriendUserBean;
import net.joycool.wap.bean.friend.FriendVoteBean;

public interface IFriendService {
	public void flushFriend(int id);
	public FriendBean getFriend(int id);
	public FriendBean getFriend(Integer iid);
	
	public Vector getFriendList(String condition);
	public boolean addFriend(FriendBean bean);
	public boolean delFriend(String condition);
	public boolean updateFriend(String set, String condition);
	public int getFriendCount(String condition);
	
	public FriendProposalBean getFriendProposal(String condition);
	public Vector getFriendProposalList(String condition);
	public boolean addFriendProposal(FriendProposalBean bean);
	public boolean delFriendProposal(String condition);
	public boolean updateFriendProposal(String set, String condition);
	public int getFriendProposalCount(String condition);
	
	public FriendMarriageBean getFriendMarriage(String condition);
	public Vector getFriendMarriageList(String condition);
	public boolean addFriendMarriage(FriendMarriageBean bean);
	public boolean delFriendMarriage(String condition);
	public boolean updateFriendMarriage(String set, String condition);
	public int getFriendMarriageCount(String condition);
	
	public FriendRingBean getFriendRing(String condition);
	public Vector getFriendRingList(String condition);
	public boolean addFriendRing(FriendRingBean bean);
	public boolean delFriendRing(String condition);
	public boolean updateFriendRing(String set, String condition);
	public int getFriendRingCount(String condition);
	
	public FriendReviewBean getFriendReview(String condition);
	public Vector getFriendReviewList(String condition);
	public boolean addFriendReview(FriendReviewBean bean);
	public boolean delFriendReview(String condition);
	public boolean updateFriendReview(String set, String condition);
	public int getFriendReviewCount(String condition);
	
	public FriendBagBean getFriendBag(String condition);
	public Vector getFriendBagList(String condition);
	public boolean addFriendBag(FriendBagBean bean);
	public boolean delFriendBag(String condition);
	public boolean updateFriendBag(String set, String condition);
	public int getFriendBagCount(String condition);
	
	public FriendGuestBean getFriendGuest(String condition);
	public Vector getFriendGuestList(String condition);
	public boolean addFriendGuest(FriendGuestBean bean);
	public boolean delFriendGuest(String condition);
	public boolean updateFriendGuest(String set, String condition);
	public int getFriendGuestCount(String condition);

	public FriendDrinkBean getFriendDrink(int friendDrinkId);
	public FriendDrinkBean getFriendDrink(String condition);
	public Vector getFriendDrinkList(String condition);
	public boolean addFriendDrink(FriendDrinkBean bean);
	public boolean delFriendDrink(String condition);
	public boolean updateFriendDrink(String set, String condition);
	public int getFriendDrinkCount(String condition);
	
	public FriendUserBean getFriendUser(String condition);
	public Vector getFriendUserList(String condition);
	public boolean addFriendUser(FriendUserBean bean);
	public boolean delFriendUser(String condition);
	public boolean updateFriendUser(String set, String condition);
	public int getFriendUserCount(String condition);
	
	public FriendBadUserBean getFriendBadUser(String condition);
	public Vector getFriendBadUserList(String condition);
	public boolean addFriendBadUser(FriendBadUserBean bean);
	public boolean delFriendBadUser(String condition);
	public boolean updateFriendBadUser(String set, String condition);
	public int getFriendBadUserCount(String condition);
	/**
	 * 统计红包
	 */
	public long getFriendBagTotal(String condition);
	
	/**
	 * 缓存同城的交友信息
	 */
	public int getFriendCacheCount(String condition);
	public Vector getFriendCacheList(String condition);
	
	/**
	 * 得到结婚ID
	 */
	public int getFriendMarriageId(String condition);
	
	//macq_2006-11-21_增加用户照片排名_start
	public FriendVoteBean getFriendVote(String condition);
	public Vector getFriendVoteList(String condition);
	public List getFriendVoteList2(String condition);
	public boolean addFriendVote(FriendVoteBean bean);
	public boolean delFriendVote(String condition);
	public boolean updateFriendVote(String set, String condition);
	public int getFriendVoteCount(String condition);
	//macq_2006-11-21_增加用户照片排名_end
	
	//wucx_2006-12-6_增加闹婚礼_start
	public FriendActionBean getFriendAction(String condition);
	public Vector getFriendActionList(String condition);
	public boolean addFriendAction(FriendActionBean bean);
	public boolean delFriendAction(String condition);
	public boolean updateFriendAction(String set, String condition);
	public int getFriendActionCount(String condition);
//	wucx_2006-12-6_增加闹婚礼_end
	
	//wucx_2006-12-11_增加卡通_start
	public FriendCartoonBean getFriendCartoon(String condition);
	public Vector getFriendCartoonList(String condition);
	public boolean addFriendCartoon(FriendCartoonBean bean);
	public boolean delFriendCartoon(String condition);
	public boolean updateFriendCartoon(String set, String condition);
	public int getFriendCartoonCount(String condition);
//	wucx_2006-12-11_增加卡通_end
}
