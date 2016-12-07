package net.joycool.wap.service.infc;

import java.util.HashMap;
import java.util.Vector;

import net.joycool.wap.bean.job.AngerBean;
import net.joycool.wap.bean.job.AngerCardBean;
import net.joycool.wap.bean.job.AngerExpressionBean;
import net.joycool.wap.bean.job.CardBean;
import net.joycool.wap.bean.job.CardTypeBean;
import net.joycool.wap.bean.job.HandbookingerRecordBean;
import net.joycool.wap.bean.job.HappyCardBean;
import net.joycool.wap.bean.job.HappyCardCategoryBean;
import net.joycool.wap.bean.job.HappyCardSendBean;
import net.joycool.wap.bean.job.HappyCardStatBean;
import net.joycool.wap.bean.job.HappyCardTypeBean;
import net.joycool.wap.bean.job.HuntQuarryAppearRateBean;
import net.joycool.wap.bean.job.HuntQuarryBean;
import net.joycool.wap.bean.job.HuntTaskBean;
import net.joycool.wap.bean.job.HuntUserQuarryBean;
import net.joycool.wap.bean.job.HuntUserWeaponBean;
import net.joycool.wap.bean.job.HuntWeaponBean;
import net.joycool.wap.bean.job.JCLotteryGuessBean;
import net.joycool.wap.bean.job.JCLotteryHistoryBean;
import net.joycool.wap.bean.job.JCLotteryNumberBean;
import net.joycool.wap.bean.job.HandbookingerBean;
import net.joycool.wap.bean.job.JobMusicBean;
import net.joycool.wap.bean.job.JobWareHouseBean;
import net.joycool.wap.bean.job.LuckBean;
import net.joycool.wap.bean.job.PsychologyAnswerBean;
import net.joycool.wap.bean.job.PsychologyBean;
import net.joycool.wap.bean.job.SpiritBean;

public interface IJobService {
	public boolean updateUserStatus(String set, String condition);

	public JobWareHouseBean getJobWareHouse();

	public JobWareHouseBean getJobWareHouse(String condition);

	public JobMusicBean getJobMusicRand(String condition);

	public JobMusicBean getJobMusic(String condition);

	// 通过条件增加一条用户下注信息
	public boolean addJCLotteryGuess(JCLotteryGuessBean jcLotteryGuess);

	// 通过条件增加开奖号码信息
	public boolean addJCLotteryNumber(JCLotteryNumberBean jcLotteryNumber);

	// 通过条件更新上期遗留奖金
	public boolean updateJCLotteryNumber(String set, String condition);

	// 通过条件增加历史开奖记录
	public boolean addJCLotteryHistory(JCLotteryHistoryBean jcLotteryHistory);

	// 通过条件获得中奖号码
	public JCLotteryNumberBean getJCLotteryNumber(String condition);

	// 通过条件获得用户下注信息
	public JCLotteryGuessBean getJCLotteryGuess(String condition);

	// 通过条件获得用户下注信息
	public Vector getJCLotteryGuessList(String condition);

	// 通过条件获得用户下注信息
	public boolean updateJCLotteryGuess(String set, String condition);

	// 取得最大的jc_lottery_number中ID字段值
	public int getMaxJCLotteryNumber();

	// 通过条件取得jc_lottery_number中上期遗留奖金
	public long getJCLotteryNumberCount(String condition);

	// 通过条件获得当前下注金额
	public long getSumWager(String condition);

	// 通过条件获得中奖历史记录
	public Vector getJCLotteryHistoryList(String condition);

	// 通过条件取得用户下注次数
	public int getJCLotteryGuessCount(String condition);

	// zhul_2006-07-11_新增card游戏 start
	// 关于机会卡种类的接口
	public boolean addCardType(CardTypeBean cardType);

	public CardTypeBean getCardType(String condition);

	public Vector getCardTypeList(String condition);

	public boolean deleteCardType(String condition);

	public boolean updateCardType(String set, String condition);

	public int getCardTypeCount(String condition);

	// 关于机会卡具体信息的接口
	public boolean addCard(CardBean card);

	public CardBean getCard(String condition);

	public Vector getCardList(String condition);

	public boolean deleteCard(String condition);

	public boolean updateCard(String condition);

	public int getCardCount(String condition);

	// zhul_2006-07-11_新增card游戏 end

	// zhul _2006-07-17_新增打猎游戏 start
	// about weapon interface
	public boolean addHuntWeapon(HuntWeaponBean huntWeapon);

	public HuntWeaponBean getHuntWeapon(String condition);

	public HashMap getHuntWeaponMap(String condition);

	public boolean deleteHuntWeapon(String condition);

	public boolean updateHuntWeapon(String condition);

	public int getHuntWeaponCount(String condition);

	// about quarry_apear_rate interface
	public boolean addHuntQuarryAppearRate(
			HuntQuarryAppearRateBean quarryAppearRate);

	public HuntQuarryAppearRateBean getHuntQuarryAppearRate(String condition);

	public Vector getHuntQuarryAppearRateList(String condition);

	public boolean deleteHuntQuarryAppearRate(String condition);

	public boolean updateHuntQuarryAppearRate(String set, String condition);

	public int getHuntQuarryAppearRateCount(String condition);

	// about hunt_quarry_Bean interface
	public boolean addHuntQuarry(HuntQuarryBean huntQuarry);

	public HuntQuarryBean getHuntQuarry(String condition);

	public HashMap getHuntQuarryMap(String condition);

	public boolean deleteHuntQuarry(String condition);

	public boolean updateHuntQuarry(String set, String condition);

	public int getHuntQuarryCount(String condition);

	// about hunt_user_weapon_Bean interface
	public boolean addHuntUserWeapon(HuntUserWeaponBean huntUserWeapon);

	public HuntUserWeaponBean getHuntUserWeapon(String condition);

	public Vector getHuntUserWeaponList(String condition);

	public boolean deleteHuntUserWeapon(String condition);

	public boolean updateHuntUserWeapon(String set, String condition);

	public int getHuntUserWeaponCount(String condition);

	// about hunt_user_quarry_Bean interface
	public boolean addHuntUserQuarry(HuntUserQuarryBean huntUserQuarry);

	public HuntUserQuarryBean getHuntUserQuarry(String condition);

	public Vector getHuntUserQuarryList(String condition);

	public boolean deleteHuntUserQuarry(String condition);

	public boolean updateHuntUserQuarry(String set, String condition);

	public int getHuntUserQuarryCount(String condition);

	// about jc_hunt_timer_task interface
	public boolean addHuntTask(HuntTaskBean huntTask);

	public HuntTaskBean getHuntTask(String condition);

	public Vector getHuntTaskList(String condition);

	public boolean deleteHuntTask(String condition);

	public boolean updateHuntTask(String set, String condition);

	public int getHuntTaskCount(String condition);

	// zhul _2006-07-17_新增打猎游戏 end

	// zhul_2006-08-29 运势游戏 start
	public boolean addLuck(LuckBean luck);

	public LuckBean getLuck(String condition);

	public HashMap getLuckBeanMap(String condition);

	public boolean deleteLuck(String condition);

	public boolean updateLuck(String set, String condition);

	public int getLuckBeanCount(String condition);

	// zhul_2006-08-29 运势游戏 end
	// fanys 2006-08-29 start
	public boolean addPsychology(PsychologyBean psychology);

	public PsychologyBean getPsychology(String condition);

	public Vector getPsychologyList(String condition);

	public int getPsychologyCount(String condition);

	public boolean updatePsychology(String set, String condition);

	public boolean deletePsychology(String condition);

	public boolean addPsychologyAnswer(PsychologyAnswerBean psychologyAnswer);

	public PsychologyAnswerBean getPsychologyAnswer(String condition);

	public Vector getPsychologyAnswerList(String condition);

	public boolean updatePsychologyAnswer(String set, String condition);

	public boolean deletePsychologyAnswer(String condition);

	// fanys 2006-08-29 end

	// fanys 2006-09-13 start_贺卡
	public boolean addHappyCard(HappyCardBean cardsBean);

	public HappyCardBean getHappyCard(String condition);

	public Vector getHappyCardList(String condition);

	public int getHappyCardCount(String condition);

	public boolean updateHappyCard(String set, String condition);

	public boolean deleteHappyCard(String condition);

	public boolean addHappyCardType(HappyCardTypeBean happyCardTypeBean);

	public HappyCardTypeBean getHappyCardType(String condition);

	public Vector getHappyCardTypeList(String condition);

	public int getHappyCardTypeCount(String condition);

	public boolean updateHappyCardType(String set, String condition);

	public boolean deleteHappyCardType(String condition);

	public boolean addHappyCardCategory(
			HappyCardCategoryBean happyCardCategoryBean);

	public HappyCardCategoryBean getHappyCardCategory(String condition);

	public Vector getHappyCardCategoryList(String condition);

	public int getHappyCardCategoryCount(String condition);

	public boolean updateHappyCardCategory(String set, String condition);

	public boolean deleteHappyCardCategory(String condition);

	public boolean addHappyCardSend(HappyCardSendBean happyCardSendBean);

	public HappyCardSendBean getHappyCardSend(String condition);

	public Vector getHappyCardSendList(String condition);

	public int getHappyCardSendCount(String condition);

	public boolean updateHappyCardSend(String set, String condition);

	public boolean deleteHappyCardSend(String condition);

	// 统计
	public boolean addHappyCardSendStat(HappyCardStatBean bean);

	public void deleteHappyCardSendStat(String condition);

	public boolean updateHappyCardSendStat(String set, String condition);

	// fanys 2006-09-13 end

	// wucx 2006-9-25 静国神社 start
	public boolean addSpirit(SpiritBean spiritBean);

	public SpiritBean getSpirit(String condition);

	public Vector getSpiritList(String condition);

	public int getSpiritCount(String condition);

	public boolean updateSpirit(String set, String condition);

	public void deleteSpirit(String condition);

	// wucx 2006-9-25 静国神社 end

	// wucx 2006-11-8赌马游戏 start
	public boolean addHandbookinger(HandbookingerBean handbookingerBean);

	public HandbookingerBean getHandbookinger(String condition);

	public Vector getHandbookingerList(String condition);

	public int getHandbookingerCount(String condition);

	public boolean updateHandbookinger(String set, String condition);

	public boolean deleteHandbookinger(String condition);

	public boolean addHandbookingerRecord(
			HandbookingerRecordBean handbookingerRecordBean);

	public HandbookingerRecordBean getHandbookingerRecord(String condition);

	public Vector getHandbookingerRecordList(String condition);

	public int getHandbookingerRecordCount(String condition);

	public boolean updateHandbookingerRecord(String set, String condition);

	public boolean deleteHandbookingerRecord(String condition);

	// wucx 2006-11-8赌马游戏 end
	// wucx 2006-11-20出气筒 start
	public boolean addAnger(AngerBean AngerBean);

	public AngerBean getAnger(String condition);

	public Vector getAngerList(String condition);

	public int getAngerCount(String condition);

	public boolean updateAnger(String set, String condition);

	public boolean deleteAnger(String condition);
	public boolean addAngerExpression(AngerExpressionBean AngerBean);

	public AngerExpressionBean getAngerExpression(String condition);

	public Vector getAngerExpressionList(String condition);

	public int getAngerExpressionCount(String condition);

	public boolean updateAngerExpression(String set, String condition);

	public boolean deleteAngerExpression(String condition);
	public boolean addAngerCard(AngerCardBean AngerBean);

	public AngerCardBean getAngerCard(String condition);

	public Vector getAngerCardList(String condition);

	public int getAngerCardCount(String condition);

	public boolean updateAngerCard(String set, String condition);

	public boolean deleteAngerCard(String condition);
	// wucx 2006-11-20出气筒 end
}
