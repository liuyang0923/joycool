package net.joycool.wap.service.infc;

import java.util.ArrayList;
import java.util.Vector;

import net.joycool.wap.bean.chat.JCRoomBean;
import net.joycool.wap.bean.chat.JCRoomCompainBean;
import net.joycool.wap.bean.chat.JCRoomContentBean;
import net.joycool.wap.bean.chat.JCRoomContentCountBean;
import net.joycool.wap.bean.chat.JCRoomOnlineBean;
import net.joycool.wap.bean.chat.RoomAfficheBean;
import net.joycool.wap.bean.chat.RoomApplyBean;
import net.joycool.wap.bean.chat.RoomGrantBean;
import net.joycool.wap.bean.chat.RoomHallInviteBean;
import net.joycool.wap.bean.chat.RoomInviteBean;
import net.joycool.wap.bean.chat.RoomInviteRankBean;
import net.joycool.wap.bean.chat.RoomInviteResourceBean;
import net.joycool.wap.bean.chat.RoomInviteStatBean;
import net.joycool.wap.bean.chat.RoomManagerBean;
import net.joycool.wap.bean.chat.RoomPaymentBean;
import net.joycool.wap.bean.chat.RoomRateBean;
import net.joycool.wap.bean.chat.RoomUserBean;
import net.joycool.wap.bean.chat.RoomVoteBean;

public interface IChatService {
	// 通过条件检查jcroomforbin中是否存在登陆的userid
	public boolean getForBID(String condition);

	// 获得jcroomforbin中所有记录
	public Vector getForBID();

	// 通过条件删除jcroomforbin中存在的登陆userid
	public void deltetForBID(String condition);

	// 通过条件添加登陆userid到jcroomforbin中
	public void addForBID(String condition);

	// 取得所有公聊记录
	public Vector getMessageList(String condition);

	// 通过条件取得聊天室人数
	public int getOnlineCount(String condition);

	// 通过条件取得聊天室人数列表
	public Vector getOnlineList(String condition);

	// 通过条件取得聊天室名称
	public JCRoomBean getRoomName(String condition);

	// 通过条件取得聊天室人数列表
	public JCRoomOnlineBean getOnlineUser(String condition);

	// 通过条件在online表中删除登陆用户
	public void deleteOnlineUser(String condition);

	// 通过条件更新online表中登陆用户
	public void updateOnlineUser(String set, String condition);

	// 通过条件向online表中插入登陆用户
	public void addOnlineUser(String condition);

	// 通过条件向jcroomcontent表中插入登陆用户
	public JCRoomContentBean addContent(JCRoomContentBean bean);

	// 通过条件在jcroomconent表中删除聊天记录
	public boolean deleteContent(String condition);

	// 获得所有的聊天记录
	public Vector getContent();

	// 通过条件获得聊天记录
	public JCRoomContentBean getContent(String condition);

	// 通过条件添加投诉记录
	public void addJCRoomCompain(String condition);

	// 通过条件更新投诉记录
	public void updateJCRoomCompain(String set, String condition);

	// 获得所有的投诉记录
	public Vector getJCRoomCompainList();

	// 通过条件获得相关投诉记录
	public JCRoomCompainBean getJCRoomCompain(String condition);

	// 通过条件删除相关投诉记录
	public void delJCRoomCompain(String condition);

	// 通过条件显示相关聊天记录
	public Vector getContentList(String condition);

	// 通过条件获取聊天记录数量
	public int getMessageCount(String condition);

	// mcq_2006-6-27_增加自建聊天室方法—_start
	public int getJCRoomCount(String condition);

	public boolean addJCRoom(JCRoomBean jcRoom);

	public boolean updateJCRoom(String set, String condition);

	public boolean deleteJCRoom(String condition);

	public Vector getJCRoomList(String condition);

	public JCRoomBean getJCRoom(String condition);

	public JCRoomBean getJCRoomSpec(String condition);

	public int getJCRoomPaymentCount(String condition);

	public boolean addJCRoomPayment(RoomPaymentBean roomPayment);

	public boolean updateJCRoomPayment(String set, String condition);

	public boolean deleteJCRoomPayment(String condition);

	public Vector getJCRoomPaymentList(String condition);

	public RoomPaymentBean getJCRoomPayment(String condition);

	public int getJCRoomManagerCount(String condition);

	public boolean addJCRoomManager(RoomManagerBean roomManager);

	public boolean updateJCRoomManager(String set, String condition);

	public boolean deleteJCRoomManager(String condition);

	public Vector getJCRoomManagerList(String condition);

	public RoomManagerBean getJCRoomManager(String condition);

	public int getJCRoomUserCount(String condition);

	public boolean addJCRoomUser(RoomUserBean roomUser);

	public boolean updateJCRoomUser(String set, String condition);

	public boolean deleteJCRoomUser(String condition);

	public Vector getJCRoomUserList(String condition);

	public RoomUserBean getJCRoomUser(String condition);

	public int getJCRoomGrantCount(String condition);

	public boolean addJCRoomGrant(RoomGrantBean roomGrant);

	public boolean updateJCRoomGrant(String set, String condition);

	public boolean deleteJCRoomGrant(String condition);

	public Vector getJCRoomGrantList(String condition);

	public RoomGrantBean getJCRoomGrant(String condition);

	public int getJCRoomAfficheCount(String condition);

	public boolean addJCRoomAffiche(RoomAfficheBean roomAffiche);

	public boolean updateJCRoomAffiche(String set, String condition);

	public boolean deleteJCRoomAffiche(String condition);

	public Vector getJCRoomAfficheList(String condition);

	public RoomAfficheBean getJCRoomAffiche(String condition);

	// mcq_2006-6-27_增加自建聊天室方法—_end

	// 李北金_2006-6-27_查询优化，主要是避免使用or，非特殊情况请勿使用
	public Vector getMessageList0(String query);

	// 李北金_2006-6-27_查询优化

	// 李北金_2006-6-27_查询优化，主要是避免使用or，非特殊情况请勿使用
	public int getMessageCount0(String query);

	// 李北金_2006-6-27_查询优化

	// zhul_2006-07-06_增加统计聊天室公聊消息方法_start
	public boolean addJCRoomContentCount(JCRoomContentCountBean roomContentCount);

	public boolean updateJCRoomContentCount(String set, String condition);

	public boolean deleteJCRoomContentCount(String condition);

	public Vector getJCRoomContentCountList(String condition);

	public JCRoomContentCountBean getJCRoomContentCount(String condition);

	// zhul_2006-07-06_增加统计聊天室公聊消息方法_end

	// mcq_2006-7-9_申请自建聊天室方法_start
	public RoomApplyBean getRoomApply(String condition);

	public Vector getRoomApplyList(String condition);

	public boolean addRoomApply(RoomApplyBean bean);

	public boolean delRoomApply(String condition);

	public boolean updateRoomApply(String set, String condition);

	public int getRoomApplyCount(String condition);

	// mcq_2006-7-9_申请自建聊天室方法_end
	// mcq_2006-7-9_自建聊天室投票方法_start
	public RoomVoteBean getRoomVote(String condition);

	public Vector getRoomVoteList(String condition);

	public boolean addRoomVote(RoomVoteBean bean);

	public boolean delRoomVote(String condition);

	public boolean updateRoomVote(String set, String condition);

	public int getRoomVoteCount(String condition);

	// mcq_2006-7-9_自建聊天室投票方法_end

	// mcq_2006-7-12_自建聊天室邀请次数方法_start
	public RoomHallInviteBean getRoomHallInvite(String condition);

	public Vector getRoomHallInviteList(String condition);

	public boolean addRoomHallInvite(RoomHallInviteBean bean);

	public boolean delRoomHallInvite(String condition);

	public boolean updateRoomHallInvite(String set, String condition);

	public int getRoomHallInviteCount(String condition);

	// mcq_2006-7-12_自建聊天室邀请次数方法_end

	// mcq_2006-7-12_自建聊天室通过push邀请次数方法_start
	public RoomInviteBean getRoomInvite(String condition);

	public Vector getRoomInviteList(String condition);

	public boolean addRoomInvite(RoomInviteBean bean);

	public boolean delRoomInvite(String condition);

	public boolean updateRoomInvite(String set, String condition);

	public int getRoomInviteCount(String condition);

	public int getMaxRoomInviteId(String condition);

	// mcq_2006-7-12_自建聊天室通过push邀请次数方法_end
	// fanys 2006-08-15 start
	// 获取邀请排行榜资源列表
	public Vector getRoomInviteResourceList(String condition);

	public RoomInviteResourceBean getRoomInviteResource(String condition);
	
	public RoomInviteRankBean getRoomInviteRank(String condition);
	// 获取排行榜(保存下来的以前的排名)
	public Vector getRoomInviteRankList(String condition);

	// 获取当前的实时的排名
	public Vector getCurRoomInviteRankList(String condition);

	// 添加一条排名记录
	public boolean addRoomInviteRankBean(RoomInviteRankBean bean);

	// fanys 2006-08-15 end
	// fanys 2006-08-17 邀请好友来乐酷－－统计
	public boolean addRoomInviteStat(RoomInviteStatBean bean);

	public void deleteRoomInviteStat(String condition);

	public boolean updateRoomInviteStat(String set, String condition);

	//zhul 2006-08-31 控制随机聊天室 start
	public RoomRateBean getRoomRate(String condition);

	public ArrayList getRoomRateList(String condition);

	public boolean addRoomRate(RoomRateBean roomRate);

	public boolean deleteRoomRate(String condition);

	public boolean updateRoomRate(String set, String condition);

	public int getRoomRateCount(String condition);
	//zhul 2006-08-31 控制随机聊天室 end
	
	//mcq_2006-09-5_获取满足特定条件全部聊天室记录id_start
	public Vector getRoomIdCountList(String condition);
	//mcq_2006-09-5_获取满足特定条件全部聊天室记录id_end
	
	//mcq_2006-09-5_更新聊天室记录_start
	public boolean updateRoomContent(String set, String condition);
	//mcq_2006-09-5_更新聊天室记录_end
	
	//zhul 2006-09-05 获取聊天室在线人数 start
	public int getRoomOnlineNum(String roomId);
	//zhul 2006-09-05 获取聊天室在线人数 end
	
	//zhul 2006-09-16 获取所有聊天室ID
	public int[] getAllRoomId();
}
