/*
 * Created on 2005-11-15
 *
 */
package net.joycool.wap.service.factory;

import net.joycool.wap.bean.question.QuestionService;
import net.joycool.wap.bean.question.QuestionServiceImpl;
import net.joycool.wap.service.impl.AdverServiceImpl;
import net.joycool.wap.service.impl.AuctionServiceImpl;
import net.joycool.wap.service.impl.BankServiceImpl;
import net.joycool.wap.service.impl.BeginnerServiceImpl;
import net.joycool.wap.service.impl.BroadcastServiceImpl;
import net.joycool.wap.service.impl.CatalogServiceImpl;
import net.joycool.wap.service.impl.CharitarianServiceImpl;
import net.joycool.wap.service.impl.ChatServiceImpl;
import net.joycool.wap.service.impl.ChatStatServiceImpl;
import net.joycool.wap.service.impl.DiyServiceImpl;
import net.joycool.wap.service.impl.DummyServiceImpl;
import net.joycool.wap.service.impl.EBookServiceImpl;
import net.joycool.wap.service.impl.ForumMessageServiceImpl;
import net.joycool.wap.service.impl.ForumServiceImpl;
import net.joycool.wap.service.impl.FriendAdverMessageServiceImpl;
import net.joycool.wap.service.impl.FriendAdverServiceImpl;
import net.joycool.wap.service.impl.FriendLinkServiceImpl;
import net.joycool.wap.service.impl.FriendServiceImpl;
import net.joycool.wap.service.impl.GameServiceImpl;
import net.joycool.wap.service.impl.GuestbookServiceImpl;
import net.joycool.wap.service.impl.HomeServiceImpl;
import net.joycool.wap.service.impl.HuangYeServiceImpl;
import net.joycool.wap.service.impl.ImageServiceImpl;
import net.joycool.wap.service.impl.JaLineServiceImpl;
import net.joycool.wap.service.impl.JcForumServiceImpl;
import net.joycool.wap.service.impl.JobServiceImpl;
import net.joycool.wap.service.impl.LinkServiceImpl;
import net.joycool.wap.service.impl.MessageServiceImpl;
import net.joycool.wap.service.impl.ModuleServiceImpl;
import net.joycool.wap.service.impl.MoneyServiceImpl;
import net.joycool.wap.service.impl.NewsServiceImpl;
import net.joycool.wap.service.impl.NoticeServiceImpl;
import net.joycool.wap.service.impl.PGameServiceImpl;
import net.joycool.wap.service.impl.PKServiceImpl;
import net.joycool.wap.service.impl.RingServiceImpl;
import net.joycool.wap.service.impl.SpServiceImpl;
import net.joycool.wap.service.impl.StatisticServiceImpl;
import net.joycool.wap.service.impl.StockServiceImpl;
import net.joycool.wap.service.impl.TongServiceImpl;
import net.joycool.wap.service.impl.TopServiceImpl;
import net.joycool.wap.service.impl.UserCashServiceImpl;
import net.joycool.wap.service.impl.UserServiceImpl;
import net.joycool.wap.service.impl.VideoServiceImpl;
import net.joycool.wap.service.impl.WGameFightServiceImpl;
import net.joycool.wap.service.impl.WGameHallServiceImpl;
import net.joycool.wap.service.impl.WGamePKServiceImpl;
import net.joycool.wap.service.impl.WGameServiceImpl;
import net.joycool.wap.service.impl.WorldCupServiceImpl;
import net.joycool.wap.service.infc.IAdverServic;
import net.joycool.wap.service.infc.IAuctionService;
import net.joycool.wap.service.infc.IBankService;
import net.joycool.wap.service.infc.IBeginnerService;
import net.joycool.wap.service.infc.IBroadcastService;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.ICharitarianService;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.IChatStatService;
import net.joycool.wap.service.infc.IDiyService;
import net.joycool.wap.service.infc.IDummyService;
import net.joycool.wap.service.infc.IEBookService;
import net.joycool.wap.service.infc.IForumMessageService;
import net.joycool.wap.service.infc.IForumService;
import net.joycool.wap.service.infc.IFriendAdverMessageService;
import net.joycool.wap.service.infc.IFriendAdverService;
import net.joycool.wap.service.infc.IFriendLinkService;
import net.joycool.wap.service.infc.IFriendService;
import net.joycool.wap.service.infc.IGameService;
import net.joycool.wap.service.infc.IGuestbookService;
import net.joycool.wap.service.infc.IHomeService;
import net.joycool.wap.service.infc.IHuangYeService;
import net.joycool.wap.service.infc.IImageService;
import net.joycool.wap.service.infc.IJaLineService;
import net.joycool.wap.service.infc.IJcForumService;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.service.infc.ILinkService;
import net.joycool.wap.service.infc.IMessageService;
import net.joycool.wap.service.infc.IModuleService;
import net.joycool.wap.service.infc.IMoneyService;
import net.joycool.wap.service.infc.INewsService;
import net.joycool.wap.service.infc.INoticeService;
import net.joycool.wap.service.infc.IPGameService;
import net.joycool.wap.service.infc.IPKService;
import net.joycool.wap.service.infc.IRingService;
import net.joycool.wap.service.infc.ISpService;
import net.joycool.wap.service.infc.IStatisticService;
import net.joycool.wap.service.infc.IStockService;
import net.joycool.wap.service.infc.ITongService;
import net.joycool.wap.service.infc.ITopService;
import net.joycool.wap.service.infc.IUserCashService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.service.infc.IVideoService;
import net.joycool.wap.service.infc.IWGameFightService;
import net.joycool.wap.service.infc.IWGameHallService;
import net.joycool.wap.service.infc.IWGamePKService;
import net.joycool.wap.service.infc.IWGameService;
import net.joycool.wap.service.infc.IWorldCupService;
//问题接龙2007-01-30李泉
/**
 * @author lbj
 * 
 */
public class ServiceFactory {
	public static IUserService createUserService() {
		return new UserServiceImpl();
	}

	public static IMessageService createMessageService() {
		return new MessageServiceImpl();
	}

	public static INewsService createNewsService() {
		return new NewsServiceImpl();
	}

	public static IImageService createImageService() {
		return new ImageServiceImpl();
	}

	public static ICatalogService createCatalogService() {
		return new CatalogServiceImpl();
	}

	public static IJaLineService createJaLineService() {
		return new JaLineServiceImpl();
	}

	public static IEBookService createEBookService() {
		return new EBookServiceImpl();
	}

	public static IGameService createGameService() {
		return new GameServiceImpl();
	}

	public static IDiyService createDiyService() {
		return new DiyServiceImpl();
	}

	public static IGuestbookService createGuestbookService() {
		return new GuestbookServiceImpl();
	}

	public static IPGameService createPGameService() {
		return new PGameServiceImpl();
	}

	public static ISpService createSpService() {
		return new SpServiceImpl();
	}

	public static IWGameService createWGameService() {
		return new WGameServiceImpl();
	}

	public static IBroadcastService createBroadcastService() {
		return new BroadcastServiceImpl();
	}

	public static IWGamePKService createWGamePKService() {
		return new WGamePKServiceImpl();
	}

	public static IRingService createRingService() {
		return new RingServiceImpl();
	}

	public static IFriendLinkService createFriendLinkService() {
		return new FriendLinkServiceImpl();
	}

	public static IWGameHallService createWGameHallService() {
		return new WGameHallServiceImpl();
	}

	public static IForumService createForumService() {
		return new ForumServiceImpl();
	}

	public static IForumMessageService createForumMessageService() {
		return new ForumMessageServiceImpl();
	}

	public static INoticeService createNoticeService() {
		return new NoticeServiceImpl();
	}

	public static IChatService createChatService() {
		return new ChatServiceImpl();
	}

	public static IJobService createJobService() {
		return new JobServiceImpl();
	}

	public static IHuangYeService createHuangYeService() {
		return new HuangYeServiceImpl();
	}

	public static IWorldCupService createWorldCupService() {
		return new WorldCupServiceImpl();
	}

	public static IFriendAdverService createFriendAdverService() {
		return new FriendAdverServiceImpl();
	}

	public static IFriendAdverMessageService createFriendAdverMessageService() {
		return new FriendAdverMessageServiceImpl();
	}

	/*
	 * 功能:统计现金流方法 mcq_2006-7-24
	 */
	public static IMoneyService createMoneyService() {
		return new MoneyServiceImpl();
	}

	public static IBankService createBankService() {
		return new BankServiceImpl();
	}

	public static IVideoService createVideoService() {
		return new VideoServiceImpl();
	}

	public static ITopService createTopService() {
		return new TopServiceImpl();
	}

	/**
	 * liuyi 2006-09-12 乐酷模块服务
	 * 
	 * @return
	 */
	public static IModuleService createModuleService() {
		return new ModuleServiceImpl();
	}

	// mcq_2006_9_14_增加我的家园_start
	public static IHomeService createHomeService() {
		return new HomeServiceImpl();
	}

	// mcq_2006_9_14_增加我的家园_end

	// wucx_2006_9_27_增加数据统计_start
	public static IStatisticService createStatisticService() {
		return new StatisticServiceImpl();
	}

	// wucx_2006_9_27_增加数据统计_end

	// wucx_2006-10-8_增加乐酷现金流追踪_start
	public static IUserCashService createUserCashService() {
		return new UserCashServiceImpl();
	}

	// wucx_2006_10-8_增加乐酷现金流追踪_end
	// wucx_2006-10-19_用户友好度操作_start
	// public static IFriendLevelService createFriendLevelService() {
	// return new FriendLevelServiceImpl();
	// }
	// wucx_2006_10-19_用户友好度操作_end

	// wucx_2006-10-20_数据分析_start
	public static IChatStatService createChatStatService() {
		return new ChatStatServiceImpl();
	}

	// wucx_2006_10-10-20_数据分析_end
	// wucx_2006-10-26交友系统_start
	public static IFriendService createFriendService() {
		return new FriendServiceImpl();
	}

	// wucx_2006_10-10-26交友系统_end

	// wucx_2006_10-10-20_数据分析_end
	// macq_2006-11-14_股票系统_start
	public static IStockService createStockService() {
		return new StockServiceImpl();
	}

	// macq_2006-11-14_股票系统_end
	// macq_2006-11-23_新手系统_start
	public static IBeginnerService createBeginnerService() {
		return new BeginnerServiceImpl();
	}

	// macq_2006-11-23_新手系统_end

	// macq_2006-11-24_慈善基金_start
	public static ICharitarianService createCharitarianService() {
		return new CharitarianServiceImpl();
	}

	// macq_2006-11-24_慈善基金_end

	// liuyi 2006-12-06 推广链接 start
	public static ILinkService createLinkService() {
		return new LinkServiceImpl();
	}

	// liuyi 2006-12-06 推广链接 end

	// 乐酷论坛
	public static IJcForumService createJcForumService() {
		return new JcForumServiceImpl();
	}

	// macq_2006-12-13_虚拟物品_start
	public static IDummyService createDummyService() {
		return new DummyServiceImpl();
	}

	// macq_2006-12-13_虚拟物品_end

	// macq_2006-12-13_拍卖系统_start
	public static IAuctionService createAuctionService() {
		return new AuctionServiceImpl();
	}

	// macq_2006-12-13_拍卖系统_end

	// macq_2006-12-24_城帮系统_start
	public static ITongService createTongService() {
		return new TongServiceImpl();
	}
	// macq_2006-12-24_城帮系统_end

	//问题接龙2007-01-30李泉
	public static QuestionService createQuestionService() {
		return new QuestionServiceImpl();
	}
	//问题接龙2007-01-30李泉
	// macq_2007-1-30_PK系统_start
	public static IPKService createPKService() {
		return new PKServiceImpl();
	}
	// macq_2007-1-30_PK系统_end

	// 2007-01-30李泉_end
	public static IAdverServic createAdverService() {
		return new AdverServiceImpl();
	}
	// 2007-01-30李泉_end
	//2007-10-22桂平 街霸 
	public static IWGameFightService createWGameFightService() {
		return new WGameFightServiceImpl();
	}
}
